package org.ethan.eRpc.consumer.invoke;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FutureContainer {
	
	private static Map<String,ERpcFuture>resultMap = new ConcurrentHashMap<String, ERpcFuture>();
	
	public static ERpcFuture createFuture(long timeout, String eRpcId) {
		ERpcFuture future = new ERpcFuture(timeout,eRpcId);
		resultMap.put(eRpcId, future);
		return future;
	}
	
	public static ERpcFuture getFuture(String eRpcId) {
		return resultMap.get(eRpcId);
	}
	
}
