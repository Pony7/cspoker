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

import org.apache.log4j.Logger;
import org.cspoker.client.User;
import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.bot.gametree.rollout.DistributionRollout4;
import org.cspoker.client.bots.bot.gametree.search.SearchBotFactory;
import org.cspoker.client.bots.bot.gametree.search.ShowdownRolloutNode;
import org.cspoker.client.bots.bot.gametree.search.nodevisitor.Log4JOutputVisitor;
import org.cspoker.client.bots.bot.gametree.search.nodevisitor.SWTTreeVisitor;
import org.cspoker.client.bots.bot.gametree.search.nodevisitor.StatisticsVisitor;
import org.cspoker.client.bots.listener.DefaultBotListener;
import org.cspoker.client.common.SmartClientContext;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.client.communication.embedded.EmbeddedCSPokerServer;
import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.DisplayExecutor;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
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
		Log4JPropertiesLoader
				.load("org/cspoker/client/bots/logging/log4j.properties");
	}

	private final static Logger logger = Logger.getLogger(RunHumanVsBot.class);
	
	public static void main(String[] args) throws LoginException,
			RemoteException, IllegalActionException {
		new RunHumanVsBot().testPlay();
	}

	private ClientCore client;
	protected CSPokerServer server;
	private final DisplayExecutor displayexecutor;
	private StatisticsVisitor.Factory stats;

	public RunHumanVsBot() {
		server = new EmbeddedCSPokerServer();
		displayexecutor = DisplayExecutor.getInstance();
	}

	public void testPlay() throws IllegalActionException, LoginException,
			RemoteException {
		final TableId tableId = new TableId(0);

		int smallBlind = 50;
		final int buyin = smallBlind * 200;
		int delay = 1500;
		User u = new User("Human", "test");
		client = new ClientCore(u);
		client.login(server);

		final LobbyWindow lobby = new LobbyWindow(client);
		lobby.setLobbyContext(client.getCommunication());
		client.getGui().setLobby(lobby);

		TableConfiguration tConfig = new TableConfiguration(smallBlind, delay,
				false, true);
		lobby.getContext().createHoldemTable(u.getUserName() + "'s test table",
				tConfig);
		// Run blocking calls in extra thread
		displayexecutor.execute(new Runnable() {

			public void run() {
				lobby.show();

			}
		});
		final GameWindow w = client.getGui().getGameWindow(tableId, true);
		w.getUser().sitIn(new SeatId(0), buyin);
		// Run blocking calls in extra thread
		displayexecutor.execute(new Runnable() {

			public void run() {
				w.show();

			}
		});
		stats = new StatisticsVisitor.Factory();
		BotFactory botFactory = new SearchBotFactory(
				new ShowdownRolloutNode.Factory(new DistributionRollout4.Factory()),
				new Log4JOutputVisitor.Factory(2), new SWTTreeVisitor.Factory(client.getGui().getDisplay()), stats
				);
		
//		BotFactory botFactory = new WekaClassificationBotFactory(
//				new DistributionShowdownNode4.Factory(),
//				 "/home/guy/Bureaublad/weka-3-6-0/ANN-c-cb.model",
//				 "/home/guy/Bureaublad/weka-3-6-0/J48-c-fcr.model",
//				new Log4JOutputVisitor.Factory(2), new SWTTreeVisitor.Factory(
//						client.getGui().getDisplay()));
		
//		BotFactory botFactory = new WekaRegressionBotFactory(
//				new DistributionShowdownNode4.Factory(),
//				 "/home/guy/Bureaublad/weka-3-6-0/M5P-r-b.model",
//				 "/home/guy/Bureaublad/weka-3-6-0/M5P-r-f.model",
//				 "/home/guy/Bureaublad/weka-3-6-0/M5P-r-c.model",
//				 "/home/guy/Bureaublad/weka-3-6-0/M5P-r-r.model",
//				new Log4JOutputVisitor.Factory(2), new SWTTreeVisitor.Factory(
//						client.getGui().getDisplay()));
		
		// BotFactory botFactory = new PrologCafeBotFactory(new
		// DistributionShowdownNode4.Factory(),
		// new Log4JOutputVisitor.Factory(2), new
		// SWTTreeVisitor.Factory(client.getGui().getDisplay()));

		startBot(botFactory, tableId, buyin);

		// Listen to events#
		Display display = Display.getDefault();
		while (!display.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	private int botIndex = 1;

	private void startBot(BotFactory botFactory, final TableId tableId,
			final int buyin) throws LoginException, RemoteException,
			IllegalActionException {
		// Start Bot
		SmartClientContext clientContext = new SmartClientContext(server.login(
				"Bot " + botIndex++, "test"));
		final SmartLobbyContext lobbyContext = clientContext
				.getLobbyContext(new DefaultLobbyListener());
		final PlayerId botId = clientContext.getAccountContext().getPlayerID();
		final SingleThreadRequestExecutor executor = SingleThreadRequestExecutor
				.getInstance();
		Bot bot = botFactory.createBot(botId, tableId, lobbyContext, buyin,
				executor, new DefaultBotListener() {
					@Override
					public void onSitOut(SitOutEvent event) {
						// if(event.getPlayerId().equals(botId)){
						// try {
						// startBot(tableId, buyin);
						// } catch (LoginException e) {
						// e.printStackTrace();
						// } catch (RemoteException e) {
						// e.printStackTrace();
						// } catch (IllegalActionException e) {
						// e.printStackTrace();
						// }
						// }
					}
					
					@Override
					public void onActionPerformed() {
						StatisticsVisitor statistics = stats.getStatistics();
						logger.info("NbNodes="+statistics.getNbNodes());
						logger.info("NbPrunedSubTrees="+statistics.getNbPrunedSubtrees());
						logger.info("NbPrunedTokens="+statistics.getNbPrunedTokens());
						logger.info("NbOpponentModelCalls="+statistics.getNbOpponentModelCalls());
					}
				});
		bot.start();
		bot.startGame();
	}
}
