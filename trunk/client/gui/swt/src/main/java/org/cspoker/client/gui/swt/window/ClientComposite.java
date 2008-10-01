/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.gui.swt.window;

import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

/**
 * A superclass of all windows
 * 
 * @author Cedric, Stephan
 */
public abstract class ClientComposite
		extends Composite {
	
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	
	private ClientCore core;
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	/**
	 * Creates a new window with the given parent, style, and a ClientCore for
	 * reference/callback
	 * 
	 * @param parent The parent window
	 * @param style The desired style bits
	 * @param core The client core
	 */
	public ClientComposite(Composite parent, int style, ClientCore core) {
		super(parent, style);
		this.core = core;
		
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}
	
	/***************************************************************************
	 * Methods
	 **************************************************************************/
	
	/**
	 * Creates and adds a Listener to the shell which pops up a confirmation
	 * dialog when the window is closed
	 */
	protected void createCloseListener() {
		getShell().addListener(SWT.Close, new Listener() {
			
			public void handleEvent(Event event) {
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(getShell(), style);
				messageBox.setText("Information");
				messageBox.setMessage("Are you sure you want to exit?");
				event.doit = messageBox.open() == SWT.YES;
			}
		});
	}
	
	/**
	 * @return The Client Core
	 */
	public ClientCore getClientCore() {
		return core;
	}
}
