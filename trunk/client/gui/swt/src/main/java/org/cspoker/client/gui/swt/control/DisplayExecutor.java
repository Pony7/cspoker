/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.gui.swt.control;

import java.util.concurrent.Executor;

import org.eclipse.swt.widgets.Display;

/**
 * Executor that executes submitted {@link Runnable} tasks in a UI thread
 * 
 * @author Stephan Schmidt
 */
public class DisplayExecutor
		implements Executor {
	
	private final Display display;
	
	/**
	 * @param display The display in which to run the tasks
	 */
	public DisplayExecutor(Display display) {
		this.display = display;
	}
	
	/**
	 * Runs the task in the given UI thread
	 * 
	 * @param command The {@link Runnable} to execute
	 * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
	 * @see Display#asyncExec(Runnable)
	 * @throws IllegalStateException If the inherent display has been disposed
	 */
	public void execute(Runnable command) {
		if (display.isDisposed())
			throw new IllegalStateException("Display is disposed");
		display.asyncExec(command);
	}
	
}
