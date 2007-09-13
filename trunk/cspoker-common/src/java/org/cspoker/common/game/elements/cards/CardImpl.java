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

package org.cspoker.common.game.elements.cards;

import org.cspoker.common.game.elements.cards.cardElements.Rank;
import org.cspoker.common.game.elements.cards.cardElements.Suit;

/**
 * A class of cards that can be compared
 * @author Cedric
 *
 */
public class CardImpl implements Card{

	/**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The rank of this card
	 */
	private final  Rank rank;
	/**
	 * The suit of this card
	 */
	private final Suit suit;

	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Creates a new card with the given suit and rank.
	 * @param	suit
	 * 			the given suit
	 * @param	rank
	 * 			the given rank
	 * @post	the suit of this card is equal to the given suit
	 *			| new.getSuit().equals(suit)
	 * @post	the rank of this card is equal to the given rank
	 * 			| new.getRank().equals(rank)
	 */
	public CardImpl(Suit suit, Rank rank){
		this.rank = rank;
		this.suit = suit;
	}

	/**********************************************************
	 * Methods
	 **********************************************************/


	/**
	 * Returns the rank of this card
	 * @see Card
	 */
	public Rank getRank() {
		return rank;
	}
	/**
	 * Returns the suit of this card
	 * @see Card
	 */
	public Suit getSuit() {
		return suit;
	}


	/**********************************************************
	 * Equals, HashCode and CompareTo
	 **********************************************************/

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((rank == null) ? 0 : rank.hashCode());
		result = PRIME * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	/**
	 * Checks whether this card is equal to a given object
	 * @param	obj
	 * 			the given object
	 * @return	False if the given object is null or if the given object isn't a card
	 * 			| if(obj == null || getClass() != obj.getClass() ) result ==false
	 * @return	Else check if this card equals the given card
	 * 			| result == this.equals((Card) obj)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		return equals((Card) obj);

	}
	/**
	 * Checks if this card equals a given other card
	 * @see	Card
	 */
	public boolean equals(Card other){
		if (rank!=other.getRank())
			return false;
		if (suit!=other.getSuit())
			return false;
		return true;
	}
	/**
	 * Compares this card to a given other card by it's rank
	 * @see Card
	 */
	public int compareTo(Card other) {
		if(other.getRank().getValue()>getRank().getValue())
			return -1;
		if(getRank().getValue()>other.getRank().getValue())
			return 1;
		return 0;
	}
	/**
	 * Returns a textual representation of this card
	 */
	@Override
	public String toString(){
		return getRank().toString() + getSuit().toString();
	}

	/**
	 * Checks if this card and the given card have an equal rank
	 * @see Card
	 */
	public boolean equalRank(Card card) {
		return getRank().getValue()==card.getRank().getValue();
	}
	/**
	 * Checks if this card and the given card have an equal rank
	 * @see Card
	 */
	public boolean equalSuit(Card card){
		return getSuit().equals(card.getSuit());
	}
}
