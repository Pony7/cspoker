package org.cspoker.ai.bots;

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

import java.io.IOException;
import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.ai.bots.bot.Bot;
import org.cspoker.ai.bots.bot.BotFactory;
import org.cspoker.ai.bots.bot.gametree.mcts.FixedSampleMCTSBotFactory;
import org.cspoker.ai.bots.bot.gametree.mcts.listeners.SWTTreeListener;
import org.cspoker.ai.bots.bot.gametree.mcts.nodes.MCTSBucketShowdownNode;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.backpropagation.MaxDistributionPlusBackPropStrategy;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.backpropagation.MixedBackPropStrategy;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.backpropagation.SampleWeightedBackPropStrategy;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.MaxValueSelector;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.SamplingToFunctionSelector;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.UCTPlusPlusSelector;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.UCTSelector;
import org.cspoker.ai.bots.bot.gametree.search.nodevisitor.StatisticsVisitor;
import org.cspoker.ai.bots.listener.DefaultBotListener;
import org.cspoker.ai.bots.listener.ProfitCalculator;
import org.cspoker.ai.bots.listener.ProfitListener;
import org.cspoker.ai.opponentmodels.weka.WekaRegressionModelFactory;
import org.cspoker.client.User;
import org.cspoker.client.common.SmartClientContext;
import org.cspoker.client.common.SmartLobbyContext;
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
import org.cspoker.server.embedded.EmbeddedCSPokerServer;
import org.eclipse.swt.widgets.Display;

public class RunHumanVsBot{

	static {
		Log4JPropertiesLoader
		.load("org/cspoker/ai/experiments/logging/log4j.properties");
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
	private TableConfiguration tConfig;

	public RunHumanVsBot() {
		server = new EmbeddedCSPokerServer();
		displayexecutor = DisplayExecutor.getInstance();
	}

	public void testPlay() throws IllegalActionException, LoginException,
	RemoteException {
		final TableId tableId = new TableId(0);

		int smallBet = 100;
		int delay = 1500;
		User u = new User("Human", "test");
		client = new ClientCore(u);
		client.login(server);

		final LobbyWindow lobby = new LobbyWindow(client);
		lobby.setLobbyContext(client.getCommunication());
		client.getGui().setLobby(lobby);

		tConfig = new TableConfiguration(smallBet, delay,
				false, false, true);
		lobby.getContext().createHoldemTable(u.getUserName() + "'s test table", tConfig);
		// Run blocking calls in extra thread
		displayexecutor.execute(new Runnable() {

			public void run() {
				lobby.show();

			}
		});
		final GameWindow w = client.getGui().getGameWindow(tableId, true);
		w.getUser().sitIn(new SeatId(0), 0);
		// Run blocking calls in extra thread
		displayexecutor.execute(new Runnable() {

			public void run() {
				w.show();

			}
		});
		stats = new StatisticsVisitor.Factory();
		
		BotFactory botFactory;
		try {
			botFactory = new FixedSampleMCTSBotFactory(
					"Plus Bot",
					WekaRegressionModelFactory.createForZip("org/cspoker/ai/opponentmodels/weka/models/model1.zip"),
					new SamplingToFunctionSelector(50,new UCTSelector(40000)),
					new SamplingToFunctionSelector(50,new UCTPlusPlusSelector()),
					new MaxValueSelector(),
					new MCTSBucketShowdownNode.Factory(),
					new MixedBackPropStrategy.Factory(
							50,
							new SampleWeightedBackPropStrategy.Factory(),
							new MaxDistributionPlusBackPropStrategy.Factory()
					),
					20000,40000,80000,100000,
					new SWTTreeListener.Factory(client.getGui().getDisplay()));
			
//			botFactory = new SearchBotFactory(
//					new WekaRegressionModelFactory("/home/guy/Werk/thesis/weka-3-6-0/model1"),
//					new ShowdownBucketRolloutNode.Factory(),
//					new Log4JOutputVisitor.Factory(2), new SWTTreeVisitor.Factory(client.getGui().getDisplay()), stats
//					);
				

//			botFactory = new MCTSBotFactory(
//					new WekaRegressionModelFactory("/home/guy/Werk/thesis/weka-3-6-0/model1"),
//					new SamplingToFunctionSelector(new UCTSelector(40000)),
//					new SampleProportionateSelector(),
//					new MCTSShowdownRollOutNode.Factory(),
//					new SWTTreeListener.Factory(client.getGui().getDisplay()));
//			
//			botFactory = new MCTSBotFactory(
//					"Bot 5000",
//					new WekaRegressionModelFactory("/home/guy/Werk/thesis/weka-3-6-0/model1"),
//					new SamplingToFunctionSelector(20,new UCTPlusSelector(40000,3)),
//					new MixedSelectionStrategy(new SamplingSelector(), new MinValueSelector(),0.95),
//					new MaxValueSelector(),
//					new MCTSBucketShowdownNode.Factory(),
//					new MixtureBackPropStrategy.Factory(new MaxUnderValueSelector(2)),
//					1000,
//					new SWTTreeListener.Factory(client.getGui().getDisplay()));

//				BotFactory botFactory = new WekaRegressionBotFactory(
//						new ShowdownRolloutNode.Factory(new DistributionRollout4.Factory()),
//						 "/home/guy/Werk/thesis/weka-3-6-0/model1",
//						new Log4JOutputVisitor.Factory(2), new SWTTreeVisitor.Factory(
//								client.getGui().getDisplay()));

		// BotFactory botFactory = new PrologCafeBotFactory(new
		// DistributionShowdownNode4.Factory(),
		// new Log4JOutputVisitor.Factory(2), new
		// SWTTreeVisitor.Factory(client.getGui().getDisplay()));

		} catch (IOException e) {
			throw new IllegalStateException(e);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		startBot(botFactory, tableId, 0);

		// Listen to events#
		Display display = Display.getDefault();
		while (!display.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	private int botIndex = 1;
	private Bot bot;

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
		bot = botFactory.createBot(botId, tableId, lobbyContext, buyin,
				executor, 
				new ProfitListener(1,new ProfitCalculator(){
					
					@Override
					public double getProfit() {
						return bot.getProfit().getMean()*1.0/tConfig.getSmallBet();
					}
					
					@Override
					public String toString() {
						return "Bot";
					}
					
				}), 
				new DefaultBotListener() {
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
				if(statistics!=null){
					logger.info("NbNodes="+statistics.getNbNodes());
					logger.info("NbPrunedSubTrees="+statistics.getNbPrunedSubtrees());
					logger.info("NbPrunedTokens="+statistics.getNbPrunedTokens());
					logger.info("NbOpponentModelCalls="+statistics.getNbOpponentModelCalls());
				}
			}
		});
		bot.start();
		bot.startGame();
	}
	
}
