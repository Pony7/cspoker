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
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.SeatId;

public class SitInState extends ForwardingGameState {

	private final SitInEvent event;

	public SitInState(GameState gameState, SitInEvent event) {
		super(gameState);
		this.event = event;
	}

	@Override
	public boolean sitsIn(PlayerId playerId) {
		if(event.getPlayer().getId().equals(playerId)){
			return true;
		}
		return super.sitsIn(playerId);
	}

	@Override
	public int getBetSize(PlayerId playerId) {
		if(event.getPlayer().getId().equals(playerId)){
			return 0;
		}
		return super.getBetSize(playerId);
	}

	@Override
	public Set<Card> getCards(PlayerId playerId) {
		if(event.getPlayer().getId().equals(playerId)){
			return Collections.emptySet();
		}
		return super.getCards(playerId);
	}

	@Override
	public PlayerId getPlayerId(SeatId seatId) {
		if(event.getPlayer().getSeatId().equals(seatId)){
			return event.getPlayer().getId();
		}
		return super.getPlayerId(seatId);
	}

	@Override
	public SeatId getSeatId(PlayerId playerId) {
		if(event.getPlayer().getId().equals(playerId)){
			return event.getPlayer().getSeatId();
		}
		return super.getSeatId(playerId);
	}

	@Override
	public int getStack(PlayerId playerId) {		
		if(event.getPlayer().getId().equals(playerId)){
			return event.getPlayer().getStackValue();
		}
		return super.getStack(playerId);
	}

	public HoldemTableEvent getLastEvent() {
		return event;
	}

}
