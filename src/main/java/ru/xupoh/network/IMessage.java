package ru.xupoh.network;

import io.netty.buffer.ByteBuf;

public interface IMessage {
	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf
	 */
	public void fromBytes(ByteBuf buf);

	/**
	 * Deconstruct your message into the supplied byte buffer
	 * 
	 * @param buf
	 */
	public void toBytes(ByteBuf buf);
}