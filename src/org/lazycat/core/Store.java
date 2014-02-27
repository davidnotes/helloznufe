package org.lazycat.core;

import com.lmax.disruptor.EventHandler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class Store implements EventHandler<Message>{

	private JedisPool jedisPool = new JedisPool(new JedisPoolConfig(),"192.168.56.210");
	private EventServer eventServer;
	public Store(){
		super();
	}
	
	public Store(JedisPool jedisPool, EventServer eventServer){
		this.jedisPool = jedisPool;
		this.eventServer = eventServer;
	}
	
	public Jedis getJedis(){
		return jedisPool.getResource();
	}
	
	public void onEvent(Message message, long sequence, boolean endOfBatch){
		//课程表+1；
		//课程池-1
		//已选课+1
		Jedis jedis = null;
		//System.out.println(message.getState());
		if(message.getState() == 0){
			String[] urls = message.getUrl().split("/");
			String userid = urls[2];
			String courseid = urls[4];
			try{
				jedis = jedisPool.getResource();
				String time = jedis.hgetAll("course:" + courseid).get("time");
				int credit = Integer.parseInt(jedis.hgetAll("course:" + courseid).get("credit"));
				int creditPre = Integer.parseInt(jedis.hgetAll("coursetable:"+userid).get("credit"));
				jedis.hset("coursetable:"+userid, time, courseid);
				jedis.hset("coursetable:"+userid, "credit", String.valueOf(credit+creditPre));
				jedis.lpush("coursealready:"+userid, courseid.substring(0, courseid.length()-2));
				jedis.decr("coursepool:"+courseid);
				int num = Integer.parseInt(jedis.get("coursepool:"+courseid));
				if(num < 10){
					if(9 == num){
						jedis.lpush("courseleft:9", courseid);
					}else{
						jedis.lrem("courseleft:" + (num + 1), 0, courseid);
						jedis.lpush("courseleft:" + num, courseid);
					}
				}
				System.out.println("选课成功");
			}catch(JedisConnectionException e){
				// returnBrokenResource when the state of the object is unrecoverable
			    if (null != jedis) {
			        jedisPool.returnBrokenResource(jedis);
			        jedis = null;
			    }
			}finally{
				if(null != jedis)
					jedisPool.returnResource(jedis);
			}
			eventServer.response(message.getUrl()+"success");
			eventServer.setRes(true);
			//发消息给node端
			//message.getEventServer().response("success");
		}else{
			//发消息给Node端
			//message.getEventServer().response("failed, state = " + message.getState());
			eventServer.response(message.getUrl()+"fail");
			System.out.println("选课失败，state = " + message.getState());
			eventServer.setRes(true);
		}
	}
}
