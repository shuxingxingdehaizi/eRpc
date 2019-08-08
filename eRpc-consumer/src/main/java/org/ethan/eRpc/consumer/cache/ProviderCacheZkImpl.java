package org.ethan.eRpc.consumer.cache;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.ethan.eRpc.common.bean.Host;
import org.ethan.eRpc.common.util.ERpcURLParser;
import org.ethan.eRpc.common.util.PropertiesUtil;
import org.ethan.eRpc.common.zk.ZkClient;
import org.ethan.eRpc.common.zk.util.ZkUtil;
import org.ethan.eRpc.consumer.loadbalance.ERpcConsumerLB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProviderCacheZkImpl implements ProviderCache {
	
	private static final Logger logger = LoggerFactory.getLogger(ProviderCacheZkImpl.class);
	/**
	 * 监听zk节点，更新本地缓存，获取服务时，直接从cache获取
	 * Cache结构如下：
	 * 
	 * serviceName
	 * 			  version1
	 * 						providers
	 * 								Host1 Host2...
	 * 						timeout
	 * 
	 * 			  version2
	 * 
	 * 
	 */
	private Map<String,Map<String,Map<String,Object>>>cache = new ConcurrentHashMap<String,Map<String,Map<String,Object>>>();
	
	private ZooKeeper zk;
	
	private static final String ROOT = "/eRpc";
	
	/**
	 * eRpc负载均衡算法
	 */
	private String loadbalance = PropertiesUtil.getConfig("loadbalance") == null ? "RoundRobin" : PropertiesUtil.getConfig("loadbalance");
	
	
	public ProviderCacheZkImpl(String zkAddr) throws IOException, KeeperException, InterruptedException {
		zk = ZkClient.getClient(zkAddr);
		createParentNode();
		getServiceList();
	}
	
	private void createParentNode() throws KeeperException, InterruptedException {
		try {
			ZkUtil.create(zk, ROOT+"/service", CreateMode.PERSISTENT);
		} catch (NodeExistsException e) {
			// TODO: handle exception
		}
	}
	
	private void getServiceList() {
		String serviceBasePath = ROOT+"/service";
		zk.getChildren(serviceBasePath, new Watcher() {
			public void process(WatchedEvent event) {
				assert serviceBasePath.equals(event.getPath());
				getServiceList();
			}
			
		}, new ChildrenCallback() {
			public void processResult(int rc, String path, Object ctx, List<String> children) {
				switch (Code.get(rc)) {
				case CONNECTIONLOSS:
					getServiceList();
					break;
				case OK:
					getService(children);
					break;
				default:
					KeeperException e = KeeperException.create(Code.get(rc));
					logger.error("getServiceList failed",e);
					throw new RuntimeException(e);
				}
			}
		}, null);
	}
	
	private void getService(List<String>serviceNames) {
		if(serviceNames == null || serviceNames.isEmpty()) {
			return;
		}
		for(String serviceName : serviceNames) {
			if(!cache.containsKey(serviceName)) {
				cache.put(serviceName, new ConcurrentHashMap<String,Map<String,Object>>());
			}
			getVersion(serviceName);
		}
	}
	
	private void getVersion(String serviceName) {
		String versionPath = ROOT+"/service/"+serviceName;
		zk.getChildren(versionPath, new Watcher() {
			public void process(WatchedEvent event) {
				assert versionPath.equals(event.getPath());
				getVersion(serviceName);
			}
			
		}, new ChildrenCallback() {
			public void processResult(int rc, String path, Object ctx, List<String> children) {
				switch (Code.get(rc)) {
				case CONNECTIONLOSS:
					getVersion(serviceName);
					break;
				case OK:
					if(children != null && !children.isEmpty()) {
						for(String version : children) {
							getProviders(serviceName,version);
						}
					}
					break;
				default:
					KeeperException e = KeeperException.create(Code.get(rc));
					logger.error("getVersion failed",e);
					throw new RuntimeException(e);
				}
			}
		}, null);
	}
	
	private void getProviders(String serviceName,String version) {
		String providersBasePath = ROOT+"/service/"+serviceName+"/"+version+"/providers";
		zk.getChildren(providersBasePath, new Watcher() {
			public void process(WatchedEvent event) {
				assert providersBasePath.equals(event.getPath());
				getProviders(serviceName,version);
			}
			
		}, new ChildrenCallback() {
			public void processResult(int rc, String path, Object ctx, List<String> children) {
				switch (Code.get(rc)) {
				case CONNECTIONLOSS:
					getProviders(serviceName,version);
					break;
				case OK:
					removedProvider(serviceName,version,children);
					if(children != null && !children.isEmpty()) {
						for(String provider : children) {
							getProviderInfo(serviceName,version,provider);
						}
					}
					break;
				default:
					KeeperException e = KeeperException.create(Code.get(rc));
					logger.error("getProviders failed",e);
					throw new RuntimeException(e);
				}
			}
		}, null);
	}
	
	private void removedProvider(String serviceName,String version,List<String> providerList) {
		List<String>oldProviders = cache.get(serviceName).get(version) == null ? null : (List)cache.get(serviceName).get(version).get("providers");
		if(oldProviders == null || oldProviders.isEmpty()) {
			return;
		}
		if(providerList == null || providerList.isEmpty()) {
			cache.get(serviceName).get(version).clear();
		}else {
			for(String oldProvider : oldProviders) {
				if(!providerList.contains(oldProvider)) {
					oldProviders.remove(oldProvider);
				}
			}
			cache.get(serviceName).get(version).put("providers", oldProviders);
		}
	}
	
	private void getProviderInfo(String serviceName,String version,String provider) {
		String providerPath = ROOT+"/service/"+serviceName+"/"+version+"/providers/"+provider;
		zk.getData(providerPath, false, new DataCallback() {
			public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
				// TODO Auto-generated method stub
				switch (Code.get(rc)) {
				case CONNECTIONLOSS:
					getProviderInfo(serviceName,version,provider);
					break;
				case OK:
					try {
						String jsonStr = new String(data,"UTF-8");
						if(cache.get(serviceName).get(version) == null) {
							cache.get(serviceName).put(version, new ConcurrentHashMap<String, Object>());
						}
						if(cache.get(serviceName).get(version).get("providers") == null) {
							cache.get(serviceName).get(version).put("providers", new ArrayList<String>());
						}
						Map<String,Object>info = ERpcURLParser.parseHostInfoJSON(jsonStr);
						((List)cache.get(serviceName).get(version).get("providers")).add(info.get("host"));
						cache.get(serviceName).get(version).put("timeout",((Map)info.get("params")).get("timeout"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						throw new RuntimeException(e);
					}
					break;
				default:
					KeeperException e = KeeperException.create(Code.get(rc));
					logger.error("getProviderInfo failed",e);
					throw new RuntimeException(e);
				}
			}
		}, null);
	}
	
	@Override
	public Host getProvider(String serviceName, String version) {
		// TODO Auto-generated method stub
		List<Host>providers = (List)cache.get(serviceName).get(version).get("providers");
		if(providers == null || providers.isEmpty()) {
			return null;
		}
		return providers.get(ERpcConsumerLB.getIndex(serviceName, providers.size(),loadbalance));
	}
}
