package org.client;

import java.io.IOException;

import org.client.GUI.ClientGUI;

/**
 * The core of any client
 * @author Cedric
 *
 */
public class ClientCore {
	/**
	 * The gui of this client
	 */
	private final ClientGUI gui;
	/**
	 * The communication used by this client
	 */
	//private PlayerCommunication comm;
	/**
	 * The sockets client used by this client core
	 */
	//private SocketsClient socketsClient;
	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Creates a new client core
	 */
	public ClientCore(){
		this.gui= new ClientGUI(this);
		gui.start();
	}
	
	/**********************************************************
	 * Functions
	 **********************************************************/
	/**
	 * Creates a new communication with a server at the given url and port
	 * for a user with the given user name and password
	 */
	public void createCommunication(String url, int port, String userName, String password){
		System.out.println("LOGIN ATTEMPT");
		System.out.println("url : "+url);
		System.out.println("port : "+port);
		System.out.println("user name : "+userName);
		System.out.println("password : "+password);
//		try {
//			this.socketsClient=new SocketsClient(url,port,userName,password);
//		} catch (IOException e) {
//			gui.displayErrorMessage(e.getMessage());
//		}
//		this.comm=socketsClient.getPlayerCommunication();
		gui.startGame();
	}
}
