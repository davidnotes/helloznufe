package org.lazycat.demo;

import org.lazycat.core.Message;

import redis.clients.jedis.Jedis;

import com.lmax.disruptor.EventHandler;

public class CoursePoolFilter implements EventHandler<Message>{
	private Jedis jedis;
	public void setJedis(Jedis jedis){
		this.jedis = jedis;
	}
	
	public CoursePoolFilter(){
		super();
	}
	
	public CoursePoolFilter(Jedis jedis){
		this.jedis = jedis;
	}
	
	@Override
	public void onEvent(Message message, long sequence, boolean endOfBatch) throws Exception {
		String courseid = message.getUrl().split("/")[4];
		if(Integer.parseInt(jedis.get("coursepool:" + courseid)) < 1){
			message.setState(message.getState()+1*2^3);
		}
	}
	
	public void test(){
		jedis.decr("coursepool:B100010201");
		System.out.println(jedis.get("coursepool:B100010201"));
	}
}
