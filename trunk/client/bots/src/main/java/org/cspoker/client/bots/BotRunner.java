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
package org.cspoker.client.bots;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

import javax.security.auth.login.LoginException;

import net.jcip.annotations.NotThreadSafe;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.bot.gametree.mcts.MCTSBotFactory;
import org.cspoker.client.bots.bot.gametree.mcts.listeners.SWTTreeListener;
import org.cspoker.client.bots.bot.gametree.mcts.nodes.MCTSBucketShowdownNode;
import org.cspoker.client.bots.bot.gametree.mcts.strategies.SampleProportionateSelector;
import org.cspoker.client.bots.bot.gametree.mcts.strategies.SamplingToFunctionSelector;
import org.cspoker.client.bots.bot.gametree.mcts.strategies.UCTSelector;
import org.cspoker.client.bots.bot.gametree.opponentmodel.weka.WekaRegressionModelFactory;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.bots.listener.GameLimitingBotListener;
import org.cspoker.client.bots.listener.ReSitInBotListener;
import org.cspoker.client.bots.listener.SpeedTestBotListener;
import org.cspoker.client.common.SmartClientContext;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.listener.DefaultLobbyListener;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.common.util.threading.GlobalThreadPool;
import org.cspoker.common.util.threading.SingleThreadRequestExecutor;

@NotThreadSafe
public class BotRunner implements LobbyListener {

	private static final TableConfiguration config = new TableConfiguration(100,
			0, false, true);

	public static final int nbGamesPerConfrontation = 10000;

	public static final int reportInterval = 2;

	public final int nbPlayersPerGame;

	static {
		Log4JPropertiesLoader
		.load("org/cspoker/client/bots/logging/log4j.properties");
	}

	private final static Logger logger = Logger.getLogger(BotRunner.class);

	private final BotFactory[] bots;

	private final SmartLobbyContext[] botLobbies;
	private final PlayerId[] botIDs;

	protected final SmartLobbyContext directorLobby;

	private final ExecutorService executor;

	private volatile Bot[] bot;

	private volatile int[] botProfit;

	private volatile int[] botIndex;

	private BotListener speedMonitor;
	private volatile BotListener gameLimiter;

