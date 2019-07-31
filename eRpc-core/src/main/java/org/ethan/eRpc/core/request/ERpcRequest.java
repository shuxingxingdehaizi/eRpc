package org.ethan.eRpc.core.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ERpcRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5204887840604288909L;

	private Header header;
	
	/**
	 * 请求体
	 */
	private String body;
	
	/**
	 * 一些附属信息，会随着RPC调用一直传递下去
	 */
	private Map<String,Object>attachment = new HashMap<String, Object>();
	
	public static class Header {
		/**
		 * 消费者IP
		 */
		private String serverIp;
		
		/**
		 * 调用的接口名
		 */
		private String serviceName;
		
		private String version;

		public String getServerIp() {
			return serverIp;
		}

		public void setServerIp(String serverIp) {
			this.serverIp = serverIp;
		}

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}
}
