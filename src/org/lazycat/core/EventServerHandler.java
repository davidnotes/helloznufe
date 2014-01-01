package org.lazycat.core;

import com.lmax.disruptor.EventHandler;

public class EventServerHandler implements EventHandler<EventServer>{

	@Override
	public void onEvent(EventServer eventServer, long sequence, boolean endOfBatch)
			throws Exception {
		// TODO Auto-generated method stub
		if(eventServer.getRes()){
			eventServer.response("");
		}else{
			eventServer.request();
		}
		
	}
	

}
