package org.ethan.eRpc.core.exporter;

import org.ethan.eRpc.common.bean.ServiceBean;
import org.ethan.eRpc.common.exception.ERpcException;

public interface ServiceExporter {
	
	public void export(ServiceBean service) throws ERpcException;
	
	
	public void unexport(String serviceName) throws ERpcException;
	
	
	public void unexportAll() throws ERpcException;
	
	public ServiceBean getServiceBean(String serviceName,String version);
	
}
