package org.ethan.eRpc.common.zk;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;


public class ZkClient {
	
	private static ZooKeeper client;
	
	private static void init(String zkAddr) throws IOException {
		client = new ZooKeeper(zkAddr, 10000, new Watcher() {
				@Override
				public void process(WatchedEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	}
	
	public static ZooKeeper getClient(String zkAddr) throws IOException {
		if(client == null) {
			init(zkAddr);
		}
		return client;
	}
}
