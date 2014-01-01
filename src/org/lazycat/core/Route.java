package org.lazycat.core;

/*
 * 将Node传进来的json数据解析成StoreObject
 * json数据格式为{"head":"ObjectName","body":"类json值串"}
 * */
public interface Route {
	public StoreObject route(JSONModel json);
}
