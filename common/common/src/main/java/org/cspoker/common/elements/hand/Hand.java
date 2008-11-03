/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.common.elements.hand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Suit;

/**
 * A class of hands, that contain 0 to 7 cards
 * 
 * @author Cedric
 */
public class Hand implements Iterable<Card>, Comparable<Hand> {
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	/**
	 * The maximum number of cards in a hand being 7 in Texas Hold'em (5 on the
	 * table and 2 private cards)
	 */
	public final static int MAX_CARDS = 7;

	private final List<Card> cards;

	private HandInfo handInfo;

	/**
	 * Creates a new empty hand
	 * 
	 * @post there are no cards in the new hand | new.getNBCards()==0
	 */
	public Hand() {
		cards = new ArrayList<Card>();
	}

	/**
	 * Private array containing the cards of this hand
	 */

	/***************************************************************************
	 * Constructors
	 **************************************************************************/

	/**
	 * Constructs a new hand with the same cards as in the given card list
	 * 
	 * @param cardCollection
	 *            the given card list
	 * @throws IllegalArgumentException
	 *             if the number of cards in the card list is greater than the
	 *             maximum number of cards allowed in any hand or if the card
	 *             list isn't effective | cardList.size()>MAX_CARDS ||
	 *             cardList==null
	 */
	public Hand(final Collection<Card> cardCollection) {
		if ((cardCollection == null)
				|| (cardCollection.size() > Hand.MAX_CARDS)) {
			throw new IllegalArgumentException();
		}
		cards = new ArrayList<Card>(cardCollection);
	}

	/**
	 * Create a new hand with the same cards as the given hand
	 * 
	 * @param h
	 *            the hand to clone.
	 * @throws IllegalArgumentException
	 *             if the given hand isn't effective | h==null
	 * @post the new hand contains every card of the given hand | for each Card
	 *       x|h.contains(x) -> result.contains(x)
	 */
	public Hand(final Hand h) {
		if (h == null) {
			throw new IllegalArgumentException();
		}
		cards = new ArrayList<Card>(h.cards);
	}

	/**
	 * Adds the given card to this hand if there is room
	 * 
	 * @param card
	 *            the given card
	 * @throws IllegalArgumentException
	 *             if this hand is full or if this hand already contains a card
	 *             equal to the given card | isFull() || this.contains(card)
	 * @post the new hand contains the given card | new.contains(card)
	 * @post the number of cards in this hand has increased by one |
	 *       new.getNBCards()=this.getNBCards()+1
	 */
	public boolean add(final Card card) {
		if (isFull() || this.contains(card)) {
			throw new IllegalArgumentException();
		}

		// TODO take this out if we make Hand immutable
		handInfo = null;

		return cards.add(card);
	}

	/***************************************************************************
	 * Methods
	 **************************************************************************/

	/**
	 * Checks whether this hand contains a card equal to the given card
	 * 
	 * @param card
	 *            the given card
	 * @result True if there exists a card in this hand that is equal to the
	 *         given card ; false otherwise | result == (for a Card x in
	 *         this.getCards() : x.equals(card))
	 */
	public boolean contains(final Card card) {
		return cards.contains(card);
	}

	/**
	 * Checks whether this hand contains the given array of cards
	 * 
	 * @param array
	 *            the given array of cards
	 * @result True if for every card in the array there exists a card in this
	 *         hand that is equal to the given card; false otherwise | result ==
	 *         (for every Card x in array:(for a Card x in this.getCards() :
	 *         x.equals(card)))
	 */
	public boolean contains(final Card[] array) {
		boolean contains = true;
		for (int j = 0; j < array.length - 1; j++) {
			if (!cards.contains(array[j])) {
				contains = false;
			}
		}
		return contains;
	}

	/**
	 * Get the card at the given position in this hand
	 * 
	 * @param index
	 *            the position of the card in the hand
	 * @throws IllegalArgumentException
	 *             if the given index is negative or greater than the number of
	 *             cards in this hand minus one | index < 0 || index >
	 *             getNBCards()-1
	 * @result This hand contains the resulting card | this.contains(result)
	 * @result This hand contains the resulting card at the given index |
	 *         index==this.getIndexOf(result)
	 */
	public Card get(final int index) {
		if ((index < 0) || (index > cards.size() - 1)) {
			throw new IllegalArgumentException();
		}
		return cards.get(index);
	}

	// TODO make private again
	HandInfo getHandInfo() {
		assert size() == 5 : "Illegal argument, hand size != 5";

		if (handInfo == null) {
			final int product = getProduct();
			final boolean flush = isFlush();
			handInfo = new HandInfo(product, flush);

		}
		return handInfo;
	}

	// TODO delete this method - who gets the extra chip should not be
	// determined by the highest card, it is more common to give it to the
	// closest player to the dealer's left. At least it should be configurable
	/**
	 * Returns the card with the highest rank in this hand. If there are plural
	 * cards with equal highest rank, the suits of the cards are used.(clubs
	 * ranking the lowest, followed by diamonds,hearts, and spades as in bridge)
	 */
	public Card getHighestRankCard() {
		Card highestRankCard = cards.get(0);
		for (int i = 0; i < cards.size(); i++) {
			final int compareTo = cards.get(i).compareTo(highestRankCard);
			if ((compareTo == 1)
					|| ((compareTo == 0) && (cards.get(i).getSuit().getValue() > highestRankCard
							.getSuit().getValue()))) {
				highestRankCard = cards.get(i);
			}
		}
		return highestRankCard;
	}

	/**
	 * Returns a list with alle the cards in the hand.
	 * 
	 * @return A list with alle the cards in the hand.
	 */
	public List<Card> getCards() {
		return new ArrayList<Card>(cards);
	}

