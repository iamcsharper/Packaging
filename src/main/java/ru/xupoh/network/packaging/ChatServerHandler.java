package ru.xupoh.network.packaging;

import java.net.InetAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
	final static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " chat service! Total players "
				+ channels.size() + "\n");

		for (Channel channel : channels) {
			channel.writeAndFlush("Greet " + ctx.channel().remoteAddress() + "!\n");
		}

		channels.add(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		channels.remove(incoming);

		for (Channel channel : channels) {
			channel.write("[SERVER]: " + incoming.remoteAddress() + " has left!\n");
		}
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// Send the received message to all channels but the current one.
		for (Channel c : channels) {
			if (c != ctx.channel()) {
				c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + "\n");
			} else {
				c.writeAndFlush("[you] " + msg + '\n');
			}
		}

		// Close the connection if the client has sent 'bye'.
		if ("bye".equals(msg.toLowerCase())) {
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		//cause.printStackTrace();
		System.out.println("Отключился " + ctx.channel().remoteAddress());
		ctx.close();
	}
}
