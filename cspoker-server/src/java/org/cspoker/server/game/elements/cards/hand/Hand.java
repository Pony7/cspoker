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
package org.cspoker.server.game.elements.cards.hand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cspoker.common.game.elements.cards.Card;

/**
 * A class of hands, that contain 0 to 7 cards
 * 
 * @author Cedric
 */
public class Hand implements Iterable<Card> {

	/***************************************************************************
	 * Variables
	 **************************************************************************/
	/**
	 * The maximum number of cards in a hand being 7 in Texas Hold'em (5 on the
	 * table and 2 private cards)
	 */
	public final static int MAX_CARDS = 7;
	private List<Card> cards;

	/**
	 * Private array containing the cards of this hand
	 */

	/***************************************************************************
	 * Constructors
	 **************************************************************************/

	/**
	 * Creates a new empty hand
	 * 
	 * @post there are no cards in the new hand | new.getNBCards()==0
	 */
	public Hand() {
		this.cards = new ArrayList<Card>();
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
	public Hand(Hand h) {
		if (h == null)
			throw new IllegalArgumentException();
		this.cards = new ArrayList<Card>(h.cards);
	}

	/**
	 * Constructs a new hand with the same cards as in the given card list
	 * 
	 * @param cardList
	 *            the given card list
	 * @throws IllegalArgumentException
	 *             if the number of cards in the card list is greater than the
	 *             maximum number of cards allowed in any hand or if the card
	 *             list isn't effective | cardList.size()>MAX_CARDS ||
	 *             cardList==null
	 */
	public Hand(Collection<Card> cardList) {
		if ((cardList.size() > MAX_CARDS) || (cardList == null))
			throw new IllegalArgumentException();
		this.cards = new ArrayList<Card>(cardList);
	}

	/***************************************************************************
	 * Methods
	 **************************************************************************/

	/**
	 * Returns the number of cards in this hand
	 */
	public int size() {
		return this.cards.size();
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
	public boolean add(Card card) {
		if (isFull() || this.contains(card))
			throw new IllegalArgumentException();
		return this.cards.add(card);
	}

	/**
	 * Checks whether this hand is full of cards
	 * 
	 * @return True if the number of cards in this hand equals the maximum
	 *         number of cards of any hand ; false otherwise | result ==
	 *         (getNBCards()==MAX_CARDS)
	 */
	public boolean isFull() {
		return this.cards.size() == MAX_CARDS;
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
	public Card get(int index) {
		if ((index < 0) || (index > this.cards.size() - 1))
			throw new IllegalArgumentException();
		return this.cards.get(index);
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
	public int indexOf(Card card) {
		if (!this.cards.contains(card))
			throw new IllegalArgumentException();
		return this.cards.indexOf(card);
	}

	/**
	 * Checks whether this hand contains a card equal to the given card
	 * 
	 * @param card
	 *            the given card
	 * @result True if there exists a card in this hand that is equal to the
	 *         given card ; false otherwise | result == (for a Card x in
	 *         this.getCards() : x.equals(card))
	 */
	public boolean contains(Card card) {
		return this.cards.contains(card);
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
	public boolean contains(Card[] array) {
		boolean contains = true;
		for (int j = 0; j < array.length - 1; j++) {
			if (!this.cards.contains(array[j]))
				contains = false;
		}
		return contains;
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
	public void removeCard(Card card) {
		if (!this.cards.contains(card))
			throw new IllegalArgumentException();
		if (!this.cards.contains(card))
			throw new IllegalArgumentException();
		this.cards.remove(card);
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
	public void removeCard(Card[] array) {
		if (!contains(array))
			throw new IllegalArgumentException();
		for (int j = 0; j < array.length; j++) {
			this.removeCard(array[j]);
		}
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
		Card highestRankCard = this.cards.get(0);
		for (int i = 0; i < this.cards.size(); i++) {
			int compareTo = this.cards.get(i).compareTo(highestRankCard);
			if ((compareTo == 1) || ((compareTo == 0) && (this.cards.get(i).getSuit().getValue() > highestRankCard.getSuit().getValue()))) {
				highestRankCard = this.cards.get(i);
			}
		}
		return highestRankCard;
	}

	/**
	 * Returns a textual representation of this hand
	 */
	@Override
	public String toString() {
		String result = "";

		if (this.cards.size() >= 5) {
			Hand bestFive = HandEvaluator.getBestFive(this);
			result = "type: " + HandEvaluator.getDescription(bestFive.getAsList());
		}
		result += " cards: ";

		result += this.cards.toString();
		return result;
	}

	/**
	 * Returns an iterator that iterates over the cards in this hand
	 */
	public Iterator<Card> iterator() {
		return this.cards.iterator();
	}

	public List<Card> getAsList() {
		return this.cards;
	}
}
