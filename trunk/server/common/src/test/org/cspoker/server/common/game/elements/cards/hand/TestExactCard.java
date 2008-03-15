/**
 * 
 */
package org.cspoker.server.common.game.elements.cards.hand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.cspoker.server.common.game.elements.cards.deck.Deck;

/**
 * @author Duplicity
 * 
 */
public class TestExactCard {
	private final Map<CardShell, Card> cardMap;

	public TestExactCard() {
		cardMap = new HashMap<CardShell, Card>();

		final Deck deck = new Deck();
		final List<Card> list = deck.deal(52);
		for (final Card card : list) {
			final Rank rank = card.getRank();
			final Suit suit = card.getSuit();

			cardMap.put(new CardShell(rank, suit), card);
		}
	}

	public Card getExactCard(final Rank rank, final Suit suit) {
		return cardMap.get(new CardShell(rank, suit));
	}

	/*
	 * Pair containing rank and suit just for hashing cards.
	 */
	private static class CardShell {
		private final Rank rank;

		private final Suit suit;

		public CardShell(final Rank rank, final Suit suit) {
			this.rank = rank;
			this.suit = suit;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((rank == null) ? 0 : rank.hashCode());
			result = prime * result + ((suit == null) ? 0 : suit.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			final CardShell other = (CardShell) obj;
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
}
