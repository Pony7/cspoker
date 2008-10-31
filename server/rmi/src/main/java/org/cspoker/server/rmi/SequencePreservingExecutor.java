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
package org.cspoker.server.rmi;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;

@ThreadSafe
public class SequencePreservingExecutor implements Executor {

	private final static Logger logger = Logger.getLogger(SequencePreservingExecutor.class);

	private final Executor executor;

	@GuardedBy("queueReaderLock")
	private final Queue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();

	private final AtomicBoolean taskPending = new AtomicBoolean(false);

	private final Object queueReaderLock = new Object();

	public SequencePreservingExecutor(Executor executor) {
		this.executor = executor;
	}

	public void execute(final Runnable command) {
		queue.add(command);
		if(taskPending.compareAndSet(false, true)){
			executor.execute(new Runnable(){
				public void run() {
					synchronized (queueReaderLock) {
						// allow new runnables of this kind to be submitted
						taskPending.set(false);
						// empty the queue
						readQueue();
					}
				}
				
				public void readQueue(){
					Runnable task;
					while ((task = queue.poll()) != null) {
						try {
							task.run();
						} catch (Exception e) {
							logger.error(e);
							throw new IllegalStateException(
									"Performed task that threw an exception.",
									e);
						}
					}
				}
				
			});
		}

	}

}
