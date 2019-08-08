package org.ethan.eRpc.consumer.cache;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.common.util.PropertiesUtil;


public class ProviderCacheFactory {
	
	private static ProviderCache localCache;
	
	
	private static void initExporters() throws ERpcException{
		String registryCenterAddr = PropertiesUtil.getConfig("registryCenter");
		if(registryCenterAddr == null) {
			throw new ERpcException("registryCenter not found");
		}
		
		if(registryCenterAddr.startsWith("zookeeper://")) {
			try {
				localCache = new ProviderCacheZkImpl(registryCenterAddr.replace("zookeeper://", ""));
			} catch (IOException | KeeperException | InterruptedException e) {
				throw new ERpcException(e);
			}
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
