package ru.xupoh.test;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> { 
	 
    /**
     * The {@link ChannelInboundHandlerAdapter} that will handle all upstream 
     * message events from Netty. 
     */ 
    private final ChannelInboundHandlerAdapter channelHandler = new ServerChannelHandler(); 
 
    @Override 
    protected void initChannel(SocketChannel ch) throws Exception { 
 
        // Initialize our session Object when the channel is initialized, attach 
        // it to the channel.

    	ch.attr(Attributes.SESSION).setIfAbsent(new UserIO(ch)); 
 
        // Initialize the pipeline channel handlers. 
    	//TODO: 100 секунд записать куда-нибудь в конфиг!
        ChannelDuplexHandler timeout = new IdleStateHandler(100, 0, 0); 
        
        // TODO: вернуть логин
        //ByteToMessageDecoder loginHandshakeHandler = new LoginHandshakeHandler();
        //ch.pipeline().addLast("login-handshake", loginHandshakeHandler);
        
        ch.pipeline().addLast("channel-handler", channelHandler); 
        ch.pipeline().addLast("timeout", timeout); 
    } 
}