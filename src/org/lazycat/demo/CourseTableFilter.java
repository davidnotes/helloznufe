package org.lazycat.demo;

import java.util.List;
import java.util.Map;

import org.lazycat.core.Message;

import redis.clients.jedis.Jedis;

import com.lmax.disruptor.EventHandler;

public class CourseTableFilter  implements EventHandler<Message>{
	
	private Jedis jedis;
	
	public CourseTableFilter(){
		super();
	}
	
	public CourseTableFilter(Jedis jedis){
		this.jedis = jedis;
	}
	
	public void setJedis(Jedis jedis){
		this.jedis = jedis;
	}
	
	@Override
	public void onEvent(Message message, long sequence, boolean endOfBatch) throws Exception {
		String[] urls = message.getUrl().split("/");
		String userid = urls[2];
		String courseid = urls[4];
		Map<String, String> course = jedis.hgetAll("course:" + courseid.substring(0,courseid.length()-2));//取出课程信息
		
		List<String> courseTable = jedis.lrange(userid + ":" + courseid, 0, 48);
		
		String time = course.get("time");
		int day = Integer.parseInt(time.substring(0, 0)), t = Integer.parseInt(time.substring(1, 1));
		if(courseTable.get(5*day+t) != null){
			message.setState(message.getState()+1^2);
		}
		
		int credit = Integer.parseInt(course.get("credit"));
		if(Integer.parseInt(courseTable.get(1)) + credit > 30){
			message.setState(message.getState()+2^2);
		}
		
	}
	
	public void test(){
		jedis.decr("coursepool:B100010201");
		System.out.println(jedis.get("coursepool:B100010201"));
	}

}
