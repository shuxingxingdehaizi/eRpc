package org.ethan.eRpc.consumer.cache;

import org.ethan.eRpc.common.bean.Host;
import org.springframework.stereotype.Service;

@Service
public class ProviderCacheZkImpl implements ProviderCache {

	@Override
	public Host getProvider(String serviceName, String version) {
		// TODO Auto-generated method stub
		Host host = new Host();
		host.setApplicationName("testErpcProviderServer");
		host.setHostName("pc-EthanZhao");
		host.setIp("127.0.0.1");
		host.setPort(6666);
		return host;
	}

}
