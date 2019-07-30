package org.ethan.eRpc.core.exporter;

import java.util.ArrayList;
import java.util.List;

import org.ethan.eRpc.core.ERpcException;
import org.ethan.eRpc.core.util.PropertiesUtil;
import org.springframework.stereotype.Service;

@Service
public class ExporterFactory {
	
	public List<ServiceExporter>getExporters()throws ERpcException{
		String registryCenterAddr = PropertiesUtil.getConfig("registryCenter");
		if(registryCenterAddr == null) {
			throw new ERpcException("registryCenter not found");
		}
		
		List<ServiceExporter>exporters = new ArrayList<ServiceExporter>();
		exporters.add(new LocalExporter());
		
		if(registryCenterAddr.startsWith("zookeeper:\\")) {
			exporters.add(new ZookeeperExporter(registryCenterAddr));
		}else if(registryCenterAddr.startsWith("nacos:\\")) {
			exporters.add(new NacosExporter(registryCenterAddr));
		}else {
			throw new ERpcException("Invalid registryCenter address");
		}
		return exporters;
	}
}
