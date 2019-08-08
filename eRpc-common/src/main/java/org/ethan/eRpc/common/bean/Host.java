package org.ethan.eRpc.common.bean;

public class Host {
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

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return (hostName+"."+ip+":"+port).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof Host) {
			Host src = (Host)obj;
			return (hostName+"."+ip+":"+port).equals(src.getHostName()+"."+src.getIp()+":"+src.getPort());
		}else {
			return false;
		}
	}
}
