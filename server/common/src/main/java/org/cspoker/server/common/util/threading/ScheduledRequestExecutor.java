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

package org.cspoker.server.common.util.threading;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScheduledRequestExecutor extends AbstractExecutorService {

	private ScheduledExecutorService executor;

	private static final ScheduledRequestExecutor scheduledRequestExecutor = new ScheduledRequestExecutor();

	private ScheduledRequestExecutor() {
		executor = Executors.newScheduledThreadPool(1);
	}

	public static ScheduledRequestExecutor getInstance() {
		return scheduledRequestExecutor;
	}

	public ScheduledFuture<?> schedule(Runnable command, long delay,
			TimeUnit unit) {
		return executor.schedule(command, delay, unit);
	}

	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay,
			TimeUnit unit) {
		return executor.schedule(callable, delay, unit);
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
			long initialDelay, long period, TimeUnit unit) {
		return executor
				.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
			long initialDelay, long delay, TimeUnit unit) {
		return executor.scheduleWithFixedDelay(command, initialDelay, delay,
				unit);
	}

	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return executor.awaitTermination(timeout, unit);
	}

	public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks)
			throws InterruptedException {
		return executor.invokeAll(tasks);
	}

	public <T> List<Future<T>> invokeAll(
			Collection<Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return executor.invokeAll(tasks, timeout, unit);
	}

	public <T> T invokeAny(Collection<Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		return executor.invokeAny(tasks);
	}

	public <T> T invokeAny(Collection<Callable<T>> tasks,
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
		return executor.submit(task);
	}

	public Future<?> submit(Runnable task) {
		return executor.submit(task);
	}

	public <T> Future<T> submit(Runnable task, T result) {
		return executor.submit(task, result);
	}

	public void execute(Runnable command) {
		executor.execute(command);
	}

}
