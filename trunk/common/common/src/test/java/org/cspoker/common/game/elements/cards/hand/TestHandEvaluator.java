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

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.cspoker.common.elements.hand.Hand;
import org.cspoker.common.elements.hand.HandType;

/**
 * A test class for all the methods in the HandEvaluator class
 * 
 * @author Cedric
 * 
 */
public class TestHandEvaluator extends TestCase {

	@Override
	protected void setUp(){
		BasicConfigurator.configure(); 
	}
	
	@Override
	protected void tearDown(){
		BasicConfigurator.resetConfiguration();
	}
	
	private static TestExactCard testExactCard = new TestExactCard();
	
	public void testHighCardHands() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		// test hand1 > hand2
		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.TWO, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.THREE, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.THREE, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand1.getShortDescription().equals(
				HandType.HIGH_CARD.getDescription()));
		assertTrue(hand2.getShortDescription().equals(
				HandType.HIGH_CARD.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 1);

		// test hand2>hand1
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.THREE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.HIGH_CARD.getDescription()));
		assertTrue(hand1.compareTo(hand2) == -1);
		// test hand2==hand1
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.THREE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.HIGH_CARD.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testPairHands() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		// test hand1>hand2
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.THREE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.THREE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand1.getShortDescription().equals(
				HandType.PAIR.getDescription()));
		assertTrue(hand2.getShortDescription().equals(
				HandType.PAIR.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 1);

		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.KING, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand2.getShortDescription().equals(
				HandType.PAIR.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 1);

		// test hand2>hand1
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.THREE, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand2.getShortDescription().equals(
				HandType.PAIR.getDescription()));
		assertTrue(hand1.compareTo(hand2) == -1);

		// test hand2==hand1

		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand2.getShortDescription().equals(
				HandType.PAIR.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testDoublePairHands() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand1.getShortDescription().equals(
				HandType.TWO_PAIR.getDescription()));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertFalse(hand2.getShortDescription().equals(
				HandType.TWO_PAIR.getDescription()));

		// hand1 >hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.TWO_PAIR.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 1);

		// hand1 <hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.TWO_PAIR.getDescription()));
		assertTrue(hand1.compareTo(hand2) == -1);

		hand2 = hand1;
		assertTrue(hand2.getShortDescription().equals(
				HandType.TWO_PAIR.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testThreeOfAKindHands() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand1.getShortDescription().equals(
				HandType.THREE_OF_A_KIND.getDescription()));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertFalse(hand2.getShortDescription().equals(
				HandType.THREE_OF_A_KIND.getDescription()));

		// hand1 >hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.THREE_OF_A_KIND.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 1);

		// hand1 <hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.THREE_OF_A_KIND.getDescription()));
		assertTrue(hand1.compareTo(hand2) == -1);

		hand2 = hand1;
		assertTrue(hand2.getShortDescription().equals(
				HandType.THREE_OF_A_KIND.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testFourOfAKindHands() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.CLUBS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand1.getShortDescription().equals(
				HandType.FOUR_OF_A_KIND.getDescription()));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertFalse(hand2.getShortDescription().equals(
				HandType.FOUR_OF_A_KIND.getDescription()));

		// hand1 >hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.FOUR_OF_A_KIND.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 1);

		// hand1 <hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.FOUR_OF_A_KIND.getDescription()));
		assertTrue(hand1.compareTo(hand2) == -1);

		hand2 = hand1;
		assertTrue(hand2.getShortDescription().equals(
				HandType.FOUR_OF_A_KIND.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testFullHouseHands() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.CLUBS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand1.getShortDescription().equals(
				HandType.FULL_HOUSE.getDescription()));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertFalse(hand2.getShortDescription().equals(
				HandType.FULL_HOUSE.getDescription()));

		// hand1 >hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.FULL_HOUSE.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 1);

		// hand1 <hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.FULL_HOUSE.getDescription()));
		assertTrue(hand1.compareTo(hand2) == -1);

		hand2 = hand1;
		assertTrue(hand2.getShortDescription().equals(
				HandType.FULL_HOUSE.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testFlushHands() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.TWO, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.NINE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.SPADES));

		assertTrue(hand1.getShortDescription().equals(
				HandType.FLUSH.getDescription()));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertFalse(hand2.getShortDescription().equals(
				HandType.FLUSH.getDescription()));

		// hand1 >hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SEVEN, Suit.CLUBS));
		assertTrue(hand2.getShortDescription().equals(
				HandType.FLUSH.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 1);

		// hand1 <hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.NINE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.THREE, Suit.HEARTS));
		assertTrue(hand2.getShortDescription().equals(
				HandType.FLUSH.getDescription()));
		assertTrue(hand1.compareTo(hand2) == -1);

		hand2 = hand1;
		assertTrue(hand2.getShortDescription().equals(
				HandType.FLUSH.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testStraightHands() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SIX, Suit.CLUBS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand1.getShortDescription().equals(
				HandType.STRAIGHT.getDescription()));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertFalse(hand2.getShortDescription().equals(
				HandType.STRAIGHT.getDescription()));

		// hand1 >hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.THREE, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.STRAIGHT.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 1);

		// hand1 <hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.QUEEN, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.KING, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.JACK, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TEN, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.STRAIGHT.getDescription()));
		assertTrue(hand1.compareTo(hand2) == -1);

		hand2 = hand1;
		assertTrue(hand2.getShortDescription().equals(
				HandType.STRAIGHT.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testStraightFlushHands() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));

		assertTrue(hand1.getShortDescription().equals(
				HandType.STRAIGHT_FLUSH.getDescription()));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SEVEN, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.DIAMONDS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.DIAMONDS));

		assertFalse(hand2.getShortDescription().equals(
				HandType.STRAIGHT_FLUSH.getDescription()));

		// hand1 >hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TWO, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.THREE, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		assertTrue(hand2.getShortDescription().equals(
				HandType.STRAIGHT_FLUSH.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 1);

		// hand1 <hand 2
		hand2 = new Hand();
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.QUEEN, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.KING, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.JACK, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TEN, Suit.CLUBS));
		assertTrue(hand2.getShortDescription().equals(
				HandType.STRAIGHT_FLUSH.getDescription()));
		assertTrue(hand1.compareTo(hand2) == -1);

		hand2 = hand1;
		assertTrue(hand2.getShortDescription().equals(
				HandType.STRAIGHT_FLUSH.getDescription()));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testSixCardHand() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));

		assertTrue(hand1.getBestFive().getShortDescription().equals(
				HandType.STRAIGHT_FLUSH.getDescription()));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));

		assertTrue(hand2.getBestFive().getShortDescription().equals(
				HandType.THREE_OF_A_KIND.getDescription()));

		// test hand1>hand 2
		assertTrue(hand1.compareTo(hand2) == 1);

		// test hand1<hand 2

		hand1 = new Hand();
		hand1 = hand1.add(testExactCard.getExactCard(Rank.TWO, Suit.CLUBS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.TWO, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));

		assertTrue(hand1.compareTo(hand2) == -1);

		// test hand1==hand2
		hand1 = new Hand();
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.CLUBS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));

		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testSevenCardHand() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.NINE, Suit.SPADES));

		assertTrue(hand1.getBestFive().getShortDescription().equals(
				HandType.STRAIGHT_FLUSH.getDescription()));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.KING, Suit.CLUBS));

		assertTrue(hand2.getBestFive().getShortDescription().equals(
				HandType.THREE_OF_A_KIND.getDescription()));

		// test hand1>hand 2
		assertTrue(hand1.compareTo(hand2) == 1);

		// test hand1<hand 2

		hand1 = new Hand();
		hand1 = hand1.add(testExactCard.getExactCard(Rank.TWO, Suit.CLUBS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.TWO, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.DIAMONDS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));

		assertTrue(hand1.compareTo(hand2) == -1);

		// test hand1==hand2
		hand1 = new Hand();
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.CLUBS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));

		assertTrue(hand1.compareTo(hand2) == 0);
	}

	public void testAceHighStraightVSKingHighStraight() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1 = hand1.add(testExactCard.getExactCard(Rank.KING, Suit.HEARTS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.QUEEN, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.JACK, Suit.CLUBS));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.TEN, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.NINE, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.TWO, Suit.SPADES));
		hand1 = hand1.add(testExactCard.getExactCard(Rank.THREE, Suit.DIAMONDS));

		hand2 = hand2.add(testExactCard.getExactCard(Rank.KING, Suit.HEARTS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.QUEEN, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.JACK, Suit.CLUBS));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.TEN, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.NINE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand2 = hand2.add(testExactCard.getExactCard(Rank.THREE, Suit.DIAMONDS));

		assertTrue(hand1.compareTo(hand2) == -1);
	}

}
