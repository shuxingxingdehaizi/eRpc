package org.ethan.eRpc.core.exporter;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.ethan.eRpc.common.bean.ServiceBean;
import org.ethan.eRpc.common.exception.ERpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 将服务暴露到远端(zookeeper注册中心)
 * @author Admin
 *
 */
public class ZookeeperExporter implements ServiceExporter {
	
	@Override
	public ServiceBean getServiceBean(String serviceName, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	static final Logger logger = LoggerFactory.getLogger(ServiceExporter.class);
	
	private String zkAddr;
	
	protected ZookeeperExporter(String zkAddr) {
		this.zkAddr = zkAddr;
	}

	@Override
	public void export(ServiceBean service) {
		// TODO Auto-generated method stub
		logger.info("Export service ["+service+"] to zookeeper");
	}

	@Override
	public void unexport(String serviceName) throws ERpcException {
		// TODO Auto-generated method stub	
	}

	@Override
	public void unexportAll() throws ERpcException {
		// TODO Auto-generated method stub
		
	}
	
	private void getZkClient() throws IOException {
		ZooKeeper zkClient = new ZooKeeper(zkAddr, 10000, new Watcher() {
				@Override
				public void process(WatchedEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
//		zkClient.co
	}

}
