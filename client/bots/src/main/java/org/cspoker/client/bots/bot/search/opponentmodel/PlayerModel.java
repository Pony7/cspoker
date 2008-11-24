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

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.util.lazy.IFactory;

public class PlayerModel implements OpponentModel{

	private final Map<Round, OpponentModel> roundModels = Collections.synchronizedMap(
			new EnumMap<Round, OpponentModel>(Round.class));
	
	public PlayerModel(IFactory<OpponentModel> factory) {
		for(Round round:Round.values()){
			roundModels.put(round, factory.create());
		}
	}
	
	public OpponentModel getOpponentModelFor(Round round){
		return roundModels.get(round);
	}

	@Override
	public void addAllIn(GameState gameState, AllInEvent allInEvent) {
		getOpponentModelFor(gameState.getRound()).addAllIn(gameState, allInEvent);
	}

	@Override
	public void addBet(GameState gameState, BetEvent betEvent) {
		getOpponentModelFor(gameState.getRound()).addBet(gameState, betEvent);
	}

	@Override
	public void addCall(GameState gameState, CallEvent callEvent) {
		getOpponentModelFor(gameState.getRound()).addCall(gameState, callEvent);
	}

	@Override
	public void addCheck(GameState gameState, CheckEvent checkEvent) {
		getOpponentModelFor(gameState.getRound()).addCheck(gameState, checkEvent);
	}

	@Override
	public void addFold(GameState gameState, FoldEvent foldEvent) {
		getOpponentModelFor(gameState.getRound()).addFold(gameState, foldEvent);
	}

	@Override
	public void addRaise(GameState gameState, RaiseEvent raiseEvent) {
		getOpponentModelFor(gameState.getRound()).addRaise(gameState, raiseEvent);
	}

	@Override
	public double getBetProbability(GameState gameState) {
		return getOpponentModelFor(gameState.getRound()).getBetProbability(gameState);
	}

	@Override
	public double getCallProbability(GameState gameState) {
		return getOpponentModelFor(gameState.getRound()).getCallProbability(gameState);
	}

	@Override
	public double getCheckProbability(GameState gameState) {
		return getOpponentModelFor(gameState.getRound()).getCheckProbability(gameState);
	}

	@Override
	public double getFoldProbability(GameState gameState) {
		return getOpponentModelFor(gameState.getRound()).getFoldProbability(gameState);
	}

	@Override
	public double getRaiseProbability(GameState gameState) {
		return getOpponentModelFor(gameState.getRound()).getRaiseProbability(gameState);
	}
	
}
