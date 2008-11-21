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
import org.cspoker.client.bots.bot.search.action.BetAction;
import org.cspoker.client.bots.bot.search.action.CallAction;
import org.cspoker.client.bots.bot.search.action.CheckAction;
import org.cspoker.client.bots.bot.search.action.FoldAction;
import org.cspoker.client.bots.bot.search.action.RaiseAction;
import org.cspoker.client.bots.bot.search.action.SimulationAction;
import org.cspoker.client.common.gamestate.GameState;

public class FinalRoundModel implements OpponentModel{

	public double getProbabilityOf(SimulationAction action, GameState gameState){
		return action.calculateProbabilityIn(this, gameState);
	}

	@Override
	public double getBetProbability(BetAction betAction, GameState gameState) {
		return 0.2;
	}

	@Override
	public double getCallProbability(CallAction callAction, GameState gameState) {
		return 0.5;
	}

	@Override
	public double getCheckProbability(CheckAction checkAction,
			GameState gameState) {
		return 0.5;
	}

	@Override
	public double getFoldProbability(FoldAction foldAction, GameState gameState) {
		return 0.1;
	}

	@Override
	public double getRaiseProbability(RaiseAction raiseAction,
			GameState gameState) {
		int nbRaises = gameState.getNbRaises();
		return 0.2*3.0/(2+nbRaises);
	}
	
}
