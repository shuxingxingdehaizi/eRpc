package org.ethan.eRpc.consumer.invoke;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

import org.ethan.eRpc.common.bean.ERpcRequest;
import org.ethan.eRpc.common.bean.ERpcResponse;
import org.ethan.eRpc.common.bean.Host;
import org.ethan.eRpc.common.bean.ServiceBean;
import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.common.exception.ERpcSerializeException;
import org.ethan.eRpc.common.serialize.ERpcSerialize;
import org.ethan.eRpc.common.util.PropertiesUtil;
import org.ethan.eRpc.consumer.cache.ProviderCache;
import org.ethan.eRpc.consumer.cache.ProviderCacheFactory;
import org.ethan.eRpc.consumer.socket.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ERpcConsumerInvoker implements InitializingBean,ApplicationContextAware{
	
	private static final Logger logger = LoggerFactory.getLogger(ERpcConsumerInvoker.class);

	private Object localExporter;
	
	private ApplicationContext applicationContext;
	
	private ProviderCache providerCache;
	
	private ERpcSerialize serializer;
	
	private boolean isERpcProviderEnabled = false;
	
	
	
	
	public<T> T invoke(String serviceName,String version,Class<T> returnType,Map<String,Object> params) throws ERpcException {
		//先尝试本地invoke
		
		if(isERpcProviderEnabled) {
			ServiceBean serviceBean = getLocalServiceBean(serviceName, version);
			if(serviceBean != null) {
				logger.info("Find service["+serviceName+"]with version["+version+"] in local,try local invoke");
				return localInvoke(serviceBean,returnType,params);
			}
			
		}

		//未找到本地服务，尝试远程调用
		Host host = providerCache.getProvider(serviceName, version);
		if(host == null) {
			throw new ERpcException("No provider found for service["+serviceName+"] and version["+version+"]");
		}
		try {
			return invoke(host, serviceName,returnType,version, params);
		} catch (InterruptedException | IOException | ERpcSerializeException e) {
			// TODO Auto-generated catch block
			logger.error("Error occurs when remote invoke",e);
			throw new ERpcException("Error occurs when remote invoke",e);
		}
	}
	
	private<T> T localInvoke(ServiceBean serviceBean,Class<T> returnType,Map<String,Object> params) throws ERpcException {
		Object controller = null;
		try {
			controller = applicationContext.getBean(serviceBean.getBeanName());
		} catch (BeansException e1) {
			// TODO Auto-generated catch block
			logger.error("Error occurs when getBean",e1);
		}
		
		if(controller == null) {
			throw new ERpcException("Controller bean["+serviceBean.getBeanName()+"] not found!");
		}
		
		Method serviceMethod = serviceBean.getServiceMethod();
		
		try {			
			Object result = serviceMethod.invoke(controller, params);
			
			return serializer.respBodyDeSerialize(serializer.respBodySerialize(result), returnType);
			
		} catch (ERpcSerializeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ERpcException("Error occurs when  Serialize !",e);
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
		return null;
	}
	
	private<T> T invoke(Host host,String serviceName,Class<T> returnType,String version,Map<String,Object> params) throws UnsupportedEncodingException, InterruptedException, IOException, ERpcSerializeException, ERpcException {
		ERpcRequest request = new ERpcRequest();
		ERpcRequest.Header header = new ERpcRequest.Header();
		String eRpcId = UUID.randomUUID().toString();
		header.seteRpcId(eRpcId);
		header.setServerIp(host.getIp());
		header.setServiceName(serviceName);
		header.setVersion(version);
		request.setHeader(header);
		request.setBody(serializer.reqBodySerialize(params));
		
		ERpcFuture future = FutureContainer.createFuture(10000L,eRpcId);
		Client.getClient().sendRequest(host.getIp(), host.getPort(), serializer.reqSerialize(request));
		ERpcResponse response =  future.get();
		return serializer.respBodyDeSerialize(response.getBody(), returnType);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		providerCache = ProviderCacheFactory.getProdiderCache();
		initDigister();
		
		try {
			Class ExporterFactoryClass = Class.forName("org.ethan.eRpc.core.exporter.ExporterFactory");
			Object exporterFactoryClass = applicationContext.getBean(ExporterFactoryClass);
			Method getLocalExporter = exporterFactoryClass.getClass().getMethod("getLocalExporter");
			localExporter = getLocalExporter.invoke(exporterFactoryClass);
		} catch (ClassNotFoundException e) {
			logger.info("ERpc Provider not enable");
			isERpcProviderEnabled = false;
		}
	}
	
	/**
	 * 获取本地暴露的方法
	 * @throws ERpcException 
	 */
	private ServiceBean getLocalServiceBean(String serviceName,String version) throws ERpcException {
		try {
			Method getServiceBean = localExporter.getClass().getMethod("getServiceBean", java.lang.String.class,java.lang.String.class);
			return (ServiceBean)getServiceBean.invoke(getServiceBean, serviceName,version);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.error("Error occurs when getLocalServiceBean",e);
			throw new ERpcException("Error occurs when getLocalServiceBean",e);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}
	
	private void initDigister() throws ERpcSerializeException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String serializerClass = PropertiesUtil.getConfig("serializer");
		if(serializerClass == null || "".equals(serializerClass.trim())) {
			throw new ERpcSerializeException("serializer not found!");
		}
		
		this.serializer = (ERpcSerialize) this.getClass().getClassLoader().loadClass(serializerClass).newInstance();
	}

	public ERpcSerialize getSerializer() {
		return serializer;
	}
}
