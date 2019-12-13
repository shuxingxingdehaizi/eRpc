package org.ethan.eRpc.common.util;


import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class MDCUtil {
	private static ThreadLocal<Map<String,Object>>sLocal = new ThreadLocal<Map<String,Object>>();
	
	private static final int LENGTH = 15;
	
	public static String getAndsetTraceId() {
		if(sLocal.get() == null) {
			sLocal.set(new HashMap<String, Object>());
		}
		String traceId = RandomUtils.getRandomString(LENGTH, RandomStrType.TYPE_MIX_NUMERIC_CHAR);
		sLocal.get().put("traceId", traceId);
		
		return traceId;
	}
	
	public static void setTraceId(String traceId) {
		if(sLocal.get() == null) {
			sLocal.set(new HashMap<String, Object>());
		}
		sLocal.get().put("traceId", traceId);
	}
	
	public static String getTraceId() {
		if(sLocal.get() == null) {
			return null;
		}
		return (String)sLocal.get().get("traceId");
	}
	
	public static void clear() {
		sLocal.remove();
	}
	
	private static String getRandomStr() {
		char[] ca = {'1','2','3','4','5','6','7','8','9','0',
					'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
					'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		char[] result = new char[LENGTH];
		SecureRandom r = new SecureRandom();
		for(int i=0;i<LENGTH;i++) {
			result[i] = ca[r.nextInt(ca.length)];
		}
		return new String(result);
	}
	
	public static void main(String[] args) {
		for(int i=0;i<100;i++) {
			System.out.println(getRandomStr());
		}
		
	}
}
