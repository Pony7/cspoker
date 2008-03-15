/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.server.common.util.threading;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

public class RequestExecutor implements ExecutorService {

	private final static Logger logger = Logger
			.getLogger(RequestExecutor.class);

	private LoggingThreadPool executor;

	private RequestExecutor() {
		executor = new LoggingThreadPool(Math.max(2,Runtime.getRuntime()
				.availableProcessors() * 2), Math.max(2,Runtime.getRuntime()
				.availableProcessors() * 3), 2, TimeUnit.SECONDS,
				new PriorityBlockingQueue<Runnable>(20,
						new SocketRunnableComparator()), "CSPoker-Main");
		executor
				.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.allowsCoreThreadTimeOut();
	}

	public void execute(Runnable command) {
		logger.trace("Received command: " + command);
		executor.execute(command);
	}

	private int getCorePoolSize() {
		return executor.getCorePoolSize();
	}

	private int getMaximumPoolSize() {
		return executor.getMaximumPoolSize();
	}

	private static RequestExecutor instance = null;

	public synchronized static ExecutorService getInstance() {
		if (instance == null) {
			instance = new RequestExecutor();
			logger.trace("Created Request Executor with corePoolSize "
					+ instance.getCorePoolSize() + " and maxPoolSize "
					+ instance.getMaximumPoolSize());
		}
		return instance;
	}

	
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return executor.awaitTermination(timeout, unit);
	}

	
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
			throws InterruptedException {
		return executor.invokeAll(tasks);
	}

	
	public <T> List<Future<T>> invokeAll(
			Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return executor.invokeAll(tasks, timeout, unit);
	}

	
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		return executor.invokeAny(tasks);
	}

	
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
			long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return executor.invokeAny(tasks, timeout, unit);
	}

	
	public boolean isShutdown() {
		return executor.isShutdown();
	}

	
	public boolean isTerminated() {
		return executor.isTerminated();
	}

	
	public void shutdown() {
		executor.shutdown();
	}

	
	public List<Runnable> shutdownNow() {
		return executor.shutdownNow();
	}

	
	public <T> Future<T> submit(Callable<T> task) {
		logger.trace("Received task: " + task);
		return executor.submit(task);
	}

	
	public Future<?> submit(Runnable task) {
		logger.trace("Received task: " + task);
		return executor.submit(task);
	}

	
	public <T> Future<T> submit(Runnable task, T result) {
		logger.trace("Received task: " + task);
		return executor.submit(task, result);
	}
}
