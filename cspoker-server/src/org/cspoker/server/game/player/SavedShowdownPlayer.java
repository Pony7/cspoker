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
package org.cspoker.server.game.player;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandTypeCalculator;

public class SavedShowdownPlayer {
	
	private final SavedPlayer player;
	
	private final Hand hand;
	
	public SavedShowdownPlayer(SavedPlayer player, Hand hand){
		this.player = player;
		this.hand = hand;
	}
	
	//TODO Hand is not immutable
	
	/**
	 * Returns a textual representation of this showdown player.
	 */
	@Override
	public String toString(){
		String toReturn = player.getName()+" has a "+HandTypeCalculator.calculateHandType(hand)+". His best hand: ";
		for(Card card:hand){
			toReturn+=card;
			toReturn+=", ";
		}
		return toReturn.substring(0, toReturn.length()-2)+".";
	}

}
