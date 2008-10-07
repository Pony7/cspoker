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
	 * Creates a new dialog
	 * 
	 * @param parent The containing shell
	 * @param style Style bits
	 * @param core The {@link ClientCore}
	 */
	public ClientDialog(Shell parent, int style, final ClientCore core) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | style);
		this.clientCore = core;
		this.gui = clientCore.getGui();
		getParent().setImage(SWTResourceManager.getImage(ClientGUI.Resources.CS_POKER_ICON));
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
