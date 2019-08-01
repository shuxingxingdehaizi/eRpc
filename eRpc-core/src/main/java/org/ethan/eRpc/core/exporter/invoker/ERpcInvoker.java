package org.ethan.eRpc.core.exporter.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.ethan.eRpc.core.ERpcException;
import org.ethan.eRpc.core.bean.ServiceBean;
import org.ethan.eRpc.core.context.ERpcRequestContext;
import org.ethan.eRpc.core.exporter.ExporterFactory;
import org.ethan.eRpc.core.exporter.LocalExporter;
import org.ethan.eRpc.core.request.ERpcRequest;
import org.ethan.eRpc.core.response.ERpcResponse;
import org.ethan.eRpc.core.serialize.ERpcSerializeException;
import org.ethan.eRpc.core.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class ERpcInvoker {
	
	static final Logger logger = LoggerFactory.getLogger(ERpcInvoker.class);
	
	@Autowired
	private ExporterFactory exporterFactory;
	
	public void invoke(ERpcRequestContext eRpcRequestContext) throws ERpcException{
		ApplicationContext springContext = eRpcRequestContext.getSpringContext();
		
		ERpcRequest eRpcRequest = eRpcRequestContext.getRequest();
		String serviceName = eRpcRequest.getHeader().getServiceName();
		String version = eRpcRequest.getHeader().getVersion();
		
		ServiceBean serviceBean = exporterFactory.getLocalExporter().getServiceBean(serviceName, version);
		if(serviceBean == null) {
			throw new ERpcException("No provider found for service["+serviceName+"] and version["+version+"]");
		}
		Object controller = null;
		try {
			controller = springContext.getBean(serviceBean.getBeanName());
		} catch (BeansException e1) {
			// TODO Auto-generated catch block
			logger.error("Error occurs when getBean",e1);
		}
		
		if(controller == null) {
			throw new ERpcException("Controller bean["+serviceBean.getBeanName()+"] not found!");
		}
		
		Method serviceMethod = serviceBean.getServiceMethod();
		
		try {
			Object[] params = eRpcRequestContext.getSerializer().reqBodyDeSerialize(serviceBean.getParams(), eRpcRequest.getBody());
			
			Object result = serviceMethod.invoke(controller, params);
			
			
			eRpcRequestContext.getResponse().setBody(eRpcRequestContext.getSerializer().respBodySerialize(result));
			
			assembleResponseHeader(eRpcRequestContext.getRequest(), eRpcRequestContext.getResponse());
			
		} catch (ERpcSerializeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ERpcException("Error occurs when   bean["+serviceBean.getBeanName()+"] not found!");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void assembleResponseHeader(ERpcRequest request,ERpcResponse response) throws ERpcException {
		ERpcResponse.Header header= response.new Header();
		
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error("Error occurs when getLocalHost",e);
			throw new ERpcException("Error occurs when getLocalHost",e);
		}
		
		header.setServerIp(addr.getHostAddress().toString());
		header.setServiceName(request.getHeader().getServiceName());
		header.seteRpcId(request.getHeader().geteRpcId());
		
		response.setAttachment(request.getAttachment());
		response.setHeader(header);
	}
}
