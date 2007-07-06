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

package game.cards;

import java.util.Comparator;

/**
 * 
 * @author Kenzo
 *
 */
public class CardImpl implements Card, Comparable<Card>{
	
	/**********************************************************
	 * Variables
	 **********************************************************/
	
	private final  Rank rank;
	
	private final Suit suit;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	public CardImpl(Suit suit, Rank rank){
		this.rank = rank;
		this.suit = suit;
	}
	
	/**********************************************************
	 * Methods
	 **********************************************************/


	public Rank getRank() {
		return rank;
	}

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CardImpl other = (CardImpl) obj;
		
		if (rank!=other.rank){
			return false;
		}
		if (suit!=other.suit){
			return false;
		}
		return true;
	}
	/**
	 * Compares this card to a given other card by it's rank
	 * @param other
	 * 			the given other car
	 */
	public int compareTo(Card other) {
		if(other.getRank().getValue()>this.getRank().getValue())
			return -1;
		if(this.getRank().getValue()>other.getRank().getValue())
			return 1;
		return 0;
	}
	/**
	 * Returns a textual representation of this card
	 */
	public String toString(){
		return getSuit().toString()+" "+getRank().toString();
	}

	public boolean equalRank(Card card) {
		return this.getRank().getValue()==card.getRank().getValue();
	}
}
