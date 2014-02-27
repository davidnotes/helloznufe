package org.lazycat.test;

import org.lazycat.core.Message;

import com.lmax.disruptor.EventTranslator;

public class MessageTranslator implements EventTranslator<Message>{
	private String in;

	public void setIn(String in){
		this.in = in;
	}

	@Override
	public void translateTo(Message message, long sequence) {
		// TODO Auto-generated method stub
		message.respawner(in);
	    //System.out.println("mt:"+message.getIn());
	}

}
