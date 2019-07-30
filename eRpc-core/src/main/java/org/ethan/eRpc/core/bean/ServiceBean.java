package org.ethan.eRpc.core.bean;

import java.io.Serializable;
import java.util.List;

public class ServiceBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 882722081226250383L;

	/**
	 * 服务名称
	 */
	private String name;
	
	/**
	 * 服务参数列表
	 */
	private List<String>paramsClasses;
	
	/**
	 * 服务版本号
	 */
	private String version;
	
	/**
	 * 服务超时时间ms
	 */
	private long timeOut;
	
	/**
	 * 服务提供者
	 */
	private List<Host> providers;
	
	/**
	 * 消费者
	 */
	private List<Host> consumers;
	
	
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public List<String> getParamsClasses() {
		return paramsClasses;
	}



	public void setParamsClasses(List<String> paramsClasses) {
		this.paramsClasses = paramsClasses;
	}



	public String getVersion() {
		return version;
	}



	public void setVersion(String version) {
		this.version = version;
	}



	public long getTimeOut() {
		return timeOut;
	}



	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}



	public List<Host> getProviders() {
		return providers;
	}



	public void setProviders(List<Host> providers) {
		this.providers = providers;
	}



	public List<Host> getConsumers() {
		return consumers;
	}



	public void setConsumers(List<Host> consumers) {
		this.consumers = consumers;
	}



	public static class Host{
		/**
		 * 域名或IP
		 */
		private String hostName;
		
		/**
		 * 端口
		 */
		private int port;
		
		/**
		 * 应用名
		 */
		private String applicationName;

		public String getHostName() {
			return hostName;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getApplicationName() {
			return applicationName;
		}

		public void setApplicationName(String applicationName) {
			this.applicationName = applicationName;
		}
	}
}
