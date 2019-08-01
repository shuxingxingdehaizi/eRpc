package org.ethan.eRpc.consumer.cache;

import org.ethan.eRpc.core.ERpcException;
import org.ethan.eRpc.core.util.PropertiesUtil;
import org.springframework.stereotype.Service;

public class ProviderCacheFactory {
	
	private static ProviderCache localCache;
	
	
	private static void initExporters() throws ERpcException{
		String registryCenterAddr = PropertiesUtil.getConfig("registryCenter");
		if(registryCenterAddr == null) {
			throw new ERpcException("registryCenter not found");
		}
		
		if(registryCenterAddr.startsWith("zookeeper://")) {
			localCache = new ProviderCacheZkImpl();
		}else if(registryCenterAddr.startsWith("nacos://")) {
			localCache = new ProviderCacheNacosImpl();
		}else {
			throw new ERpcException("Invalid registryCenter address");
		}
	}

	public static ProviderCache getProdiderCache() throws ERpcException {
		if(localCache == null) {
			initExporters();
		}
		return localCache;
	}
}
