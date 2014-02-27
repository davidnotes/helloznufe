package org.lazycat.filter;

import org.lazycat.core.Message;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.lmax.disruptor.EventHandler;

public class CoursePoolFilter implements EventHandler<Message>{
	JedisPool pool = null;
	public Jedis getJedis(){
		return pool.getResource();
	}
	public CoursePoolFilter(JedisPool pool){
		this.pool = pool;
	}
	
	@Override
	public void onEvent(Message message, long sequence, boolean endOfBatch)
			throws Exception {
		Jedis jedis = null;
		String courseid = message.getUrl().split("/")[4];
		try{
			jedis = getJedis();
			if(Integer.parseInt(jedis.get("coursepool:" + courseid)) < 1){
				message.setState(message.getState() + (1<<3));
			}
		}catch(JedisConnectionException e){
			// returnBrokenResource when the state of the object is unrecoverable
		    if (null != jedis) {
		        pool.returnBrokenResource(jedis);
		        jedis = null;
		    }
		}finally{
			if(null != jedis)
				pool.returnResource(jedis);
		}
	}
	
	public boolean filter(Message message, Jedis jedis){
		String courseid = message.getUrl().split("/")[4];
		try{
			jedis = getJedis();
			
			if(Integer.parseInt(jedis.get("coursepool:" + courseid)) < 1){
				message.setState(message.getState() + (1<<3));
			}
		}catch(JedisConnectionException e){
			// returnBrokenResource when the state of the object is unrecoverable
		    if (null != jedis) {
		        this.pool.returnBrokenResource(jedis);
		        jedis = null;
		    }
		}finally{
			if(null != jedis)
				this.pool.returnResource(jedis);
		}
		return true;
	}
}
