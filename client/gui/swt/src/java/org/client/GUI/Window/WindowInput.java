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
