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
package org.cspoker.ai.lore;

import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.ai.bots.bot.Bot;
import org.cspoker.ai.bots.bot.BotFactory;
import org.cspoker.ai.bots.bot.gametree.mcts.MCTSBotFactory;
import org.cspoker.ai.bots.bot.gametree.mcts.listeners.MCTSListener.Factory;
import org.cspoker.ai.bots.bot.gametree.mcts.nodes.MCTSBucketShowdownNode;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.backpropagation.SampleWeightedBackPropStrategy;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.MaxValueSelector;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.SamplingSelector;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.SamplingToFunctionSelector;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.UCTSelector;
import org.cspoker.ai.bots.listener.DefaultBotListener;
import org.cspoker.ai.bots.listener.ProfitInfo;
import org.cspoker.ai.bots.listener.ProfitListener;
import org.cspoker.ai.bots.util.RunningStats;
import org.cspoker.ai.opponentmodels.weka.WekaRegressionModelFactory;
import org.cspoker.client.common.SmartClientContext;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.client.pokersource.PokersourceServer;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.listener.DefaultLobbyListener;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.common.util.threading.SingleThreadRequestExecutor;

public class Lore {

	static {
		Log4JPropertiesLoader
		.load("org/cspoker/ai/lore/logging/log4j.properties");
	}

	private final static Logger logger = Logger.getLogger(Lore.class);

	public static void main(String[] args) throws Exception {
		(new Lore(new PokersourceServer("http://pokersource.eu/POKER_REST"))).start();
	}

	private final RemoteCSPokerServer server;
	private final BotFactory botFactory;
	private Bot bot;

	public Lore(RemoteCSPokerServer server) throws Exception {
		this.server = server;
		this.botFactory = 
			//			 new CallBotFactory();
			new MCTSBotFactory(
					"CSPoker Bot",
					WekaRegressionModelFactory.createForZip("org/cspoker/ai/opponentmodels/weka/models/model1.zip"),
					new SamplingToFunctionSelector(50,new UCTSelector(2000000)),
					new SamplingSelector(),
					new MaxValueSelector(),
					new MCTSBucketShowdownNode.Factory(),
					new SampleWeightedBackPropStrategy.Factory(),
					1500,getListeners());
	}

	protected Factory[] getListeners() {
		return new Factory[]{};
	}

	protected void start() throws RemoteException, LoginException, IllegalActionException {
		RemoteServerContext conn = server.login("foobar", "foobar");
		SmartClientContext clientContext = new SmartClientContext(conn);
		final SmartLobbyContext lobbyContext = clientContext.getLobbyContext(new DefaultLobbyListener());
		final PlayerId botId = clientContext.getAccountContext().getPlayerID();
		final SingleThreadRequestExecutor executor = SingleThreadRequestExecutor.getInstance();
		final TableId tableId = new TableId(0);
		bot = botFactory.createBot(botId, tableId, lobbyContext, 200000, executor, new DefaultBotListener() {
			@Override
			public void onSitOut(SitOutEvent sitOutEvent) {
				if(botId.equals(sitOutEvent.getPlayerId())){
					logger.info("Sitting in again after "+sitOutEvent);
					bot.reSitIn();
				}
			}
		}, new ProfitListener(1,new ProfitInfo(){

			@Override
			public RunningStats getProfit() {
				return bot.getProfit();
			}
			
			@Override
			public String getBotName() {
				return botFactory.toString();
			}
			
			@Override
			public TableConfiguration getTableConfiguration() {
				try {
					return lobbyContext.getHoldemTableInformation(tableId).getTableConfiguration();
				} catch (RemoteException e) {
					e.printStackTrace();
					logger.error(e);
					throw new RuntimeException(e);
				} catch (IllegalActionException e) {
					e.printStackTrace();
					logger.error(e);
					throw new RuntimeException(e);
				}
			}
			
		}));
		bot.start();
	}



}
