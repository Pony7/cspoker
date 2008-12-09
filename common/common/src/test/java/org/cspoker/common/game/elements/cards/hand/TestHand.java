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
package org.cspoker.common.game.elements.cards.hand;

import java.util.Iterator;

import junit.framework.TestCase;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.cspoker.common.elements.hand.Hand;

/**
 * A test class for the general testing of hands
 * 
 * @author Cedric
 * 
 */
public class TestHand extends TestCase {

	private static TestExactCard testExactCard = new TestExactCard();

	protected Hand hand1;

	public void test() {
		hand1 = new Hand();
		hand1 = hand1.add(testExactCard.getExactCard(Rank.THREE, Suit.HEARTS));
		assertTrue(hand1.size() == 1);
		assertTrue(hand1.contains(testExactCard.getExactCard(Rank.THREE,
				Suit.HEARTS)));

		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		assertTrue(hand1.size() == 2);
		assertTrue(hand1.contains(testExactCard.getExactCard(Rank.THREE,
				Suit.HEARTS)));

		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.CLUBS));
		assertTrue(hand1.size() == 3);

		hand1 = hand1.add(testExactCard.getExactCard(Rank.SIX, Suit.DIAMONDS));
		assertTrue(hand1.size() == 4);

		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		assertTrue(hand1.size() == 5);
		assertTrue(hand1.contains(testExactCard.getExactCard(Rank.THREE,
				Suit.HEARTS)));
		assertTrue(hand1.contains(testExactCard.getExactCard(Rank.ACE,
				Suit.HEARTS)));

		hand1 = hand1.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		assertTrue(hand1.size() == 6);

		hand1 = hand1.add(testExactCard.getExactCard(Rank.QUEEN, Suit.CLUBS));
		assertTrue(hand1.size() == 7);

		try {
			hand1 = hand1.add(testExactCard.getExactCard(Rank.QUEEN, Suit.CLUBS));
			fail("Exception Expected.");
		} catch (IllegalArgumentException e) {
			assertTrue(hand1.size() == 7);
		}

		assertTrue(hand1.contains(testExactCard.getExactCard(Rank.QUEEN,
				Suit.CLUBS)));
		assertFalse(hand1.contains(testExactCard.getExactCard(Rank.QUEEN,
				Suit.HEARTS)));
		assertTrue(hand1.isFull());

		hand1 = hand1.removeCard(testExactCard.getExactCard(Rank.QUEEN, Suit.CLUBS));
		assertFalse(hand1.contains(testExactCard.getExactCard(Rank.QUEEN,
				Suit.CLUBS)));
		assertFalse(hand1.isFull());
		assertTrue(hand1.size() == 6);

		try {
			hand1 = hand1
					.removeCard(testExactCard.getExactCard(Rank.QUEEN,
							Suit.CLUBS));
			fail("Exception Expected.");
		} catch (IllegalArgumentException e) {
			assertFalse(hand1.contains(testExactCard.getExactCard(Rank.QUEEN,
					Suit.CLUBS)));
			assertFalse(hand1.isFull());
			assertTrue(hand1.size() == 6);
		}

		assertTrue(testExactCard.getExactCard(Rank.ACE, Suit.CLUBS).compareTo(
				testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS)) > 0);
		assertTrue(testExactCard.getExactCard(Rank.DEUCE, Suit.HEARTS)
				.compareTo(testExactCard.getExactCard(Rank.KING, Suit.SPADES)) < 0);
	}

	public void testIterator() {
		hand1 = new Hand();
		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.CLUBS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.DEUCE, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.KING, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.JACK, Suit.HEARTS));

		Iterator<Card> iterator = hand1.iterator();

		int n=0;
		while (iterator.hasNext()) {
			assertNotNull(iterator.next());
			n++;
		}
		assertEquals(n, 6);
	}
}
