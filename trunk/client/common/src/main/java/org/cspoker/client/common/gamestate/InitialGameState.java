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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

public class InitialGameState extends AbstractGameState {
	
	private final TableConfiguration tableConfiguration;
	private final PlayerId dealer;
	private PlayerId[] players;
	private int[] stacks;
	private final boolean[] isPlaying;
	
	public InitialGameState(TableConfiguration tableConfiguration, PlayerId dealer, Collection<SeatedPlayer> players) {
		this.tableConfiguration = tableConfiguration;
		this.dealer = dealer;
		HashMap<PlayerId, SeatId> seatsBuilder = new HashMap<PlayerId, SeatId>(tableConfiguration.getMaxNbPlayers());
		this.players = new PlayerId[tableConfiguration.getMaxNbPlayers()];
		this.isPlaying = new boolean[tableConfiguration.getMaxNbPlayers()];
		for (SeatedPlayer seatedPlayer : players) {
			isPlaying[seatedPlayer.getSeatId().getId()] = true;
			this.players[seatedPlayer.getSeatId().getId()] = seatedPlayer.getId();
			this.stacks[seatedPlayer.getSeatId().getId()] = seatedPlayer.getStackValue();
			seatsBuilder.put(seatedPlayer.getId(), seatedPlayer.getSeatId());
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

	public int getPotSize() {
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

	public TableConfiguration getTableConfiguration() {
		return tableConfiguration;
	}

	public PlayerId getDealer() {
		return dealer;
	}
	
	public boolean isPlaying(PlayerId playerId) {
		return isPlaying[getSeatId(playerId).getId()];
	}
	
	public HoldemTableEvent getLastEvent() {
		return null;
	}
	
	public GameState getPreviousGameState() {
		return null;
	}
}
