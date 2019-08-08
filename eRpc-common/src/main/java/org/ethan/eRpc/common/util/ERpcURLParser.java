package org.ethan.eRpc.common.util;

import java.util.HashMap;
import java.util.Map;

import org.ethan.eRpc.common.bean.Host;

import com.alibaba.fastjson.JSON;

public class ERpcURLParser {
	
	public static Map<String,String> getParameterMap(String url) {
		//eRpc://192.168.200.1:6666/application1.rpcTest1?version=1.0&timeout=10000
		Map<String,String>params = new HashMap<String, String>();
		if(!url.contains("?")) {
			return params;
		}
		String paramsStr = url.split("\\?")[1];
		if("".equals(paramsStr.trim())) {
			return params;
		}
 		for(String ent : paramsStr.split("&")) {
			if(ent.contains("=")) {
				String[]m = ent.split("=");
				params.put(m[0],"".contentEquals(m[1].trim())? null : m[1].trim());
			}else {
				params.put(ent, null);
			}
		}
		return params;
	}
	
	public static Host getHost(String hostInfoJSON){
		Map<String,Object>info = JSON.parseObject(hostInfoJSON, Map.class);
		Host host = new Host();
		host.setApplicationName((String)info.get("applicationName"));
		host.setHostName((String)info.get("host"));
		
		
		String url = ((String)info.get("url")).replace("eRpc://", "");
		String ipAndPort = url.split("/")[0];
		host.setIp(ipAndPort.split(":")[0]);
		host.setIp(ipAndPort.split(":")[1]);
		return host;
	}
	
	public static Map<String,Object> parseHostInfoJSON(String hostInfoJSON) {
		Map<String,Object>info = JSON.parseObject(hostInfoJSON, Map.class);
		
		Map<String,Object>result = new HashMap<String, Object>();
		Host host = new Host();
		host.setApplicationName((String)info.get("applicationName"));
		host.setHostName((String)info.get("host"));
		
		
		String url = ((String)info.get("url")).replace("eRpc://", "");
		String ipAndPort = url.split("/")[0];
		host.setIp(ipAndPort.split(":")[0]);
		host.setPort(Integer.valueOf(ipAndPort.split(":")[1]));
		
		result.put("host", host);
		result.put("params", getParameterMap(url));
		return result;
	}
	
	
	public static void main(String[] args) {
		System.out.println(ERpcURLParser.getParameterMap("eRpc://192.168.200.1:6666/application1.rpcTest1?version=1.0&timeout=10000"));
	}
}
