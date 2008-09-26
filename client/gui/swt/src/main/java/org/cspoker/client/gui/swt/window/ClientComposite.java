package org.cspoker.client.gui.swt.window;

import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.GameState;
import org.cspoker.client.gui.swt.control.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

/**
 * A superclass of all windows
 * 
 * @author Cedric
 */
public abstract class ClientComposite
		extends Composite {
	
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	
	protected GameState gameState;
	private ClientCore core;
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	/**
	 * Creates a new window with the given parent, style, and a ClientCore for
	 * reference/callback
	 */
	public ClientComposite(Composite parent, int style, ClientCore core) {
		super(parent, style);
		this.core = core;
		if (parent instanceof ClientComposite) {
			gameState = ((ClientComposite) parent).getGameState();
		}
		
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}
	
	/***************************************************************************
	 * Methods
	 **************************************************************************/
	
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
	
	public GameState getGameState() {
		return gameState;
	}
	
	public ClientCore getClientCore() {
		return core;
	}
}
