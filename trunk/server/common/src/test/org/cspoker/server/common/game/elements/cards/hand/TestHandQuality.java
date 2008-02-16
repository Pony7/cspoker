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
package org.cspoker.server.common.game.elements.cards.hand;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.cspoker.server.common.game.elements.cards.deck.Deck;
import org.cspoker.server.common.util.TestExactCard;

/**
 * A test class for the quality of a hand
 * 
 * @author Cedric
 * 
 */
public class TestHandQuality extends TestCase {
	private static Logger logger = Logger.getLogger(TestHandQuality.class);

	private static TestExactCard testExactCard = new TestExactCard();

	public void testHighCardHand() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand1.add(testExactCard.getExactCard(Rank.NINE, Suit.HEARTS));
		hand1.add(testExactCard.getExactCard(Rank.THREE, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand1.getShortDescription().equals(
				HandType.HIGH_CARD.getDescription()));
		TestHandQuality.logger.info(hand1.getShortDescription());

		hand2.add(testExactCard.getExactCard(Rank.NINE, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.HIGH_CARD.getDescription()));
		TestHandQuality.logger.info(hand2.getShortDescription());

	}

	public void testPairHand() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.SPADES));
		assertTrue(hand1.getShortDescription().equals(
				HandType.PAIR.getDescription()));
		TestHandQuality.logger.info(hand1.getShortDescription());

		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand2.add(testExactCard.getExactCard(Rank.FOUR, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.PAIR.getDescription()));
		TestHandQuality.logger.info(hand2.getShortDescription());
	}

	public void testDoublePairHand() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.DIAMONDS));
		hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.HEARTS));
		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.CLUBS));
		hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.SPADES));
		assertTrue(hand1.getShortDescription().equals(
				HandType.TWO_PAIR.getDescription()));
		TestHandQuality.logger.info(hand1.getShortDescription());

		hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.HEARTS));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.TWO_PAIR.getDescription()));
		TestHandQuality.logger.info(hand2.getShortDescription());
	}

	public void testThreeOfAKindHand() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.HEARTS));
		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.CLUBS));
		hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand1.getShortDescription().equals(
				HandType.THREE_OF_A_KIND.getDescription()));
		TestHandQuality.logger.info(hand1.getShortDescription());

		hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.HEARTS));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.THREE_OF_A_KIND.getDescription()));
		TestHandQuality.logger.info(hand2.getShortDescription());
	}

	public void testFourOfAKindHand() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.DIAMONDS));
		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.CLUBS));
		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		assertTrue(hand1.getShortDescription().equals(
				HandType.FOUR_OF_A_KIND.getDescription()));
		TestHandQuality.logger.info(hand1.getShortDescription());

		hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
		hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.CLUBS));
		hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		assertTrue(hand2.getShortDescription().equals(
				HandType.FOUR_OF_A_KIND.getDescription()));
		TestHandQuality.logger.info(hand2.getShortDescription());
	}

	public void testFlushHand() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.EIGHT, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.THREE, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.DEUCE, Suit.SPADES));
		assertTrue(hand1.getShortDescription().equals(
				HandType.FLUSH.getDescription()));
		TestHandQuality.logger.info(hand1.getShortDescription());

		hand2.add(testExactCard.getExactCard(Rank.ACE, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.SEVEN, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.DIAMONDS));
		assertTrue(hand2.getShortDescription().equals(
				HandType.FLUSH.getDescription()));
		TestHandQuality.logger.info(hand2.getShortDescription());
	}

	public void testStraightHand() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.CLUBS));
		hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.THREE, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.DEUCE, Suit.SPADES));
		assertTrue(hand1.getShortDescription().equals(
				HandType.STRAIGHT.getDescription()));
		TestHandQuality.logger.info(hand1.getShortDescription());

		hand2.add(testExactCard.getExactCard(Rank.NINE, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.FIVE, Suit.HEARTS));
		hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.SEVEN, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.EIGHT, Suit.DIAMONDS));
		assertTrue(hand2.getShortDescription().equals(
				HandType.STRAIGHT.getDescription()));
		TestHandQuality.logger.info(hand2.getShortDescription());
	}

	public void testFullHouseHand() {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.HEARTS));
		hand1.add(testExactCard.getExactCard(Rank.KING, Suit.CLUBS));
		hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.SPADES));
		assertTrue(hand1.getShortDescription().equals(
				HandType.FULL_HOUSE.getDescription()));
		TestHandQuality.logger.info(hand1.getShortDescription());

		hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.DIAMONDS));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.HEARTS));
		hand2.add(testExactCard.getExactCard(Rank.KING, Suit.SPADES));
		hand2.add(testExactCard.getExactCard(Rank.SIX, Suit.CLUBS));
		assertTrue(hand2.getShortDescription().equals(
				HandType.FULL_HOUSE.getDescription()));
		TestHandQuality.logger.info(hand2.getShortDescription());
	}

	public void testHandQuality() {

		boolean qualityGreater, qualitySmaller, qualityEqual;
		int compare;
		int quality1, quality2;
		Map<String, Integer> rankMap = new HashMap<String, Integer>();
		double totalTests = 10000.0;
		for (int j = 0; j < totalTests; j++) {
			Deck deck = new Deck();
			Hand hand1 = new Hand(deck.deal(5));
			Hand hand2 = new Hand(deck.deal(5));

			compare = hand1.compareTo(hand2);
			qualityGreater = (compare == 1);
			qualitySmaller = (compare == -1);
			qualityEqual = (compare == 0);
			quality1 = hand1.getRank().intValue();
			quality2 = hand2.getRank().intValue();

			String rank = hand1.getShortDescription();
			if (!rankMap.containsKey(rank)) {
				rankMap.put(rank, Integer.valueOf(1));
			}
			rankMap
					.put(rank, Integer
							.valueOf(rankMap.get(rank).intValue() + 1));

			if (qualityGreater != (quality1 < quality2)
					|| qualitySmaller != (quality1 > quality2)
					|| qualityEqual != (quality1 == quality2)) {
				TestHandQuality.logger.info("compareTo "
						+ hand1.compareTo(hand2));
				TestHandQuality.logger.info(hand1.toString());
				TestHandQuality.logger.info(hand1.getShortDescription());
				TestHandQuality.logger.info("quality 1 " + quality1);
				TestHandQuality.logger.info(hand2.toString());
				TestHandQuality.logger.info(hand2.getShortDescription());
				TestHandQuality.logger.info("quality 2 " + quality2);
				fail("error");
			}
			if (j % (totalTests / 10) == 0)
				TestHandQuality.logger.info(Integer.valueOf(j));
		}

		Set<Entry<String, Integer>> entrySet = rankMap.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			TestHandQuality.logger.info(entry.getKey() + " % " + 100
					* entry.getValue().intValue() / totalTests);
		}
	}
}
