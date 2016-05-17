package ru.xupoh.test;

import java.util.HashMap;
import java.util.Map;

public class ServerRunner extends Thread {

	/**
	 * Run tick rate
	 */
	private int tickRate = 50;

	/**
	 * For TPS measures
	 */
	private int tickCounter = 0;

	/**
	 * Measure on seconds
	 */
	private float[] tickTimeArray = new float[50];

	/**
	 * Link to server
	 */
	private ChatServer server;

	private Map<Integer, ITicker> listeners = new HashMap<Integer, ITicker>();

	private int threadTimeout;

	/**
	 * Creates a new ServerRunner
	 * 
	 * @param A
	 *            link to {@link ChatServer}
	 * @param How
	 *            many ticks per second
	 * @param Timeout
	 *            to close too long tick thread
	 */
	public ServerRunner(ChatServer server, int tickRate, int threadTimeout) {
		super.setName("Server Runner Cycle");
		super.setDaemon(true);

		this.server = server;
		this.tickRate = tickRate;
		this.threadTimeout = threadTimeout;
	}

	public void registerTickListener(ITicker listener, int delay) {
		if (delay <= 0)
			delay = 0;

		this.listeners.put(delay, listener);
	}

	@Override
	public void run() {
		try {
			long lastTime = System.currentTimeMillis();
			long sum = 0L;
			long timeOfLastWarning = 0;

			long toomuch = (long) Math.ceil(50d * this.tickRate);

			System.out.println(toomuch);

			while (this.server.isRunning()) {
				long difference = System.currentTimeMillis() - lastTime;

				// Если время
				if (difference > toomuch && lastTime - timeOfLastWarning >= 10000L) {
					System.out.println("Can't keep up! Did the system time change, or is the server overloaded?");
					difference = toomuch;
					timeOfLastWarning = lastTime;
				}

				if (difference < 0L) {
					System.out.println("Time ran backwards! Did the system time change?");
					difference = 0L;
				}

				sum += difference;
				lastTime = System.currentTimeMillis();

				while (sum > this.tickRate) {
					sum -= this.tickRate;
					this.tick();
				}

				Thread.sleep(Math.max(1L, this.tickRate - sum));
			}
		} catch (Throwable throwable1) {
			System.err.println("Encountered an unexpected exception " + throwable1);
			System.exit(0);
		} finally {
			try {
				this.server.stop();
			} catch (Throwable throwable) {
				System.err.println("Exception stopping the server" + throwable);
			} finally {
				System.exit(0);
			}
		}
	}

	private void tick() {
		this.tickCounter = (this.tickCounter + 1) % Integer.MAX_VALUE;

		long time = System.nanoTime();

		listeners.forEach((delay, ticker) -> {
			if (tickCounter % delay == 0) {
				ticker.tick();
			}
		});

		// NOTE: В секундах
		float tickTime = (System.nanoTime() - time) / 1000000.0f;

		tickTimeArray[this.tickCounter % tickTimeArray.length] = tickTime;
	}

	public float getTPS() {
		float res = 0.f;

		for (float l : tickTimeArray)
			res += l;

		return (float) (Math.ceil(10 * tickTimeArray.length / res) / 10);
	}
}
