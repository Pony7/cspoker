package org.client;

import org.client.GUI.ClientGUI;
import org.client.User.User;

/**
 * The core of any client
 * @author Cedric
 *
 */
public class ClientCore {
	/**
	 * The gui of this client
	 */
	private ClientGUI gui;
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
		gui= new ClientGUI(this);
	}
	
	/**********************************************************
	 * Functions
	 **********************************************************/
	public void createCommunication(String url, String port, String userName, String password){
		System.out.println("url "+url);
		System.out.println("poort "+port);
		System.out.println("user name "+userName);
		System.out.println("password "+password);
		
		//this.socketsClient=new SocketsClient(url,port,userName,password);
		//this.comm=socketsClient.getPlayerCommunication();
	}
}
