/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.common.elements.cards;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public final class Card
		implements Comparable<Card>, Serializable {
	
	private static final long serialVersionUID = 1370688591501465441L;
	
	@XmlAttribute
	private Rank rank;
	
	@XmlAttribute
	private Suit suit;
	
	public Card(final Rank rank, final Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	protected Card() {
	// no op
	}
	
	public String getLongDescription() {
		return rank + " of " + suit;
	}
	
	public Rank getRank() {
		return rank;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	@Override
	public String toString() {
		if (rank == null || suit == null)
			return "Unknown Card";
		return rank.getShortDescription() + suit.getShortDescription();
	}
	
	/**
	 * Compares this card to a given other card by it's rank
	 */
	public int compareTo(final Card other) {
		final int thisVal = (getRank() != null) ? (getRank().getValue()) : -1;
		final int anotherVal = (other.getRank() != null) ? (other.getRank().getValue()) : -1;
		
		return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Card other = (Card) obj;
		if (rank == null) {
			if (other.rank != null) {
				return false;
			}
		} else if (!rank.equals(other.rank)) {
			return false;
		}
		if (suit == null) {
			if (other.suit != null) {
				return false;
			}
		} else if (!suit.equals(other.suit)) {
			return false;
		}
		return true;
	}
}
