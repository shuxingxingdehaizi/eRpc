package org.ethan.eRpc.core.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.ethan.eRpc.common.bean.ERpcRequest;
import org.ethan.eRpc.common.bean.ERpcResponse;
import org.ethan.eRpc.common.bean.ERpcThreadLocal;
import org.ethan.eRpc.common.bean.ServiceBean;
import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.common.exception.ERpcSerializeException;
import org.ethan.eRpc.common.serialize.ERpcSerialize;
import org.ethan.eRpc.common.util.PropertiesUtil;
import org.ethan.eRpc.core.context.ERpcRequestContext;
import org.ethan.eRpc.core.exporter.ExporterFactory;
import org.ethan.eRpc.core.exporter.invoker.ERpcInvoker;
import org.ethan.eRpc.core.filter.ERpcFilter;
import org.ethan.eRpc.core.filter.ERpcFilterChain;
import org.ethan.eRpc.core.route.RouteIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

@Component
@Sharable
public class ERpcServerHandler extends ChannelInboundHandlerAdapter implements InitializingBean,ApplicationContextAware{
	
	static final Logger logger = LoggerFactory.getLogger(ERpcServerHandler.class);
			
	private List<ERpcFilter>filters;
	
	private ApplicationContext springContext;
	
	private ERpcSerialize serializer;
	
	private RouteIndicator routeIndicator;
	
	@Autowired
	private ERpcInvoker invoker;
	
	@Autowired
	private ExporterFactory exporterFactory;
	
	public ERpcServerHandler() {}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		logger.info(ctx.channel().localAddress().toString() + " Chanel Activated!");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		logger.info(ctx.channel().localAddress().toString() + " Channel deactivated!");
	}

	/**
	 * 服务器收到客户端请求后触发的方法
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		//1.读取请求,解码
		ERpcRequest request = this.serializer.reqDeSerialize((ByteBuf)msg);
		
		//将traceId放入线程本地变量
		ERpcThreadLocal.add("traceId", request.getAttachment("traceId"));
		
		String serviceName = request.getHeader().getServiceName();
		String version = request.getHeader().getVersion();
		
		ServiceBean serviceBean = exporterFactory.getLocalExporter().getServiceBean(serviceName, version);
		
		if(serviceBean == null) {
			throw new ERpcException("No provider found for service["+serviceName+"] and version["+version+"]");
		}
		
		request.setRequestParam(serializer.reqBodyDeSerialize(serviceBean.getParams(), request.getBody()));
		
		ERpcResponse response = new ERpcResponse();
		
		
		//2.组装请求上下文
		ERpcRequestContext requestContext = new ERpcRequestContext(springContext,ctx,serializer,request,response,serviceBean);
		
		byte[] responseBytes = null;
		try {
			//3.前置过滤器
			if(this.filters != null && !this.filters.isEmpty()) {
	        	ERpcFilterChain chain = new ERpcFilterChain(this.filters);
	            chain.doPreFilter(chain, requestContext);
	        }
			
			invoker.invoke(requestContext);
			
			//5.后置过滤器
			if(this.filters != null && !this.filters.isEmpty()) {
	        	ERpcFilterChain chain = new ERpcFilterChain(this.filters);
	            chain.doAfterFilter(chain, requestContext);
	        }
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Error occurs when handle request:"+JSON.toJSONString(request),e);
			invoker.assembleResponseHeader(request, response);
		}
		
		//6.响应编码
		responseBytes = this.serializer.respSerialize(requestContext.getResponse());
        
        //3.生成响应，发送至客户端
        ctx.channel().writeAndFlush(responseBytes).sync();
       
	}
	
	private void reqDeSerialize(ByteBuf requestMsg,ERpcRequest request,ServiceBean serviceBean) throws ERpcSerializeException, ERpcException {
		
		request = this.serializer.reqDeSerialize(requestMsg);
		
		//将traceId放入线程本地变量
		ERpcThreadLocal.add("traceId", request.getAttachment("traceId"));
		
		String serviceName = request.getHeader().getServiceName();
		String version = request.getHeader().getVersion();
		
		serviceBean = exporterFactory.getLocalExporter().getServiceBean(serviceName, version);
		
		if(serviceBean == null) {
			throw new ERpcException("No provider found for service["+serviceName+"] and version["+version+"]");
		}
		
		request.setRequestParam(serializer.reqBodyDeSerialize(serviceBean.getParams(), request.getBody()));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("Data receive complete!");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		ctx.close();
	    System.out.println("Error occurs when handle request:" + cause.getMessage());
	}
	
//	private void getSpringContext() {
//		springContext = springContextHolder.getSpringApplicationContext();
//	}
//	
	private void initFilterChain() {
        Map<String,ERpcFilter>filterMaps = springContext.getBeansOfType(ERpcFilter.class);
        if(filterMaps != null && !filterMaps.isEmpty()) {
        	this.filters = new ArrayList<ERpcFilter>();
        	for(Map.Entry<String,ERpcFilter>ent : filterMaps.entrySet()) {
        		this.filters.add(ent.getValue());
        	}
        	
        	//对拦截器排序
        	filters.sort(new Comparator<ERpcFilter>() {
				@Override
				public int compare(ERpcFilter o1, ERpcFilter o2) {
					// TODO Auto-generated method stub
					return o1.getOrder() - o2.getOrder();
				}
			});
        	logger.info("==============Filter chain begin================");
        	for(ERpcFilter f : filters) {
        		logger.info(f.getOrder()+":"+f.getClass().getName());
        	}
        	logger.info("==============Filter chain end================");
        }else {
        	logger.info("No filter found");
        }
	}
	
	private void initDigister() throws ERpcSerializeException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String serializerClass = PropertiesUtil.getConfig("serializer");
		if(serializerClass == null || "".equals(serializerClass.trim())) {
			throw new ERpcSerializeException("serializer not found!");
		}
		
		this.serializer = (ERpcSerialize) this.getClass().getClassLoader().loadClass(serializerClass).newInstance();
		
		logger.info("Use ["+serializerClass+"] as requestDecorder");
	}
	
	private void initRouteIndicator() {
		Map<String,RouteIndicator>routeIndicators = springContext.getBeansOfType(RouteIndicator.class);
		if(routeIndicators.size() == 1) {
			routeIndicator = (RouteIndicator)routeIndicators.values().toArray()[0];
		}else {
			for(Map.Entry<String, RouteIndicator>ent : routeIndicators.entrySet()) {
				if(!ent.getKey().equals("defaultIndicator")) {
					routeIndicator = ent.getValue();
					
					break;
				}
			}
		}
		logger.info("Use ["+routeIndicator.getClass().getName()+"] as routeIndicator");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
//		getSpringContext();
		
		initFilterChain();
		
		initDigister();
		
		initRouteIndicator();
		
//		serviceExporter.afterPropertiesSet();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.springContext = applicationContext;
	}
}
