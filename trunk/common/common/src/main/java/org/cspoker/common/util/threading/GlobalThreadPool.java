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
package org.cspoker.common.util.threading;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class GlobalThreadPool extends AbstractExecutorService {

	private final static Logger logger = Logger
	.getLogger(GlobalThreadPool.class);

	private LoggingThreadPool executor;

	private GlobalThreadPool() {
		executor = new LoggingThreadPool(Math.max(2, Runtime.getRuntime()
				.availableProcessors()), "CSPoker-Main");
		executor
		.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	}

	private int getCorePoolSize() {
		return executor.getCorePoolSize();
	}

	private int getMaximumPoolSize() {
		return executor.getMaximumPoolSize();
	}

	private static GlobalThreadPool instance = null;

	public synchronized static ExecutorService getInstance() {
		if (instance == null) {
			instance = new GlobalThreadPool();
			logger.debug("Created Request Executor with corePoolSize "
					+ instance.getCorePoolSize() + " and maxPoolSize "
					+ instance.getMaximumPoolSize());
		}
		return instance;
	}

	public boolean awaitTermination(long timeout, TimeUnit unit)
	throws InterruptedException {
		return executor.awaitTermination(timeout, unit);
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

	public void execute(final Runnable command) {
		executor.execute(new Runnable(){
			@Override
			public void run() {
				try{
					command.run();
				}catch(Exception e){
					// This normally bad code of catch on Exception is here for a *reason*.
					// Future *eats* all exceptions *silently*. This clause at least allows
					// the exception to emit noise for debugging. This is particularly pernicious
					// if you have something like a NullPointerException
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}


	@Override
	public <T> Future<T> submit(final Callable<T> task) {
		return executor.submit(new Callable<T>(){
			@Override
			public T call() throws Exception {
				try{
					return task.call();
				}catch(Exception e){
					// This normally bad code of catch on Exception is here for a *reason*.
					// Future *eats* all exceptions *silently*. This clause at least allows
					// the exception to emit noise for debugging. This is particularly pernicious
					// if you have something like a NullPointerException
					logger.error(e);
					e.printStackTrace();
					throw e;
				}

			}
		});
	}

	@Override
	public Future<?> submit(final Runnable task) {
		return executor.submit(new Runnable(){
			@Override
			public void run() {
				try{
					task.run();
				}catch(Exception e){
					// This normally bad code of catch on Exception is here for a *reason*.
					// Future *eats* all exceptions *silently*. This clause at least allows
					// the exception to emit noise for debugging. This is particularly pernicious
					// if you have something like a NullPointerException
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Override
	public <T> Future<T> submit(final Runnable task, final T result) {
		return executor.submit(new Runnable(){
			@Override
			public void run() {
				try{
					task.run();
				}catch(Exception e){
					// This normally bad code of catch on Exception is here for a *reason*.
					// Future *eats* all exceptions *silently*. This clause at least allows
					// the exception to emit noise for debugging. This is particularly pernicious
					// if you have something like a NullPointerException
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}, result);
	}
}
