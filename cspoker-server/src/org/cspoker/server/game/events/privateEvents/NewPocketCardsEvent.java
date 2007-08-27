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
package org.cspoker.server.game.events.privateEvents;

import java.util.List;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.server.game.events.GameEvent;
import org.cspoker.server.game.player.SavedPlayer;

public class NewPocketCardsEvent extends GameEvent{

	private final SavedPlayer player;

	public NewPocketCardsEvent(SavedPlayer player){
		this.player = player;
	}

	public List<Card> getPocketCards() {
		return player.getPocketCards();
	}

	@Override
	public String toString(){
		String toReturn = player.getName()+" has received new pocket cards: ";
		for(Card card:player.getPocketCards()){
			toReturn+=card;
			toReturn+=", ";
		}
		return toReturn.substring(0, toReturn.length()-2)+".";
	}
}
