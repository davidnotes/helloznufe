package org.lazycat.demo;
import org.lazycat.core.JSONModel;
import org.lazycat.core.Route;
import org.lazycat.core.StoreObject;

import com.alibaba.fastjson.JSON;

public class TestRoute implements Route{

	@Override
	public StoreObject route(JSONModel json) {
		switch(json.getHead()){
			case "orderform":
				return JSON.parseObject(json.getBody(),OrderForm.class);
				
			//else ToDo
			case "":
				return null;
			default:
				System.out.println("Route " + json.getHead() + " not found!");
				break;
		}
		return null;
	}

}
