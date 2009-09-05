/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.common.util.threading;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class SingleThreadRequestExecutor extends AbstractExecutorService {

	private final static Logger logger = Logger
	.getLogger(SingleThreadRequestExecutor.class);


	private ScheduledExecutorService executor;

	private static final SingleThreadRequestExecutor scheduledRequestExecutor = new SingleThreadRequestExecutor();

	private SingleThreadRequestExecutor() {
		executor = Executors.newSingleThreadScheduledExecutor();
	}

	public static SingleThreadRequestExecutor getInstance() {
		return scheduledRequestExecutor;
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
				}catch(Throwable e){
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
				}catch(Throwable e){
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
	public Future<?> submit(final Runnable task) {
		return executor.submit(new Runnable(){
			@Override
			public void run() {
				try{
					task.run();
				}catch(Throwable e){
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
				}catch(Throwable e){
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

	public ScheduledFuture<?> schedule(final Runnable command, long delay,
			TimeUnit unit) {
		return executor.schedule(new Runnable(){
			@Override
			public void run() {
				try{
					command.run();
				}catch(Throwable e){
					// This normally bad code of catch on Exception is here for a *reason*.
					// Future *eats* all exceptions *silently*. This clause at least allows
					// the exception to emit noise for debugging. This is particularly pernicious
					// if you have something like a NullPointerException
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}, delay, unit);
	}

	public <V> ScheduledFuture<V> schedule(final Callable<V> callable, long delay,
			TimeUnit unit) {
		return executor.schedule(new Callable<V>(){
			@Override
			public V call() throws Exception {
				try{
					return callable.call();
				}catch(Throwable e){
					// This normally bad code of catch on Exception is here for a *reason*.
					// Future *eats* all exceptions *silently*. This clause at least allows
					// the exception to emit noise for debugging. This is particularly pernicious
					// if you have something like a NullPointerException
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}

			}
		}, delay, unit);
	}

	public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command,
			long initialDelay, long period, TimeUnit unit) {
		return executor
		.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				try{
					command.run();
				}catch(Throwable e){
					// This normally bad code of catch on Exception is here for a *reason*.
					// Future *eats* all exceptions *silently*. This clause at least allows
					// the exception to emit noise for debugging. This is particularly pernicious
					// if you have something like a NullPointerException
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}, initialDelay, period, unit);
	}

	public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command,
			long initialDelay, long delay, TimeUnit unit) {
		return executor.scheduleWithFixedDelay(new Runnable(){
			@Override
			public void run() {
				try{
					command.run();
				}catch(Throwable e){
					// This normally bad code of catch on Exception is here for a *reason*.
					// Future *eats* all exceptions *silently*. This clause at least allows
					// the exception to emit noise for debugging. This is particularly pernicious
					// if you have something like a NullPointerException
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}, initialDelay, delay,
		unit);
	}

}
