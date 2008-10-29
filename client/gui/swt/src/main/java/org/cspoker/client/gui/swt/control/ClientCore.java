/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.gui.swt.control;

import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.client.User;
import org.cspoker.client.common.CommunicationProvider;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.client.gui.swt.window.LoginDialog;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListenerTree;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.lobby.listener.LobbyListenerTree;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.common.api.shared.listener.ServerListenerTree;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.eclipse.swt.widgets.Display;

/**
 * The core of the SWT client which manages the windows, the remote
 * {@link ServerContext} and the currently active user.
 * <p>
 * Furthermore, references to other parts of the SWT Client may be retrieved
 * from this class' utility methods, i.e. {@link #getGui()} etc., thus it is
 * intended that the reference to the {@link ClientCore} is shared between UI
 * components for such purposes.
 * <p>
 * In particular, the SWT client is started by calling {@link ClientCore#run()}.
 * 
 * @author Cedric, Stephan
 */
public class ClientCore
		implements ServerListenerTree, Runnable {
	
	private static final User DEFAULT_TEST_USER = new User("test", "test");
	private static final String LOG4J_PROPERTY_FILE = "logging/log4j.properties";
	/**
	 * Default port for RMI (<code>1099</code>)
	 */
	public static final int DEFAULT_PORT_RMI = 1099;
	/**
	 * Default port for sockets (<code>8081</code>)
	 */
	public static final int DEFAULT_PORT_SOCKET = 8081;
	/**
	 * Default URL is <code>localhost</code>
	 */
	public static final String DEFAULT_URL = "localhost";
	
	private final static Logger logger = Logger.getLogger(ClientCore.class);
	static {
		Log4JPropertiesLoader.load(LOG4J_PROPERTY_FILE);
	}
	
	/**
	 * Starts a new client
	 * 
	 * @param args No parameters needed
	 */
	public static void main(String... args) {
		new ClientCore().run();
	}
	
	/**
	 * The {@link ClientGUI} of this client
	 */
	private ClientGUI gui;
	/**
	 * The {@link User} of this client
	 */
	private User user;
	/**
	 * The communication used by this client
	 */
	private RemoteServerContext communication;
	
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
	 * Creates a new <code>ClientCore</code> for the given user
	 * <p>
	 * 
	 * @param user The {@link User} of this client. Calling
	 *            {@link ClientCore#run()} after this constructor will generate
	 *            a {@link LoginDialog} for the given user.
	 */
	public ClientCore(User user) {
		this.user = user;
		gui = new ClientGUI(this);
	}
	
	/**
	 * @return The currently used {@link ServerContext} if the client is logged
	 *         in, <code>null</code> otherwise
	 */
	public RemoteServerContext getCommunication() {
		return communication;
	}
	
	/**
	 * @return The currently active {@link User}. Only one user can be logged in
	 *         at any given time via a single {@link ClientCore}.
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Starts the CSPoker SWT Client by creating a new {@link ClientGUI} object
	 * and opening a new {@link LoginDialog}.
	 * <p>
	 * Terminates after the last shell is closed, otherwise the Event
	 * Dispatchers will keep the GUI open.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		CommunicationProvider communicationProvider = new CommunicationProvider();
		// new LoadProvidersFromXml(communicationProvider);
		// new LoadEmbeddedProvider(communicationProvider);
		// Run the whole GUI inside a try-catch for now so we can catch
		// unexpected failures
		try {
			gui.createNewLoginDialog().open();
			getUser().setLoggedIn(true);
			// TODO Make sure we register to receive events from the server
			// communication.subscribe(this);
			LobbyWindow lobby = new LobbyWindow(this);
			lobby.setLobbyContext(this.getCommunication());
			getGui().setLobby(lobby);
			lobby.show();
		} catch (final Exception e) {
			logger.error("Unexpected error", e);
			logger.info("Attempting reset");
			resetAll();
			// TODO Kill communication, reset everything
			
			Display.getDefault().asyncExec(new Runnable() {
				
				public void run() {
					ClientGUI.displayException(e);
				}
			});
			// and then start anew
			run();
		}
	}
	
	/**
	 * Helper method to reset everything. Disposes of all created windows and
	 * kills the server communication.
	 */
	public void resetAll() {
		for (GameWindow w : getGui().getGameWindows()) {
			if (!w.isDisposed())
				w.dispose();
		}
		getGui().getGameWindows().clear();
		if (!gui.getLobby().getShell().isDisposed())
			gui.getLobby().getShell().dispose();
		getUser().setLoggedIn(false);
		// TODO replace or ignore?
		// communication.trigger();
	}
	
	/**
	 * @pre The user is not currently logged in
	 * @param user the user to set
	 */
	public void setUser(User user) {
		// Cannot switch user when logged in
		assert (user == null || !user.isLoggedIn()) : "You can not switch to another account while logged in!";
		this.user = user;
	}
	
	/**
	 * @return The {@link ClientGUI} managing windows and display preferences
	 *         for this ClientCore.
	 */
	public ClientGUI getGui() {
		return gui;
	}
	
	/**
	 * TODO Wouldn't it be nice to have ChatListeners in the tree as well so we
	 * have chat listeners for a given table?
	 * 
	 * @see org.cspoker.common.api.shared.listener.ServerListenerTree#getChatListener()
	 */
	public ChatListener getChatListener() {
		return getGui().getLobby();
	}
	
	/**
	 * @return The {@link LobbyListenerTree} implemented by this SWT client
	 * @see org.cspoker.common.api.shared.listener.ServerListenerTree#getLobbyListenerTree()
	 */
	public LobbyListenerTree getLobbyListenerTree() {
		return new LobbyListenerTree() {
			
			public LobbyListener getLobbyListener() {
				return getGui().getLobby();
			}
			
			public HoldemTableListenerTree getHoldemTableListenerTree(final long tableId) {
				return new HoldemTableListenerTree() {
					
					public HoldemTableListener getHoldemTableListener() {
						return getGui().getGameWindow(tableId, false);
					}
					
					public HoldemPlayerListener getHoldemPlayerListener() {
						return getGui().getGameWindow(tableId, false);
					}
				};
			}
		};
	}
	
	/**
	 * The ClientCore should be a central entity for trying to recover from
	 * remote exceptions encountered throughout the client's various GameWindows
	 * 
	 * @param e The exception returned from the server
	 * @return If checking connection status was successful
	 */
	public boolean handleRemoteException(RemoteException e) {
		logger.error("RemoteException occurred", e);
		logger.error("Cause: ", e.getCause());
		// Try to reconnect a couple of times
		// TODO Implement reconnect status display and attempts
		return false;
	}
	
	/**
	 * Log in the current user to the {@link CSPokerServer}.
	 * 
	 * @param loginServer The remote login server to connect to
	 * @return <code>null</code>, if the dialog was disposed or the login was
	 *         unsuccessful, or the {@link RemoteServerContext} retrieved from
	 *         the server.
	 * @throws LoginException When the user provided an illegal password
	 */
	public RemoteServerContext login(RemoteCSPokerServer loginServer)
			throws LoginException {
		try {
			communication = loginServer.login(user.getUserName(), user.getPassword());
		} catch (RemoteException e) {
			handleRemoteException(e);
		}
		
		return communication;
	}
	
	@Override
	public ChatListener getServerChatListener() {
		return gui.getLobby();
	}
	
	@Override
	public ChatListener getTableChatListener(long tableID) {
		return gui.getGameWindow(tableID, false).getUserInputComposite();
	}
}
