package org.lazycat.core;

import java.util.Map;

import org.lazycat.util.ObjectToMap;

import com.lmax.disruptor.EventHandler;

import redis.clients.jedis.Jedis;

public class Store implements EventHandler<Message>{

	private Jedis jedis;
	
	public Store(){
		super();
	}
	public Store(Jedis jedis){
		this.jedis = jedis;
	}
	
	public void onEvent(Message message, long sequence, boolean endOfBatch){
		//课程表+1；
		//课程池-1
		//已选课+1
		System.out.println(message.getState());
		if(message.getState() == 0){
			String[] urls = message.getUrl().split("/");
			String userid = urls[2];
			String courseid = urls[4];
			jedis.lpush("coursetable:"+userid, courseid);
			jedis.lpush("coursealready:"+userid, courseid.substring(0, courseid.length()-2));
			jedis.decr("coursepool:"+courseid);
			//发消息给node端
		}else{
			//发消息给Node端
		}
		
		
		
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
