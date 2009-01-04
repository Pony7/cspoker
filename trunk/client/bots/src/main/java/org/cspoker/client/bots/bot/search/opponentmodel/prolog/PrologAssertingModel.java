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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.action.ActionWrapper;
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
	
	private final static Logger logger = Logger.getLogger(PrologAssertingModel.class);

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
	public OpponentModel getModelFor(final PlayerId playerId, GameState gameState) {
		return new OpponentModel(){

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
				AssertRetractVisitor visitor = new AssertRetractVisitor(assertingVisitor);
				visitor.readHistory(gameState);
				String goals = "", vars = "[", resultVar;
				
				List<ActionWrapper> deterministicActions = new ArrayList<ActionWrapper>();
				if(gameState.hasBet()){
					//call, raise or fold
					deterministicActions.add(new CallAction(gameState, playerId));
					resultVar = "PCall";
					goals+=buildGoal("call", resultVar, visitor);
					vars+="string("+resultVar+")";
					
					deterministicActions.add(new FoldAction(gameState, playerId));
					resultVar = "PFold";
					goals+=", "+buildGoal("fold", resultVar, visitor);
					vars+=", string("+resultVar+")";
					
					if(!gameState.getPlayer(botId).isAllIn() && gameState.isAllowedToRaise(playerId)){
						int lowerRaiseBound = gameState.getLowerRaiseBound(playerId);
						int upperRaiseBound = gameState.getUpperRaiseBound(playerId);
						
						deterministicActions.add(new RaiseAction(gameState, playerId, lowerRaiseBound));
						resultVar = "PRaise1";
						//raise is no target, bet is!
						//TODO fix in prolog
						goals+=", "+buildGoal("bet", resultVar, visitor);
						vars+=", string("+resultVar+")";
						
						if(upperRaiseBound>lowerRaiseBound){
							deterministicActions.add(new RaiseAction(gameState, playerId, Math.min(5*lowerRaiseBound, upperRaiseBound)));
							resultVar = "PRaise2";
							goals+=", "+buildGoal("bet", resultVar, visitor);
							vars+=", string("+resultVar+")";
						}
					}
				}else{
					//check or bet
					deterministicActions.add(new CheckAction(gameState, playerId));
					resultVar = "PCheck";
					goals+=buildGoal("check", resultVar, visitor);
					vars+="string("+resultVar+")";
					
					if(!gameState.getPlayer(botId).isAllIn() && gameState.isAllowedToRaise(playerId)){
						int lowerRaiseBound = gameState.getLowerRaiseBound(playerId);
						int upperRaiseBound = gameState.getUpperRaiseBound(playerId);

						deterministicActions.add(new BetAction(gameState, playerId, lowerRaiseBound));
						resultVar = "PBet1";
						goals+=", "+buildGoal("bet", resultVar, visitor);
						vars+=", string("+resultVar+")";
						
						if(upperRaiseBound>lowerRaiseBound){
							deterministicActions.add(new BetAction(gameState, playerId, Math.min(5*lowerRaiseBound, upperRaiseBound)));
							resultVar = "PBet2";
							goals+=", "+buildGoal("bet", resultVar, visitor);
							vars+=", string("+resultVar+")";
						}
					}
				}
				vars+="]";
				
				//goals = "PCall=0.5, PFold=0.1, PRaise1=0.1, PRaise2=0.1, PCheck=0.5, PBet1=0.1, PBet2=0.1";
				goals = visitor.wrapGoal(goals);
				logger.trace("Executing Prolog Goal: "+goals+" with vars "+vars);
				Object[] results = prologEngine.deterministicGoal(goals, vars);
				
				HashSet<ProbabilityAction> actions = new LinkedHashSet<ProbabilityAction>();
				double totalProbability = 0;
				for(int i=0;i<deterministicActions.size();i++){
					double prob = Double.parseDouble((String)results[i]);
					actions.add(new ProbabilityAction(deterministicActions.get(i), prob));
					totalProbability += prob;
				}
				
				HashSet<ProbabilityAction> normalizedActions = new HashSet<ProbabilityAction>();
				for(ProbabilityAction action:actions){
					normalizedActions.add(new ProbabilityAction(action.getActionWrapper(), action.getProbability()/totalProbability));
				}	
				return normalizedActions;
			}
			
			private String buildGoal(String action, String resultVar,
					AssertRetractVisitor visitor) {
				return "prior_action_probability("+visitor.getGameId()+", "+(visitor.getActionId()+1)
						+", player_"+playerId.getId()+", "+action+", "+visitor.getRound()+", "+resultVar+")";
			}
			
		};
	}
}
