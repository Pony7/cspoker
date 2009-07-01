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
package org.cspoker.client.bots.bot.gametree.mcts.nodes;

import net.jcip.annotations.Immutable;

import org.cspoker.client.bots.bot.gametree.mcts.nodes.ShowdownNode.Factory;
import org.cspoker.client.bots.bot.gametree.mcts.strategies.backpropagation.BackPropagationStrategy;
import org.cspoker.client.bots.bot.gametree.mcts.strategies.selection.SelectionStrategy;
import org.cspoker.client.bots.bot.gametree.opponentmodel.OpponentModel;

@Immutable
public class Config {

	private final Factory showdownNodeFactory;
	private final OpponentModel model;
	private final SelectionStrategy decisionNodeSelectionStrategy;
	private final SelectionStrategy opponentNodeSelectionStrategy;
	private final SelectionStrategy moveSelectionStrategy;
	private final BackPropagationStrategy.Factory backPropStratFactory;

	public Config(OpponentModel model, 
			ShowdownNode.Factory showdownNodeFactory, 
			SelectionStrategy decisionNodeSelectionStrategy, 
			SelectionStrategy opponentNodeSelectionStrategy, 
			SelectionStrategy moveSelectionStrategy, 
			BackPropagationStrategy.Factory backPropStratFactory
			) {
		this.model = model;
		this.showdownNodeFactory = showdownNodeFactory;
		this.decisionNodeSelectionStrategy=decisionNodeSelectionStrategy;
		this.opponentNodeSelectionStrategy = opponentNodeSelectionStrategy;
		this.moveSelectionStrategy=moveSelectionStrategy;
		this.backPropStratFactory = backPropStratFactory;
	}
	
	public OpponentModel getModel() {
		return model;
	}
	
	public Factory getShowdownNodeFactory() {
		return showdownNodeFactory;
	}
	
	public SelectionStrategy getMoveSelectionStrategy() {
		return moveSelectionStrategy;
	}
	
	public SelectionStrategy getDecisionNodeSelectionStrategy() {
		return decisionNodeSelectionStrategy;
	}
	
	public BackPropagationStrategy.Factory getBackPropStratFactory() {
		return backPropStratFactory;
	}

	public SelectionStrategy getOpponentNodeSelectionStrategy() {
		return opponentNodeSelectionStrategy;
	}
	
}
