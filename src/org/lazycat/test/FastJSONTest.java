package org.lazycat.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/*
public static final Object parse(String text); // 把JSON文本parse为JSONObject或者JSONArray
public static final JSONObject parseObject(String text)； // 把JSON文本parse成JSONObject
public static final <T> T parseObject(String text, Class<T> class); // 把JSON文本parse为JavaBean
public static final JSONArray parseArray(String text); // 把JSON文本parse成JSONArray
public static final <T> List<T> parseArray(String text, Class<T> class); //把JSON文本parse成JavaBean集合
public static final String toJSONString(Object object); // 将JavaBean序列化为JSON文本
public static final String toJSONString(Object object, boolean prettyFormat); // 将JavaBean序列化为带格式的JSON文本
public static final Object toJSON(Object javaObject); 将JavaBean转换为JSONObject或者JSONArray。
 * */

public class FastJSONTest {

	 public static void main(String[] args){
		 encode();
		 //System.out.println(Runtime.getRuntime().availableProcessors());
	 }
	 
	 public static void encode(){
		 Group group = new Group();
		 group.setId(0L);
		 group.setName("admin");

		 User guestUser = new User();
		 guestUser.setId(2L);
		 guestUser.setName("guest");

		 User rootUser = new User();
		 rootUser.setId(3L);
		 rootUser.setName("root");

		 group.getUsers().add(guestUser);
		 group.getUsers().add(rootUser);

		 String jsonString = JSON.toJSONString(group);
		 JSONObject jb = JSON.parseObject(jsonString);
		 System.out.println(jsonString);
		 
		 System.out.println(jb.toString());
		 
		 //decode(jsonString);
		 
		 
	 }
	 
	 public static void decode(String jsonString){
		 Group group2 = JSON.parseObject(jsonString, Group.class);
		 System.out.println("id:"+group2.getId()+"\nname:"+group2.getName()+"\nlist:"+group2.getUsers());
		 
	 }
}

class User {
	private Long id;
	private String name;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
}
class Group {
	private Long id;
	private String name;
	private List<User> users = new ArrayList<User>();

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public List<User> getUsers() { return users; }
	public void setUsers(List<User> users) { this.users = users; }
}