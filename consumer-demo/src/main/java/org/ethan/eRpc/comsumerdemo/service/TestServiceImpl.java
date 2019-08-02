package org.ethan.eRpc.comsumerdemo.service;

import java.util.Map;

import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.comsumerdemo.sao.TestSao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 业务类
 */
@Service
public class TestServiceImpl {
	
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
}
