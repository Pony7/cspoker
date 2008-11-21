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

import java.util.EnumSet;
import java.util.Set;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

public abstract class ForwardingGameState extends AbstractGameState{

	private final GameState gameState;

	public ForwardingGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public int getLargestBet() {
		return gameState.getLargestBet();
	}

	public int getMinNextRaise() {
		return gameState.getMinNextRaise();
	}

	public int getPreviousRoundsPotSize() {
		return gameState.getPreviousRoundsPotSize();
	}

	public int getRoundPotSize() {
		return gameState.getRoundPotSize();
	}

	public Round getRound() {
		return gameState.getRound();
	}

	public EnumSet<Card> getCommunityCards() {
		return gameState.getCommunityCards();
	}

	public PlayerId getDealer() {
		return gameState.getDealer();
	}

	public GameState getPreviousGameState() {
		return gameState; //no delegation!
	}
	
	public PlayerId getLastBettor() {
		return gameState.getLastBettor();
	}
	
	public PlayerId getNextToAct() {
		return gameState.getNextToAct();
	}
	
	public PlayerState getPlayer(PlayerId playerId) {
		return gameState.getPlayer(playerId);
	}
	
	public PlayerId getPlayerId(SeatId seatId) {
		return gameState.getPlayerId(seatId);
	}
	
	public Set<PlayerId> getAllSeatedPlayerIds() {
		return gameState.getAllSeatedPlayerIds();
	}
	
	public TableConfiguration getTableConfiguration() {
		return gameState.getTableConfiguration();
	}
	
	@Override
	public int getNbRaises() {
		return gameState.getNbRaises();
	}
}
