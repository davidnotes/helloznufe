package org.lazycat.test;

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
	
	
}
