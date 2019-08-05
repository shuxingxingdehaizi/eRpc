package org.ethan.eRpc.common.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ethan.eRpc.common.exception.ERpcException;

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
	 * 请求体解码后的请求参数列表
	 */
	private Object[] requestParam;
	
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
		
		/**
		 * eRpcId
		 * @return
		 */
		private String eRpcId;

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

		public String geteRpcId() {
			return eRpcId;
		}

		public void seteRpcId(String eRpcId) {
			this.eRpcId = eRpcId;
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
	
	public Object[] getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(Object[] requestParam) {
		this.requestParam = requestParam;
	}

	public Set<String> attachmentKeys() {
		return attachment == null ? null : attachment.keySet();
	}
	
	public Object getAttachment(String key) {
		return attachment == null ? null : attachment.get(key);
	}

	public void addAttachment(String key,Object value) throws ERpcException {
		if(this.attachment == null) {
			this.attachment = new HashMap<String, Object>();
		}
		if(this.attachment.containsKey(key)) {
			throw new ERpcException("Attachment ["+key+"] is aleady exists!");
		}
		this.attachment.put(key, value);
	}

	public Map<String, Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}
}
