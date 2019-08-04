package org.ethan.eRpc.providerdemo.controller;

import java.util.HashMap;
import java.util.Map;

import org.ethan.eRpc.core.annotation.EService;
import org.ethan.eRpc.providerdemo.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	
	@RequestMapping("/test1")
	@EService(name="rpcTest1",verion="1.0")
	public Map<String,Object>test1(String param1){
		Map<String,Object> result = new HashMap<String, Object>();
		
		result.put("responseCode", "000000");
		result.put("resultMsg", "success");
		result.put("data", "Hi there,This is rpcTest1 provider, you just said :"+param1);
		return result;
	}
	
	@RequestMapping("/test2")
	@EService(name="rpcTest2",verion="1.0")
	public Map<String,Object>test2(String p1,int p2){
		Map<String,Object> result = new HashMap<String, Object>();
		
		result.put("responseCode", "000000");
		result.put("resultMsg", "success");
		result.put("data", "Hi there,This is rpcTest2 provider, you just said :"+p1+"--"+p2);
		return result;
	}
	
	@RequestMapping("/test3")
	@EService(name="rpcTest3",verion="2.0")
	public Map<String,Object>test3(User user){
		Map<String,Object> result = new HashMap<String, Object>();
		
		result.put("responseCode", "000000");
		result.put("resultMsg", "success");
		result.put("data", "Hello "+user.getName()+", your age is "+user.getAge());
		return result;
	}
}
