package org.ethan.eRpc.comsumerdemo.sao;

import java.util.Map;

import org.ethan.eRpc.consumer.annotation.EParam;
import org.ethan.eRpc.consumer.annotation.EServceClient;
import org.ethan.eRpc.consumer.annotation.EService;

@EServceClient
public interface Test2Sao {
	
	public Map<String,Object>rpcTest2(String p1,int p2);
	
	@EService(name="rpcTest3",timeOut=5000,version="2.0")
	public Map<String,Object>test3(@EParam(name = "user")Map<String,Object>params);
}
