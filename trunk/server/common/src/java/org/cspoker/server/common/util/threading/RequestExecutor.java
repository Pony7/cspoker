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

import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class RequestExecutor implements Executor {

	private final static Logger logger = Logger
			.getLogger(RequestExecutor.class);

	private LoggingThreadPool executor;

	private RequestExecutor() {
		executor = new LoggingThreadPool(1, Runtime.getRuntime()
				.availableProcessors() * 2, 1, TimeUnit.SECONDS,
				new PriorityBlockingQueue<Runnable>(1000,
						new SocketRunnableComparator()), "TestServer");
		executor
				.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void execute(Runnable command) {
		logger.trace("Received task: " + command);
		executor.execute(command);
	}

	private int getCorePoolSize() {
		return executor.getCorePoolSize();
	}

	private int getMaximumPoolSize() {
		return executor.getMaximumPoolSize();
	}

	private static RequestExecutor instance = null;

	public synchronized static Executor getInstance() {
		if (instance == null) {
			instance = new RequestExecutor();
			logger.trace("Created Request Executor with corePoolSize "
					+ instance.getCorePoolSize() + " and maxPoolSize "
					+ instance.getMaximumPoolSize());
		}
		return instance;
	}

}
