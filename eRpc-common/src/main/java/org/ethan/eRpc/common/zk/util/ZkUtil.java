package org.ethan.eRpc.common.zk.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public class ZkUtil {

	static final Logger logger = LoggerFactory.getLogger(ZkUtil.class);
	
	public static void create(ZooKeeper client,String path,byte[] content,CreateMode createMode,StringCallback calllback,Object context) {
		Map<String,Object>realContext = new HashMap<String, Object>();
		realContext.put("origin", context);
		realContext.put("path", path);
		create(client,path,content,createMode,calllback,realContext,1);
	}
	
	private static void create(ZooKeeper client, String path, byte[] content, CreateMode createMode, StringCallback calllback,Map<String,Object> context, int index) {
		String[] paths = path.split("/");
		String resultPath = "";
		for(int i=0;i<index;i++) {
			resultPath += "/"+paths[i+1];
		}
		String tp = resultPath;
		StringCallback stringCallback = null;
		byte[] currentContent = new byte[0];
		if(index >= paths.length-1) {
			stringCallback = calllback;
			currentContent = content;
		}else {
			stringCallback = new StringCallback() {
				@Override
				public void processResult(int rc, String path, Object ctx, String name) {
					// TODO Auto-generated method stub
					switch (Code.get(rc)) {
					case CONNECTIONLOSS:
						create(client,(String)context.get("path"),content,createMode,calllback,context,index);
						break;
					case OK:
						create(client,(String)context.get("path"),content,createMode,calllback,context,index+1);
						break;
					case NODEEXISTS:
						create(client,(String)context.get("path"),content,createMode,calllback,context,index+1);
						break;
					default:
						logger.error("something went create ["+tp+"]:"+KeeperException.create(Code.get(rc),tp));
						break;
					}
				}
			};
		}
		if(index >= paths.length-1) {
			client.create(tp, currentContent, Ids.OPEN_ACL_UNSAFE, createMode, stringCallback , context.get("origin"));
		}else {
			client.create(tp, currentContent, Ids.OPEN_ACL_UNSAFE, createMode, stringCallback , context);
		}
	}
	
	public static void create(ZooKeeper client,String path,CreateMode createMode) throws KeeperException, InterruptedException {
		create(client,path, new byte[0], createMode);
	}
	
	public static void create(ZooKeeper client,String path,byte[] content,CreateMode createMode) throws KeeperException, InterruptedException {
		String[] paths = path.split("/");
		String realPath = "";
		for(int i=0;i<paths.length-1;i++) {
			realPath += "/"+paths[i+1];
			try {
				client.create(realPath, new byte[0], Ids.OPEN_ACL_UNSAFE, createMode);
//				if(realPath.equals("/a/b/c")) {
//					throw new NodeExistsException(path+" esists!");
//				}
				System.out.println("create "+realPath);
			} catch (NodeExistsException e) {
				if(i == paths.length-2) {
					throw e;
				}
			}
		}
	}
	
	public static void main(String[] args) throws KeeperException, InterruptedException {
		System.out.println("/a/b/c".split("/"));
//		create(null,"/a/b/c",null);
		
		create(null, "/a/b/c/d",new byte[0], null, new StringCallback() {
			
			@Override
			public void processResult(int rc, String path, Object ctx, String name) {
				// TODO Auto-generated method stub
				
			}
		},null);
	}
}
