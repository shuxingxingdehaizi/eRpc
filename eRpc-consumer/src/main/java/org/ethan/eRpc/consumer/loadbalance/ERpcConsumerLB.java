package org.ethan.eRpc.consumer.loadbalance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ERpcConsumerLB {
	private static Map<String,AtomicLong>counter = new ConcurrentHashMap<String, AtomicLong>();
	
	/**
	 * @param key 负载均衡维度
	 * @param nodeCount 总节点数
	 * @param loadbalanceMethod 负载均衡算法
	 * @return 节点索引
	 */
	public static int getIndex(String key,int nodeCount,String loadbalanceMethod) {
		if("RoundRobin".equals(loadbalanceMethod)) {
			return roundRobin(key,nodeCount);
		}else {
			throw new RuntimeException("loadbalanc eMethod["+loadbalanceMethod+"] not support!");
		}
	}
	
	/**
	 * 轮询
	 * @param key
	 * @param nodeCount
	 * @param loadbalanceMethod
	 * @return
	 */
	public static int roundRobin(String key,int nodeCount) {
		if(counter.get(key) == null) {
			counter.put(key, new AtomicLong(0));
		}
		long currentCount = counter.get(key).incrementAndGet();
		return (int)(currentCount%nodeCount);
	}
}
