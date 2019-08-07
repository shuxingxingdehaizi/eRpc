package org.ethan.eRpc.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.ethan.eRpc.common.exception.ERpcException;

public class NetUtil {
	public static String getHostIP() throws ERpcException{
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			return addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			throw new ERpcException("Error occurs when getLocalHost",e);
		}
	}
	
	public static String getHostName()  throws ERpcException{
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			throw new ERpcException("Error occurs when getLocalHost",e);
		}
	}
}
