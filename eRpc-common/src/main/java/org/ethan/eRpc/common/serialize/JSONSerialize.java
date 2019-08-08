package org.ethan.eRpc.common.serialize;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.ethan.eRpc.common.bean.ERpcRequest;
import org.ethan.eRpc.common.bean.ERpcResponse;
import org.ethan.eRpc.common.bean.ServiceBean;
import org.ethan.eRpc.common.exception.ERpcSerializeException;
import org.ethan.eRpc.common.util.PropertiesUtil;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;

public class JSONSerialize implements ERpcSerialize {

	private String charSet = PropertiesUtil.getConfig("charSet");
	
	
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

	
	public ERpcRequest reqDeSerialize(ByteBuf stream)  throws ERpcSerializeException{
		String requestStr = getMessage(stream);
		return JSON.parseObject(requestStr, ERpcRequest.class);
	}

	
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

	
	public Object[] reqBodyDeSerialize(List<ServiceBean.Param> params , String body) throws ERpcSerializeException {
		// TODO Auto-generated method stub
		
		Map<String,Object>paramMap = JSON.parseObject(body, Map.class);
		if(params!= null) {
			Object[] result = new Object[params.size()];
			int index = 0;
			for(ServiceBean.Param p : params) {
				Object pv = paramMap.get(p.getName());
				if(pv instanceof Map) {
					try {
						result[index++] = JSON.toJavaObject((JSON)pv, Class.forName(p.getClassName()));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					result[index++] = pv;
				}
			}
			return result;
		}
		return null;
	}
	
	
	public String respBodySerialize(Object result) throws ERpcSerializeException {
		// TODO Auto-generated method stub
		return JSON.toJSONString(result);
	}
	
	public<T> T respBodyDeSerialize(String result,Class<T>classes) throws ERpcSerializeException {
		// TODO Auto-generated method stub
		return JSON.parseObject(result, classes);
	}
	
	private String getMessage(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con, charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	

	public String reqBodySerialize(Map<String,Object> params) throws ERpcSerializeException {
		// TODO Auto-generated method stub
		return JSON.toJSONString(params);
	}

	public static void main(String[] args) {
		String jsonStr = "{\"header\":{\"serviceName\":\"rpcTest1\"},body:\"{\\\"param1\\\":\\\"aaaaaaaaaaaa\\\"}\"}";
		ERpcRequest m = JSON.parseObject(jsonStr, ERpcRequest.class);
		
		Integer a= 1;
		System.out.println(!(a instanceof Integer));
	}
}
