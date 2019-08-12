package org.ethan.eRpc.core.socket;


import java.nio.charset.Charset;

import javax.net.ssl.SSLEngine;

import org.ethan.eRpc.common.bean.socket.ssl.SSLContextFactory;
import org.ethan.eRpc.common.util.PropertiesUtil;
import org.ethan.eRpc.core.handler.ERpcServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;

@Component("eRpcServer")
public class Server implements InitializingBean,DisposableBean{
	
	static final Logger logger = LoggerFactory.getLogger(Server.class);
	
	@Autowired
	private ERpcServerHandler requestHandler;
	
	
	private static final int PORT = Integer.valueOf(PropertiesUtil.getConfig("port"));
	
	private boolean sslEnable = Boolean.valueOf(PropertiesUtil.getConfig("isSSLEnable"));
	
	public Server() {
		
	}
	
	public void start() throws InterruptedException {
		logger.info("Begin to start eRpc server");
		long startTime = System.currentTimeMillis();
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		
		EventLoopGroup group = new NioEventLoopGroup(10);
		
		try {
			ServerBootstrap sb = new  ServerBootstrap();
			sb.option(ChannelOption.SO_BACKLOG, 1024);
			sb.group(bossGroup, group)//绑定线程池
				.channel(NioServerSocketChannel.class)//指定channel
				.localAddress(PORT);
			
			sb.childHandler(new ChannelInitializer<SocketChannel>() {//客户端建立链接时触发操作
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					logger.info("Got a client connect request: Host:"+ch.localAddress().getHostName()+" Port:"+ch.localAddress().getPort());
					if(sslEnable) {
				        SSLEngine engine = SSLContextFactory.getServerContext().createSSLEngine();
				        engine.setUseClientMode(false);
				        engine.setNeedClientAuth(true);
				        ch.pipeline().addLast("ssl", new SslHandler(engine));
				        
					}
//					ch.pipeline().addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
					ch.pipeline().addLast("encoder",new StringEncoder(Charset.forName("UTF-8")));//会把服务端返回的字符串编码为字节码
//					ch.pipeline().addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));//会把服务端收的字节码转换为字符串
                    ch.pipeline().addLast("handler",requestHandler); // 客户端触发操作
                    ch.pipeline().addLast(new ByteArrayEncoder());
			        ch.flush();
				}
			});
			
			
			ChannelFuture cf = sb.bind().sync();// 服务器异步创建绑定
			if(sslEnable) {
				logger.info("eRpc server start success in ["+(System.currentTimeMillis()-startTime)+"]ms and Listening on port :"+PORT+" with SSL enable");
			}else {
				logger.info("eRpc server start success in ["+(System.currentTimeMillis()-startTime)+"]ms and Listening on port :"+PORT);
			}
			
			cf.channel().closeFuture().sync(); // 关闭服务器通道
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			group.shutdownGracefully().sync();
			bossGroup.shutdownGracefully().sync();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {//Spring加载完后启动服务器
		// TODO Auto-generated method stub
		start();
	}

	@Override
	public void destroy() throws Exception {//Spring容器销毁时，销毁服务器
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Server().start();
	}
}
