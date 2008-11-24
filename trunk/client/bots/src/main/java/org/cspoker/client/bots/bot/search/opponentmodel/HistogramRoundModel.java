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

import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.modifiers.AllInState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;

public class HistogramRoundModel implements OpponentModel{

	public final static int weightOfPrior=200;

	//prior derived from data set
	private volatile int nbCheck = 2303*weightOfPrior/3819;
	private volatile int nbBet = 1516*weightOfPrior/3819;
	private volatile int totalNoBet = weightOfPrior;

	private volatile int nbFold = 641*weightOfPrior/1217;
	private volatile int nbCall = 408*weightOfPrior/1217;
	private volatile int nbRaise = 168*weightOfPrior/1217;
	private volatile int totalBet = weightOfPrior;

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



}
