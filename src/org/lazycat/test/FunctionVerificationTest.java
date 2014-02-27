package org.lazycat.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lazycat.core.EventServer;
import org.lazycat.core.Message;
import org.lazycat.core.Store;
import org.lazycat.filter.*;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventPublisher;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
public class FunctionVerificationTest {
	public static JedisPool jpool = new JedisPool(new JedisPoolConfig(),"192.168.56.210");
	
	public static void main(String[] args){
		FunctionVerificationTest fvt = new FunctionVerificationTest();
		//fvt.eventServer();
		//OrderForm of = new OrderForm(1,"001","002","OK");
		//fvt.test3();
		fvt.test2();
		//fvt.test4();
		//fvt.test5();
		//fvt.test6();
		//fvt.test7();
		//fvt.test8();
	}
	
	public void eventServer(){
		EventServer eventServer = new EventServer();
        int i = 0;
        long start = System.currentTimeMillis();
        while (i < 10000){
        	//eventServer.request();
        	System.out.println(i+":"+eventServer.request());
        	System.out.println();
        	eventServer.response("testServer:hello wolrd. " + (9999 - i) + "times left");
        	i++;
        }        
        System.out.println("Test passed");
        System.out.println("Total time:" + (System.currentTimeMillis()-start)/1000 + "s");
	}
	
	@SuppressWarnings("unchecked")
	public void test2(){
		EventServer eventServer = new EventServer();
		ExecutorService exec = Executors.newCachedThreadPool();
		Disruptor<Message> disruptor = new Disruptor<>(Message.EVENT_FACTORY, 8, exec);
		//final EventHandler<Message> filter = new FilterHandler(); 
		final EventHandler<Message> cid    = new CourseIDFilter(jpool);
		final EventHandler<Message> cPool  = new CoursePoolFilter(jpool);
		final EventHandler<Message> cTable = new CourseTableFilter(jpool);
		final EventHandler<Message> store = new Store(jpool, eventServer);
		//disruptor.handleEventsWith(cid, cPool, cTable).then(store);
		disruptor.handleEventsWith( cid, cTable, cPool).then(store);
		RingBuffer<Message> ringBuffer = disruptor.start();
		String in = "{\"method\":\"PUT\",\"url\":\"/user/1009050103/course/B100010201\","
				+ "\"headers\":{\"user-agent\":\"curl/7.22.0 (x86_64-pc-linux-gnu) libcurl/7.22.0 OpenSSL/1.0.1 zlib/1.2.3.4 libidn/1.23 librtmp/2.3\",\"host\":\"192.168.56.100:8080\",\"accept\":\"*/*\"},"
				+ "\"data\":\"\"}"; 	
		for(;;){
			in = eventServer.request();
			eventServer.setRes(false);
			long seq = ringBuffer.next();
			Message m = ringBuffer.get(seq);
			m.respawner(in);
			ringBuffer.publish(seq);
			while(true){
				if(eventServer.getRes())
					break;
			}
			
			//eventServer.response("hello,world");
		}
		//disruptor.shutdown();
		//exec.shutdown();
	}

	
	public void test3(){
		Map<String,String> m = new HashMap<String,String>();
		m.put("foo", "bar");
		System.out.println("hello:"+m.get("hello"));
	}
	
