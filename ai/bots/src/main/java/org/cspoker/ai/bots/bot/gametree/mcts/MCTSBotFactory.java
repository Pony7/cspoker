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
package org.cspoker.ai.bots.bot.gametree.mcts;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.cspoker.ai.bots.bot.Bot;
import org.cspoker.ai.bots.bot.BotFactory;
import org.cspoker.ai.bots.bot.gametree.mcts.listeners.MCTSListener;
import org.cspoker.ai.bots.bot.gametree.mcts.nodes.Config;
import org.cspoker.ai.bots.bot.gametree.mcts.nodes.ShowdownNode;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.backpropagation.BackPropagationStrategy;
import org.cspoker.ai.bots.bot.gametree.mcts.strategies.selection.SelectionStrategy;
import org.cspoker.ai.bots.bot.gametree.search.expander.sampling.Sampler;
import org.cspoker.ai.bots.listener.BotListener;
import org.cspoker.ai.opponentmodels.OpponentModel;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.handeval.spears2p2.StateTableEvaluator;

public class MCTSBotFactory implements BotFactory {

	private final ConcurrentHashMap<PlayerId, OpponentModel> opponentModels = new ConcurrentHashMap<PlayerId, OpponentModel>();
	private final MCTSListener.Factory[] listeners;
	private final OpponentModel.Factory opponentModelFactory;
	private final SelectionStrategy decisionNodeSelectionStrategy;
	private final SelectionStrategy opponentNodeSelectionStrategy;
	private final SelectionStrategy moveSelectionStrategy;
	private final ShowdownNode.Factory showdownNodeFactory;
	private final Sampler sampler;
	private final int decisionTime;
	private final String name;
	private final BackPropagationStrategy.Factory backPropStratFactory;

	public MCTSBotFactory(
			String name,
			OpponentModel.Factory opponentModelFactory, 
			SelectionStrategy decisionNodeSelectionStrategy,
			SelectionStrategy opponentNodeSelectionStrategy,
			SelectionStrategy moveSelectionStrategy,
			ShowdownNode.Factory showdownNodeFactory,
			BackPropagationStrategy.Factory backPropStratFactory,
			Sampler sampler,
			int decisionTime,
			MCTSListener.Factory... listeners) {
		this.name = name;
		this.listeners = listeners;
		this.opponentModelFactory = opponentModelFactory;
		this.decisionNodeSelectionStrategy=decisionNodeSelectionStrategy;
		this.opponentNodeSelectionStrategy=opponentNodeSelectionStrategy;
		this.moveSelectionStrategy = moveSelectionStrategy;
		this.showdownNodeFactory = showdownNodeFactory;
		this.backPropStratFactory = backPropStratFactory;
		this.sampler = sampler;
		this.decisionTime = decisionTime;
		StateTableEvaluator.getInstance();
	}

	public Bot createBot(final PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, int buyIn, ExecutorService executor,
			BotListener... botListeners) {
		OpponentModel opponentModel = opponentModels.get(botId);
		if(opponentModel==null){
			opponentModel = opponentModelFactory.create();
			opponentModels.put(botId, opponentModel);
		}
		Config config = new Config(opponentModel, showdownNodeFactory, 
				decisionNodeSelectionStrategy, opponentNodeSelectionStrategy, 
				moveSelectionStrategy, backPropStratFactory, sampler);
		return new MCTSBot(botId, tableId, lobby, executor, buyIn,
				config,
				decisionTime,
				listeners,
				botListeners);
	}

	@Override
	public String toString() {
		return name;
	}
}
