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
	 * @see Display#syncExec(Runnable)
	 * @throws IllegalStateException If the inherent display has been disposed
	 */
	public void execute(Runnable command) {
		if (display.isDisposed())
			throw new IllegalStateException("Display is disposed");
		display.syncExec(command);
	}
	
}
