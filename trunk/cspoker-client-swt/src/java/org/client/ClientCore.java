package org.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.client.GUI.ClientGUI;
import org.client.User.User;
import org.client.rmi.RemotePlayerCommunicationFactory;
import org.cspoker.common.game.IllegalActionException;
import org.cspoker.common.game.PlayerCommunication;
import org.cspoker.common.game.RemotePlayerCommunication;
import org.cspoker.common.game.elements.table.TableId;

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
		this.gui= new ClientGUI(this);
		gui.start();
	}
	
	/**********************************************************
	 * Communication
	 **********************************************************/
	/**
	 * Creates a new communication with a server at the given url and port
	 * for a user with the given user name and password
	 */
	public void createCommunication(String url, int port, String username, String password){
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
	public void login(String url, int port, String userName, String password){
		this.client=new User(userName);
		createCommunication(url,port,userName,password);
		gui.selectTable();
	}
	/**********************************************************
	 * Bet
	 **********************************************************/
	public void call(){
		try {
			communication.call();
		} catch (Exception e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void bet(int amount){
		try {
			communication.bet(amount);
		} catch (Exception e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void fold(){
		try {
			communication.fold();
		} catch (Exception e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void check(){
		try {
			communication.check();
		} catch (Exception e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void raise(int amount){
		try {
			communication.raise(amount);
		} catch (Exception e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void deal(){
		try {
			communication.deal();
		} catch (Exception e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void allIn(){
		try {
			communication.allIn();
		} catch (Exception e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void say(String message){
		try {
			communication.say(message);
		} catch (Exception e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void joinTable(TableId id){
		//TODO: change when communication is fully implemented and tested
//		try {
//			communication.joinTable(id);
//		} catch (Exception e) {
//			gui.displayErrorMessage(e.getMessage());
//		}
		gui.startGame();
	}
	public void leaveTable(){
		try {
			communication.leaveTable();
		} catch (Exception e) {
			gui.displayErrorMessage(e.getMessage());
		}
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
	public void startGame(){
		try {
			communication.startGame();
		} catch (Exception e) {
			gui.displayErrorMessage(e.getMessage());
		}
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
