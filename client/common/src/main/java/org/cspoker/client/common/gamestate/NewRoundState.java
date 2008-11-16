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

import java.util.Collections;
import java.util.Set;

import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

public class NewRoundState extends AbstractGameState {

	private final TableConfiguration tableConfiguration;
	private final PlayerId dealer;
	private final PlayerId[] players;
	private int[] stacks;
	private final boolean[] sitsIn;
	private final boolean[] hasFolded;
	private final NewRoundEvent event;
	private final Set<Card> communityCards;
	private final Set<Card>[] holeCards;

	public NewRoundState(TableConfiguration tableConfiguration, GameState state, NewRoundEvent event) {
		this.tableConfiguration = tableConfiguration;
		this.event = event;
		this.dealer = state.getDealer();
		this.communityCards = state.getCommunityCards();
		
		this.players = new PlayerId[tableConfiguration.getMaxNbPlayers()];
		this.sitsIn = new boolean[tableConfiguration.getMaxNbPlayers()];
		this.hasFolded = new boolean[tableConfiguration.getMaxNbPlayers()];
		this.holeCards = new Set[tableConfiguration.getMaxNbPlayers()];
		this.stacks = new int[tableConfiguration.getMaxNbPlayers()];
		
		for(int seat=0;seat<tableConfiguration.getMaxNbPlayers();seat++){
			PlayerId playerId = state.getPlayerId(new SeatId(seat));
			if(playerId != null){
				this.sitsIn[seat] = state.sitsIn(playerId);
				this.players[seat] = playerId;
				this.stacks[seat] = state.getStack(playerId);
				holeCards[seat] = state.getCards(playerId);
				hasFolded[seat] = state.hasFolded(playerId);
			}else{
				holeCards[seat] = Collections.emptySet();
			}
		}
	}

	public int getBetSize(PlayerId playerId) {
		return 0;
	}

	public Set<Card> getCards(PlayerId playerId) {
		return holeCards[getSeatId(playerId).getId()];
	}

	public Set<Card> getCommunityCards() {
		return communityCards;
	}

	public int getLargestBet() {
		return 0;
	}

	public int getMinNextRaise() {
		return tableConfiguration.getBigBlind();
	}

	public PlayerId getNextToAct() {
		return null;
	}

	public PlayerId getPlayerId(SeatId seatId) {
		return players[seatId.getId()];
	}

	
	public int getPreviousRoundsPotSize() {
		return event.getPots().getTotalValue();
	}
	
	public int getRoundPotSize() {
		return 0;
	}

	public Round getRound() {
		return event.getRound();
	}

	public SeatId getSeatId(PlayerId playerId) {
		for (int i = 0; i < players.length; i++) {
			if(playerId.equals(players[i])){
				return new SeatId(i);
			}
		}
		return null;
	}

	public int getStack(PlayerId playerId) {
		return stacks[getSeatId(playerId).getId()];
	}

	public PlayerId getDealer() {
		return dealer;
	}

	public boolean hasFolded(PlayerId playerId) {
		return hasFolded[getSeatId(playerId).getId()];
	}

	public HoldemTableEvent getLastEvent() {
		return event;
	}

	public GameState getPreviousGameState() {
		return null;
	}

	public PlayerId getLastBettor() {
		return null;
	}

	public boolean sitsIn(PlayerId playerId) {
		return sitsIn[getSeatId(playerId).getId()];
	}
}
