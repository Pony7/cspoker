package org.cspoker.client.gui.swt.control;

import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.cspoker.client.User;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.client.gui.swt.window.LoginDialog;
import org.cspoker.client.xml.sockets.RemotePlayerCommunicationFactoryForSocket;
import org.cspoker.common.api.account.event.AccountListener;
import org.cspoker.common.api.cashier.event.CashierListener;
import org.cspoker.common.api.chat.event.ChatListener;
import org.cspoker.common.api.lobby.LobbyContext;
import org.cspoker.common.api.lobby.event.LobbyListener;
import org.cspoker.common.api.shared.event.ServerListener;
import org.cspoker.common.elements.table.DetailedTable;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.eclipse.swt.widgets.Display;

/**
 * The core of any client
 * 
 * @author Cedric
 */
public class ClientCore
		implements ServerListener, Runnable {
	
	private static final User DEFAULT_TEST_USER = new User("test", "test");
	/**
	 * Default port is 8081 (Socket)
	 */
	private static final int DEFAULT_PORT = 8081;
	/**
	 * Default URL is localhost
	 */
	public static final String DEFAULT_URL = "localhost";
	static {
		Log4JPropertiesLoader.load("org/cspoker/client/gui/text/logging/log4j.properties");
	}
	
	/**
	 * Starts a new client
	 */
	public static void main(String[] args) {
		new ClientCore().run();
	}
	
	/**
	 * The gui of this client
	 */
	private final ClientGUI gui;
	/**
	 * The client of this client core
	 */
	private User user;
	/**
	 * The communication used by this client
	 */
	private LobbyContext communication;
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	/**
	 * Creates a new client core
	 */
	public ClientCore() {
		this(DEFAULT_TEST_USER);
	}
	
	/**
	 * Creates a new client core
	 */
	public ClientCore(User user) {
		this.gui = new ClientGUI(this);
		this.user = user;
	}
	
	/***************************************************************************
	 * Communication
	 **************************************************************************/
	/**
	 * Creates a new communication with a server at the given url and port for a
	 * user with the given user name and password
	 * 
	 * @throws LoginException
	 * @throws RemoteException
	 */
	public RemotePlayerCommunication createCommunication(String url, int port, String username, String password)
			throws LoginException, RemoteException {
		RemotePlayerCommunication comm = null;
		System.out.println("LOGIN ATTEMPT");
		System.out.println("url : " + url);
		System.out.println("port : " + port);
		System.out.println("user name : " + username);
		System.out.println("password : " + password);
		// Create communication
		comm = new RemotePlayerCommunicationFactoryForSocket(url, port)
				.getRemotePlayerCommunication(username, password);
		// Subscribe to all events as a listener
		comm.subscribeAllEventsListener(this);
		return comm;
	}
	
	public DetailedTable createDefaultTable() {
		try {
			// Generate a test table with a small blind of 5 and 2 second deal
			// delays
			return communication.createTable(user.getUserName() + "'s table", new TableConfiguration(200, 2000));
		} catch (Exception e) {
			e.printStackTrace();
			gui.displayErrorMessage(e);
			return null;
		}
	}
	
	public LobbyContext getCommunication() {
		return communication;
	}
	
	public User getUser() {
		return user;
	}
	
	public void login(String serverUrl)
			throws LoginException, RemoteException {
		login(serverUrl, DEFAULT_PORT, user.getUserName(), user.getPassword());
	}
	
	public void onGameEvent(final GameEvent event) {
		System.out.println(event);
		Display.getDefault().asyncExec(new Runnable() {
			
			public void run() {
				try {
					GameWindow gw = gui.getGameWindow(event.getTableId());
					assert (gw != null) : "Could not find a window for table id " + event.getTableId();
					event.dispatch(gw);
					gw.getUserInputComposite().showGameEventMessage(event);
					gw.redraw();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Starts the CSPoker SWT Client by opening a new {@link LoginDialog}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		gui.createNewLoginDialog().open();
	}
	
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/***************************************************************************
	 * Login
	 **************************************************************************/
	/**
	 * Logs in a new user with the given username and password to the given
	 * server url and port.
	 * 
	 * @param url the given server url
	 * @param port the given server port
	 * @param userName the given user name
	 * @param password the given password
	 * @throws RemoteException
	 * @throws LoginException
	 */
	private void login(String url, int port, String userName, String password)
			throws LoginException, RemoteException {
		communication = createCommunication(url, port, userName, password);
		gui.lobby = new LobbyWindow(Display.getDefault(), gui, this);
	}
	
	public ClientGUI getGui() {
		return gui;
	}
	
	public AccountListener getAccountListener() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public CashierListener getCashierListener() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ChatListener getChatListener() {
		return gui.lobby;
	}
	
	public LobbyListener getLobbyListener() {
		return gui.lobby;
	}
	
	public DetailedTable getTable(Table t) {
		// TODO Auto-generated method stub
		return null;
	}
}
