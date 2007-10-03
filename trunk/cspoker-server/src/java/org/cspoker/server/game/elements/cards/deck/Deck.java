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

package org.cspoker.server.game.elements.cards.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.cspoker.common.game.elements.cards.cardElements.Rank;
import org.cspoker.common.game.elements.cards.cardElements.Suit;
import org.cspoker.server.game.elements.cards.deck.randomGenerator.RandomOrgSeededRandomGenerator;

/**
 * A class to represent a deck of cards.
 * 
 * @author Kenzo & Cedric
 * 
 */
public class Deck {
    public static final class Card implements Comparable<Card> {
	private final Rank rank;

	private final Suit suit;

	private Card(final Rank rank, final Suit suit) {
	    this.rank = rank;
	    this.suit = suit;
	}

	/**
	 * Compares this card to a given other card by it's rank
	 * 
	 * @see Card
	 */
	public int compareTo(final Card other) {
	    final int thisVal = (this.getRank().getValue());
	    final int anotherVal = (other.getRank().getValue());

	    return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
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
	    return this.rank.getShortDescription()
		    + this.suit.getShortDescription();
	}
    }

    private static Logger logger = Logger.getLogger(Deck.class);

    /*
     * Sorted prototype deck for copying
     */
    private static final List<Card> PROTO_DECK = new ArrayList<Card>();

    static {
	// Initialize prototype deck
	for (final Suit suit : Suit.values()) {
	    for (final Rank rank : Rank.values()) {
		Deck.PROTO_DECK.add(new Card(rank, suit));
	    }
	}
    }

    /**
     * The sequence of cards in this deck.
     */
    private final List<Card> cards;

    /**
     * Construct a new deck of cards and shuffle it.
     */
    public Deck() {
	this.cards = new ArrayList<Card>(Deck.PROTO_DECK);
	Collections.shuffle(this.cards, RandomOrgSeededRandomGenerator
		.getInstance().getRandom());
    }

    /**
     * Returns a list of Cards removed from the deck. This method is not
     * thread-safe. Since the deck is modified, it is important to synchronize
     * on the deck in multi-threaded programs.
     * 
     * @param number
     *                number of cards to return
     * @return {@link List} of Cards
     */
    public List<Card> deal(final int number) {
	final int deckSize = this.cards.size();
	final List<Card> handView = this.cards.subList(deckSize - number,
		deckSize);
	final ArrayList<Card> hand = new ArrayList<Card>(handView);
	handView.clear();
	return hand;
    }

    // TODO delete this method
    /**
     * Draw the card on the top of this deck.
     * 
     * @post The second card in the deck becomes the new top card
     * @result The top card from the deck is returned
     * @throws IllegalStateException
     *                 There must be at least one card in the deck |
     *                 getDeckSize()<=0
     */
    public Card drawCard() {
	return this.deal(1).get(0);
    }

    /**
     * Returns the textual representation of this deck.
     */
    @Override
    public String toString() {
	return this.cards.toString();
    }
}
