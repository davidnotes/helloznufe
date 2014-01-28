package org.lazycat.core;

import com.lmax.disruptor.EventHandler;

public class FilterHandler implements EventHandler<Message>{
	public void onEvent(Message message, long sequence, boolean endOfBatch){
		System.out.println(sequence + message.getIn());
	}
}
