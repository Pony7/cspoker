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

import org.cspoker.client.bots.bot.search.OpponentModel;
import org.cspoker.client.bots.bot.search.action.SimulationAction;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.modifiers.AllInState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;

public class FinalRoundModel implements OpponentModel{

	public final static int weightOfPrior=200;
	
	//prior derived from data set
	
	private int nbCheck = 2303*weightOfPrior/3819;
	private int nbBet = 1516*weightOfPrior/3819;
	private int totalNoBet = weightOfPrior;
	
	private int nbFold = 641*weightOfPrior/1217;
	private int nbCall = 408*weightOfPrior/1217;
	private int nbRaise = 168*weightOfPrior/1217;
	private int totalBet = weightOfPrior;

	public double getProbabilityOf(SimulationAction action, GameState gameState){
		return action.calculateProbabilityIn(this, gameState);
	}

	@Override
	public double getCheckProbability(GameState gameState) {
		return nbCheck*1.0/totalNoBet;
	}

	@Override
	public double getBetProbability(GameState gameState) {
		return nbBet*1.0/totalNoBet;
	}

	@Override
	public double getCallProbability(GameState gameState) {
		return nbCall*1.0/totalBet;
	}

	@Override
	public double getFoldProbability(GameState gameState) {
		return nbFold*1.0/totalBet;
	}

	@Override
	public double getRaiseProbability(GameState gameState) {
		return nbRaise*1.0/totalBet;
	}

	@Override
	public void addAllIn(GameState gameState, AllInEvent allInEvent) {
		AllInState newState = new AllInState(gameState, allInEvent);
		if(gameState.hasBet()){
			if(newState.getRaise()>0){
				addRaise(newState.getRaise());
			}else{
				addCall();
			}
		}else{
			addBet(allInEvent.getAmount());
		}
	}

	public void addCheck() {
		++nbCheck;
		++totalNoBet;
	}
	
	public void addBet(int amount) {
		++nbBet;
		++totalNoBet;
	}

	public void addCall() {
		++nbCall;
		++totalBet;
	}

	public void addRaise(int raise) {
		++nbRaise;
		++totalBet;
	}

	public void addFold() {
		++nbFold;
		++totalBet;
	}
	
	
	
}
