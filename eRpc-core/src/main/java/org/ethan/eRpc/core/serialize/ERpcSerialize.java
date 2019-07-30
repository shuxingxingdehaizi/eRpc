package org.ethan.eRpc.core.serialize;

import java.io.Serializable;

import org.ethan.eRpc.core.request.ERpcRequest;
import org.ethan.eRpc.core.response.ERpcResponse;

public interface ERpcSerialize{
	/**
	 *  请求序列化
	 * @param obj
	 * @return
	 */
	public byte[] reqSerialize(ERpcRequest request) throws ERpcSerializeException;
	
	/**
	 *  请求反序列化
	 * @param obj
	 * @return
	 */
	public ERpcRequest reqDeSerialize(byte[] stream) throws ERpcSerializeException;
	
	/**
	 *  响应序列化
	 * @param obj
	 * @return
	 */
	public byte[] respSerialize(ERpcResponse response) throws ERpcSerializeException;
	
	/**
	 *  响应反序列化
	 * @param obj
	 * @return
	 */
	public ERpcResponse respDeSerialize(byte[] stream) throws ERpcSerializeException;
}
