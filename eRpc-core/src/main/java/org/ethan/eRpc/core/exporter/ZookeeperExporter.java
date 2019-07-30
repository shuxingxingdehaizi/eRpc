package org.ethan.eRpc.core.exporter;

import org.ethan.eRpc.core.bean.ServiceBean;

/**
 * 将服务暴露到远端(zookeeper注册中心)
 * @author Admin
 *
 */
public class ZookeeperExporter implements ServiceExporter {
	
	private String zkAddr;
	
	protected ZookeeperExporter(String zkAddr) {
		this.zkAddr = zkAddr;
	}

	@Override
	public void export(ServiceBean service) {
		// TODO Auto-generated method stub

	}

}