	/**
	 * Returns the index a card equal to the given card in this hand
	 * 
	 * @param card
	 *            the given card
	 * @throws IllegalArgumentException
	 *             if this hand doesn't contain a card equal to the given card | !
	 *             this.contains(card)
	 * @result This hand contains the given card at the resulting index |
	 *         card=this.getCard(result)
	 */
	public int indexOf(final Card card) {
		if (!cards.contains(card)) {
			throw new IllegalArgumentException();
		}
		return cards.indexOf(card);
	}

	public boolean isFlush() {
		assert size() == 5 : "Illegal argument, hand size != 5";

		final Iterator<Card> iterator = iterator();
		final Suit suit = iterator.next().getSuit();

		while (iterator.hasNext()) {
			if (suit != iterator.next().getSuit()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks whether this hand is full of cards
	 * 
	 * @return True if the number of cards in this hand equals the maximum
	 *         number of cards of any hand ; false otherwise | result ==
	 *         (getNBCards()==MAX_CARDS)
	 */
	public boolean isFull() {
		return cards.size() == Hand.MAX_CARDS;
	}

	/**
	 * Returns an iterator that iterates over the cards in this hand
	 */
	public Iterator<Card> iterator() {
		return cards.iterator();
	}

	/**
	 * Removes a card equal to the given card from this hand
	 * 
	 * @param card
	 *            the given card
	 * @throws IllegalArgumentException
	 *             if this hand doesn't contain a card equal to the given card | !
	 *             this.contains(card)
	 * @post The hand doesn't contain the given card anymore |
	 *       !new.contains(card)
	 */
	public void removeCard(final Card card) {
		if (!cards.contains(card)) {
			throw new IllegalArgumentException();
		}

		// TODO take this out if we make Hand immutable
		handInfo = null;

		cards.remove(card);
	}

	/**
	 * Removes all the cards in the given array from this hand
	 * 
	 * @param array
	 *            the given hand
	 * @throws IllegalArgumentException
	 *             if this hand doesn't contain the cards of the given array | !
	 *             this.contains(array)
	 * @post The hand doesn't contain the cards of the given array anymore |
	 *       !new.contains(array)
	 */
	public void removeCard(final Card[] array) {
		if (!this.contains(array)) {
			throw new IllegalArgumentException();
		}

		// TODO take this out if we make Hand immutable
		handInfo = null;

		for (final Card element : array) {
			this.removeCard(element);
		}
	}

	/**
	 * Returns the number of cards in this hand
	 */
	public int size() {
		return cards.size();
	}

	/**
	 * Returns a textual representation of this hand
	 */

	@Override
	public String toString() {
		String result = "";

		if (cards.size() >= 5) {
			final Hand bestFive = getBestFive();
			result = bestFive.getDescription();
			result += ": ";
		}
		result += cards.toString();
		return result;
	}

	private int getProduct() {
		assert size() == 5 : "Illegal argument, hand size != 5";

		int product = 1;
		for (final Card card : cards) {
			product *= card.getRank().getPrime();
		}
		return product;
	}

	protected static class HandInfo {
		private final int product;

		private final boolean flush;

		private Integer hashCode;

		public HandInfo(final int product, final boolean flush) {
			this.product = product;
			this.flush = flush;
		}

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
			final HandInfo other = (HandInfo) obj;
			if (flush != other.flush) {
				return false;
			}
			if (product != other.product) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			if (hashCode == null) {
				final int prime = 31;
				int result = 1;
				result = prime * result + (flush ? 1231 : 1237);
				result = prime * result + product;
				hashCode = Integer.valueOf(result);
			}
			return hashCode.intValue();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getHandInfo().hashCode());
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
		final Hand other = (Hand) obj;
		return getHandInfo().equals(other.getHandInfo());
	}

	public Integer getRank() {
		return HandRanks.getInstance().getHandRank(this);
	}

	public String getDescription() {
		return HandRanks.getInstance().getLongDescription(this);
	}

	public String getShortDescription() {
		return HandRanks.getInstance().getShortDescription(this);
	}

	/**
	 * This function takes a seven card hand and returns the best five card hand
	 * that is a subset
	 * 
	 * @param hand
	 *            The seven card hand
	 * @return The best five card hand that is a subset
	 */
	public Hand getBestFive() {
		if (cards.size() == 5) {
			return this;
		}

		assert cards.size() == 7 || cards.size() == 6 : "Illegal state, hand size != 7 or 6 or 5";

		int minRank = Integer.MAX_VALUE;
		List<Card> bestHand = null;

		// Refactor
		if (cards.size() == 7) {
			for (int i = 0; i < 6; i++) {
				for (int j = i + 1; j < 7; j++) {
					final List<Card> fiveCardHand = new ArrayList<Card>(cards);
					fiveCardHand.remove(j);
					fiveCardHand.remove(i);

					final int rank = (new Hand(fiveCardHand)).getRank()
							.intValue();
					if (rank < minRank) {
						minRank = rank;
						bestHand = fiveCardHand;
					}
				}
			}
		} else if (cards.size() == 6) {
			for (int i = 0; i < 6; i++) {
				final List<Card> fiveCardHand = new ArrayList<Card>(cards);
				fiveCardHand.remove(i);

				final int rank = (new Hand(fiveCardHand)).getRank().intValue();
				if (rank < minRank) {
					minRank = rank;
					bestHand = fiveCardHand;
				}
			}
		}

		return new Hand(bestHand);
	}

	public int compareTo(Hand other) {
		Integer thisRank = getBestFive().getRank();
		Integer otherRank = other.getBestFive().getRank();
		return otherRank.compareTo(thisRank);
	}

}
