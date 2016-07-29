package ru.xupoh.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.xupoh.network.IMessage;

public class SimpleIndexedCodec extends IndexedMessageToMessageCodec<IMessage> {
    @Override
    public void encodeInto(ChannelHandlerContext ctx, IMessage msg, ByteBuf target) throws Exception
    {
        msg.toBytes(target);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IMessage msg)
    {
        msg.fromBytes(source);
    }

}