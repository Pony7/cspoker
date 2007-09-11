package org.client.GUI;

import org.client.ClientCore;
import org.client.GUI.Window.Window;
import org.client.GUI.Window.WindowLogin;
import org.client.User.User;
import org.eclipse.swt.widgets.Display;

/**
 * A class for a gui of a client
 * @author Cedric
 *
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
		start();
	}
	/**********************************************************
	 * Functions
	 **********************************************************/
	public void start(){
		this.currentWindow=new WindowLogin(display,this);
	}
	public void login(String url, String port, String userName, String password){
		this.user=new User(userName);
		clientCore.createCommunication(url,port,userName,password);
	}
}
