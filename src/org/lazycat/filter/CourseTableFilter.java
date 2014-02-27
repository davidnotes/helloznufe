package org.lazycat.filter;

import java.util.Map;

import org.lazycat.core.Message;

import com.lmax.disruptor.EventHandler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
public class CourseTableFilter implements EventHandler<Message>{
	JedisPool pool = null;
	
	public CourseTableFilter(JedisPool pool){
		this.pool = pool;
	}
	public Jedis getJedis(){
		return pool.getResource();
	}
	@Override
	public void onEvent(Message message, long sequence, boolean endOfBatch)
			throws Exception {
		Jedis jedis = null;
		
		String[] urls = message.getUrl().split("/");
		String userid = urls[2];
		String courseid = urls[4];
		try{
			jedis = getJedis();
			Map<String, String> course = jedis.hgetAll("course:" + courseid);//取出课程信息
			Map<String, String> courseTable = jedis.hgetAll("coursetable:"+userid);//取出学生课程表
			if(null != courseTable.get(course.get("time"))){
				message.setState(message.getState() + (1<<1) );
			}
			int credit = Integer.parseInt(course.get("credit"));
			if(Integer.parseInt(courseTable.get("credit")) + credit > 30){
				message.setState(message.getState() + (1<<2) );
			}
		}catch(JedisConnectionException e){
			if (null != jedis) {
		        pool.returnBrokenResource(jedis);
		        jedis = null;
		    }
		}finally{
			if(null != jedis)
				pool.returnResource(jedis);
		}	
	}
	
}
