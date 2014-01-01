package org.lazycat.core;

import com.lmax.disruptor.EventFactory;

public class EventServerFactory implements EventFactory<EventServer>{

	@Override
	public EventServer newInstance() {
		// TODO Auto-generated method stub
		return new EventServer();
	}
	
}
