package org.ethan.eRpc.comsumerdemo.controller;

import org.ethan.eRpc.comsumerdemo.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@Autowired
	private TestServiceImpl testService;
	
	@RequestMapping("/test1")
	public Object eRpcTest(String p1) {
		return testService.test1(p1);
	}
}
