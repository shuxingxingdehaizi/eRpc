package org.ethan.eRpc.core.exporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.common.util.PropertiesUtil;
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
			try {
				remoteExpoeter = new ZookeeperExporter(registryCenterAddr.replace("zookeeper://", ""));
			} catch (IOException | KeeperException | InterruptedException e) {
				// TODO Auto-generated catch block
				throw new ERpcException("Error occurs when get ZookeeperExporter",e);
			}
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

	public ServiceExporter getLocalExporter() throws ERpcException {
		if(localExporter == null) {
			initExporters();
		}
		return localExporter;
	}

	public ServiceExporter getRemoteExpoeter() throws ERpcException {
		if(remoteExpoeter == null) {
			initExporters();
		}
		return remoteExpoeter;
	}
	
	
}
