package org.lazycat.core;

import org.zeromq.ZMQ;
/*
 * The ZMQ server used in the write part.
 * */
public class EventServer {
	
	private ZMQ.Context context;
    private ZMQ.Socket  socket;
    private boolean res = false;
    
    public EventServer(){
    	context = ZMQ.context(1);
    	socket  = context.socket(ZMQ.REP);
    	socket.bind("tcp://*:5555");
    	res = false;
    }
    
    public EventServer(String bindStrategy){
    	context = ZMQ.context(1);
    	socket  = context.socket(ZMQ.REP);
    	socket.bind(bindStrategy);
    	res = false;
    }
	
    public boolean getRes(){
    	return res;
    }
    
    public void setRes(boolean res){
    	this.res = res;
    }
    
	public void response(String res){
		socket.send(res, 0);
	}
	
	public String request(){
		byte[] req = socket.recv(0);
		return new String(req);
	}
}
