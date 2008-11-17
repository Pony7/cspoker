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

import java.util.Set;

import org.cspoker.client.common.gamestate.ForwardingGameState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.elements.cards.Card;

import com.google.common.collect.Sets;

public class NewCommunityCardsState extends ForwardingGameState {

	private final NewCommunityCardsEvent event;

	public NewCommunityCardsState(GameState gameState, NewCommunityCardsEvent event) {
		super(gameState);
		this.event = event;
	}

	@Override
	public Set<Card> getCommunityCards() {
		return Sets.union(super.getCommunityCards(),event.getCommunityCards());
	}
	
	public HoldemTableEvent getLastEvent() {
		return event;
	}

}
