package ru.xupoh.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {
	/**
	 * The bootstrap that will oversee the management of the entire network.
	 */
	private ServerBootstrap bootstrap = new ServerBootstrap();

	/**
	 * The event loop group that will be attached to the bootstrap.
	 */
	private EventLoopGroup loopGroup = new NioEventLoopGroup();

	/**
	 * The {@link ChannelInitializer} that will determine how channels will be
	 * initialized when registered to the event loop group.
	 */
	private ChannelInitializer<SocketChannel> channelInitializer = new ServerChannelInitializer();

	/**
	 * Порт приема клиентов
	 */
	private int port;

	/**
	 * Запущен ли сервер?
	 */
	private boolean isRunning;

	/**
	 * Был ли сервер выключен (защита от повтора)
	 */
	private boolean serverStopped;

	/**
	 * Дрыц-тыц, запускатор
	 */
	private ServerRunner serverRunner;

	public ChatServer(int port) {
		this.port = port;
		this.isRunning = true;
		this.serverRunner = new ServerRunner(this, 50, 3);
	}

	private void start() {
		try {
			bootstrap.group(loopGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(channelInitializer);
			bootstrap.bind(port).syncUninterruptibly();
		} catch (Exception e) {
			System.out.println("Can't initialize server, error " + e.toString());
		} finally {
			this.serverRunner.start();
		}
	}

	public void stop() {
		if (!this.serverStopped) {
			// Does something like saving and etc.
			this.serverStopped = true;
		}
	}

	public static void main(String[] args) {
		final ChatServer server = new ChatServer(25565);
		
		/*server.addTicker(10, new ITicker() {
			@Override
			public void tick() {
				System.out.println("10 ticked! TPS: " + server.getServerRunner().getTPS());
			}
		});*/
		
		server.addTicker(40, new ITicker() {
			@Override
			public void tick() {
				System.out.println("50 ticked! Saving user data...");
			}
		});
		
		server.start();

		Runtime.getRuntime().addShutdownHook(new Thread("Server Shutdown Thread") {
			public void run() {
				server.stop();
			}
		});
	}

	public void addTicker(int delay, ITicker ticker) {
		this.serverRunner.registerTickListener(ticker, delay);
	}

	/**
	 * @return the isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}

	public ServerRunner getServerRunner() {
		return serverRunner;
	}
}