package org.cspoker.server.common.util.threading;

import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RequestExecutor implements Executor {

	private LoggingThreadPool executor;

	private RequestExecutor() {
		executor = new LoggingThreadPool(1, Runtime.getRuntime()
				.availableProcessors() + 1, 1, TimeUnit.SECONDS,
				new PriorityBlockingQueue<Runnable>(1000,
						new SocketRunnableComparator()), "TestServer");
		executor
				.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void execute(Runnable command) {
		executor.execute(command);
	}

	private static Executor instance = null;

	public synchronized static Executor getInstance() {
		if (instance == null) {
			instance = new RequestExecutor();
		}
		return instance;
	}

}
