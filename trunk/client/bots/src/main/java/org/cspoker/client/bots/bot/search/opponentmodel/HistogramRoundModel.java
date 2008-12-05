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
package org.cspoker.client.bots.bot.search.opponentmodel;

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
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.modifiers.AllInState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.elements.player.PlayerId;

public class HistogramRoundModel implements OpponentModel{

	public final static int weightOfPrior=200;

	private final PlayerId playerId;
	private final PlayerId botId;

	//prior derived from data set
	private volatile int nbCheck = 2303*weightOfPrior/3819;
	private volatile int nbBet = 1516*weightOfPrior/3819;
	private volatile int totalNoBet = weightOfPrior;

	private volatile int nbFold = 441*weightOfPrior/1017;
	private volatile int nbCall = 408*weightOfPrior/1017;
	private volatile int nbRaise = 168*weightOfPrior/1017;
	private volatile int totalBet = weightOfPrior;

	public HistogramRoundModel(PlayerId playerId, PlayerId botId) {
		this.playerId = playerId;
		this.botId = botId;
	}
	
	@Override
	public void addAllIn(GameState gameState, AllInEvent allInEvent) {
		AllInState newState = new AllInState(gameState, allInEvent);
		if(gameState.hasBet()){
			if(newState.getRaise()>0){
				++nbRaise;
			}else{
				++nbCall;
			}
			++totalBet;
		}else{
			++nbBet;
			++totalNoBet;
		}
	}

	public void addCheck(GameState gameState, CheckEvent checkEvent) {
		++nbCheck;
		++totalNoBet;
	}

	public void addBet(GameState gameState, BetEvent betEvent) {
		++nbBet;
		++totalNoBet;
	}

	public void addCall(GameState gameState, CallEvent callEvent) {
		++nbCall;
		++totalBet;
	}

	public void addRaise(GameState gameState, RaiseEvent raiseEvent) {
		++nbRaise;
		++totalBet;
	}

	public void addFold(GameState gameState, FoldEvent foldEvent) {
		++nbFold;
		++totalBet;
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
			double callProbability = getCallProbability(gameState);
			totalProbability+=callProbability;
			actions.add(new ProbabilityAction(new CallAction(gameState, playerId),callProbability));
			
			double foldProbability = getFoldProbability(gameState);
			totalProbability+= foldProbability;
			actions.add(new ProbabilityAction(new FoldAction(gameState, playerId),foldProbability));
			
			if(!gameState.getPlayer(botId).isAllIn() && gameState.isAllowedToRaise(playerId)){
				int lowerRaiseBound = gameState.getLowerRaiseBound(playerId);
				int upperRaiseBound = gameState.getUpperRaiseBound(playerId);
				double raiseProbability = getRaiseProbability(gameState);
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
			double checkProbability = getCheckProbability(gameState);
			totalProbability+=checkProbability;
			actions.add(new ProbabilityAction(new CheckAction(gameState, playerId),checkProbability));
			
			if(!gameState.getPlayer(botId).isAllIn() && gameState.isAllowedToRaise(playerId)){
				int lowerRaiseBound = gameState.getLowerRaiseBound(playerId);
				int upperRaiseBound = gameState.getUpperRaiseBound(playerId);
				double betProbability = getBetProbability(gameState);
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

	public double getCheckProbability(GameState gameState) {
		return nbCheck*1.0/totalNoBet;
	}

	public double getBetProbability(GameState gameState) {
		return nbBet*1.0/totalNoBet;
	}

	public double getCallProbability(GameState gameState) {
		return nbCall*1.0/totalBet;
	}

	public double getFoldProbability(GameState gameState) {
		return nbFold*1.0/totalBet*Math.pow(0.6, gameState.getNbRaises());
	}

	public double getRaiseProbability(GameState gameState) {
		return nbRaise*1.0/totalBet*Math.pow(0.9, gameState.getNbRaises());
	}

}