	public static BotRunner create(RemoteCSPokerServer cspokerServer) {
		try {
			return new BotRunner(cspokerServer,
					new BotFactory[] {
					new MCTSBotFactory(
							new WekaRegressionModelFactory("/home/guy/Werk/thesis/weka-3-6-0/model1"),
							new SamplingToFunctionSelector(new UCTSelector(40000)),
							new SampleProportionateSelector(),
							new MCTSBucketShowdownNode.Factory()),
					new MCTSBotFactory(
							new WekaRegressionModelFactory("/home/guy/Werk/thesis/weka-3-6-0/model1"),
							new SamplingToFunctionSelector(new UCTSelector(40000)),
							new SampleProportionateSelector(),
							new MCTSBucketShowdownNode.Factory()),
					// ML bots
					//						 new RuleBasedBotFactory1(),
					//				new RuleBasedBotFactory2(),
					//				new SearchBotFactory(new ShowdownRolloutNode.Factory(new DistributionRollout4.Factory())),
//				new MCTSBotFactory(),
					//				 new SearchBotFactory(new
					//				 DistributionShowdownNode4.Factory()),
					// new RuleBasedBotFactory1(),
					// new RuleBasedBotFactory2(),

					// new SearchBotFactory(new
					// DistributionShowdownNode1.Factory()),
					// new SearchBotFactory(new
					// DistributionShowdownNode2.Factory()),
					// new SearchBotFactory(new
					// DistributionShowdownNode4.Factory()),
					// new RuleBasedBotFactory(),
					//						new PrologCafeBotFactory(
					//								new DistributionShowdownNode4.Factory()),
					//				new WekaClassificationBotFactory(
					//						new DistributionShowdownNode4.Factory(),
					//						"/home/guy/Bureaublad/weka-3-6-0/ANN-c-cb.model",
					//						"/home/guy/Bureaublad/weka-3-6-0/ANN-c-fcr.model"
					//				),
					//				new WekaRegressionBotFactory(
					//						new ShowdownRolloutNode.Factory(new DistributionRollout4.Factory()),
					//						"/home/guy/Werk/thesis/weka-3-6-0/M5P-r-b.model",
					//						"/home/guy/Werk/thesis/weka-3-6-0/M5P-r-f.model",
					//						"/home/guy/Werk/thesis/weka-3-6-0/M5P-r-c.model",
					//						"/home/guy/Werk/thesis/weka-3-6-0/M5P-r-r.model"
					//				),

					//				new PrologCafeBotFactory(new ShowdownRolloutNode.Factory(new DistributionRollout4.Factory())),
					// new SearchBotFactory(new CachedShowdownNodeFactory(new
					// UniformShowdownNode.Factory())),
			});
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	public BotRunner(RemoteCSPokerServer cspokerServer, BotFactory[] bots) {
		try {
			this.bots = bots;
			nbPlayersPerGame = 2;// bots.length;//or a constant
			bot = new Bot[nbPlayersPerGame];
			botProfit = new int[nbPlayersPerGame];
			botIndex = new int[nbPlayersPerGame];

			botLobbies = new SmartLobbyContext[bots.length];
			botIDs = new PlayerId[bots.length];

			SmartClientContext director = new SmartClientContext(cspokerServer
					.login("director", "test"));
			directorLobby = director.getLobbyContext(BotRunner.this);

			for (int i = 0; i < bots.length; i++) {
				SmartClientContext clientContext = new SmartClientContext(
						cspokerServer.login(bots[i].toString(), "test"));
				botLobbies[i] = clientContext
				.getLobbyContext(new DefaultLobbyListener());
				botIDs[i] = clientContext.getAccountContext().getPlayerID();
			}

			executor = SingleThreadRequestExecutor.getInstance();

			speedMonitor = new SpeedTestBotListener(reportInterval, this);

			// set start indexes
			for (int i = 0; i < nbPlayersPerGame - 1; i++) {
				botIndex[i] = i;
			}
			botIndex[nbPlayersPerGame - 1] = nbPlayersPerGame - 2;

			iterateBots();

		} catch (LoginException e) {
			throw new IllegalStateException("Login Failed", e);
		} catch (RemoteException e) {
			logger.error(e);
			throw new IllegalStateException("Server setup failed.", e);
		} catch (IllegalActionException e) {
			logger.error(e);
			throw new IllegalStateException("Server setup failed.", e);
		}

	}

	private void iterateBots() {
		if (botIndex[nbPlayersPerGame - 1] < bots.length - 1) {
			botIndex[nbPlayersPerGame - 1]++;
			resetStateAndStartPlay();
		} else {
			for (int i = nbPlayersPerGame - 2; i >= 0; i--) {
				if (botIndex[i] + 1 < botIndex[i + 1]) {
					botIndex[i]++;
					resetStateAndStartPlay();
					return;
				}
			}
			shutdown();
		}
	}

	private void resetStateAndStartPlay() {
		gameLimiter = new GameLimitingBotListener(this, nbGamesPerConfrontation);
		speedMonitor = new SpeedTestBotListener(reportInterval, this);
		playOnNewtable();
	}

	private void playOnNewtable() {
		try {
			String tableName = bots[botIndex[0]].toString();
			for (int i = 1; i < nbPlayersPerGame; i++) {
				tableName += " vs " + bots[botIndex[i]].toString();
			}
			TableId tableId = directorLobby
			.createHoldemTable(tableName, config).getId();

			bot[0] = bots[botIndex[0]].createBot(botIDs[botIndex[0]], tableId,
					botLobbies[botIndex[0]], 20000, executor,
					new ReSitInBotListener(this), speedMonitor, gameLimiter);
			bot[0].start();
			for (int i = 1; i < nbPlayersPerGame; i++) {
				bot[i] = bots[botIndex[i]].createBot(botIDs[botIndex[i]],
						tableId, botLobbies[botIndex[i]], 5000, executor);
				bot[i].start();
			}
			bot[0].startGame();
		} catch (RemoteException e) {
			logger.error(e);
			throw new IllegalStateException("Server setup failed.", e);
		} catch (IllegalActionException e) {
			logger.error(e);
			throw new IllegalStateException("Server setup failed.", e);
		}
	}

	private void shutdown() {
		executor.shutdownNow();
		GlobalThreadPool.getInstance().shutdownNow();
	}

	public void onTableCreated(TableCreatedEvent tableCreatedEvent) {
		logger
		.debug(tableCreatedEvent.getTable().getName()
				+ " table created.");
	}

	public void onTableRemoved(TableRemovedEvent tableRemovedEvent) {
		logger.debug("Table " + tableRemovedEvent.getTableId() + " removed.");
	}

	public void respawnBots() {
		stopRunningBots();
		playOnNewtable();
	}

	private void stopRunningBots() {
		for (int i = 0; i < nbPlayersPerGame; i++) {
			int profit = bot[i].getProfit();
			botProfit[i] += profit;
			logger.debug(bot[i] + " gains " + profit);
			bot[i].stop();
		}
	}

	public void moveToNextCombination() {
		stopRunningBots();
		for (int i = 0; i < nbPlayersPerGame; i++) {
			logger.info(bots[botIndex[i]].toString() + " averages "
					+ botProfit[i] * 1.0 / config.getBigBlind()
					/ nbGamesPerConfrontation + " bb/game");
			botProfit[i] = 0;
		}
		iterateBots();
	}

	public BotFactory getBotFactory(int i) {
		return bots[botIndex[i]];
	}

	public double getBotProfit(int i) {
		return (botProfit[i] + bot[i].getProfit()) * 1.0 / config.getBigBlind();
	}

}
