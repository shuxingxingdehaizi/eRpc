package org.ethan.eRpc.comsumerdemo.controller;

import java.util.Map;

import org.ethan.eRpc.comsumerdemo.sao.Test2Sao;
import org.ethan.eRpc.comsumerdemo.service.TestServiceImpl;
import org.ethan.eRpc.consumer.porxy.BeanTestService;
import org.ethan.eRpc.consumer.porxy.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@Autowired
	private TestServiceImpl testService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/test1")
	public Object eRpcTest(String p1) {
		return testService.test1(p1);
	}
	
	@RequestMapping("/test2")
	public Object eRpcTest2(String p1,int p2) {
		return testService.test2(p1,p2);
	}
	
	@RequestMapping("/beanTest3")
	public Object eRpcTest3() {
		return userService.addUser();
	}
}
