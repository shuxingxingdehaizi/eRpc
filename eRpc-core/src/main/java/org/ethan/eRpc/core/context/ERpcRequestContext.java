package org.ethan.eRpc.core.context;


import org.ethan.eRpc.core.request.ERpcRequest;
import org.ethan.eRpc.core.response.ERpcResponse;

import io.netty.channel.ChannelHandlerContext;

public class ERpcRequestContext {
	private ChannelHandlerContext channelContext;
	
	private ERpcRequest request;
	
	private ERpcResponse response;
	

	public ERpcRequestContext(ChannelHandlerContext channelContext, ERpcRequest request, ERpcResponse response) {
		super();
		this.channelContext = channelContext;
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
	
}
