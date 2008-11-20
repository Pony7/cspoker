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

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Suit;
import org.cspoker.common.util.Pair;

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

	private final EnumSet<Card> cards;

	public final UniqueHandHash handHash;

	/***************************************************************************
	 * Constructors
	 **************************************************************************/

	public Hand(Collection<Card> cardCollection) {
		if ((cardCollection == null)
				|| (cardCollection.size() > Hand.MAX_CARDS)) {
			throw new IllegalArgumentException();
		}
		cards = EnumSet.copyOf(cardCollection);
		handHash = new UniqueHandHash(getProduct(), isAllSameSuite());
	}

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
	public Hand(final EnumSet<Card> cardCollection) {
		if ((cardCollection == null)
				|| (cardCollection.size() > Hand.MAX_CARDS)) {
			throw new IllegalArgumentException();
		}
		cards = EnumSet.copyOf(cardCollection);
		handHash = new UniqueHandHash(getProduct(), isAllSameSuite());
	}

	public Hand add(final Card card) {
		EnumSet<Card> newCards = getCards();
		newCards.add(card);
		return new Hand(newCards);
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

	// TODO delete this method - who gets the extra chip should not be
	// determined by the highest card, it is more common to give it to the
	// closest player to the dealer's left. At least it should be configurable
	/**
	 * Returns the card with the highest rank in this hand. If there are plural
	 * cards with equal highest rank, the suits of the cards are used.(clubs
	 * ranking the lowest, followed by diamonds,hearts, and spades as in bridge)
	 */
	public Card getHighestRankCard() {
		Card highestRankCard = null;
		for(Card card:cards){
			if(highestRankCard==null || card.compareTo(highestRankCard)==1){
				highestRankCard  = card;
			}
		}
		return highestRankCard;
	}

	/**
	 * Returns a list with all the cards in the hand.
	 * 
	 * @return A list with all the cards in the hand.
	 */
	public EnumSet<Card> getCards() {
		return EnumSet.copyOf(cards);
	}

	public boolean isAllSameSuite() {
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
	 *            the given card\
	 */
	public Hand removeCard(final Card card) {
		EnumSet<Card> newCards = getCards();
		newCards.remove(card);
		return new Hand(newCards);
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

		int product = 1;
		for (final Card card : cards) {
			product *= card.getRank().getPrime();
		}
		return product;
	}

	protected static class UniqueHandHash {

		public final int hashCode;

		public UniqueHandHash(final int product, final boolean flush) {
			final int prime = 31;
			int result = 1;
			result = prime * result + (flush ? 1231 : 1237);
			result = prime * result + product;
			hashCode = result;
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof UniqueHandHash))
				return false;
			UniqueHandHash other = (UniqueHandHash) obj;
			if (hashCode != other.hashCode)
				return false;
			return true;
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (handHash.hashCode);
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
		return handHash.equals(other.handHash);
	}

	public int getRank() {
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
		Pair<Hand,Integer> pair = getBestFiveWithRank();
		return pair.getLeft();
	}

	public int getBestFiveRank() {
		Pair<Hand,Integer> pair = getBestFiveWithRank();
		return pair.getRight();
	}

	public Pair<Hand,Integer> getBestFiveWithRank() {
		if (cards.size() == 5) {
			return new Pair<Hand, Integer>(this,getRank());
		}

		int minRank = Integer.MAX_VALUE;
		EnumSet<Card> bestHand = null;

		if (cards.size() == 7) {
			for(Card firstToRemove:cards){
				for(Card secondToRemove:cards){
					if(firstToRemove.compareTo(secondToRemove)<0){
						final EnumSet<Card> selection = getCards();
						selection.remove(firstToRemove);
						selection.remove(secondToRemove);

						final int rank = (new Hand(selection)).getRank();
						if (rank < minRank) {
							minRank = rank;
							bestHand = selection;
						}
					}
				}
			}
		} else if (cards.size() == 6) {
			for(Card firstToRemove:cards){
				final EnumSet<Card> selection = getCards();
				selection.remove(firstToRemove);

				final int rank = (new Hand(selection)).getRank();
				if (rank < minRank) {
					minRank = rank;
					bestHand = selection;
				}
			}
		}
		return new Pair<Hand, Integer>(new Hand(bestHand),minRank);
	}

	public int compareTo(Hand other) {
		Integer thisRank = getBestFiveRank();
		Integer otherRank = other.getBestFiveRank();
		return otherRank.compareTo(thisRank);
	}

}
