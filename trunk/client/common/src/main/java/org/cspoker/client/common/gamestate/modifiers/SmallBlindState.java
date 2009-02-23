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
import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.elements.player.PlayerId;

public class SmallBlindState
		extends ForwardingGameState {
	
	private final SmallBlindEvent event;
	private final PlayerState playerState;
	
	public SmallBlindState(GameState gameState, SmallBlindEvent event) {
		super(gameState);
		this.event = event;
		PlayerState oldPlayerState = super.getPlayer(event.getPlayerId());
		final int newStack = oldPlayerState.getStack() - event.getAmount();
		;
		playerState = new ForwardingPlayerState(oldPlayerState) {
			
			@Override
			public int getBet() {
				return SmallBlindState.this.event.getAmount();
			}
			
			@Override
			public PlayerId getPlayerId() {
				return SmallBlindState.this.event.getPlayerId();
			}
			
			@Override
			public int getStack() {
				return newStack;
			}
			
			@Override
			public boolean sitsIn() {
				return true;
			}
			
			@Override
			public boolean hasFolded() {
				return false;
			}
			
			@Override
			public boolean isPlayingGame() {
				return true;
			}
			
			@Override
			public boolean isSmallBlind() {
				return true;
			}
			
			@Override
			public boolean hasChecked() {
				return false;
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
		return event.getAmount();
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
		return 0;
	}
	
	@Override
	public void acceptVisitor(GameStateVisitor visitor) {
		visitor.visitSmallBlindState(this);
	}
	
	public SmallBlindEvent getEvent() {
		return event;
	}
	
}
