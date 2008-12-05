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
package org.cspoker.client.bots.bot.search.node;

import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.action.SampledAction;
import org.cspoker.client.bots.bot.search.action.SearchBotAction;
import org.cspoker.client.bots.bot.search.node.expander.SamplingExpander;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public class OpponentActionNode extends ActionNode{

	private final static Logger logger = Logger.getLogger(OpponentActionNode.class);
	
	private final SamplingExpander expander;
	
	public OpponentActionNode(PlayerId opponentId, PlayerId botId, GameState gameState, AllPlayersModel playersModel, int tokens, NodeVisitor... visitors) {
		super(opponentId, botId, gameState, playersModel, visitors);
		this.expander = new SamplingExpander(this, tokens);
	}

	
	@Override
	public double getEV() {
		int average = 0;
		Set<? extends EvaluatedAction<? extends SampledAction>> actions = getExpander().expand();
		for(EvaluatedAction<? extends SampledAction> eval : actions){
			average += eval.getEvaluatedAction().getTimes()*eval.getEV(); 
		}
		return Double.valueOf(average)/actions.iterator().next().getEvaluatedAction().getOutof();
	}
	
	@Override
	public Set<SearchBotAction> getAllPossibleActions() {
		return opponentModeler.getModelFor(playerId).getAllPossibleActions(gameState);
	}

	@Override
	public Set<ProbabilityAction> getProbabilityActions() {
		return opponentModeler.getModelFor(playerId).getProbabilityActions(gameState);
	}
	
	protected <A extends ActionWrapper> EvaluatedAction<A> getFoldEVForBot(A action, GameState nextState) {
		EvaluatedAction<A> result;
		//fold action by opponent
		int stack = nextState.getPlayer(botId).getStack();
		int pots = nextState.getGamePotSize();
		result = new EvaluatedAction<A>(action, stack+pots);
		return result;
	}
	
	public SamplingExpander getExpander() {
		return expander;
	}
	
	@Override
	public String toString() {
		return "Opponent "+playerId+" Action Node";
	}
	
}
