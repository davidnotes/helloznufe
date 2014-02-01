package org.lazycat.demo;

import org.lazycat.core.Message;

import com.lmax.disruptor.EventHandler;

public class RegisterFilter implements EventHandler<Message>{
	public void onEvent(Message message, long sequence, boolean endOfBatch) throws Exception {
		//注册完成！！！
	}
}
