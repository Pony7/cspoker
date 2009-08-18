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
package org.cspoker.client.xml.http;

import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Place holder for a real ScheduledFuture that allows canceling at all times.
 * 
 * @author guy
 *
 * @param <T> result of computation
 */
public class DummyScheduledFuture<T> implements ScheduledFuture<T> {

	public DummyScheduledFuture() {
	}
	
	public long getDelay(TimeUnit unit) {
		return Long.MAX_VALUE;
	}

	public int compareTo(Delayed o) {
		return 1;
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return true;
	}

	public T get() throws InterruptedException, ExecutionException {
		return null;
	}

	public T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return null;
	}

	public boolean isCancelled() {
		return true;
	}

	public boolean isDone() {
		return false;
	}

}
