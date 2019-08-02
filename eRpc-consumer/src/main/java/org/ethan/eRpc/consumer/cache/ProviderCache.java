package org.ethan.eRpc.consumer.cache;

import org.ethan.eRpc.common.bean.Host;

/**
 * 远端服务在本地的镜像
 * @author Admin
 *
 */
public interface ProviderCache {
	public Host getProvider(String serviceName,String version);
}
