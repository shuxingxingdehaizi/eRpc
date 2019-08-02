package org.ethan.eRpc.core.exporter;

import java.util.concurrent.ConcurrentHashMap;

import org.ethan.eRpc.common.bean.ServiceBean;
import org.ethan.eRpc.common.exception.ERpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将服务暴露到本地
 * @author Admin
 *
 */
public class LocalExporter implements ServiceExporter{

	static final Logger logger = LoggerFactory.getLogger(ServiceExporter.class);
	
	private ConcurrentHashMap<String, ConcurrentHashMap<String,ServiceBean>>localRegistMap = new ConcurrentHashMap<String, ConcurrentHashMap<String,ServiceBean>>();
	
	protected LocalExporter() {}
			
	@Override
	public synchronized void export(ServiceBean service) throws ERpcException{
		// TODO Auto-generated method stub
		if(service == null) {
			logger.error("Service that to be export is null");
			throw new ERpcException("Service that to be export is null");
		}
		if(service.getName() == null || "".equals(service.getName().trim())) {
			logger.error("Service that to be export don't set name");
			throw new ERpcException("Service that to be export don't set name");
		}
		if(localRegistMap.get(service.getName()) != null) {
			if(localRegistMap.get(service.getName()).get(service.getVersion())!= null) {
				logger.error("Service ["+service.getName()+"] version["+service.getVersion()+"] has been exported twice!");
				throw new ERpcException("Service ["+service.getName()+"] version["+service.getVersion()+"] has been exported twice!");
			}
		}else {
			localRegistMap.put(service.getName(), new ConcurrentHashMap<String, ServiceBean>());
		}
		localRegistMap.get(service.getName()).put(service.getVersion(), service);
		logger.info("Export service ["+service+"] to local");
	}

	@Override
	public synchronized void unexport(String serviceName) throws ERpcException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unexportAll() throws ERpcException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServiceBean getServiceBean(String serviceName, String version) {
		// TODO Auto-generated method stub
		if(localRegistMap.get(serviceName) == null) {
			return null;
		}
		return localRegistMap.get(serviceName).get(version);
	}

}
