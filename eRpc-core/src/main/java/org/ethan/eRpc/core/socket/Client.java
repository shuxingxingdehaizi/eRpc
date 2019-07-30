package org.ethan.eRpc.core.socket;
//package org.ethan.eRpc.core.annotation;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
//
//import javax.net.ssl.SSLEngine;
//
//import org.ethan.io.myIO.nettyNio.handler.MyClientHandler;
//import org.ethan.io.myIO.nettyNio.ssl.SSLContextFactory;
//import org.ethan.io.myIO.util.PropertiesUtil;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.DelimiterBasedFrameDecoder;
//import io.netty.handler.codec.Delimiters;
//import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
//import io.netty.handler.codec.LengthFieldPrepender;
//import io.netty.handler.codec.bytes.ByteArrayEncoder;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//import io.netty.handler.ssl.SslHandler;
//import io.netty.handler.stream.ChunkedWriteHandler;
//import io.netty.util.CharsetUtil;
//
//public class NettyNioClient {
//	
//	 private final String host = PropertiesUtil.getClientConfig("serverAddr");
//	   
//	 private final int port = Integer.valueOf(PropertiesUtil.getClientConfig("serverPort"));
//	 
//	 private boolean sslEnable = Boolean.valueOf(PropertiesUtil.getClientConfig("isSSLEnable"));;
//	 
//	 ChannelFuture cf;
//	 
//	 public NettyNioClient() {}
//	 
//	 public void start() throws InterruptedException, IOException {
//		EventLoopGroup group = new NioEventLoopGroup();
//		try {
//			Bootstrap b = new Bootstrap();
//			b.group(group)
//				.channel(NioSocketChannel.class)
//				.remoteAddress(this.host,this.port)
//				.handler(new ChannelInitializer<SocketChannel>() {
//					@Override
//					protected void initChannel(SocketChannel ch) throws Exception {
//						// TODO Auto-generated method stub
//						System.out.println("connectting...");
//						if(sslEnable) {
//							SSLEngine engine = SSLContextFactory.getClientContext().createSSLEngine();
//					        engine.setUseClientMode(true);
//					        ch.pipeline().addLast("ssl", new SslHandler(engine));
//					        
//						}
////						ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//						
////						ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
////						ch.pipeline().addLast(new LengthFieldPrepender(4));
////						ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
//						
//						ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
//						ch.pipeline().addLast(new MyClientHandler());
//						
//                        ch.pipeline().addLast(new ByteArrayEncoder());
//                        ch.pipeline().addLast(new ChunkedWriteHandler());
//					}
//				});
//				
//			
//			 cf = b.connect().sync(); // 异步连接服务器
//			 
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
//		} finally {
//			 group.shutdownGracefully().sync(); // 释放线程池资源
//		}
//	 }
//	 
//	 public void sendRequest(String request) throws UnsupportedEncodingException {
//		 cf.channel().write(request.getBytes("UTF-8"));
//		 cf.channel().flush();
//	 }
//	 
//	 public static void main(String[] args) throws InterruptedException, IOException {
//		 NettyNioClient client = new NettyNioClient();
//		 client.start();
//	}
//}
