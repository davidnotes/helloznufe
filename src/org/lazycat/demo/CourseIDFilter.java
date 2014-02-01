package org.lazycat.demo;

import java.util.List;

import org.lazycat.core.Message;

import redis.clients.jedis.Jedis;

import com.lmax.disruptor.EventHandler;

public class CourseIDFilter implements EventHandler<Message> {
	private Jedis jedis;
	
	public CourseIDFilter(){
		super();
	}
	
	public CourseIDFilter(Jedis jedis){
		this.jedis = jedis;
	}
	
	public void setJedis(Jedis jedis){
		this.jedis = jedis;
	}
	
	
	@Override
	/*
	 * 取出已选课程的所有ID，进行遍历，通过返回0,否则返回1*/
	public void onEvent(Message message, long sequence, boolean endOfBatch){
		String courseid = message.getUrl().split("/")[4];//取出courseID done
		courseid = courseid.substring(0, courseid.length()-2);
		List<String> courseList = jedis.lrange("coursealready:" + courseid, 0, 100 );//To be tested
		if(courseList.contains(courseid)){
			message.setState(message.getState() + 1);
		}
	}
	
	public void test(){
		jedis.decr("coursepool:B100010201");
		System.out.println(jedis.get("coursepool:B100010201"));
	}

}
