package org.lazycat.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lazycat.core.EventServer;
import org.lazycat.core.Message;
import org.lazycat.core.MessageTranslator;
import org.lazycat.core.Store;
import org.lazycat.demo.*;

import redis.clients.jedis.Jedis;

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
		//fvt.test4();
		//fvt.test5();
		//fvt.test6();
		fvt.test7();
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
		Jedis jedis = new Jedis("192.168.56.100");
		final SequenceBarrier ringBufferSequenceBarrier = ringBuffer.newBarrier();
		
		MessageTranslator mt = new MessageTranslator();
		
		EventPublisher<Message> eventPublisher = new EventPublisher<>(ringBuffer);
		//final EventHandler<Message> filter = new FilterHandler(); 
		final EventHandler<Message> cid    = new CourseIDFilter(jedis);
		final EventHandler<Message> cPool  = new CoursePoolFilter(jedis);
		final EventHandler<Message> cTable = new CourseTableFilter(jedis);
		
		
		//BatchEventProcessor<Message> bep = new BatchEventProcessor<>(ringBuffer, ringBufferSequenceBarrier, filter);
		BatchEventProcessor<Message> cidBEP   = new BatchEventProcessor<Message>(ringBuffer, ringBufferSequenceBarrier, cid);
		BatchEventProcessor<Message> cPoolBEP = new BatchEventProcessor<Message>(ringBuffer, ringBufferSequenceBarrier, cPool);
		BatchEventProcessor<Message> cTableBEP= new BatchEventProcessor<Message>(ringBuffer, ringBufferSequenceBarrier, cTable);
		
		final SequenceBarrier filterBarrier = 
				ringBuffer.newBarrier(cidBEP.getSequence(),cPoolBEP.getSequence(),cTableBEP.getSequence());
		
		final EventHandler<Message> storeHandler = new Store(jedis);
		BatchEventProcessor<Message> storeBEP = new BatchEventProcessor<Message>(ringBuffer, filterBarrier, storeHandler);
		
		ringBuffer.setGatingSequences(storeBEP.getSequence());
		final ExecutorService executor = Executors.newFixedThreadPool(4);
		executor.submit(cidBEP);
		executor.submit(cPoolBEP);
		executor.submit(cTableBEP);
		executor.submit(storeBEP);
		
		System.out.println("STARTED");
        final long startTime = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			//mt.setIn("hello,world");
            //eventPublisher.publishEvent(mt);
        }
		String in = "{\"method\":\"PUT\",\"url\":\"/user/1009050103/course/B100010201\","
				+ "\"headers\":{\"user-agent\":\"curl/7.22.0 (x86_64-pc-linux-gnu) libcurl/7.22.0 OpenSSL/1.0.1 zlib/1.2.3.4 libidn/1.23 librtmp/2.3\",\"host\":\"192.168.56.100:8080\",\"accept\":\"*/*\"},"
				+ "\"data\":\"\"}";
		mt.setIn(in);
		eventPublisher.publishEvent(mt);
		
		while (ringBuffer.getCursor() != storeBEP.getSequence().get()){
            //busy spin waiting for the event processors to handle everything
        }
		//bep.halt();
		final long endTime = System.currentTimeMillis();
        System.out.printf("Time Taken: %d millis%n", (endTime - startTime));
        System.exit(0);
	}
	
	public void test5(){
		String url = "/user/1009050103/course/B100100101";
		String[] urls = url.split("/");
		for(int i = 0; i < urls.length; i++){
			System.out.println(i + ":" + urls[i]);
		}
		
		System.out.println(urls[4].substring(0,urls[4].length()-2));
		
	}
	
	public void test6(){
		Message m = new Message();
		String in = "{\"method\":\"PUT\",\"url\":\"/user/1009050103/course/B100010201\","
				+ "\"headers\":{\"user-agent\":\"curl/7.22.0 (x86_64-pc-linux-gnu) libcurl/7.22.0 OpenSSL/1.0.1 zlib/1.2.3.4 libidn/1.23 librtmp/2.3\",\"host\":\"192.168.56.100:8080\",\"accept\":\"*/*\"},"
				+ "\"data\":\"\"}";
		m.respawner(in);
		System.out.println(m);
	}
	
	public void test7(){
		Jedis jedis = new Jedis("192.168.56.100");
		jedis.decr("coursepool:B100010201");
		System.out.println(jedis.get("coursepool:B100010201"));
		final CourseIDFilter cid    = new CourseIDFilter(jedis);
		final CoursePoolFilter cPool  = new CoursePoolFilter(jedis);
		final CourseTableFilter cTable = new CourseTableFilter(jedis);
		
		cid.test();
	}
}
