package org.ethan.eRpc.consumer.socket.handler;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.ethan.eRpc.consumer.invoke.ERpcInvoker;
import org.ethan.eRpc.consumer.invoke.FutureContainer;
import org.ethan.eRpc.core.response.ERpcResponse;
import org.ethan.eRpc.core.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Service
public class ERpcClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

	@Autowired
	private ERpcInvoker incoker;
	/**
	 * 客户端收到服务器响应时触发的方法，此处写实际业务逻辑
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// TODO Auto-generated method stub
		byte[] con = new byte[msg.readableBytes()];
		msg.readBytes(con);
		ERpcResponse response = incoker.getSerializer().respDeSerialize(con);
        System.out.println("Response form server:" + JSON.toJSONString(response));
        FutureContainer.getFuture(response.getHeader().geteRpcId()).setResponse(response);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
