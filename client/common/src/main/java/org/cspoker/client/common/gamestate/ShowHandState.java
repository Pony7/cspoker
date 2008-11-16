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

import java.util.Set;

import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;

public class ShowHandState extends ForwardingGameState {

	private final ShowHandEvent event;

	public ShowHandState(GameState gameState, ShowHandEvent event) {
		super(gameState);
		this.event = event;
	}

	@Override
	public Set<Card> getCards(PlayerId playerId) {
		if(event.getShowdownPlayer().getId().equals(playerId)){
			return event.getShowdownPlayer().getHandCards();
		}
		return super.getCards(playerId);
	} 
	
	public HoldemTableTreeEvent getLastEvent() {
		return event;
	}

}
