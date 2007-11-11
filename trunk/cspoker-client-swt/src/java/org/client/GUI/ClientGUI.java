package org.client.GUI;

import org.client.ClientCore;
import org.client.GUI.Window.Window;
import org.client.GUI.Window.WindowError;
import org.client.GUI.Window.WindowGame;
import org.client.GUI.Window.WindowLogin;
import org.client.GUI.Window.WindowTableSelection;
import org.eclipse.swt.widgets.Display;

/**
 * A class for a gui of a client
 * @author Cedric
 */
public class ClientGUI {

	/**
	 * The display of this clientGui
	 */
	private Display display;
	/**
	 * The clientCore of this clientGui
	 */
	private ClientCore clientCore;
	/**
	 * The current window of this client gui
	 */
	private Window currentWindow;

	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Creates a new cleintGui with a given clientCore
	 * @param clientCore
	 * 			the given clientCore
	 */
	public ClientGUI(ClientCore clientCore){
		display = new Display();
		this.clientCore =clientCore;
	}
	/**********************************************************
	 * Window & shell
	 **********************************************************/
	/**
	 * Sets the given window as the current window
	 * @param window
	 * 			the given window
	 */
	public void setAsCurrentWindow(Window window){
		this.currentWindow=window;
	}
	/**
	 * Disposes the current shell
	 */
	public void disposeCurrentShell(){
		try {
			display.getActiveShell().dispose();
		} catch (NullPointerException e) {
		}
	}
	/**********************************************************
	 * LOGIN
	 **********************************************************/
	/**
	 * Starts the new gui by creating a new login screen
	 */
	public void start(){
		disposeCurrentShell();
		new WindowLogin(display,this,clientCore);
	}
	/**********************************************************
	 * START
	 **********************************************************/
	public void selectTable(){
		disposeCurrentShell();
		new WindowTableSelection(display,this,clientCore);
	}
	/**
	 * Starts a new game
	 */
	public void startGame(){
		disposeCurrentShell();
		new WindowGame(display,this,clientCore);
	}
	/**********************************************************
	 * ERRORS
	 **********************************************************/
	/**
	 * Displays a fresh window with the given error message
	 * @param	message
	 * 			the given error message
	 */
	public void displayErrorMessage(String message){
		this.currentWindow.getShell().setEnabled(false);
		new WindowError(display,this,clientCore,message);
	}
	/**
	 * Continues the flow of this gui (by enabling the shell of the current window)
	 */
	public void continueFlow(){
		this.currentWindow.getShell().setEnabled(true);
	}
}