	public void test4(){

		EventServer eventServer = new EventServer();
		RingBuffer<Message> ringBuffer = new RingBuffer<Message>(
				Message.EVENT_FACTORY,
				new SingleThreadedClaimStrategy(32),
	            new SleepingWaitStrategy());
		
		final SequenceBarrier ringBufferSequenceBarrier = ringBuffer.newBarrier();
		
		MessageTranslator mt = new MessageTranslator();
		
		EventPublisher<Message> eventPublisher = new EventPublisher<>(ringBuffer);
		//final EventHandler<Message> filter = new FilterHandler(); 
		final EventHandler<Message> cid    = new CourseIDFilter(jpool);
		final EventHandler<Message> cPool  = new CoursePoolFilter(jpool);
		final EventHandler<Message> cTable = new CourseTableFilter(jpool);
		
		
		//BatchEventProcessor<Message> bep = new BatchEventProcessor<>(ringBuffer, ringBufferSequenceBarrier, filter);
		BatchEventProcessor<Message> cidBEP   = new BatchEventProcessor<Message>(ringBuffer, ringBufferSequenceBarrier, cid);
		BatchEventProcessor<Message> cPoolBEP = new BatchEventProcessor<Message>(ringBuffer, ringBufferSequenceBarrier, cPool);
		BatchEventProcessor<Message> cTableBEP= new BatchEventProcessor<Message>(ringBuffer, ringBufferSequenceBarrier, cTable);
		
		final SequenceBarrier filterBarrier = 
				ringBuffer.newBarrier(cidBEP.getSequence(),cPoolBEP.getSequence(),cTableBEP.getSequence());
		
		final EventHandler<Message> storeHandler = new Store(jpool,eventServer);
		BatchEventProcessor<Message> storeBEP = new BatchEventProcessor<Message>(ringBuffer, filterBarrier, storeHandler);
		
		ringBuffer.setGatingSequences(storeBEP.getSequence());
		final ExecutorService executor = Executors.newFixedThreadPool(4);
		executor.submit(cidBEP);
		executor.submit(cPoolBEP);
		executor.submit(cTableBEP);
		executor.submit(storeBEP);
		
		System.out.println("STARTED");
        //final long startTime = System.currentTimeMillis();
		String in = "{\"method\":\"PUT\",\"url\":\"/user/1009050103/course/B100010201\","
				+ "\"headers\":{\"user-agent\":\"curl/7.22.0 (x86_64-pc-linux-gnu) libcurl/7.22.0 OpenSSL/1.0.1 zlib/1.2.3.4 libidn/1.23 librtmp/2.3\",\"host\":\"192.168.56.100:8080\",\"accept\":\"*/*\"},"
				+ "\"data\":\"\"}";
		for(int i = 0; i < 1000; i ++) {
			mt.setIn(in);
			eventPublisher.publishEvent(mt);
		}
		int count = 1; 
		while(0 == count){
			in = eventServer.request();
			System.out.println(count++);
			
			mt.setIn(in);
			
			eventPublisher.publishEvent(mt);
			eventServer.response("hello,world");
		}
		/*
		  while (ringBuffer.getCursor() != storeBEP.getSequence().get()){
	          //busy spin waiting for the event processors to handle everything
	       }
		 * */
		//bep.halt();
		//final long endTime = System.currentTimeMillis();
        //System.out.printf("Time Taken: %d millis%n", (endTime - startTime));
        //System.exit(0);
	}
	
	public void test5(){
		String url = "/user/1009050103/course/B100100101";
		String[] urls = url.split("/");
		for(int i = 0; i < urls.length; i++){
			System.out.println(i + ":" + urls[i]);
		}
		System.out.println(urls[4].substring(0,urls[4].length()-2));
		
	}
	/*
	public void test6(){
		Message m = new Message();
	
		String in = "{\"method\":\"PUT\",\"url\":\"/user/1009050103/course/B100010201\","
				+ "\"headers\":{\"user-agent\":\"curl/7.22.0 (x86_64-pc-linux-gnu) libcurl/7.22.0 OpenSSL/1.0.1 zlib/1.2.3.4 libidn/1.23 librtmp/2.3\",\"host\":\"192.168.56.100:8080\",},"
				+ "\"data\":\"\"}";
		//m.respawner(in);
				
		System.out.println(m);
	}
	*/
	public void test8(){
		System.out.println(2 + (1<<2));
		int num = 10;
		Integer num2 = new Integer(10);
		System.out.println(num == num2);
		System.out.println(Integer.parseInt("128"));
	}
}
