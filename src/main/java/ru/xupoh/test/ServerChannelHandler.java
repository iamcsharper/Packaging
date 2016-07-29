package ru.xupoh.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import ru.xupoh.network.IMessage;

public class ServerChannelHandler extends SimpleChannelInboundHandler<IMessage> {
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.channel().close();
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			
			if (event.state() == IdleState.READER_IDLE)
				ctx.channel().close();
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		UserIO session = ctx.channel().attr(Attributes.SESSION).get();
		
		if (session == null)
			throw new Exception("Null session. Is that f*cking possible?");
		
		// TODO: handle user logout
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IMessage msg) throws Exception {
		try {
            UserIO session = ctx.channel().attr(Attributes.SESSION).get();
            
            assert session != null : "Null session";
            
            session.handleIncomingMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }		
	}
}
