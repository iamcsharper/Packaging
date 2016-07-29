package ru.xupoh.test;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.netty.channel.socket.SocketChannel;
import ru.xupoh.network.IMessage;

public class UserIO extends Ticker {
	private final BlockingQueue<IMessage> receivedQueue = new LinkedBlockingQueue<IMessage>();
	private final BlockingQueue<IMessage> messageQueue = new LinkedBlockingQueue<IMessage>();

	private SocketChannel ch;

	public UserIO(SocketChannel ch) {
		this.ch = ch;
	}

	public void sendMessage(IMessage msg) {
		messageQueue.add(msg);
	}

	public void handleIncomingMessage(IMessage msg) {
		receivedQueue.add(msg);
	}

	@Override
	public void tick() {
		IMessage incoming;
		try {
			while ((incoming = receivedQueue.take()) != null) {
				System.out.println(incoming.getClass().getName());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		IMessage outgoing;
		try {
			while ((outgoing = messageQueue.take()) != null) {
				System.out.println(outgoing.getClass().getName());
				
				ch.writeAndFlush();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getTickDelay() {
		return 2;
	}
}
