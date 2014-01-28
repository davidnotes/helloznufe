package org.lazycat.test;

import java.lang.reflect.Field;

import org.lazycat.demo.OrderForm;



public class PerfTest {
	public static void main(String[] args){

		//boolean If = true;
		//test1(If);
		//test1(!If);

	}

	public static void test1(boolean test){
		if(test){
			long start = System.currentTimeMillis();
			for(int i = 0; i < 10000000; i++);
			System.out.println(" if Test:"+(System.currentTimeMillis()-start));
		}else{
			long start = System.currentTimeMillis();
			for(int i = 0; i < 10000000; i++);
			System.out.println("elseTest:"+(System.currentTimeMillis()-start));
		}
	}
	
	public static void test2() throws IllegalArgumentException, IllegalAccessException{
		OrderForm of = new OrderForm(1,"001","002","OK");
		Class<?> c1 = of.getClass();
		System.out.println(c1.getName());
		long start = System.currentTimeMillis();
		for(int i = 0; i < 10000000; i++){
			of.getId();
			of.getCustomID();
			of.getBookFormID();
			of.getState();
			//System.out.println(""+of.getId());
			//System.out.println(""+of.getCustomID());
			//System.out.println(""+of.getBookFormID());
			//System.out.println(""+of.getState());
		}
		long t1 = System.currentTimeMillis()-start;
		
		
		start = System.currentTimeMillis();
		Class<? extends Object> c;
		Field f[];
		for(int j = 0; j < 0; j++){
			c = of.getClass();
			f = c.getDeclaredFields();
			for(int i = 0; i < f.length; i++){
				f[i].getName();
				//System.out.print(""+f[i].getName());
				f[i].setAccessible(true);
				f[i].get(of);
				//System.out.println(":"+f[i].get(of));
			}
		}
		System.out.println(t1);
		System.out.println(System.currentTimeMillis()-start);
	}
}
