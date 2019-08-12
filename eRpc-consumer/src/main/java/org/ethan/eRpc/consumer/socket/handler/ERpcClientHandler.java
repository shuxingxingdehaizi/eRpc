package org.ethan.eRpc.consumer.socket.handler;

import org.ethan.eRpc.common.bean.ERpcResponse;
import org.ethan.eRpc.common.bean.ERpcThreadLocal;
import org.ethan.eRpc.common.exception.ERpcSerializeException;
import org.ethan.eRpc.common.serialize.ERpcSerialize;
import org.ethan.eRpc.common.util.MDCUtil;
import org.ethan.eRpc.common.util.PropertiesUtil;
import org.ethan.eRpc.consumer.invoke.FutureContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Service
public class ERpcClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
	
	private static ERpcSerialize serializer;
	
	private static final Logger logger = LoggerFactory.getLogger(ERpcClientHandler.class);
	
	/**
	 * 客户端收到服务器响应时触发的方法，此处写实际业务逻辑
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		if(serializer == null) {
			initSerializer();
		}
		// TODO Auto-generated method stub
		byte[] con = new byte[msg.readableBytes()];
		msg.readBytes(con);
		ERpcResponse response = serializer.respDeSerialize(con);
		MDCUtil.setTraceId((String)response.getAttachment("traceId"));
		logger.info("Response form server:" + JSON.toJSONString(response));
        FutureContainer.getFuture(response.getHeader().geteRpcId()).setResponse(response);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
	
	private void initSerializer() throws ERpcSerializeException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String serializerClass = PropertiesUtil.getConfig("serializer");
		if(serializerClass == null || "".equals(serializerClass.trim())) {
			throw new ERpcSerializeException("serializer not found!");
		}
		
		serializer = (ERpcSerialize) this.getClass().getClassLoader().loadClass(serializerClass).newInstance();
	}
}
