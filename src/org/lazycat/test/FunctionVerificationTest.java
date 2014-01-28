package org.lazycat.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lazycat.core.EventServer;
import org.lazycat.core.FilterHandler;
import org.lazycat.core.Message;
import org.lazycat.core.MessageTranslator;
import org.lazycat.demo.OrderForm;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventPublisher;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;

public class FunctionVerificationTest {
	
	public static void main(String[] args){
		FunctionVerificationTest fvt = new FunctionVerificationTest();
		//fvt.eventServer();
		//OrderForm of = new OrderForm(1,"001","002","OK");
		//fvt.test3();
		fvt.test4();
	}
	
	public void eventServer(){
		EventServer eventServer = new EventServer();
        int i = 0;
        long start = System.currentTimeMillis();
        while (i < 10000){
        	//eventServer.request();
        	System.out.println(eventServer.request());
        	System.out.println();
        	eventServer.response("testServer:hello wolrd. " + (9999 - i) + "times left");
        	i++;
        }        
        System.out.println("Test passed");
        System.out.println("Total time:" + (System.currentTimeMillis()-start)/1000 + "s");
	}
	
	public void test1(){
		OrderForm of = new OrderForm(1,"001","002","OK");
		Class<?> c1 = of.getClass();
		int length = c1.getName().length();
		int start  = c1.getName().lastIndexOf(".");
		System.out.println(c1.getName().subSequence(start+1, length));
	}
	
	public void test3(){
		Map<String,String> m = new HashMap<String,String>();
		m.put("foo", "bar");
		System.out.println("hello:"+m.get("hello"));
	}
	
	public void test4(){
		RingBuffer<Message> ringBuffer = new RingBuffer<Message>(
				Message.EVENT_FACTORY,
				new SingleThreadedClaimStrategy(1024),
	            new YieldingWaitStrategy());
		
		final SequenceBarrier ringBufferSequenceBarrier = ringBuffer.newBarrier();
		
		MessageTranslator mt = new MessageTranslator();
		
		EventPublisher<Message> eventPublisher = new EventPublisher<>(ringBuffer);
		final EventHandler<Message> filter = new FilterHandler(); 
		
		BatchEventProcessor<Message> bep = new BatchEventProcessor<>(ringBuffer, ringBufferSequenceBarrier, filter);
		ringBuffer.setGatingSequences(bep.getSequence());
		final ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(bep);
		System.out.println("STARTED");
        final long startTime = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			mt.setIn("hello,world");
            eventPublisher.publishEvent(mt);
        }
		while (ringBuffer.getCursor() != bep.getSequence().get()){
            //busy spin waiting for the event processors to handle everything
        }
		bep.halt();
		final long endTime = System.currentTimeMillis();
        System.out.printf("Time Taken: %d millis%n", (endTime - startTime));
        System.exit(0);
	}
}
