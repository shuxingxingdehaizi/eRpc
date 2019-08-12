package org.ethan.eRpc.common.util;

import java.util.Random;

import org.ethan.eRpc.common.bean.ERpcThreadLocal;

public class MDCUtil {
	private static final int LENGTH = 15;
	
	public static void setTraceId() {
		ERpcThreadLocal.add("traceId", getRandomStr());
	}
	
	public static void setTraceId(String traceId) {
		ERpcThreadLocal.add("traceId", traceId);
	}
	
	public static String getTraceId() {
		return (String)ERpcThreadLocal.get("traceId");
	}
	
	private static String getRandomStr() {
		char[] ca = {'1','2','3','4','5','6','7','8','9','0',
					'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
					'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		char[] result = new char[LENGTH];
		Random r = new Random();
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
