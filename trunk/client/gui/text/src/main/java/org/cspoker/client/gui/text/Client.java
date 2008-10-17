/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.gui.text;

import ClientCore;
import ClientGUI;
import GameWindow;
import LobbyWindow;
import LoginDialog;
import User;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.client.common.RemotePlayerCommunicationFactory;
import org.cspoker.client.common.RemotePlayerCommunicationFactory.NoProviderException;
import org.cspoker.client.gui.text.commands.AllInCommand;
import org.cspoker.client.gui.text.commands.BetCommand;
import org.cspoker.client.gui.text.commands.CallCommand;
import org.cspoker.client.gui.text.commands.CardsCommand;
import org.cspoker.client.gui.text.commands.CheckCommand;
import org.cspoker.client.gui.text.commands.Command;
import org.cspoker.client.gui.text.commands.CreateTableCommand;
import org.cspoker.client.gui.text.commands.FoldCommand;
import org.cspoker.client.gui.text.commands.GetTableCommand;
import org.cspoker.client.gui.text.commands.GetTablesCommand;
import org.cspoker.client.gui.text.commands.HelpCommand;
import org.cspoker.client.gui.text.commands.JoinTableCommand;
import org.cspoker.client.gui.text.commands.LeaveTableCommand;
import org.cspoker.client.gui.text.commands.PotCommand;
import org.cspoker.client.gui.text.commands.RaiseCommand;
import org.cspoker.client.gui.text.commands.SayCommand;
import org.cspoker.client.gui.text.commands.StartGameCommand;
import org.cspoker.client.gui.text.eventlistener.ConsoleListener;
import org.cspoker.client.gui.text.eventlistener.StatefulConsoleListener;
import org.cspoker.client.gui.text.savedstate.Cards;
import org.cspoker.client.gui.text.savedstate.Pot;
import org.cspoker.client.rmi.RemoteRMIServer;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListenerTree;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.lobby.listener.LobbyListenerTree;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.common.util.Log4JPropertiesLoader;

/**
 * Connect to the given server and passes on user commands.
 */
public class Client {

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
	
	private final static Logger logger = Logger.getLogger(Client.class);
	static {
		Log4JPropertiesLoader.load(LOG4J_PROPERTY_FILE);
	}
	private HashMap<String, Command> commands = new HashMap<String, Command>();
	private Console console;
	private ConsoleListener listener;
	/**
	 * The {@link User} of this client
	 */
	private User user;
	/**
	 * The communication used by this client
	 */
	private RemoteServerContext communication;

	public Client(final String username, final String password,
			Console console) {
		this.console = console;
		this.listener=new ConsoleListener(console);
		registerCommands();
	}

	private void registerCommands() throws RemoteException {
		commands.put("CREATETABLE", new CreateTableCommand(communication.getLobbyContext(listener), console));
		commands.put("JOINTABLE", new JoinTableCommand(rpc, console));
		commands.put("LEAVETABLE", new LeaveTableCommand(rpc, console));
		commands.put("GETTABLE", new GetTableCommand(rpc, console));
		commands.put("GETTABLES", new GetTablesCommand(rpc, console));

		commands.put("STARTGAME", new StartGameCommand(rpc, console));
		commands.put("CALL", new CallCommand(rpc, console));
		commands.put("BET", new BetCommand(rpc, console));
		commands.put("CHECK", new CheckCommand(rpc, console));
		commands.put("FOLD", new FoldCommand(rpc, console));
		commands.put("RAISE", new RaiseCommand(rpc, console));
		commands.put("ALLIN", new AllInCommand(rpc, console));

		commands.put("SAY", new SayCommand(rpc, console));

		Cards cards = new Cards();
		Pot pot = new Pot();
		CardsCommand cardsCommand = new CardsCommand(console, cards);
		commands.put("CARDS", cardsCommand);
		PotCommand potCommand = new PotCommand(console, pot);
		commands.put("POT", potCommand);

		HelpCommand help = new HelpCommand(console);
		commands.put("HELP", help);

		rpc.subscribeAllEventsListener(new StatefulConsoleListener(console,
				cards, pot));
	}

	private Command getCommand(String name) {
		return commands.get(name.toUpperCase());
	}

	public void execute(String command, String... args) throws Exception {
		Command c = getCommand(command);
		if (c == null) {
			throw new IllegalArgumentException("Not a valid command.");
		}
		c.execute(args);
	}

	public void close() {
		// no op
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
	 * @pre The user is not currently logged in
	 * @param user the user to set
	 */
	public void setUser(User user) {
		// Cannot switch user when logged in
		assert (user == null || !user.isLoggedIn()) : "You can not switch to another account while logged in!";
		this.user = user;
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
	public RemoteServerContext login()
			throws LoginException {
		RemoteCSPokerServer loginServer=new RemoteRMIServer(DEFAULT_URL,DEFAULT_PORT_RMI);
		try {
			communication = loginServer.login(user.getUserName(), user.getPassword());
		} catch (RemoteException e) {
			handleRemoteException(e);
		}
		
		return communication;
	}
}
