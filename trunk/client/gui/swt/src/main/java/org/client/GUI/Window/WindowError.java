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
