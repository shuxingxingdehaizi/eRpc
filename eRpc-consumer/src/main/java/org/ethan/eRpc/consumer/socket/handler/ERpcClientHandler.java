package org.ethan.eRpc.consumer.socket.handler;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ERpcClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

	/**
	 * 客户端收到服务器响应时触发的方法，此处写实际业务逻辑
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf buf = msg.readBytes(msg.readableBytes());
        System.out.println("Response form server:" + ByteBufUtil.hexDump(buf) + "-----------------" + buf.toString(Charset.forName("utf-8")));
        //TODO...
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
