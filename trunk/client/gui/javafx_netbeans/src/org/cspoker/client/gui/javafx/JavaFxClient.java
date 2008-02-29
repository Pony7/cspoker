package org.cspoker.client.gui.javafx;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.cspoker.client.gui.javafx.elements.TableInterface;
import org.cspoker.client.gui.javafx.elements.TableImpl;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;

/**
 * The core of any client
 * @author Cedric
 *
 */
public class JavaFxClient {
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
	public JavaFxClient(){
	}
	
	/**********************************************************
	 * Communication
	 **********************************************************/
        public void subscribeAllEvents(RemoteAllEventsListener listener) throws RemoteException{
            //communication.subscribeAllEventsListener(listener);
            System.out.println("Listener subscribed to all events");
        }
	/**
	 * Creates a new communication with a server at the given url and port
	 * for a user with the given user name and password
	 */
	public void createCommunication(String connection, String username, String password){
		System.out.println("LOGIN ATTEMPT");
		System.out.println("connection : "+connection);
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
	public void login(String connection, String userName, String password){
		this.client=new User(userName);
		createCommunication(connection,userName,password);
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
	public void say(String message) throws RemoteException, IllegalActionException{
		communication.say(message);
	}
	public void joinTable(int n){
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

	public TableInterface[] getTableList() {
		// TODO create method to ask the server for a list of table id's
		List<TableInterface> result=new ArrayList<TableInterface>();
		Random generator = new Random();
		int random=generator.nextInt(25);
		for(int j=0;j<random;j++){
			result.add(new TableImpl(j,5,2,4));
		}
		TableInterface[] r=new TableInterface[result.size()];
                result.toArray(r);
                return r;
	}
}
