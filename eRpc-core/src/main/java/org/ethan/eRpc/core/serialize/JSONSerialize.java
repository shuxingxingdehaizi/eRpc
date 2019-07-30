package org.ethan.eRpc.core.serialize;

import java.io.UnsupportedEncodingException;

import org.ethan.eRpc.core.request.ERpcRequest;
import org.ethan.eRpc.core.response.ERpcResponse;
import org.ethan.eRpc.core.util.PropertiesUtil;

import com.alibaba.fastjson.JSON;

public class JSONSerialize implements ERpcSerialize {

	private String charSet = PropertiesUtil.getConfig("charSet");
	
	@Override
	public byte[] reqSerialize(ERpcRequest request)  throws ERpcSerializeException{
		// TODO Auto-generated method stub
		try {
			return JSON.toJSONString(request).getBytes(charSet);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ERpcSerializeException("Error occurs when reqSerialize",e);
		}
	}

	@Override
	public ERpcRequest reqDeSerialize(byte[] stream)  throws ERpcSerializeException{
		// TODO Auto-generated method stub
		try {
			String requestStr = new String(stream,charSet);
			return JSON.parseObject(requestStr, ERpcRequest.class);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ERpcSerializeException("Error occurs when reqDeSerialize",e);
		}
	}

	@Override
	public byte[] respSerialize(ERpcResponse response) throws ERpcSerializeException {
		// TODO Auto-generated method stub
		try {
			return JSON.toJSONString(response).getBytes(charSet);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ERpcSerializeException("Error occurs when respSerialize",e);
		}
	}

	@Override
	public ERpcResponse respDeSerialize(byte[] stream) throws ERpcSerializeException {
		// TODO Auto-generated method stub
		try {
			String requestStr = new String(stream,charSet);
			return JSON.parseObject(requestStr, ERpcResponse.class);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ERpcSerializeException("Error occurs when respDeSerialize",e);
		}
	}
}
