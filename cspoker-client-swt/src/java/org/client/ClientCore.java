package org.client;

import org.client.GUI.ClientGUI;
import org.client.User.User;
import org.cspoker.common.game.IllegalActionException;
import org.cspoker.common.game.PlayerCommunication;
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
	private PlayerCommunication communication;

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
	public void createCommunication(String url, int port, String userName, String password){
		System.out.println("LOGIN ATTEMPT");
		System.out.println("url : "+url);
		System.out.println("port : "+port);
		System.out.println("user name : "+userName);
		System.out.println("password : "+password);
		//TODO: create communication module
		gui.startGame();
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
	}
	/**********************************************************
	 * Bet
	 **********************************************************/
	public void call(){
		try {
			communication.call();
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void bet(int amount){
		try {
			communication.bet(amount);
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void fold(){
		try {
			communication.fold();
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void check(){
		try {
			communication.check();
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void raise(int amount){
		try {
			communication.raise(amount);
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void deal(){
		try {
			communication.deal();
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void allIn(){
		try {
			communication.allIn();
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void say(String message){
		communication.say(message);
	}
	public void joinTable(TableId id){
		try {
			communication.joinTable(id);
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public void leaveTable(){
		try {
			communication.leaveTable();
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
	public TableId createTable(){
		try {
			return communication.createTable();
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
			return null;
		}
	}
	public void startGame(){
		try {
			communication.startGame();
		} catch (IllegalActionException e) {
			gui.displayErrorMessage(e.getMessage());
		}
	}
}
