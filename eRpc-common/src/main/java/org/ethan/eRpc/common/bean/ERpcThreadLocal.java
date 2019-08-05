package org.ethan.eRpc.common.bean;

import java.util.HashMap;
import java.util.Map;

public class ERpcThreadLocal {
	private static ThreadLocal<Map<String,Object>>eLocal = new ThreadLocal<Map<String,Object>>();
	
	public static void add(String key,Object value) {
		if(eLocal.get() == null) {
			eLocal.set(new HashMap<String, Object>());
		}
		eLocal.get().put(key, value);
	}
	
	public static Object get(String key) {
		if(eLocal.get() == null) {
			return null;
		}
		return eLocal.get().get(key);
	}
	
	public static void clear() {
		if(eLocal.get() != null) {
			eLocal.get().clear();
		}
		eLocal.remove();
	}
}
