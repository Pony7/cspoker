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
import java.util.EnumSet;
import java.util.List;

import org.cspoker.client.common.gamestate.ForwardingGameState;
import org.cspoker.client.common.gamestate.ForwardingPlayerState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;

public class NewPocketCardsState
		extends ForwardingGameState {
	
	private final NewPocketCardsEvent event;
	private final PlayerState playerState;
	private final PlayerId playerId;
	
	public NewPocketCardsState(GameState gameState, PlayerId playerId, NewPocketCardsEvent event) {
		super(gameState);
		this.event = event;
		this.playerId = playerId;
		this.playerState = new ForwardingPlayerState(super.getPlayer(playerId)) {
			
			@Override
			public EnumSet<Card> getCards() {
				return NewPocketCardsState.this.event.getPocketCards();
			}
			
			@Override
			public PlayerId getPlayerId() {
				return NewPocketCardsState.this.playerId;
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
				return new ArrayList<Integer>();
			}
			
		};
	}
	
	@Override
	public PlayerState getPlayer(PlayerId playerId) {
		if (this.playerId.equals(playerId)) {
			return playerState;
		}
		return super.getPlayer(playerId);
	}
	
	public HoldemTableTreeEvent getLastEvent() {
		return event;
	}
	
	@Override
	public void visitGameState(GameStateVisitor visitor) {
		visitor.visitNewPocketCardsState(this);
	}
	
}
