package org.lazycat.demo;

import org.lazycat.core.StoreObject;

import com.alibaba.fastjson.JSON;

public class OrderForm extends StoreObject{
	private long id;
	private String customID;
	private String bookFormID;
	private String state;
	
	public OrderForm(int id, String customID, String bookFormID, String state){
		this.id = id;
		this.customID = customID;
		this.bookFormID = bookFormID;
		this.state = state;
	}
	
	public OrderForm(String orderform){
		OrderForm of = JSON.parseObject(orderform,OrderForm.class);
		this.id = of.getId();
		this.customID = of.getCustomID();
		this.bookFormID = of.getBookFormID();
		this.state = of.getState();
	}
	
	public OrderForm(){
		super();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCustomID() {
		return customID;
	}
	public void setCustomID(String customID) {
		this.customID = customID;
	}
	public String getBookFormID() {
		return bookFormID;
	}
	public void setBookFormID(String bookFormID) {
		this.bookFormID = bookFormID;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
