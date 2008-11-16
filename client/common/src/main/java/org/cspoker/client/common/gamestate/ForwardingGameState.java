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
package org.cspoker.client.common.gamestate;

import java.util.Set;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;

public abstract class ForwardingGameState extends AbstractGameState{

	private final GameState gameState;

	public ForwardingGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	public int getBetSize(PlayerId playerId) {
		return gameState.getBetSize(playerId);
	}

	public int getLargestBet() {
		return gameState.getLargestBet();
	}

	public int getMinNextRaise() {
		return gameState.getMinNextRaise();
	}

	public PlayerId getPlayerId(SeatId seatId) {
		return gameState.getPlayerId(seatId);
	}

	public int getPreviousRoundsPotSize() {
		return gameState.getPreviousRoundsPotSize();
	}

	public int getRoundPotSize() {
		return gameState.getRoundPotSize();
	}

	public SeatId getSeatId(PlayerId playerId) {
		return gameState.getSeatId(playerId);
	}

	public int getStack(PlayerId playerId) {
		return gameState.getStack(playerId);
	}
	
	public PlayerId getNextToAct() {
		return gameState.getNextToAct();
	}

	public Round getRound() {
		return gameState.getRound();
	}

	public Set<Card> getCards(PlayerId playerId) {
		return gameState.getCards(playerId);
	}

	public Set<Card> getCommunityCards() {
		return gameState.getCommunityCards();
	}

	public PlayerId getDealer() {
		return gameState.getDealer();
	}

	public boolean hasFolded(PlayerId playerId) {
		return gameState.hasFolded(playerId);
	}
	
	public GameState getPreviousGameState() {
		return gameState;
	}
	
	public PlayerId getLastBettor() {
		return gameState.getLastBettor();
	}
	
	public boolean sitsIn(PlayerId playerId) {
		return gameState.sitsIn(playerId);
	}

}
