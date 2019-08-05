package org.ethan.eRpc.consumer.invoke;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ethan.eRpc.common.bean.ERpcResponse;
import org.ethan.eRpc.common.exception.ERpcException;

public class FutureContainer {
	
	private static Map<String,ERpcFuture>resultMap = new ConcurrentHashMap<String, ERpcFuture>();
	
	public static void createFuture(long timeout, String eRpcId) {
		ERpcFuture future = new ERpcFuture(timeout,eRpcId);
		resultMap.put(eRpcId, future);
	}
	
	public static ERpcFuture getFuture(String eRpcId) {
		return resultMap.get(eRpcId);
	}
	
	public static ERpcResponse getResponse(String eRpcId) throws ERpcException {
		ERpcResponse resposne =  resultMap.get(eRpcId).get();
		resultMap.remove(eRpcId);
		return resposne;
	}
	
}
