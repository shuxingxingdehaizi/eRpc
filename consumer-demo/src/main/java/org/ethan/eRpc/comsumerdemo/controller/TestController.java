package org.ethan.eRpc.comsumerdemo.controller;

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
	private BeanTestService beanTestService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/test1")
	public Object eRpcTest(String p1) {
		return testService.test1(p1);
	}
	
	@RequestMapping("/beanTest1")
	public Object eRpcTest1() {
		return testService.beanTest1();
	}
	
	@RequestMapping("/beanTest2")
	public Object eRpcTest2() {
		return beanTestService.beanTest();
	}
	
	@RequestMapping("/beanTest3")
	public Object eRpcTest3() {
		return userService.addUser();
	}
}
