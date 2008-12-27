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

import java.util.List;

import org.cspoker.client.common.gamestate.ForwardingGameState;
import org.cspoker.client.common.gamestate.ForwardingPlayerState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.player.PlayerId;

public class FoldState
		extends ForwardingGameState {
	
	private final FoldEvent event;
	private final PlayerState playerState;
	
	public FoldState(GameState gameState, FoldEvent event) {
		super(gameState);
		this.event = event;
		final PlayerState oldPlayerState = super.getPlayer(event.getPlayerId());
		playerState = new ForwardingPlayerState(oldPlayerState) {
			
			@Override
			public boolean hasFolded() {
				return true;
			}
			
			@Override
			public boolean sitsIn() {
				return true;
			}
			
			@Override
			public PlayerId getPlayerId() {
				return FoldState.this.event.getPlayerId();
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public List<Integer> getBetProgression() {
				return oldPlayerState.getBetProgression();
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
	
	public HoldemTableEvent getLastEvent() {
		return event;
	}
	
	@Override
	public void visitGameState(GameStateVisitor visitor) {
		visitor.visitFoldState(this);
	}
	
}
