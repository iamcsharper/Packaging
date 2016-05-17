/*package ru.xupoh.network.server;

import java.io.IOException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NetworkBuilder {
	*//**
     * The bootstrap that will oversee the management of the entire network.
     *//*
    private ServerBootstrap bootstrap = new ServerBootstrap();

    *//**
     * The event loop group that will be attached to the bootstrap.
     *//*
    private EventLoopGroup loopGroup = new NioEventLoopGroup();

    *//**
     * The {@link ChannelInitializer} that will determine how channels will be
     * initialized when registered to the event loop group.
     *//*
    private ChannelInitializer<SocketChannel> channelInitializer = new ServerChannelInitializer();

    *//**
     * Initializes this network handler effectively preparing the server to
     * listen for connections and handle network events.
     *
     * @param port
     *            the port that this network will be bound to.
     * @throws Exception
     *             if any issues occur while starting the network.
     *//*
    public void initialize(int port) throws IOException {
        // ResourceLeakDetector.setLevel(Server.DEBUG ? Level.PARANOID : NetworkConstants.RESOURCE_DETECTION);
        bootstrap.group(loopGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(channelInitializer);
        bootstrap.bind(port).syncUninterruptibly();
    }
}
*/