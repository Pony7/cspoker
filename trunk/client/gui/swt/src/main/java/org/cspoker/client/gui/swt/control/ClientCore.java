package org.cspoker.client.gui.swt.control;

import org.cspoker.client.User;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.client.gui.swt.window.LoginDialog;
import org.cspoker.common.api.account.event.AccountListener;
import org.cspoker.common.api.cashier.event.CashierListener;
import org.cspoker.common.api.chat.event.ChatListener;
import org.cspoker.common.api.lobby.event.LobbyListener;
import org.cspoker.common.api.shared.ServerContext;
import org.cspoker.common.api.shared.event.ServerListener;
import org.cspoker.common.elements.table.DetailedTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.util.Log4JPropertiesLoader;

/**
 * The core of any client
 * 
 * @author Cedric
 */
public class ClientCore
		implements ServerListener, Runnable {
	
	private boolean loggedIn = false;
	private static final User DEFAULT_TEST_USER = new User("test", "test");
	/**
	 * Default port is 8080 (RMI)
	 */
	public static final int DEFAULT_PORT_RMI = 8080;
	/**
	 * Default port is 8080 (RMI)
	 */
	public static final int DEFAULT_PORT_SOCKET = 8081;
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
	private ClientGUI gui;
	/**
	 * The client of this client core
	 */
	private User user;
	/**
	 * The communication used by this client
	 */
	private ServerContext communication;
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	/**
	 * Creates a new client core with a default test user
	 */
	public ClientCore() {
		this(DEFAULT_TEST_USER);
	}
	
	/**
	 * Creates a new client core
	 */
	public ClientCore(User user) {
		this.user = user;
	}
	
	/***************************************************************************
	 * Communication
	 **************************************************************************/
	
	public DetailedTable createDefaultTable() {
		try {
			// Generate a test table with a small blind of 5 and 2 second deal
			// delays
			return communication.getLobbyContext().createTable(user.getUserName() + "'s table",
					new TableConfiguration(200, 2000));
		} catch (Exception e) {
			e.printStackTrace();
			ClientGUI.displayErrorMessage(e);
			return null;
		}
	}
	
	public ServerContext getCommunication() {
		return communication;
	}
	
	public User getUser() {
		return user;
	}
	
	/**
	 * Starts the CSPoker SWT Client by opening a new {@link LoginDialog} Upon
	 * returning from it, the Lobby is opened
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			gui = new ClientGUI(this);
			communication = getGui().createNewLoginDialog().open();
			communication.subscribe(this);
			LobbyWindow lobby = new LobbyWindow(this);
			getGui().setLobby(lobby);
			
			lobby.show();
			lobby.refreshTables();
		} catch (Exception e) {
			// Run the whole GUI inside a try-catch for now so we can catch
			// failures
			ClientGUI.displayErrorMessage(e);
			// TODO Kill communication, reset everything
			// ...
			// and then start anew
			run();
		}
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		if (isLoggedIn()) {
			// Cannot switch user when logged in
			this.user = user;
		}
		
	}
	
	public ClientGUI getGui() {
		return gui;
	}
	
	public AccountListener getAccountListener() {
		// TODO Implement account listener
		return null;
	}
	
	public CashierListener getCashierListener() {
		// TODO Implement cashier listener
		return null;
	}
	
	public ChatListener getChatListener() {
		return getGui().getLobby();
	}
	
	public LobbyListener getLobbyListener() {
		return getGui().getLobby();
	}
	
	// public void onGameEvent(final GameEvent event) {
	// System.out.println(event);
	// Display.getDefault().asyncExec(new Runnable() {
	//		
	// public void run() {
	// try {
	// GameWindow gw = gui.getGameWindow(event.getTableId());
	// assert (gw != null) : "Could not find a window for table id " +
	// event.getTableId();
	// event.dispatch(gw);
	// gw.getUserInputComposite().showGameEventMessage(event);
	// gw.redraw();
	// } catch (RemoteException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// });
	// }
}
