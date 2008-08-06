/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.client.GUI.Window;

import org.client.ClientCore;
import org.client.GUI.ClientGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * A class of windows to ask the user for input
 * @author Cedric
 */
public class WindowInput extends Window {

	/**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The message of this input window
	 */
	private final String message;
	/**
	 * The error label of this input window
	 */
	private Label input;
	/**
	 * The button the user has to press to submit his input
	 */
	private Button enterButton;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Creates a new error window with the given display, gui and error message
	 * @super
	 * @param message
	 * 			the given error message
	 */
	public WindowInput(Display display, final ClientGUI gui,ClientCore clientCore,String message) {
		super(display, gui,clientCore);
		this.message=message;
		createInputShell();
		createCloseListener();
		createInputLabel();
		draw();
	}
	/**********************************************************
	 * Labels
	 **********************************************************/
	private void createInputLabel() {
		input=new Label(getShell(),SWT.BORDER | SWT.CENTER);
		input.setText(message);
		input.setLocation(60, 10);
		input.setSize(100, 50);
	}
	/**********************************************************
	 * Listeners
	 **********************************************************/
	/**********************************************************
	 * Shell
	 **********************************************************/
	private void createInputShell() {
		int shellWidth=200;
		int shellHeigth=100;
		getShell().setSize(shellWidth, shellHeigth);
		getShell().setText("Input Window");
	}
	/**********************************************************
	 * Images
	 **********************************************************/
	@Override
	public void drawImages() {
	}

}
