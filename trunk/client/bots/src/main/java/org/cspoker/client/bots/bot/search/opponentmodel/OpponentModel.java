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

import java.util.Set;

import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.action.SearchBotAction;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.elements.player.PlayerId;

public interface OpponentModel {
	
	void addAllIn(GameState gameState, AllInEvent allInEvent);
	
	void addCheck(GameState gameState, CheckEvent checkEvent);
	
	void addBet(GameState gameState, BetEvent betEvent);

	void addCall(GameState gameState, CallEvent callEvent);

	void addRaise(GameState gameState, RaiseEvent raiseEvent);

	void addFold(GameState gameState, FoldEvent foldEvent);

	Set<SearchBotAction> getAllPossibleActions(GameState gameState);
	
	Set<ProbabilityAction> getProbabilityActions(GameState gameState);

	public static interface Factory{
		
		OpponentModel create(PlayerId opponentId);
		
	}
	
}
