package org.ethan.eRpc.core.context;


import org.ethan.eRpc.core.bean.ServiceBean;
import org.ethan.eRpc.core.request.ERpcRequest;
import org.ethan.eRpc.core.response.ERpcResponse;
import org.ethan.eRpc.core.serialize.ERpcSerialize;
import org.springframework.context.ApplicationContext;

import io.netty.channel.ChannelHandlerContext;

public class ERpcRequestContext {
	private ApplicationContext springContext;
	
	private ChannelHandlerContext channelContext;
	
	private ERpcRequest request;
	
	private ERpcResponse response;
	
	private ERpcSerialize serializer;
	

	public ERpcRequestContext(ApplicationContext springContext,ChannelHandlerContext channelContext, ERpcSerialize serializer,ERpcRequest request, ERpcResponse response) {
		super();
		this.springContext = springContext;
		this.channelContext = channelContext;
		this.serializer = serializer;
		this.request = request;
		this.response = response;
	}

	public ERpcRequest getRequest() {
		return request;
	}

	public ERpcResponse getResponse() {
		return response;
	}

	public ChannelHandlerContext getChannelContext() {
		return channelContext;
	}

	public ApplicationContext getSpringContext() {
		return springContext;
	}

	public ERpcSerialize getSerializer() {
		return serializer;
	}
}
