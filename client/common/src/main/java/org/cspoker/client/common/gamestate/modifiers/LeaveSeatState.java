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

import java.util.Map.Entry;

import org.cspoker.client.common.gamestate.ForwardingGameState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveSeatEvent;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.SeatId;

import com.google.common.collect.ImmutableBiMap;

public class LeaveSeatState extends ForwardingGameState {

	private final LeaveSeatEvent event;
	private final ImmutableBiMap<SeatId, PlayerId> seatMap;

	public LeaveSeatState(GameState gameState, LeaveSeatEvent event) {
		super(gameState);
		this.event = event;
		ImmutableBiMap<SeatId, PlayerId> oldMap = super.getSeatMap();
		ImmutableBiMap.Builder<SeatId, PlayerId> builder = new ImmutableBiMap.Builder<SeatId, PlayerId>();
		for(Entry<SeatId, PlayerId> entry:oldMap.entrySet()){
			if(!entry.getKey().equals(event.getSeatId())){
				builder.put(entry.getKey(), entry.getValue());
			}
		}
		seatMap = builder.build();
	}

	@Override
	public PlayerState getPlayer(PlayerId playerId) {
		if(event.getPlayerId().equals(playerId)){
			return null;
		}
		return super.getPlayer(playerId);
	}
	
	@Override
	public ImmutableBiMap<SeatId, PlayerId> getSeatMap() {
		return seatMap;
	}
	
	public HoldemTableEvent getLastEvent() {
		return event;
	}

	
	@Override
	public void acceptVisitor(GameStateVisitor visitor) {
		visitor.visitLeaveSeatState(this);
	}
	
	public LeaveSeatEvent getEvent() {
		return event;
	}
}
