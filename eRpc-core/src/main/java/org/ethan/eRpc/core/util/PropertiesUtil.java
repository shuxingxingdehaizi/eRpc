package org.ethan.eRpc.core.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
	private static Properties p = new Properties();
	
	static {
		try {
			p.load(PropertiesUtil.class.getResourceAsStream("/eRpc.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getConfig(String key) {
		return p.getProperty(key);
	}
	
	public static void main(String[] args) {
		System.out.println(p.get("serverKeyStore"));
	}
}
