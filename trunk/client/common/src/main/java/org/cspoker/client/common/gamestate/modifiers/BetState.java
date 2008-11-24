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
import java.util.List;

import org.cspoker.client.common.gamestate.ForwardingGameState;
import org.cspoker.client.common.gamestate.ForwardingPlayerState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.player.PlayerId;

public class BetState
		extends ForwardingGameState {
	
	private final BetEvent event;
	
	private final int newPotSize;
	
	private final PlayerState playerState;
	
	public BetState(GameState gameState, BetEvent event) {
		super(gameState);
		this.event = event;
		
		PlayerState oldPlayerState = super.getPlayer(event.getPlayerId());
		
		final int newStack = oldPlayerState.getStack() - event.getAmount();
		this.newPotSize = super.getRoundPotSize() + event.getAmount();
		this.playerState = new ForwardingPlayerState(oldPlayerState) {
			
			@Override
			public int getStack() {
				return newStack;
			}
			
			@Override
			public int getBet() {
				return BetState.this.event.getAmount();
			}
			
			@Override
			public PlayerId getPlayerId() {
				return BetState.this.event.getPlayerId();
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
				return Collections.singletonList(getBet());
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
	public int getLargestBet() {
		return event.getAmount();
	}
	
	@Override
	public int getMinNextRaise() {
		return event.getAmount();
	}
	
	@Override
	public int getRoundPotSize() {
		return newPotSize;
	}
	
	public HoldemTableEvent getLastEvent() {
		return event;
	}
	
	@Override
	public PlayerId getLastBettor() {
		return event.getPlayerId();
	}
	
	@Override
	public int getNbRaises() {
		return 1;
	}
	
}
