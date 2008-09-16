package org.cspoker.client.gui.swt.control;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.client.user.User;
import org.cspoker.client.xml.sockets.RemotePlayerCommunicationFactoryForSocket;
import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.elements.table.TableList;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.events.gameevents.GameEvent;
import org.cspoker.common.events.serverevents.ServerEvent;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.eclipse.swt.widgets.Display;

/**
 * The core of any client
 * 
 * @author Cedric
 */
public class ClientCore
		implements RemoteAllEventsListener, Runnable {
	
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
	private RemotePlayerCommunication communication;
	
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
	
	public void allIn(TableId tableId) {
		try {
			communication.allIn(tableId);
		} catch (Exception e) {
			gui.displayErrorMessage(e);
		}
	}
	
	public void bet(TableId tableId, int amount) {
		try {
			communication.bet(tableId, amount);
		} catch (Exception e) {
			gui.displayErrorMessage(e);
		}
	}
	
	public void raise(TableId tableId, int amount) {
		try {
			communication.raise(tableId, amount);
		} catch (Exception e) {
			gui.displayErrorMessage(e);
		}
	}
	
	/***************************************************************************
	 * Bet
	 **************************************************************************/
	public void call(TableId tableId) {
		try {
			communication.call(tableId);
		} catch (Exception e) {
			try {
				communication.check(tableId);
			} catch (Exception e1) {
				gui.displayErrorMessage(e);
				e1.printStackTrace();
			}
		}
	}
	
	public void check(TableId tableId) {
		try {
			communication.check(tableId);
		} catch (Exception e) {
			gui.displayErrorMessage(e);
		}
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
	
	public TableId createTable() {
		try {
			// Generate a test table with a small blind of 5 and 2 second deal
			// delays
			return communication.createTable(user.getUserName() + "'s table", new GameProperty(200, 2000)).getId();
		} catch (Exception e) {
			e.printStackTrace();
			gui.displayErrorMessage(e);
			return null;
		}
	}
	
	public void fold(TableId tableId) {
		try {
			communication.fold(tableId);
		} catch (Exception e) {
			gui.displayErrorMessage(e);
		}
	}
	
	public RemotePlayerCommunication getCommunication() {
		return communication;
	}
	
	public TableList getTables() {
		try {
			return communication.getTables();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return new TableList(new ArrayList<Table>());
	}
	
	public User getUser() {
		return user;
	}
	
	public Table joinTable(TableId id, int buyin) {
		Table t = null;
		// Leave current table (if necessary)
		try {
			communication.leaveTable(id);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalActionException e2) {
			// Ignore, does not matter
		}
		try {
			t = communication.joinTable(id, null, buyin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public Table joinTable(TableId id, SeatId seatId, int buyin) {
		Table t = null;
		// Leave current table (if necessary)
		try {
			communication.leaveTable(id);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalActionException e2) {
			// Ignore, does not matter
		}
		try {
			t = communication.joinTable(id, seatId, buyin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public void leaveTable(TableId tableId) {
		try {
			communication.leaveTable(tableId);
		} catch (Exception e) {
			gui.displayErrorMessage(e);
		}
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
	
	@Override
	public void onServerEvent(final ServerEvent event)
			throws RemoteException {
		Display.getDefault().asyncExec(new Runnable() {
			
			public void run() {
				try {
					event.dispatch(gui.lobby);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void run() {
		gui.createNewLoginDialog().open();
	}
	
	public void say(TableId tableId, String message) {
		try {
			communication.say(tableId, message);
		} catch (Exception e) {
			gui.displayErrorMessage(e);
		}
	}
	
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	public void startGame(TableId tableId) {
		try {
			communication.startGame(tableId);
		} catch (Exception e) {
			gui.displayErrorMessage(e);
		}
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
}
