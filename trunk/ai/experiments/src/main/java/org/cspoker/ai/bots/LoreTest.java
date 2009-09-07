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
package org.cspoker.ai.bots;

import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.ai.bots.bot.Bot;
import org.cspoker.ai.bots.bot.BotFactory;
import org.cspoker.ai.bots.bot.gametree.mcts.MCTSBotFactory;
import org.cspoker.ai.bots.bot.gametree.mcts.listeners.SWTTreeListener;
import org.cspoker.ai.bots.bot.gametree.mcts.nodes.MCTSBucketShowdownNode;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.backpropagation.MaxDistributionPlusBackPropStrategy;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.backpropagation.MixedBackPropStrategy;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.backpropagation.SampleWeightedBackPropStrategy;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.MaxValueSelector;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.SamplingToFunctionSelector;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.UCTPlusPlusSelector;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.UCTSelector;
import org.cspoker.ai.bots.listener.DefaultBotListener;
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
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.common.util.threading.SingleThreadRequestExecutor;
import org.eclipse.swt.widgets.Display;

public class LoreTest {

	static {
		Log4JPropertiesLoader
		.load("org/cspoker/ai/experiments/logging/log4j.properties");
	}

	private final static Logger logger = Logger.getLogger(LoreTest.class);

	public static void main(String[] args) throws Exception {
		(new LoreTest(new PokersourceServer("http://pokersource.eu/POKER_REST"))).start();
	}

	private final RemoteCSPokerServer server;
	private final BotFactory botFactory;
	private Bot bot;
	private Display display;

	public LoreTest(RemoteCSPokerServer server) throws Exception {
		this.server = server;
		display = new Display();
		this.botFactory = 
			//			 new CallBotFactory();
			new MCTSBotFactory(
					"Plus Bot",
					WekaRegressionModelFactory.createForZip("org/cspoker/ai/opponentmodels/weka/models/model1.zip"),
					new SamplingToFunctionSelector(50,new UCTSelector(1000000)),
					new SamplingToFunctionSelector(50,new UCTPlusPlusSelector()),
					new MaxValueSelector(),
					new MCTSBucketShowdownNode.Factory(),
					new MixedBackPropStrategy.Factory(
							50,
							new SampleWeightedBackPropStrategy.Factory(),
							new MaxDistributionPlusBackPropStrategy.Factory()
					),
					1500,
					new SWTTreeListener.Factory(display));
	}

	private void start() throws RemoteException, LoginException, IllegalActionException {
		RemoteServerContext conn = server.login("foobar", "foobar");
		SmartClientContext clientContext = new SmartClientContext(conn);
		final SmartLobbyContext lobbyContext = clientContext.getLobbyContext(new DefaultLobbyListener());
		final PlayerId botId = clientContext.getAccountContext().getPlayerID();
		final SingleThreadRequestExecutor executor = SingleThreadRequestExecutor.getInstance();
		bot = botFactory.createBot(botId, new TableId(0), lobbyContext, 1, executor, new DefaultBotListener() {
			@Override
			public void onSitOut(SitOutEvent sitOutEvent) {
				if(botId.equals(sitOutEvent.getPlayerId())){
					logger.info("Sitting in again after "+sitOutEvent);
					bot.reSitIn();
				}
			}
		});
		bot.start();
		
		while (!display.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}



}
