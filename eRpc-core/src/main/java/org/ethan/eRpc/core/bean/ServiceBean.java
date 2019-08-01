package org.ethan.eRpc.core.bean;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;

public class ServiceBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 882722081226250383L;
	/**
	 * 类名
	 */
	private String className;
	
	/**
	 * Spring beanName
	 */
	private String beanName;

	/**
	 * 服务名称
	 */
	private String name;
	
	/**
	 * 方法
	 */
	private Method serviceMethod;
	
	/**
	 * 服务参数列表
	 */
	private List<Param>params;
	
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
	private List<Host> providers = new ArrayList<ServiceBean.Host>();
	
	/**
	 * 消费者
	 */
	private List<Host> consumers = new ArrayList<ServiceBean.Host>();
	
	public void addProvider(Host provider) {
		providers.add(provider);
	}
	
	public void addConsumer(Host consumer) {
		consumers.add(consumer);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Param> getParams() {
		return params;
	}

	public void setParams(List<Param> params) {
		this.params = params;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Method getServiceMethod() {
		return serviceMethod;
	}

	public void setServiceMethod(Method serviceMethod) {
		this.serviceMethod = serviceMethod;
	}
	
	public String toString() {
		return name+"---"+className+"."+this.serviceMethod.getName();
	}

	public static class Param{
		
		private String name;
		
		private String className;

		public Param(String name, String className) {
			super();
			this.name = name;
			this.className = className;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}
	}
	
	public static class Host{
		/**
		 * IP
		 */
		private String ip;
		/**
		 * 计算机名
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

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}
	}
}
