package org.cspoker.common.elements.cards;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public final class Card implements Comparable<Card>, Serializable {

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

	public String toString() {
		return rank.getShortDescription() + suit.getShortDescription();
	}

	/**
	 * Compares this card to a given other card by it's rank
	 */
	public int compareTo(final Card other) {
		final int thisVal = (getRank().getValue());
		final int anotherVal = (other.getRank().getValue());

		return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

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