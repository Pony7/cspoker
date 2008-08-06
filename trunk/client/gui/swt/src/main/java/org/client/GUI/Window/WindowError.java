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
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
/**
 * A class for windows displaying an error message
 * @author Cedric
 */
public class WindowError extends Window {

	/**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The warning image
	 */
	private final Image warning=imageFactory.getImage(getDisplay(),"warning");
	/**
	 * The message of this error window
	 */
	private final String message;
	/**
	 * The error label of this error window
	 */
	private Label error;
	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Creates a new error window with the given display, gui and error message
	 * @super
	 * @param message
	 * 			the given error message
	 */
	public WindowError(Display display, final ClientGUI gui,ClientCore clientCore,String message) {
		super(display, gui,clientCore);
		this.message=message;
		createErrorShell();
		createCloseListener();
		createErrorLabel();
		draw();
	}
	/**********************************************************
	 * Labels
	 **********************************************************/
	private void createErrorLabel() {
		error=new Label(getShell(),SWT.BORDER | SWT.CENTER);
		error.setText(message);
		error.setLocation(60, 10);
		error.setSize(100, 50);
	}
	/**********************************************************
	 * Listeners
	 **********************************************************/
	@Override
	protected void createCloseListener() {
		getShell().addShellListener(new ShellAdapter(){
			@Override
			public void shellClosed(ShellEvent arg0) {
				getGui().continueFlow();
			}
		});
	}
	/**********************************************************
	 * Shell
	 **********************************************************/
	private void createErrorShell() {
		int shellWidth=200;
		int shellHeigth=100;
		getShell().setSize(shellWidth, shellHeigth);
		getShell().setText("Exception");
	}
	/**********************************************************
	 * Images
	 **********************************************************/
	@Override
	public void drawImages() {
		getGC().drawImage(warning,5,5);
	}
}
