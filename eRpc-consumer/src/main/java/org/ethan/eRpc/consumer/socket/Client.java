package org.ethan.eRpc.consumer.socket;
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLEngine;

import org.ethan.eRpc.consumer.socket.handler.ERpcClientHandler;
import org.ethan.eRpc.core.socket.ssl.SSLContextFactory;
import org.ethan.eRpc.core.util.PropertiesUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

public class Client {
	 
	 private boolean sslEnable = Boolean.valueOf(PropertiesUtil.getConfig("isSSLEnable"));;
	 
	 private Bootstrap b;
	 
	 private ConcurrentHashMap<String, ChannelFuture>channelFutureMap = new ConcurrentHashMap<String, ChannelFuture>();
	 
	 private static Client instance;
	 
	 private Client() {}
	 
	 public synchronized static Client getClient() throws InterruptedException, IOException {
		 if(instance == null) {
			 instance = new Client();
			 instance.init();
		 }
		 return instance;
	 }
	 
	 public void init() throws InterruptedException, IOException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// TODO Auto-generated method stub
						System.out.println("connectting...");
						if(sslEnable) {
							SSLEngine engine = SSLContextFactory.getClientContext().createSSLEngine();
					        engine.setUseClientMode(true);
					        ch.pipeline().addLast("ssl", new SslHandler(engine));
					        
						}
						ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
						
//						ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
//						ch.pipeline().addLast(new LengthFieldPrepender(4));
//						ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
						
						ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
						ch.pipeline().addLast(new ERpcClientHandler());
						
                        ch.pipeline().addLast(new ByteArrayEncoder());
                        ch.pipeline().addLast(new ChunkedWriteHandler());
					}
				});
				
			
			 
			 
//			 System.out.println("Connection established.");
//			 BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
//			 String request = null;
//			 while(!"exit".equals(request)) {
////				 System.out.println("Please input some request text:");
//				 request = clientInput.readLine();
//				 sendRequest(request);
//			 }
//			 
//             cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
		} finally {
			 group.shutdownGracefully().sync(); // 释放线程池资源
		}
	 }
	 
	 private ChannelFuture getChannelFuture(String addr,int port) throws InterruptedException{
		 String key = addr+":"+port;
		 ChannelFuture cf =  channelFutureMap.get(key);
         //如果管道没有被开启或者被关闭了，那么重连
         if(cf == null){
        	 cf = this.connect(addr,port);
        	 channelFutureMap.put(key, cf);
         }
         if(!cf.channel().isActive()){
        	 cf =this.connect(addr,port);
        	 channelFutureMap.put(key, cf);
         }
         return cf;
     }
	 
	 private ChannelFuture connect(String addr,int port) throws InterruptedException {
		 return b.connect(addr,port).sync(); // 异步连接服务器
	 }
	 
	 public void sendRequest(String addr,int port,String request) throws UnsupportedEncodingException, InterruptedException {
		 getChannelFuture(addr,port).channel().writeAndFlush(request.getBytes("UTF-8"));
	 }
}
