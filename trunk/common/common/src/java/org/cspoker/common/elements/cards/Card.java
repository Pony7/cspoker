package org.cspoker.common.elements.cards;

import java.io.Serializable;

public final class Card implements Comparable<Card>, Serializable {

	private static final long serialVersionUID = 1370688591501465441L;

	private final Rank rank;
	private final Suit suit;

	public Card(final Rank rank, final Suit suit) {
		this.rank = rank;
		this.suit = suit;
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
}