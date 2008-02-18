package org.client;


import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.client.GUI.ClientGUI;
import org.client.User.User;
import org.cspoker.client.rmi.RemotePlayerCommunicationFactoryForRMI;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.common.elements.table.TableId;

/**
 * The core of any client
 * @author Cedric
 *
 */
public class ClientCore {
	/**
	 * The client of this client core
	 */
	private User client;
	/**
	 * The communication used by this client
	 */
	private RemotePlayerCommunication communication;

	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Creates a new client core
	 */
	public ClientCore(){
	}
	
	/**********************************************************
	 * Communication
	 **********************************************************/
	/**
	 * Creates a new communication with a server at the given url and port
	 * for a user with the given user name and password
	 */
	public void createCommunication(String url, String port, String username, String password){
		System.out.println("LOGIN ATTEMPT");
		System.out.println("url : "+url);
		System.out.println("port : "+port);
		System.out.println("user name : "+username);
		System.out.println("password : "+password);
		//TODO: create communication module
//		try {
//			this.communication=new RemotePlayerCommunicationFactory(url,port).login(username, password);
//		} catch (Exception e) {
//			gui.displayErrorMessage(e.getMessage());
//		} 
	}
	/**********************************************************
	 * Login
	 **********************************************************/
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
	public void login(String url, String port, String userName, String password){
		this.client=new User(userName);
		createCommunication(url,port,userName,password);
	}
	/**********************************************************
	 * Bet
	 * @throws IllegalActionException 
	 * @throws RemoteException 
	 **********************************************************/
	public void call() throws RemoteException, IllegalActionException{
		communication.call();
	}
	public void bet(int amount) throws RemoteException, IllegalActionException{
		communication.bet(amount);
	}
	public void fold() throws RemoteException, IllegalActionException{
		communication.fold();
	}
	public void check() throws RemoteException, IllegalActionException{
		communication.check();
	}
	public void raise(int amount) throws RemoteException, IllegalActionException{
		communication.raise(amount);
	}
	public void deal() throws RemoteException, IllegalActionException{
		communication.deal();
	}
	public void allIn() throws RemoteException, IllegalActionException{
		communication.allIn();
	}
	public void say(String message) throws RemoteException{
		communication.say(message);
	}
	public void joinTable(TableId id){
		//TODO: change when communication is fully implemented and tested
//		try {
//			communication.joinTable(id);
//		} catch (Exception e) {
//			gui.displayErrorMessage(e.getMessage());
//		}
	}
	public void leaveTable() throws RemoteException, IllegalActionException{
		communication.leaveTable();
	}
	public TableId createTable(){
		//TODO: change when communication is fully implemented and tested
//		try {
//			return communication.createTable();
//		} catch (Exception e) {
//			gui.displayErrorMessage(e.getMessage());
//			return null;
//		}
		return new TableId(0);
	}
	public void startGame() throws RemoteException, IllegalActionException{
		communication.startGame();
	}

	public List<TableId> getTableList() {
		// TODO create method to ask the server for a list of table id's
		List<TableId> result=new ArrayList<TableId>();
		Random generator = new Random();
		int random=generator.nextInt(10);
		for(int j=0;j<random;j++){
			result.add(new TableId(j));
		}
		return result;
	}
}
