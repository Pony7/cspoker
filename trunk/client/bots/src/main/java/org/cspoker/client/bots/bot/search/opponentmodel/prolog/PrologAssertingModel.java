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
package org.cspoker.client.bots.bot.search.opponentmodel.prolog;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.cspoker.client.bots.bot.search.action.BetAction;
import org.cspoker.client.bots.bot.search.action.CallAction;
import org.cspoker.client.bots.bot.search.action.CheckAction;
import org.cspoker.client.bots.bot.search.action.FoldAction;
import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.action.RaiseAction;
import org.cspoker.client.bots.bot.search.action.SearchBotAction;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.elements.player.PlayerId;

import com.declarativa.interprolog.PrologEngine;

public class PrologAssertingModel implements AllPlayersModel {

	private final PrologEngine prologEngine;
	private final FactAssertingVisitor assertingVisitor;
	private final PlayerId botId;
	
	
	public PrologAssertingModel(PrologEngine prologEngine, PlayerId botId) {
		this.prologEngine = prologEngine;
		this.botId = botId;
		this.assertingVisitor = new FactAssertingVisitor(prologEngine);
	}

	@Override
	public void signalNextAction(GameState gameState) {
		assertingVisitor.readHistory(gameState);
	}
	
	@Override
	public OpponentModel getModelFor(final PlayerId playerId) {
		return new OpponentModel(){
			@Override
			public void addAllIn(GameState gameState, AllInEvent allInEvent) {
			}

			@Override
			public void addBet(GameState gameState, BetEvent betEvent) {
			}

			@Override
			public void addCall(GameState gameState, CallEvent callEvent) {
			}

			@Override
			public void addCheck(GameState gameState, CheckEvent checkEvent) {
			}

			@Override
			public void addFold(GameState gameState, FoldEvent foldEvent) {
			}

			@Override
			public void addRaise(GameState gameState, RaiseEvent raiseEvent) {
			}

			@Override
			public Set<SearchBotAction> getAllPossibleActions(GameState gameState) {
				HashSet<SearchBotAction> possibleActions = new LinkedHashSet<SearchBotAction>();
				Set<ProbabilityAction> probActions = getProbabilityActions(gameState);
				for(ProbabilityAction action:probActions){
					possibleActions.add(action.getAction());
				}
				return possibleActions;
			}

			@Override
			public Set<ProbabilityAction> getProbabilityActions(GameState gameState) {
				HashSet<ProbabilityAction> actions = new LinkedHashSet<ProbabilityAction>();
				double totalProbability = 0;
				if(gameState.hasBet()){
					//call, raise or fold
					double callProbability = 0.2;
					totalProbability+=callProbability;
					actions.add(new ProbabilityAction(new CallAction(gameState, playerId),callProbability));
					
					double foldProbability = 0.1;
					totalProbability+= foldProbability;
					actions.add(new ProbabilityAction(new FoldAction(gameState, playerId),foldProbability));
					
					if(!gameState.getPlayer(botId).isAllIn() && gameState.isAllowedToRaise(playerId)){
						int lowerRaiseBound = gameState.getLowerRaiseBound(playerId);
						int upperRaiseBound = gameState.getUpperRaiseBound(playerId);
						double raiseProbability = 0.05;
						totalProbability+=raiseProbability;
						actions.add(new ProbabilityAction(new RaiseAction(gameState, playerId, lowerRaiseBound),raiseProbability));
					
						if(upperRaiseBound>lowerRaiseBound){
							totalProbability+=raiseProbability;
							actions.add(new ProbabilityAction(new RaiseAction(gameState, playerId, Math.min(5*lowerRaiseBound, upperRaiseBound))
							,raiseProbability));
						}
					}
				}else{
					//check or bet
					double checkProbability = 0.5;
					totalProbability+=checkProbability;
					actions.add(new ProbabilityAction(new CheckAction(gameState, playerId),checkProbability));
					
					if(!gameState.getPlayer(botId).isAllIn() && gameState.isAllowedToRaise(playerId)){
						int lowerRaiseBound = gameState.getLowerRaiseBound(playerId);
						int upperRaiseBound = gameState.getUpperRaiseBound(playerId);
						double betProbability = 0.1;
						totalProbability+=betProbability;
						actions.add(new ProbabilityAction(new BetAction(gameState, playerId, lowerRaiseBound),betProbability));
						
						if(upperRaiseBound>lowerRaiseBound){
							totalProbability+=betProbability;
							actions.add(new ProbabilityAction(new BetAction(gameState, playerId, Math.min(5*lowerRaiseBound, upperRaiseBound))
							,betProbability));
						}
					}
				}
				HashSet<ProbabilityAction> normalizedActions = new HashSet<ProbabilityAction>();
				for(ProbabilityAction action:actions){
					normalizedActions.add(new ProbabilityAction(action.getActionWrapper(), action.getProbability()/totalProbability));
				}	
				return normalizedActions;
			}
		};
	}
}
