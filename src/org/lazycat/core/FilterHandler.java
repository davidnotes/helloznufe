package org.lazycat.core;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.lmax.disruptor.EventHandler;

public class FilterHandler implements EventHandler<Message>{
	JedisPool jedisPool;
	
	public FilterHandler(){
		super();
	}
	
	public FilterHandler(JedisPool jedisPool){
		this.jedisPool = jedisPool;
	}
	
	public Jedis getJedis(){
		return jedisPool.getResource();
	}
	
	public void onEvent(Message message, long sequence, boolean endOfBatch){
			
	}
}
