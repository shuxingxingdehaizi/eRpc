package org.ethan.eRpc.core.exporter;

import org.ethan.eRpc.core.ERpcException;
import org.ethan.eRpc.core.bean.ServiceBean;

public interface ServiceExporter {
	
	public void export(ServiceBean service) throws ERpcException;
	
	
	public void unexport(String serviceName) throws ERpcException;
	
	
	public void unexportAll() throws ERpcException;
	
	public ServiceBean getServiceBean(String serviceName,String version);
	
}
