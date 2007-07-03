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

package game.cards.hand;

import game.cards.Card;

/**
 * This class represents a hand, being a combination of exactly 5 cards.
 * @author Cedric
 */
public class HandImpl implements Hand, Comparable<HandImpl>{

	/**
	 * Creates a new hand containing the given 5 cards
	 * @param cards
	 * 			the given 5 cards
	 * @throws IllegalArgumentException
	 * 			if the length of the given card array isn't five
	 * 			| cards.length != 5
	 */
	public HandImpl(Card[] cards){
		if(cards.length != 5)
			throw new IllegalArgumentException();
		this.type=determineType(cards);
		this.cards=cards;
	}

	private Card[] cards;
	private HandType type;
	
	/**
	 * Returns the type of this hand
	 */
	public HandType getType() {
		return type;
	}
	/**
	 * Determines the handtype for the given five cards
	 * @param cards
	 * 			the given cards
	 * @throws IllegalArgumentException
	 * 			if the length of the given card array isn't five
	 * 			| cards.length != 5
	 */
	public static HandType determineType(Card[] cards){
		if(cards.length != 5)
			throw new IllegalArgumentException();
		//TODO Auto-generated method stub
		return null;
	}
	/**
	 * Compares this handimpl tot the given handimpl
	 * @param other
	 * 			the given other hand
	 * @return -1 if the given hand is greater than this hand, 0 if they are totally equal
	 * 			1 if the given hand is smaller than this hand
	 
	 */
	public int compareTo(HandImpl other) {
		if(other.getType().getRanking()>this.getType().getRanking())
			return -1;
		if(other.getType().getRanking()<this.getType().getRanking())
			return 1;
		else {
			//TODO: complete compare
			return 0;
		}
	}
}
