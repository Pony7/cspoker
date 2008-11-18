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
package org.cspoker.client.common.gamestate.modifiers;

import java.util.Collections;
import java.util.Set;

import org.cspoker.client.common.gamestate.AbstractPlayerState;
import org.cspoker.client.common.gamestate.ForwardingGameState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.SeatId;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class SitInState extends ForwardingGameState {

	private final SitInEvent event;
	private final PlayerState playerState;
	
	public SitInState(GameState gameState, SitInEvent event) {
		super(gameState);
		this.event = event;
		playerState = new AbstractPlayerState(){

			public int getBet() {
				return 0;
			}

			public Set<Card> getCards() {
				return Collections.emptySet();
			}

			public PlayerId getPlayerId() {
				return SitInState.this.event.getPlayer().getId();
			}

			public SeatId getSeatId() {
				return SitInState.this.event.getPlayer().getSeatId();
			}

			public int getStack() {
				return SitInState.this.event.getPlayer().getStackValue();
			}

			public boolean hasFolded() {
				return false;
			}

			public boolean sitsIn() {
				return true;
			}
			
		};
	}

	@Override
	public PlayerState getPlayer(PlayerId playerId) {
		if(event.getPlayer().getId().equals(playerId)){
			return playerState;
		}
		return super.getPlayer(playerId);
	}
	
	@Override
	public Set<PlayerId> getAllSeatedPlayerIds() {
		return Sets.union(super.getAllSeatedPlayerIds(),ImmutableSet.of(event.getPlayer().getId()));
	}
	
	@Override
	public PlayerId getPlayerId(SeatId seatId) {
		if(event.getPlayer().getSeatId().equals(seatId)){
			return event.getPlayer().getId();
		}
		return super.getPlayerId(seatId);
	}

	public HoldemTableEvent getLastEvent() {
		return event;
	}

}
