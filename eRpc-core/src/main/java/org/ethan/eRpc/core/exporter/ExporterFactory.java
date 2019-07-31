package org.ethan.eRpc.core.exporter;

import java.util.ArrayList;
import java.util.List;

import org.ethan.eRpc.core.ERpcException;
import org.ethan.eRpc.core.util.PropertiesUtil;
import org.springframework.stereotype.Service;

@Service
public class ExporterFactory {
	
	private static ServiceExporter localExporter;
	
	private static ServiceExporter remoteExpoeter;
	
	private void initExporters() throws ERpcException{
		String registryCenterAddr = PropertiesUtil.getConfig("registryCenter");
		if(registryCenterAddr == null) {
			throw new ERpcException("registryCenter not found");
		}
		localExporter = new LocalExporter();
		
		if(registryCenterAddr.startsWith("zookeeper://")) {
			remoteExpoeter = new ZookeeperExporter(registryCenterAddr);
		}else if(registryCenterAddr.startsWith("nacos://")) {
			remoteExpoeter = new NacosExporter(registryCenterAddr);
		}else {
			throw new ERpcException("Invalid registryCenter address");
		}
	}
	
	public List<ServiceExporter>getExporters()throws ERpcException{
		if(localExporter == null) {
			initExporters();
		}
		
		List<ServiceExporter> exporters = new ArrayList<ServiceExporter>();
		exporters.add(localExporter);
		exporters.add(remoteExpoeter);
		return exporters;
	}

	public ServiceExporter getLocalExporter() {
		return localExporter;
	}

	public ServiceExporter getRemoteExpoeter() {
		return remoteExpoeter;
	}
	
	
}
