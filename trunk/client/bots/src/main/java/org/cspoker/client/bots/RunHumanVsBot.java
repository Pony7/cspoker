package org.cspoker.client.bots;
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

import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.cspoker.client.User;
import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.bot.search.SearchBotFactory;
import org.cspoker.client.bots.bot.search.node.leaf.DistributionShowdownNode4;
import org.cspoker.client.common.SmartClientContext;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.client.communication.embedded.EmbeddedCSPokerServer;
import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.DisplayExecutor;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.api.lobby.listener.DefaultLobbyListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.common.util.threading.SingleThreadRequestExecutor;
import org.eclipse.swt.widgets.Display;

public class RunHumanVsBot {

	static {
		Log4JPropertiesLoader.load("org/cspoker/client/bots/logging/log4j.properties");
	}
	
	public static void main(String[] args) throws LoginException, RemoteException, IllegalActionException {
		(new RunHumanVsBot()).testPlay();
	}

	private ClientCore client;
	protected CSPokerServer server;
	private DisplayExecutor displayexecutor;
	
	private BotFactory botFactory = new SearchBotFactory(new DistributionShowdownNode4.Factory());

	public RunHumanVsBot(){
		server = new EmbeddedCSPokerServer();
		displayexecutor = DisplayExecutor.getInstance();
	}

	public void testPlay() throws IllegalActionException, LoginException, RemoteException {
		final TableId tableId = new TableId(0);

		int smallBlind = 50;
		int buyin = smallBlind * 400;
		int delay = 2000;
		User u = new User("Human","test");
		client = new ClientCore(u);
		client.login(server);

		final LobbyWindow lobby = new LobbyWindow(client);
		lobby.setLobbyContext(client.getCommunication());
		client.getGui().setLobby(lobby);

		TableConfiguration tConfig = new TableConfiguration(smallBlind, delay, false);
		lobby.getContext().createHoldemTable(u.getUserName() + "'s test table", tConfig);
		// Run blocking calls in extra thread
		displayexecutor.execute(new Runnable() {

			public void run() {
				lobby.show();

			}
		});
		final GameWindow w = client.getGui().getGameWindow(tableId, true);
		w.getUser().sitIn(new SeatId(0),buyin);
		// Run blocking calls in extra thread
		displayexecutor.execute(new Runnable() {

			public void run() {
				w.show();

			}
		});
		
		//Start Bot
		SmartClientContext clientContext = new SmartClientContext(server.login("Bot", "test"));
		SmartLobbyContext lobbyContext = clientContext.getLobbyContext(new DefaultLobbyListener());
		PlayerId botId = clientContext.getAccountContext().getPlayerID();
		SingleThreadRequestExecutor executor = SingleThreadRequestExecutor.getInstance();
		Bot bot = botFactory.createBot(botId, tableId, lobbyContext, buyin, executor);
		bot.start();
		bot.startGame();
		
		// Listen to events#
		Display display = Display.getDefault();
		while (!display.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}
}
