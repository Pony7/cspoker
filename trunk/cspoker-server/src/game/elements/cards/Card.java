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

package game.elements.cards;

import game.elements.cards.cardElements.Rank;
import game.elements.cards.cardElements.Suit;

/**
 * An interface of cards with a suit and a rank
 * @author Cedric
 *
 */
public interface Card {
	/**
	 * The number of different suits a card can have
	 */
	public final static int NUM_SUITS = 4;
	/**
	 * The number of different ranks a card can have
	 */
	public final static int NUM_RANKS = 13;
	/**
	 * The number of different cards there exist.
	 */
	public final static int NUM_CARDS = NUM_SUITS*NUM_RANKS;

	/**
	 * Returns the rank of this card
	 * @return	the rank of this card
	 */
	public Rank getRank();
	/**
	 * Returns the suit of this card
	 * @return the suit of this card
	 */
	public Suit getSuit();
	/**
	 * Returns a textual representation for this card
	 */
	public String toString();

	/**
	 * Checks whether this card has an equal rank as the given card
	 * @param card
	 * 			the given card to check
	 * @return true if the rank of the given hand is equal to the rank of this card;false otherwise
	 * 			| result == (this.getRank().equals(card.getRank()))
	 */
	public boolean equalRank(Card card);
	/**
	 * Checks whether this card has an equal suit as the given card
	 * @param card
	 * 			the given card to check
	 * @return true if the suit of the given rank is equal to the suit of this card;false otherwise
	 * 			| result == (this.getSuit().equals(card.getSuit()))
	 */
	public boolean equalSuit(Card card);
	/**
	 * Compares this card to a given other card by it's rank
	 * @param other
	 * 			the given other car
	 * @return	-1 if the value of the rank of the given card is greater than the value
	 * 			of the rank of this card
	 * 			| if(other.getRank().getValue()>this.getRank().getValue()) result==-1
	 * @return	1 if the value of the rank of this card is greater than the value
	 * 			of the rank of the given card
	 * 			| if(this.getRank().getValue()>other.getRank().getValue()) result==1
	 * @return	0 otherwise
	 * 			| else result==0
	 */
	public int compareTo(Card other);
	/**
	 * Checks if this card equals a given other card
	 * @param 	other
	 * 			the given other card
	 * @return	False if the given object is a card and the rank or the suit of the given object
	 * 			aren't equal to the rank or suit of this card
	 * 			| if( getClass() == obj.getClass() && (!equalRank(other) || !equalSuit()))
	 * 			|	result==false
	 * @return	True otherwise
	 * 			| else result==true
	 */
	public boolean equals(Card other);
}
