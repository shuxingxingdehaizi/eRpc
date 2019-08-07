package org.ethan.eRpc.core.exporter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.ethan.eRpc.common.bean.ServiceBean;
import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.common.net.NetUtil;
import org.ethan.eRpc.common.util.PropertiesUtil;
import org.ethan.eRpc.common.zk.ZkClient;
import org.ethan.eRpc.common.zk.util.ZkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 将服务暴露到远端(zookeeper注册中心)
 * @author Admin
 * 
 * Zookeeper数据结构
 * /eRpc
 *		/service
 *				/service1(永久节点)---服务名
 *						/version(永久节点)--版本号
	 *							/provider(永久节点)--生产者列表
	 *									/provider1(临时节点 节点名为ip+port，节点值为系统ip+eRpc端口+timeout+version)
	 *									/provider2...
		 *						/consumer(永久节点)
		 *								/consumer1(永久节点，节点名为应用名，节点值为系统IP)
		 *								/cunsomer2...
 *				/service2
 *				/...
 *							
 *
 */
public class ZookeeperExporter implements ServiceExporter {
	
	private ZooKeeper zk;
	
	private static final String ROOT = "/eRpc";
	
	private String applicationName = PropertiesUtil.getConfig("applicationName");
	
	@Override
	public ServiceBean getServiceBean(String serviceName, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	static final Logger logger = LoggerFactory.getLogger(ServiceExporter.class);
	
	
	protected ZookeeperExporter(String zkAddr) throws IOException, KeeperException, InterruptedException {
		zk = ZkClient.getClient(zkAddr);
		createParentNode();
	}
	
	private void createParentNode() throws KeeperException, InterruptedException {
		try {
			ZkUtil.create(zk, ROOT+"/service", CreateMode.PERSISTENT);
		} catch (NodeExistsException e) {
			// TODO: handle exception
		}
	}

	@Override
	public void export(ServiceBean service) throws ERpcException{
		// TODO Auto-generated method stub
		try {
			String path =  ROOT+"/service/"+service.getName()+"/"+service.getVersion()+"/provider";
			ZkUtil.create(zk, path,new byte[0], CreateMode.PERSISTENT, new StringCallback() {
				
				@Override
				public void processResult(int rc, String path, Object ctx, String name) {
					switch (Code.get(rc)) {
					case CONNECTIONLOSS:
						try {
							export((ServiceBean)ctx);
						} catch (ERpcException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case OK:
						exportProvider(service);
						break;
					case NODEEXISTS:
						logger.info("Node "+path+" aleady exists");
						break;
					default:
						logger.error("something went wrong when export service ["+service.getName()+"]:"+KeeperException.create(Code.get(rc),path));
						break;
					}
				}
			},service);
		} catch (Exception e) {
			throw new ERpcException("Error occurs when export service"+JSON.toJSONString(service),e);
		}
	}
	
	private void exportProvider(ServiceBean service) {
		String path = null;
		try {
			path = ROOT+"/service/"+service.getName()+"/"+service.getVersion()+"/provider/"+NetUtil.getHostIP()+":"+PropertiesUtil.getConfig("port");
		} catch (ERpcException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ZkUtil.create(zk, path, getERpcURL(service).getBytes("UTF-8"), CreateMode.EPHEMERAL, new StringCallback() {
				
				@Override
				public void processResult(int rc, String path, Object ctx, String name) {
					switch (Code.get(rc)) {
					case CONNECTIONLOSS:
						exportProvider((ServiceBean)ctx);
						break;
					case OK:
						logger.info("Exported service ["+service+"] to zookeeper");
						break;
					case NODEEXISTS:
						logger.info("Node "+path+" aleady exists");
						break;
					default:
						logger.error("something went wrong when export service ["+service.getName()+"]:"+KeeperException.create(Code.get(rc),path));
						break;
					}
					
				}
			}, service);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private void create(String path) {
		for(String node : path.split("/")) {
			if("".contentEquals(node)) {
				continue;
			}
			
		}
	}

	@Override
	public void unexport(String serviceName) throws ERpcException {
		// TODO Auto-generated method stub	
//		zk.delete(ROOT+"/service/"+serviceName+"/provider/", version, cb, ctx);
	}

	@Override
	public void unexportAll() throws ERpcException {
		// TODO Auto-generated method stub
		
	}
	
	public String getERpcURL(ServiceBean service) {
		String ip = service.getProviders().get(0).getIp();
		int port = service.getProviders().get(0).getPort();
		return "eRpc://"+ip+":"+port+"/"+applicationName+"."+service.getName()+"?version="+service.getVersion()+"&timeout="+service.getTimeOut();
	}
}
