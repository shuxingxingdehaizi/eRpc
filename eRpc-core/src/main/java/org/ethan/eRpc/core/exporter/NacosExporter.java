package org.ethan.eRpc.core.exporter;

import org.ethan.eRpc.common.bean.ServiceBean;
import org.ethan.eRpc.common.exception.ERpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 将服务暴露至远端(nacos注册中心)
 * @author Admin
 *
 */
public class NacosExporter implements ServiceExporter {

	static final Logger logger = LoggerFactory.getLogger(ServiceExporter.class);
	
	private String nacosAddr;
	
	protected NacosExporter(String nacosAddr) {
		this.nacosAddr = nacosAddr;
	}
	
	@Override
	public void export(ServiceBean service) {
		// TODO Auto-generated method stub
		logger.info("Export service ["+service+"] to nacos");
	}

	@Override
	public ServiceBean getServiceBean(String serviceName, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unexport(String serviceName) throws ERpcException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unexportAll() throws ERpcException {
		// TODO Auto-generated method stub
		
	}

}
