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

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class LoggingThread extends Thread {
	public static final String DEFAULT_NAME = "LogginThread";
	private static final AtomicInteger created = new AtomicInteger();
	private static final AtomicInteger alive = new AtomicInteger();

	private static Logger logger = Logger.getLogger(LoggingThread.class);

	public LoggingThread(Runnable r) {
		this(r, DEFAULT_NAME);
	}

	public LoggingThread(Runnable runnable, String name) {
		super(runnable, name + "-" + created.incrementAndGet());
		setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				logger.error("UNCAUGHT in thread " + t.getName(), e);
			}
		});
	}

	
	public void run() {
		logger.debug("Created " + getName());
		try {
			alive.incrementAndGet();
			super.run();
		} finally {
			alive.decrementAndGet();
			logger.debug("Exiting " + getName());
		}
	}

	public static int getThreadsCreated() {
		return created.get();
	}

	public static int getThreadsAlive() {
		return alive.get();
	}
}
