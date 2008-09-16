package org.cspoker.client.gui.swt.window;

import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
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
	
	/**
	 * The gui that uses this window
	 */
	protected final ClientGUI gui;
	
	/**
	 * The client core of this window
	 */
	protected final ClientCore clientCore;
	
	protected GameState gameState;
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	/**
	 * Creates a new window with the given display and gui
	 * 
	 * @param display the given display
	 * @param gui the given gui
	 */
	public ClientComposite(Composite parent, final ClientGUI gui, final ClientCore clientCore, int style) {
		super(parent, style);
		this.gui = gui;
		this.clientCore = clientCore;
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
	/**
	 * Returns the gui used in this window
	 */
	public ClientGUI getGui() {
		return gui;
	}
	
	/**
	 * Returns the client core of this window
	 */
	public ClientCore getClientCore() {
		return clientCore;
	}
	
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
}
