package org.lazycat.core;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.*;
public class Message {
	
	public static final EventFactory<Message> EVENT_FACTORY = new MessageFactory();
	private String in;
	private String method;
	private String url;
	private String data;
	
	public String getIn() {
		return in;
	}
	public void setIn(String in) {
		this.in = in;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public Message(){
		super();
	}
	
	public void respawner(String in){
		this.in = in;
	}
	
	
}
class MessageFactory implements EventFactory<Message>{
	@Override
    public Message newInstance() {
        return new Message();
    }
}