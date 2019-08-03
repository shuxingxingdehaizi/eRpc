package org.ethan.eRpc.comsumerdemo.sao;

import java.util.Map;

import org.ethan.eRpc.consumer.annotation.EServceClient;

@EServceClient
public interface Test2Sao {
	
	public Map<String,Object>rpcTest2(String p1,int p2);
	
}
