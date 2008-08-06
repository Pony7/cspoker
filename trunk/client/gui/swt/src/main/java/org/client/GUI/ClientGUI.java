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

package org.client.GUI;

import org.client.ClientCore;
import org.client.GUI.Window.Window;
import org.client.GUI.Window.WindowError;
import org.client.GUI.Window.WindowGame;
import org.client.GUI.Window.WindowInput;
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
	/**********************************************************
	 * INPUT
	 **********************************************************/
	/**
	 * Asks the user for an input to the given question
	 * @param question
	 * 			the given question for the input
	 */
	public String askUserForInput(String question){
		this.currentWindow.getShell().setEnabled(false);
		new WindowInput(display,this,clientCore,question);
		return null;
	}
	/**
	 * Continues the flow of this gui (by enabling the shell of the current window)
	 */
	public void continueFlow(){
		this.currentWindow.getShell().setEnabled(true);
	}
}
