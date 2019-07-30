package org.ethan.eRpc.core.exporter;

import org.ethan.eRpc.core.bean.ServiceBean;

public interface ServiceExporter {
	
	public void export(ServiceBean service);
	
}
