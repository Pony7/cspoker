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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cspoker.client.common.gamestate.ForwardingGameState;
import org.cspoker.client.common.gamestate.ForwardingPlayerState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.player.PlayerId;

public class CallState
		extends ForwardingGameState {
	
	private final CallEvent event;
	private final int newPotSize;
	
	private final PlayerState playerState;
	
	public CallState(final GameState gameState, CallEvent event) {
		super(gameState);
		this.event = event;
		
		final int newBetSize = super.getLargestBet();
		
		final PlayerState player = super.getPlayer(event.getPlayerId());
		final int chipsMoved = newBetSize - player.getBet();
		this.newPotSize = super.getRoundPotSize() + chipsMoved;
		
		playerState = new ForwardingPlayerState(player) {
			
			@Override
			public int getBet() {
				return newBetSize;
			}
			
			@Override
			public int getStack() {
				return super.getStack() - chipsMoved;
			}
			
			@Override
			public PlayerId getPlayerId() {
				return CallState.this.event.getPlayerId();
			}
			
			@Override
			public boolean hasFolded() {
				return false;
			}
			
			@Override
			public boolean sitsIn() {
				return true;
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public List<Integer> getBetProgression() {
				List<Integer> result = new ArrayList<Integer>();
				result.addAll(gameState.getPlayer(gameState.getLastBettor()).getBetProgression());
				return Collections.unmodifiableList(result);
			}
			
		};
	}
	
	@Override
	public PlayerState getPlayer(PlayerId playerId) {
		if (event.getPlayerId().equals(playerId)) {
			return playerState;
		}
		return super.getPlayer(playerId);
	}
	
	@Override
	public int getRoundPotSize() {
		return newPotSize;
	}
	
	public HoldemTableEvent getLastEvent() {
		return event;
	}
	
	@Override
	public void visitGameState(GameStateVisitor visitor) {
		visitor.visitCallState(this);
	}
	
}
