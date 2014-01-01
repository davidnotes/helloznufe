package org.lazycat.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectToMap {
	public static Map<String,String> toMap(Object obj){
		Map<String,String> hashMap = new HashMap<String, String>();
		try {
			Class<? extends Object> c = obj.getClass();
			Field  f[] = c.getDeclaredFields();
			for(int i = 0; i < f.length; i++){
				//System.out.print(""+f[i].getName());
				f[i].setAccessible(true);
				//System.out.println(":"+f[i].get(obj).toString());
				hashMap.put(f[i].getName(), f[i].get(obj).toString());
			}		
		} catch (Throwable e) {
			System.err.println(e);
		}
		return hashMap;
	}
}
