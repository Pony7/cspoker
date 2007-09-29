package org.cspoker.common.game.elements.cards.cardElements;

public final class Card implements Comparable<Card> {
	private final Rank rank;
	private final Suit suit;

	public Card(final Rank rank, final Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	public String getLongDescription() {
		return this.rank + " of " + this.suit;
	}

	public Rank getRank() {
		return this.rank;
	}

	public Suit getSuit() {
		return this.suit;
	}

	@Override
	public String toString() {
		return this.rank.getShortDescription() + this.suit.getShortDescription();
	}

	/**
	 * Compares this card to a given other card by it's rank
	 * 
	 * @see Card
	 */
	public int compareTo(final Card other) {
		if (this.getSuit().getValue() > other.getSuit().getValue()) {
			return -1;
		}
		if (this.getRank().getValue() > other.getRank().getValue()) {
			return 1;
		}
		return 0;
	}
}