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

package org.cspoker.server.sockets.threading.test;

import java.util.concurrent.PriorityBlockingQueue;

import junit.framework.TestCase;

import org.cspoker.server.common.common.threading.Prioritizable;
import org.cspoker.server.common.common.threading.SocketRunnableComparator;

public class PrioritizableComparator extends TestCase {

    public void testCompare() {
	PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(50, new SocketRunnableComparator());

	queue.add(new TestRunnable("runnable 1"));
	queue.add(new TestPrioritizable(-1, "runnable 2"));
	queue.add(new TestPrioritizable(-1, "runnable 3"));
	queue.add(new TestPrioritizable(5, "runnable 4"));
	queue.add(new TestRunnable("runnable 5"));
	queue.add(new TestPrioritizable(0, "runnable 6"));
	queue.add(new TestRunnable("runnable 7"));
	queue.add(new TestPrioritizable(0, "runnable 8"));
	queue.add(new TestRunnable("runnable 9"));
	queue.add(new TestPrioritizable(6, "runnable 10"));
	queue.add(new TestPrioritizable(5, "runnable 11"));
	queue.add(new TestPrioritizable(-2, "runnable 12"));
	queue.add(new TestRunnable("runnable 13"));

	Runnable r; String names = "";
	while((r = queue.poll()) !=null){
	    names += r+", ";
	}
	assertEquals("runnable 10 with priority 6, " +
			"runnable 4 with priority 5, " +
			"runnable 11 with priority 5, " +
			"runnable 1, " +
			"runnable 8 with priority 0, " +
			"runnable 9, " +
			"runnable 5, " +
			"runnable 7, " +
			"runnable 13, " +
			"runnable 6 with priority 0, " +
			"runnable 3 with priority -1, " +
			"runnable 2 with priority -1, " +
			"runnable 12 with priority -2, "
			, names);
    }

    class TestPrioritizable implements Runnable, Prioritizable{

	private final int priority;
	private final String name;

	public TestPrioritizable(int priority, String name) {
	    this.priority = priority;
	    this.name = name;
	}

	public void run() {
	    // no op
	}

	public int getPriority() {
	    return priority;
	}

	@Override
	public String toString(){
	    return name+" with priority "+priority;
	}

    }

    class TestRunnable implements Runnable{

	private int priority;
	private final String name;

	public TestRunnable(String name) {
	    this.name = name;
	}

	public void run() {
	    // no op
	}

	@Override
	public String toString(){
	    return name;
	}

    }

}
