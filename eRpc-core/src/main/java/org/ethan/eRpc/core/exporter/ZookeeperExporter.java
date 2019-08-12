package org.ethan.eRpc.core.exporter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.ethan.eRpc.common.bean.ServiceBean;
import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.common.net.NetUtil;
import org.ethan.eRpc.common.util.MDCUtil;
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
			String path =  ROOT+"/service/"+service.getName()+"/"+service.getVersion()+"/providers";
			ZkUtil.create(zk, path,new byte[0], CreateMode.PERSISTENT, new StringCallback() {
				
				@Override
				public void processResult(int rc, String path, Object ctx, String name) {
					switch (Code.get(rc)) {
					case CONNECTIONLOSS:
						try {
							MDCUtil.setTraceId((String)ctx);
							export(service);
						} catch (ERpcException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case OK:
						MDCUtil.setTraceId((String)ctx);
						exportProvider(service);
						break;
					case NODEEXISTS:
						MDCUtil.setTraceId((String)ctx);
						exportProvider(service);
						break;
					default:
						logger.error("something went wrong when export service ["+service.getName()+"]:"+KeeperException.create(Code.get(rc),path));
						break;
					}
				}
			}, MDCUtil.getTraceId());
		} catch (Exception e) {
			throw new ERpcException("Error occurs when export service"+JSON.toJSONString(service),e);
		}
	}
	
	private void exportProvider(ServiceBean service) {
		String path = null;
		try {
			path = ROOT+"/service/"+service.getName()+"/"+service.getVersion()+"/providers/"+NetUtil.getHostIP()+":"+PropertiesUtil.getConfig("port");
		} catch (ERpcException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ZkUtil.create(zk, path, JSON.toJSONString(getProviderDetail(service)).getBytes("UTF-8"), CreateMode.EPHEMERAL, new StringCallback() {
				
				@Override
				public void processResult(int rc, String path, Object ctx, String name) {
					switch (Code.get(rc)) {
					case CONNECTIONLOSS:
						MDCUtil.setTraceId((String)ctx);
						exportProvider(service);
						break;
					case OK:
						MDCUtil.setTraceId((String)ctx);
						logger.info("Exported service ["+service+"] to zookeeper");
						break;
					case NODEEXISTS:
						MDCUtil.setTraceId((String)ctx);
						logger.error("service["+service.getName()+"]with version["+service.getVersion()+"]has aleady exported by current host");
						throw new RuntimeException("service["+service.getName()+"]with version["+service.getVersion()+"]has aleady exported by current host");
					default:
						MDCUtil.setTraceId((String)ctx);
						KeeperException ke = KeeperException.create(Code.get(rc),path);
						logger.error("something went wrong when export service ["+service.getName()+"]:"+ke);
						throw new RuntimeException(ke);
					}
					
				}
			}, MDCUtil.getTraceId());
		} catch (UnsupportedEncodingException | ERpcException e) {
			// TODO Auto-generated catch block
			logger.error("Error occurs when exportProvider",e);
			throw new RuntimeException("Error occurs when exportProvider",e);
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
	
	private Map<String,Object>getProviderDetail(ServiceBean service) throws ERpcException{
		Map<String,Object>detail = new HashMap<String, Object>();
		detail.put("url", getERpcURL(service));
		detail.put("applicationName",applicationName);
		detail.put("host",NetUtil.getHostName());
		return detail;
	}
	
	public String getERpcURL(ServiceBean service) {
		String ip = service.getProviders().get(0).getIp();
		int port = service.getProviders().get(0).getPort();
		return "eRpc://"+ip+":"+port+"/"+applicationName+"."+service.getName()+"?version="+service.getVersion()+"&timeout="+service.getTimeOut();
	}
}
