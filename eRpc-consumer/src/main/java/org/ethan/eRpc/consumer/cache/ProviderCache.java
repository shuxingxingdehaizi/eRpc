package org.ethan.eRpc.consumer.cache;

import org.ethan.eRpc.core.bean.ServiceBean.Host;

/**
 * 远端服务在本地的镜像
 * @author Admin
 *
 */
public interface ProviderCache {
	public Host getProvider(String serviceName,String version);
}
