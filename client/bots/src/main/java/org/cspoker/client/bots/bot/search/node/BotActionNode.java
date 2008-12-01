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

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.action.BetAction;
import org.cspoker.client.bots.bot.search.action.CallAction;
import org.cspoker.client.bots.bot.search.action.CheckAction;
import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.action.FoldAction;
import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.action.RaiseAction;
import org.cspoker.client.bots.bot.search.action.SearchBotAction;
import org.cspoker.client.bots.bot.search.node.expander.CompleteExpander;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;

public class BotActionNode extends ActionNode{

	private final static Logger logger = Logger.getLogger(BotActionNode.class);

	protected final CompleteExpander expander;
	
	public BotActionNode(PlayerId botId, GameState gameState,
			AllPlayersModel opponentModeler, String prefix) {
		super(botId, botId, gameState, opponentModeler, prefix);
		this.expander = new CompleteExpander(this);
	};

	public BotActionNode(PlayerId botId, GameState gameState,
			AllPlayersModel opponentModeler, String prefix, CompleteExpander expander) {
		super(botId, botId, gameState, opponentModeler, prefix);
		this.expander = expander;
	}
	
	@Override
	public double getEV() {
		return getBestEvaluatedAction().getEV();
	}


	public void performbestAction(RemoteHoldemPlayerContext context) throws RemoteException,
	IllegalActionException {
		getBestEvaluatedAction().getAction().perform(context);
	}
	
	protected EvaluatedAction<? extends SearchBotAction> getBestEvaluatedAction(){
		double maxEv=Double.NEGATIVE_INFINITY;
		EvaluatedAction<? extends SearchBotAction> action = null;
		Set<? extends EvaluatedAction<? extends SearchBotAction>> actions = getExpander().expand();
		for(EvaluatedAction<? extends SearchBotAction> eval : actions){
			if(eval.getEV()>maxEv){
				maxEv = eval.getEV();
				action = eval;
			}
		}
		return action;
	}
	
	@Override
	public Set<SearchBotAction> getAllPossibleActions() {
		HashSet<SearchBotAction> actions = new HashSet<SearchBotAction>();
		if(gameState.hasBet()){
			actions.add(new CallAction(gameState, botId));
			actions.add(new FoldAction(gameState, botId));
			if(gameState.isAllowedToRaise(botId)){
				int lowerRaiseBound = gameState.getLowerRaiseBound(botId);
				int upperRaiseBound = gameState.getUpperRaiseBound(botId);
				actions.add(new RaiseAction(gameState, botId, lowerRaiseBound));
				if(upperRaiseBound>lowerRaiseBound){
					actions.add(new RaiseAction(gameState, botId, Math.min(5*lowerRaiseBound, upperRaiseBound)));
				}
			}
		}else{
			actions.add(new CheckAction(gameState, botId));
			if(gameState.isAllowedToRaise(botId)){
				int lowerRaiseBound = gameState.getLowerRaiseBound(botId);
				int upperRaiseBound = gameState.getUpperRaiseBound(botId);
				actions.add(new BetAction(gameState, botId, lowerRaiseBound));
				if(upperRaiseBound>lowerRaiseBound){
					actions.add(new BetAction(gameState, botId, Math.min(5*lowerRaiseBound, upperRaiseBound)));
				}
			}
		}
		return actions;
	}
	
	@Override
	public Set<ProbabilityAction> getProbabilityActions() {
		Set<SearchBotAction> possibleActions = getAllPossibleActions();
		double probability = 1.0/possibleActions.size();
		HashSet<ProbabilityAction> probActions = new HashSet<ProbabilityAction>();
		for(SearchBotAction action:possibleActions){
			probActions.add(new ProbabilityAction(action,probability));
		}
		return probActions;
	}
	
	protected <A extends ActionWrapper> EvaluatedAction<A> getFoldEVForBot(A action, GameState nextState) {
		EvaluatedAction<A> result;
		//fold action
		int stack = nextState.getPlayer(botId).getStack();
		result = new EvaluatedAction<A>(action, stack);
		return result;
	}
	
	protected CompleteExpander getExpander() {
		return expander;
	}
	
	@Override
	public String toString() {
		return "Bot Action Node";
	}

}
