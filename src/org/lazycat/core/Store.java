package org.lazycat.core;

import java.util.Map;
import org.lazycat.util.ObjectToMap;
import redis.clients.jedis.Jedis;

public class Store {
	private StoreObject storeObject;
	private Jedis jedis;
	
	public Store(){
		super();
	}
	
	public Store(StoreObject so, Jedis jedis){
		this.setStoreObject(so);
		this.jedis = jedis;
	}

	public StoreObject getStoreObject() {
		return storeObject;
	}

	public void setStoreObject(StoreObject storeObject) {
		this.storeObject = storeObject;
	}
	
	public int store(StoreObject obj){
		Map<String,String> hash = ObjectToMap.toMap(obj);
		Class<?> c = obj.getClass();
		String name = c.getName();
		int length = name.length();
		int start  = name.lastIndexOf(".");
		jedis.hmset(name.substring(start+1,length)+":"+obj.getId(), hash);
		return 0;
	}
}
