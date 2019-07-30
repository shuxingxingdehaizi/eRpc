package org.ethan.eRpc.core.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class ERpcRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5204887840604288909L;

	private Header header;
	
	/**
	 * 响应
	 */
	private String body;
	
	/**
	 * 一些附属信息，会随着RPC调用一直传递下去
	 */
	private Map<String,Object>attachment = new HashMap<String, Object>();
	
	public class Header {
		/**
		 * 消费者IP
		 */
		private String serverIp;
		
		/**
		 * 调用的接口名
		 */
		private String interfaceName;

		public String getServerIp() {
			return serverIp;
		}

		public void setServerIp(String serverIp) {
			this.serverIp = serverIp;
		}

		public String getInterfaceName() {
			return interfaceName;
		}

		public void setInterfaceName(String interfaceName) {
			this.interfaceName = interfaceName;
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
