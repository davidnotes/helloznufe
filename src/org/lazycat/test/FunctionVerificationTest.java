package org.lazycat.test;

import org.lazycat.core.EventServer;
import org.lazycat.demo.OrderForm;
import org.lazycat.demo.StoreToRedis;

public class FunctionVerificationTest {
	
	public static void main(String[] args){
		StoreToRedis str = new StoreToRedis();
		OrderForm of = new OrderForm(1,"001","002","OK");
		str.store(of);
	}
	
	public static void EventServer(){
		EventServer eventServer = new EventServer();
        int i = 0;
        long start = System.currentTimeMillis();
        while (i < 10000){
        	eventServer.request();
        	//System.out.println(eventServer.request());
        	eventServer.response("testServer:hello wolrd. " + (9999 - i) + "times left");
        	i++;
        }        
        System.out.println("Test passed");
        System.out.println("Total time:" + (System.currentTimeMillis()-start)/1000 + "s");
	}
	/*
	 * 测试取到类name。
	 * */
	public static void test1(){
		OrderForm of = new OrderForm(1,"001","002","OK");
		Class<?> c1 = of.getClass();
		int length = c1.getName().length();
		int start  = c1.getName().lastIndexOf(".");
		System.out.println(c1.getName().subSequence(start+1, length));
	}
}
