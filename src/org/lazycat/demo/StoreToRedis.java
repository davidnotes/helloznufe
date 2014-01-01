package org.lazycat.demo;

import java.util.Map;

import org.lazycat.core.JSONModel;
import org.lazycat.core.StoreObject;
import org.lazycat.util.ObjectToMap;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;

public class StoreToRedis {
	private String message;
	private Jedis  jedis;
	
	public StoreToRedis(){
		super();
	}
	/*
	 * @param jedis is from JedisPool
	 * */
	public StoreToRedis(Jedis jedis){
		this.jedis   = jedis;
	}
	/*
	 * @param message is the message from zeroMQ
	 * @param jedis is from JedisPool
	 * */
	public StoreToRedis(String message, Jedis jedis){
		this.message = message;
		this.jedis   = jedis;
	}
	
	/*
	 * @param message is the message from zeroMQ
	 * @param jedisLocation is the IP of a redis server
	 * */
	public StoreToRedis(String message, String jedisLocation){
		this.message = message;
		this.jedis = new Jedis(jedisLocation);
	}
	
	public JSONModel parseMessage(String message){
		return JSON.parseObject(message,JSONModel.class); 
	}
	
	public void route(JSONModel json){
		switch(json.getHead()){
			case "orderform":
				OrderForm of = JSON.parseObject(json.getBody(),OrderForm.class);
				store(of);
				break;
			//else ToDo
			case "":
				break;
			default:
				System.out.println("Route " + json.getHead() + " not found!");
				break;
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
	
	
	
	public String getMessage(){
		return this.message;
	}
	public void setMessage(String message){
		this.message = message;
	}
}
