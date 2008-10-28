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

import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;

public class SequencePreservingExecutor implements Executor {

	//TODO port to Queue
	//USE AT OWN RISK
	
	private final static Logger logger = Logger.getLogger(SequencePreservingExecutor.class);
	
	private final Executor executor;
	private AtomicReference<Semaphore> lastSemaphore = new AtomicReference<Semaphore>(new Semaphore(1));

	public SequencePreservingExecutor(Executor executor) {
		this.executor = executor;
	}

	public void execute(final Runnable command) {
		final Semaphore newSemaphore = new Semaphore(0);
		final Semaphore oldSemaphore = lastSemaphore.getAndSet(newSemaphore);
		executor.execute(new Runnable(){
			public void run() {
				try {
					oldSemaphore.acquire();
					command.run();
					newSemaphore.release();
				} catch (InterruptedException exception) {
					logger.error("Interrupted", exception);
					Thread.currentThread().interrupt();
				}
			}
		});
	}

}
