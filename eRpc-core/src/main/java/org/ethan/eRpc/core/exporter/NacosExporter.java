package org.ethan.eRpc.core.exporter;

import org.ethan.eRpc.core.bean.ServiceBean;

/**
 * 将服务暴露至远端(nacos注册中心)
 * @author Admin
 *
 */
public class NacosExporter implements ServiceExporter {

	private String nacosAddr;
	
	protected NacosExporter(String nacosAddr) {
		this.nacosAddr = nacosAddr;
	}
	
	@Override
	public void export(ServiceBean service) {
		// TODO Auto-generated method stub

	}

}
