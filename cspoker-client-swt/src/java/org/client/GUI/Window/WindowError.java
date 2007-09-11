package org.client.GUI.Window;

import org.client.GUI.ClientGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
/**
 * A class for windows displaying an error message
 * @author Cedric
 */
public class WindowError extends Window {

	/**
	 * Creates a new error window with the given display, gui and error message
	 * @super
	 * @param message
	 * 			the given error message
	 */
	public WindowError(Display display, ClientGUI gui,String message) {
		super(display, gui);
		Shell errorShell=getShell();
		//Shell constants
		int shellWidth=200;
		int shellHeigth=200;
		errorShell.setSize(shellWidth, shellHeigth);
		
		errorShell.setText("Exception");
		
		Label error=new Label(errorShell,SWT.CENTER);
		error.setText(message);
		error.setBounds(50, 50, 100, 20);
		
		draw();
	}

}
