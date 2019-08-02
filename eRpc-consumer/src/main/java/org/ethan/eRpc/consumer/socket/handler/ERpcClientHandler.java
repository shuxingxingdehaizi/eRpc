package org.ethan.eRpc.consumer.socket.handler;

import org.ethan.eRpc.common.bean.ERpcResponse;
import org.ethan.eRpc.consumer.invoke.ERpcConsumerInvoker;
import org.ethan.eRpc.consumer.invoke.FutureContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Service
public class ERpcClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

	@Autowired
	private ERpcConsumerInvoker incoker;
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
