package org.cspoker.client.gui.swt.window;

import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialogs in CSPoker
 * 
 * @author Cedric
 */
public abstract class ClientDialog
		extends Dialog {
	
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	/**
	 * The gui that uses this window
	 */
	protected final ClientGUI gui;
	ClientCore clientCore;
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	/**
	 * Creates a new window with the given display and gui
	 * 
	 * @param display the given display
	 * @param gui the given gui
	 */
	public ClientDialog(Shell parent, int style, final ClientCore core) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | style);
		this.clientCore = core;
		this.gui = clientCore.getGui();
		getParent().setImage(SWTResourceManager.getImage(ClientGUI.CS_POKER_ICON));
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
}
