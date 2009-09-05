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

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.ForwardingGameState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.client.common.playerstate.SeatedPlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.SeatId;

import com.google.common.collect.ImmutableBiMap;

public class SitInState
		extends ForwardingGameState {
	
	private final static Logger logger = Logger.getLogger(SitInState.class);
	
	private final SitInEvent event;
	
	private final PlayerState playerState;

	private final ImmutableBiMap<SeatId, PlayerId> seatMap;
	
	public SitInState(GameState gameState, SitInEvent event) {
		super(gameState);
		this.event = event;
		playerState = new SeatedPlayerState(event.getPlayer());
		ImmutableBiMap<SeatId, PlayerId> seats;
		try {
			seats = (new ImmutableBiMap.Builder<SeatId, PlayerId>())
			.putAll(super.getSeatMap())
			.put(playerState.getSeatId(), playerState.getPlayerId())
			.build();
		} catch (Exception e) {
			logger.error("A player sits in that already sits in, ignoring. Is this a pokersource bug?");
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new RuntimeException(e);
			seats = super.getSeatMap();
		}
		seatMap = seats;
	}
	
	@Override
	public PlayerState getPlayer(PlayerId playerId) {
		if(playerState.getPlayerId().equals(playerId))
			return playerState;
		else return super.getPlayer(playerId);
	}
	
	@Override
	public int getNbPlayers() {
		return super.getNbPlayers()+1;
	}
	
	@Override
	public ImmutableBiMap<SeatId, PlayerId> getSeatMap() {
		return seatMap;
	}
	
	public HoldemTableEvent getLastEvent() {
		return event;
	}
	
	public SitInEvent getEvent() {
		return event;
	}
	
	@Override
	public void acceptVisitor(GameStateVisitor visitor) {
		visitor.visitSitInState(this);
	}
	
}
