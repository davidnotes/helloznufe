package org.lazycat.core;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.EventFactory;

public class Message {
	
	public static final EventFactory<Message> EVENT_FACTORY = new MessageFactory();
	private String in;
	private String method;
	private String url;
	private String data;
	private int state;
	
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
		JSONObject jb = JSON.parseObject(in);
		this.method = jb.getString("method");
		this.url = jb.getString("url");
		this.data = jb.getString("data");
		this.state = 0;
	}
	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}
	
	public String toString(){
		return "method : " + this.method + "; url : " + this.url + "; data : " + this.data;
	}
}
class MessageFactory implements EventFactory<Message>{
	@Override
    public Message newInstance() {
        return new Message();
    }
}