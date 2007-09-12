package org.client.GUI.Window;

import org.client.GUI.ClientGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
/**
 * A class for windows displaying an error message
 * @author Cedric
 */
public class WindowError extends Window {

	private Image warning=new Image(getDisplay(),"images/warning.png");
	/**
	 * Creates a new error window with the given display, gui and error message
	 * @super
	 * @param message
	 * 			the given error message
	 */
	public WindowError(Display display, final ClientGUI gui,String message) {
		super(display, gui);
		Shell errorShell=getShell();
		//Shell constants
		int shellWidth=200;
		int shellHeigth=100;
		errorShell.setSize(shellWidth, shellHeigth);
		
		errorShell.setText("Exception");
		
		errorShell.addShellListener(new ShellAdapter(){
			@Override
			public void shellClosed(ShellEvent arg0) {
				gui.continueFlow();
			}
		});
		
		Label error=new Label(errorShell,SWT.BORDER | SWT.CENTER);
		error.setText(message);
		error.setLocation(60, 10);
		error.setSize(100, 50);
		
		draw();
	}
	@Override
	public void drawImages() {
		getGC().drawImage(warning,5,5);
	}

}
