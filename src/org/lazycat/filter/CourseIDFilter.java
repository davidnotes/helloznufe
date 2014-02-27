package org.lazycat.filter;

import java.util.List;

import org.lazycat.core.Message;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.lmax.disruptor.EventHandler;

public class CourseIDFilter implements EventHandler<Message>{
	JedisPool pool = null;
	public CourseIDFilter(JedisPool jedisPool){
		this.pool = jedisPool;
	}
	
	public Jedis getJedis(){
		return pool.getResource();
	}
	
	@Override
	public void onEvent(Message message, long sequence, boolean endOfBatch)
			throws Exception {
		Jedis jedis = null;
		String courseid = message.getUrl().split("/")[4];//取出courseID done
		courseid = courseid.substring(0, courseid.length()-2);
		List<String> courseList = null;
		try{
			jedis = getJedis();
			courseList = jedis.lrange("coursealready:" + courseid, 0, 100 );//To be tested
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
		if(courseList != null && courseList.contains(courseid)){
			message.setState(message.getState() + 1);
		}
	}
}
