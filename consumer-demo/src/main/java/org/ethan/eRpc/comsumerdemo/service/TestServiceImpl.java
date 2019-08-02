package org.ethan.eRpc.comsumerdemo.service;

import java.util.Map;

import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.comsumerdemo.sao.TestSao;
import org.ethan.eRpc.consumer.porxy.BeanTestService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * 业务类
 */
@Service
public class TestServiceImpl implements ApplicationContextAware{
	
	ApplicationContext applicationContext;
	
	@Autowired
	@Qualifier("testSao")
	private TestSao testSao;
	
	public Map<String,Object>test1(String p1){
		try {
			return testSao.test1(p1);
		} catch (ERpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String beanTest1() {
		return ((BeanTestService)applicationContext.getBean("beanTestService")).beanTest();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}
}
