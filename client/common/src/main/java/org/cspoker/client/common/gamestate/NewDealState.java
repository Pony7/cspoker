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
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

public class NewDealState extends AbstractGameState {
	
	private final TableConfiguration tableConfiguration;
	private final PlayerId[] players;
	private int[] stacks;
	private final boolean[] sitsIn;
	private final NewDealEvent event;
	
	public NewDealState(TableConfiguration tableConfiguration, NewDealEvent newDealEvent) {
		this.event = newDealEvent;
		this.tableConfiguration = tableConfiguration;
		
		this.players = new PlayerId[tableConfiguration.getMaxNbPlayers()];
		this.sitsIn = new boolean[tableConfiguration.getMaxNbPlayers()];
		this.stacks = new int[tableConfiguration.getMaxNbPlayers()];
		
		for (SeatedPlayer seatedPlayer : newDealEvent.getPlayers()) {
			this.sitsIn[seatedPlayer.getSeatId().getId()] = seatedPlayer.isSittingIn();
			this.players[seatedPlayer.getSeatId().getId()] = seatedPlayer.getId();
			this.stacks[seatedPlayer.getSeatId().getId()] = seatedPlayer.getStackValue();
		}
	}

	public int getBetSize(PlayerId playerId) {
		return 0;
	}

	public Set<Card> getCards(PlayerId playerId) {
		return Collections.emptySet();
	}

	public Set<Card> getCommunityCards() {
		return Collections.emptySet();
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

	public int getRoundPotSize() {
		return 0;
	}
	
	public int getPreviousRoundsPotSize() {
		return 0;
	}

	public Round getRound() {
		return Round.PREFLOP;
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
		return event.getDealer();
	}
	
	public boolean hasFolded(PlayerId playerId) {
		return false;
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
