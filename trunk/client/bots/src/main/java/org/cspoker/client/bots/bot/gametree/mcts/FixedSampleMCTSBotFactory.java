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
package org.cspoker.client.bots.bot.gametree.mcts;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.bot.gametree.mcts.listeners.MCTSListener;
import org.cspoker.client.bots.bot.gametree.mcts.nodes.Config;
import org.cspoker.client.bots.bot.gametree.mcts.nodes.ShowdownNode;
import org.cspoker.client.bots.bot.gametree.mcts.strategies.backpropagation.BackPropagationStrategy;
import org.cspoker.client.bots.bot.gametree.mcts.strategies.selection.SelectionStrategy;
import org.cspoker.client.bots.bot.gametree.opponentmodel.OpponentModel;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.handeval.spears2p2.StateTableEvaluator;

public class FixedSampleMCTSBotFactory implements BotFactory {

	private static int copies = 0;
	private final int copy;

	private final ConcurrentHashMap<PlayerId, OpponentModel> opponentModels = new ConcurrentHashMap<PlayerId, OpponentModel>();
	private final MCTSListener.Factory[] listeners;
	private final OpponentModel.Factory opponentModelFactory;
	private final SelectionStrategy decisionNodeSelectionStrategy;
	private final SelectionStrategy opponentNodeSelectionStrategy;
	private final SelectionStrategy moveSelectionStrategy;
	private final ShowdownNode.Factory showdownNodeFactory;
	private final String name;
	private final BackPropagationStrategy.Factory backPropStratFactory;
	private final int samplesPreFlop;
	private final int samplesFlop;
	private final int samplesTurn;
	private final int samplesRiver;

	public FixedSampleMCTSBotFactory(
			String name,
			OpponentModel.Factory opponentModelFactory, 
			SelectionStrategy decisionNodeSelectionStrategy,
			SelectionStrategy opponentNodeSelectionStrategy,
			SelectionStrategy moveSelectionStrategy,
			ShowdownNode.Factory showdownNodeFactory,
			BackPropagationStrategy.Factory backPropStratFactory,
			int samplesPreFlop,
			int samplesFlop,
			int samplesTurn,
			int samplesRiver,
			MCTSListener.Factory... listeners) {
		copy = ++copies;
		this.name = name;
		this.listeners = listeners;
		this.opponentModelFactory = opponentModelFactory;
		this.decisionNodeSelectionStrategy=decisionNodeSelectionStrategy;
		this.opponentNodeSelectionStrategy=opponentNodeSelectionStrategy;
		this.moveSelectionStrategy = moveSelectionStrategy;
		this.showdownNodeFactory = showdownNodeFactory;
		this.backPropStratFactory = backPropStratFactory;
		this.samplesPreFlop = samplesPreFlop;
		this.samplesFlop = samplesFlop;
		this.samplesTurn = samplesTurn;
		this.samplesRiver = samplesRiver;
		StateTableEvaluator.getInstance();
	}

	public Bot createBot(final PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, int buyIn, ExecutorService executor,
			BotListener... botListeners) {
		copies++;
		OpponentModel opponentModel = opponentModels.get(botId);
		if(opponentModel==null){
			opponentModel = opponentModelFactory.create();
			opponentModels.put(botId, opponentModel);
		}
		Config config = new Config(opponentModel, showdownNodeFactory, decisionNodeSelectionStrategy, opponentNodeSelectionStrategy, moveSelectionStrategy, backPropStratFactory);
		return new FixedSampleMCTSBot(botId, tableId, lobby, executor, buyIn,
				config,
				samplesPreFlop,
				samplesFlop,
				samplesTurn,
				samplesRiver,
				listeners,
				botListeners);
	}

	@Override
	public String toString() {
		return name;//"MCTS bot v1-" + copy;
	}
}
