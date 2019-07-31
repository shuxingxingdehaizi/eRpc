package org.ethan.eRpc.core.serialize;


import java.util.List;

import org.ethan.eRpc.core.bean.ServiceBean;
import org.ethan.eRpc.core.request.ERpcRequest;
import org.ethan.eRpc.core.response.ERpcResponse;

import io.netty.buffer.ByteBuf;

public interface ERpcSerialize{
	/**
	 *  请求序列化
	 * @param obj
	 * @return
	 */
	public byte[] reqSerialize(ERpcRequest request) throws ERpcSerializeException;
	
	/**
	 *  请求参数反序列化
	 * @param obj
	 * @return
	 */
	public Object[] reqBodyDeSerialize(List<ServiceBean.Param> params , String body) throws ERpcSerializeException;
	
	/**
	 *  请求反序列化
	 * @param obj
	 * @return
	 */
	public ERpcRequest reqDeSerialize(ByteBuf stream) throws ERpcSerializeException;
	
	/**
	 *  响应序列化
	 * @param obj
	 * @return
	 */
	public byte[] respSerialize(ERpcResponse response) throws ERpcSerializeException;
	
	/**
	 *  响应体序列化
	 * @param obj
	 * @return
	 */
	public String respBodySerialize(Object result) throws ERpcSerializeException;
	
	/**
	 *  响应反序列化
	 * @param obj
	 * @return
	 */
	public ERpcResponse respDeSerialize(byte[] stream) throws ERpcSerializeException;
}
