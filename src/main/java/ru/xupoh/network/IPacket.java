package ru.xupoh.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface IPacket {
	public void toBytes(ChannelHandlerContext context, ByteBuf buffer);

	public void fromBytes(ChannelHandlerContext context, ByteBuf buffer);
}
