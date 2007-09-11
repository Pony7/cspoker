package org.client.GUI;

import org.client.ClientCore;
import org.client.GUI.Window.Window;
import org.client.GUI.Window.WindowError;
import org.client.GUI.Window.WindowGame;
import org.client.GUI.Window.WindowLogin;
import org.client.User.User;
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
	/**
	 * The user of this gui
	 */
	private User user;
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
	 * LOGIN
	 **********************************************************/
	/**
	 * Starts the new gui by creating a new login screen
	 */
	public void start(){
		disposeCurrentShell();
		this.currentWindow=new WindowLogin(display,this);
	}
	/**
	 * Logs in a new user with the given username and password to the given
	 * server url and port.
	 * @param url
	 * 			the given server url
	 * @param port
	 * 			the given server port
	 * @param userName
	 * 			the given user name
	 * @param password
	 * 			the given password
	 */
	public void login(String url, int port, String userName, String password){
		this.user=new User(userName);
		clientCore.createCommunication(url,port,userName,password);
	}
	/**********************************************************
	 * START
	 **********************************************************/
	public void disposeCurrentShell(){
		try {
			display.getActiveShell().dispose();
		} catch (NullPointerException e) {
		}
	}
	/**
	 * Starts a new game
	 */
	public void startGame(){
		disposeCurrentShell();
		this.currentWindow=new WindowGame(display,this);
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
		System.out.println("Exception : "+message);
		new WindowError(display,this,message);
	}
}
