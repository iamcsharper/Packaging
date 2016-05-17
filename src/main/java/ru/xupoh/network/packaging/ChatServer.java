package ru.xupoh.network.packaging;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {
	public static void main(String[] args) throws Exception {
		new ChatServer(8080).run();
	}
	
	private final int port;

	public ChatServer (int port) {
		this.port = port;
	}
	
	public void run () throws Exception {
		EventLoopGroup bossGroup  = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap()
				.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				//.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChatServerInitializer());
			
			bootstrap.bind(port).sync().channel().closeFuture().sync();
			
		}
		finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
