package ru.xupoh.test;

import java.lang.ref.WeakReference;

import gnu.trove.map.hash.TByteObjectHashMap;
import gnu.trove.map.hash.TObjectByteHashMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.AttributeKey;

@Sharable
public abstract class IndexedMessageToMessageCodec<A> extends MessageToMessageCodec<FMLProxyPacket, A> {
	private TByteObjectHashMap<Class<? extends A>> discriminators = new TByteObjectHashMap<Class<? extends A>>();
	private TObjectByteHashMap<Class<? extends A>> types = new TObjectByteHashMap<Class<? extends A>>();

	/**
	 * Make this accessible to subclasses
	 */

	public static final AttributeKey<ThreadLocal<WeakReference<FMLProxyPacket>>> INBOUNDPACKETTRACKER = AttributeKey.valueOf("fml:inboundpacket");

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		ctx.attr(INBOUNDPACKETTRACKER).set(new ThreadLocal<WeakReference<FMLProxyPacket>>());
	}

	public IndexedMessageToMessageCodec<A> addDiscriminator(int discriminator, Class<? extends A> type) {
		discriminators.put((byte) discriminator, type);
		types.put(type, (byte) discriminator);
		return this;
	}

	public abstract void encodeInto(ChannelHandlerContext ctx, A msg, ByteBuf target) throws Exception;

	@Override
	protected final void encode(ChannelHandlerContext ctx, A msg, List<Object> out) throws Exception {
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		byte discriminator = types.get(msg.getClass());
		buffer.writeByte(discriminator);
		encodeInto(ctx, msg, buffer);
		FMLProxyPacket proxy = new FMLProxyPacket(buffer/* .copy() */,
				ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
		WeakReference<FMLProxyPacket> ref = ctx.attr(INBOUNDPACKETTRACKER).get().get();
		FMLProxyPacket old = ref == null ? null : ref.get();
		if (old != null) {
			proxy.setDispatcher(old.getDispatcher());
		}
		out.add(proxy);
	}

	public abstract void decodeInto(ChannelHandlerContext ctx, ByteBuf source, A msg);

	@Override
	protected final void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
		testMessageValidity(msg);
		ByteBuf payload = msg.payload().duplicate();
		if (payload.readableBytes() < 1) {
			System.err.println("Empty buf");
		}
		byte discriminator = payload.readByte();
		Class<? extends A> clazz = discriminators.get(discriminator);
		if (clazz == null) {
			throw new NullPointerException(
					"Undefined message for discriminator " + discriminator + " in channel " + msg.channel());
		}
		A newMsg = clazz.newInstance();
		ctx.attr(INBOUNDPACKETTRACKER).get().set(new WeakReference<FMLProxyPacket>(msg));
		decodeInto(ctx, payload.slice(), newMsg);
		out.add(newMsg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		super.exceptionCaught(ctx, cause);
	}
}