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

package org.cspoker.common.handeval.stevebrecher;

import java.util.EnumSet;

import junit.framework.TestCase;

import org.cspoker.common.elements.cards.Card;

public class HandEvalTest extends TestCase {

	private int getRank(EnumSet<Card> cards) {
		return HandEval.getRank(cards);
	}

	public void testHighCardHands() {
		// test hand1 > hand2
		EnumSet<Card> hand1 = EnumSet.of(
				Card.ACE_SPADES, 
				Card.TWO_DIAMONDS, 
				Card.THREE_HEARTS, 
				Card.FOUR_SPADES, 
				Card.EIGHT_SPADES);
		EnumSet<Card> hand2 = EnumSet.of(
				Card.KING_SPADES, 
				Card.TWO_HEARTS,
				Card.THREE_CLUBS,
				Card.FOUR_SPADES,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)>getRank(hand2));

		// test hand2>hand1
		hand2 = EnumSet.of(
				Card.ACE_SPADES,
				Card.KING_DIAMONDS,
				Card.THREE_HEARTS,
				Card.FOUR_SPADES,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)<getRank(hand2));

		// test hand2==hand1
		hand2 = EnumSet.of(
				Card.ACE_SPADES,
				Card.TWO_DIAMONDS,
				Card.THREE_HEARTS,
				Card.FOUR_SPADES,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)==getRank(hand2));
	}

	public void testPairHands() {
		// test hand1>hand2
		EnumSet<Card> hand1 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FOUR_DIAMONDS,
				Card.ACE_HEARTS,
				Card.FIVE_SPADES,
				Card.EIGHT_SPADES);
		EnumSet<Card> hand2 = EnumSet.of(
				Card.THREE_SPADES,
				Card.THREE_HEARTS,
				Card.FIVE_CLUBS,
				Card.FOUR_SPADES,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)>getRank(hand2));

		hand2 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FOUR_HEARTS,
				Card.KING_CLUBS,
				Card.FIVE_SPADES,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)>getRank(hand2));

		// test hand2>hand1
		hand2 = EnumSet.of(
				Card.FIVE_SPADES,
				Card.FIVE_HEARTS,
				Card.THREE_CLUBS,
				Card.FOUR_SPADES,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)<getRank(hand2));

		// test hand2==hand1
		hand2 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FOUR_DIAMONDS,
				Card.ACE_HEARTS,
				Card.FIVE_SPADES,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)==getRank(hand2));
	}

	public void testDoublePairHands() {
		EnumSet<Card> hand1 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FOUR_DIAMONDS,
				Card.FIVE_HEARTS,
				Card.FIVE_SPADES,
				Card.EIGHT_SPADES);

		// hand1>hand 2
		EnumSet<Card> hand2 = EnumSet.of(
				Card.TWO_SPADES,
				Card.TWO_DIAMONDS,
				Card.FIVE_HEARTS,
				Card.FIVE_SPADES,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)>getRank(hand2));

		// hand1<hand 2
		hand2 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FOUR_DIAMONDS,
				Card.SIX_HEARTS,
				Card.SIX_SPADES,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)<getRank(hand2));

		hand2 = hand1;
		assertTrue(getRank(hand1)==getRank(hand2));
	}

	public void testThreeOfAKindHands() {
		EnumSet<Card> hand1 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FOUR_DIAMONDS,
				Card.FOUR_HEARTS,
				Card.FIVE_SPADES,
				Card.EIGHT_SPADES);

		// hand1>hand 2
		EnumSet<Card> hand2 = EnumSet.of(
				Card.TWO_SPADES,
				Card.TWO_DIAMONDS,
				Card.TWO_HEARTS,
				Card.FIVE_SPADES,
				Card.EIGHT_SPADES);

		assertTrue(getRank(hand1)>getRank(hand2));

		// hand1<hand 2
		hand2 = EnumSet.of(
				Card.SIX_SPADES,
				Card.FOUR_DIAMONDS,
				Card.SIX_HEARTS,
				Card.SIX_CLUBS,
				Card.EIGHT_SPADES);

		assertTrue(getRank(hand1)<getRank(hand2));

		hand2 = hand1;
		assertTrue(getRank(hand1)==getRank(hand2));
	}

	public void testFourOfAKindHands() {
		EnumSet<Card> hand1 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FOUR_DIAMONDS,
				Card.FOUR_HEARTS,
				Card.FOUR_CLUBS,
				Card.EIGHT_SPADES);

		// hand1>hand 2
		EnumSet<Card> hand2 = EnumSet.of(
				Card.TWO_SPADES,
				Card.TWO_DIAMONDS,
				Card.TWO_HEARTS,
				Card.TWO_CLUBS,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)>getRank(hand2));

		// hand1<hand 2
		hand2 = EnumSet.of(
				Card.SIX_SPADES,
				Card.SIX_DIAMONDS,
				Card.SIX_HEARTS,
				Card.SIX_CLUBS,
				Card.EIGHT_SPADES);
		assertTrue(getRank(hand1)<getRank(hand2));

		hand2 = hand1;
		assertTrue(getRank(hand1)==getRank(hand2));
	}

	public void testFullHouseHands() {
		EnumSet<Card> hand1 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FOUR_DIAMONDS,
				Card.FOUR_HEARTS,
				Card.EIGHT_CLUBS,
				Card.EIGHT_SPADES);

		// hand1>hand 2
		EnumSet<Card> hand2 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FOUR_DIAMONDS,
				Card.FOUR_HEARTS,
				Card.TWO_CLUBS,
				Card.TWO_SPADES);
		assertTrue(getRank(hand1)>getRank(hand2));

		// hand1<hand 2
		hand2 = EnumSet.of(
				Card.TWO_SPADES,
				Card.SIX_DIAMONDS,
				Card.SIX_HEARTS,
				Card.TWO_CLUBS,
				Card.SIX_SPADES);
		assertTrue(getRank(hand1)<getRank(hand2));

		hand2 = hand1;
		assertTrue(getRank(hand1)==getRank(hand2));
	}

	public void testFlushHands() {
		EnumSet<Card> hand1 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.TWO_SPADES,
				Card.NINE_SPADES,
				Card.EIGHT_SPADES,
				Card.SEVEN_SPADES);

		// hand1>hand 2
		EnumSet<Card> hand2 = EnumSet.of(
				Card.TWO_CLUBS,
				Card.FOUR_CLUBS,
				Card.FIVE_CLUBS,
				Card.SIX_CLUBS,
				Card.SEVEN_CLUBS);
		assertTrue(getRank(hand1)>getRank(hand2));

		//hand1>hand2
		hand2 = EnumSet.of(
				Card.TWO_DIAMONDS,
				Card.TWO_HEARTS,
				Card.FIVE_SPADES,
				Card.SIX_SPADES,
				Card.SEVEN_SPADES);
		assertTrue(getRank(hand1)>getRank(hand2));
		
		// hand1<hand 2
		hand2 = EnumSet.of(
				Card.ACE_HEARTS,
				Card.SIX_HEARTS,
				Card.NINE_HEARTS,
				Card.TWO_HEARTS,
				Card.THREE_HEARTS);
		assertTrue(getRank(hand1)<getRank(hand2));

		hand2 = hand1;
		assertTrue(getRank(hand1)==getRank(hand2));
	}

	public void testStraightHands() {
		EnumSet<Card> hand1 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FIVE_DIAMONDS,
				Card.SEVEN_HEARTS,
				Card.SIX_CLUBS,
				Card.EIGHT_SPADES);

		// hand1>hand 2
		EnumSet<Card> hand2 = EnumSet.of(
				Card.ACE_SPADES,
				Card.FIVE_DIAMONDS,
				Card.TWO_HEARTS,
				Card.THREE_CLUBS,
				Card.FOUR_SPADES);
		assertTrue(getRank(hand1)>getRank(hand2));

		// hand1<hand 2
		hand2 = EnumSet.of(
				Card.ACE_SPADES,
				Card.QUEEN_DIAMONDS,
				Card.KING_HEARTS,
				Card.JACK_CLUBS,
				Card.TEN_SPADES);
		assertTrue(getRank(hand1)<getRank(hand2));

		hand2 = hand1;
		assertTrue(getRank(hand1)==getRank(hand2));
	}

	public void testStraightFlushHands() {
		EnumSet<Card> hand1 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FIVE_SPADES,
				Card.SEVEN_SPADES,
				Card.SIX_SPADES,
				Card.EIGHT_SPADES);

		// hand1>hand 2
		EnumSet<Card> hand2 = EnumSet.of(
				Card.ACE_HEARTS,
				Card.FIVE_HEARTS,
				Card.TWO_HEARTS,
				Card.THREE_HEARTS,
				Card.FOUR_HEARTS);
		assertTrue(getRank(hand1)>getRank(hand2));

		// hand1<hand 2
		hand2 = EnumSet.of(
				Card.ACE_CLUBS,
				Card.QUEEN_CLUBS,
				Card.KING_CLUBS,
				Card.JACK_CLUBS,
				Card.TEN_CLUBS);
		assertTrue(getRank(hand1)<getRank(hand2));

		hand2 = hand1;
		assertTrue(getRank(hand1)==getRank(hand2));
	}

	public void testSixCardHand() {
		EnumSet<Card> hand1 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FIVE_SPADES,
				Card.SEVEN_SPADES,
				Card.SIX_SPADES,
				Card.EIGHT_SPADES,
				Card.ACE_CLUBS);
		EnumSet<Card> hand2 = EnumSet.of(
				Card.FOUR_HEARTS,
				Card.FOUR_SPADES,
				Card.FOUR_CLUBS,
				Card.SIX_HEARTS,
				Card.EIGHT_HEARTS,
				Card.ACE_HEARTS);
		// test hand1>hand 2
		assertTrue(getRank(hand1)>getRank(hand2));

		// test hand1<hand 2
		hand1 = EnumSet.of(
				Card.TWO_CLUBS,
				Card.TWO_SPADES,
				Card.SEVEN_HEARTS,
				Card.SEVEN_DIAMONDS,
				Card.EIGHT_SPADES,
				Card.ACE_SPADES);
		assertTrue(getRank(hand1)<getRank(hand2));

		// test hand1==hand2
		hand1 = EnumSet.of(
				Card.FOUR_HEARTS,
				Card.FOUR_SPADES,
				Card.FOUR_CLUBS,
				Card.SEVEN_SPADES,
				Card.EIGHT_SPADES,
				Card.ACE_SPADES);
		assertTrue(getRank(hand1)==getRank(hand2));
	}

	public void testSevenCardHand() {
		EnumSet<Card> hand1 = EnumSet.of(
				Card.FOUR_HEARTS,
				Card.FIVE_SPADES,
				Card.SEVEN_SPADES,
				Card.SIX_SPADES,
				Card.EIGHT_SPADES,
				Card.ACE_DIAMONDS,
				Card.NINE_SPADES);
		EnumSet<Card> hand2 = EnumSet.of(
				Card.FOUR_HEARTS,
				Card.FOUR_SPADES,
				Card.FOUR_CLUBS,
				Card.SIX_SPADES,
				Card.EIGHT_SPADES,
				Card.ACE_SPADES,
				Card.KING_CLUBS);

		// test hand1>hand 2
		assertTrue(getRank(hand1)>getRank(hand2));

		// test hand1<hand 2
		hand1 = EnumSet.of(
				Card.TWO_CLUBS,
				Card.TWO_SPADES,
				Card.SEVEN_HEARTS,
				Card.SEVEN_DIAMONDS,
				Card.EIGHT_SPADES,
				Card.ACE_SPADES,
				Card.ACE_HEARTS);
		assertTrue(getRank(hand1)<getRank(hand2));

		// test hand1==hand2
		hand1 = EnumSet.of(
				Card.FOUR_HEARTS,
				Card.FOUR_SPADES,
				Card.FOUR_CLUBS,
				Card.SEVEN_SPADES,
				Card.EIGHT_SPADES,
				Card.ACE_SPADES,
				Card.KING_DIAMONDS);
		assertTrue(getRank(hand1)==getRank(hand2));
		
		hand1 = EnumSet.of(
				Card.FOUR_SPADES,
				Card.FIVE_SPADES,
				Card.SEVEN_SPADES,
				Card.SIX_SPADES,
				Card.EIGHT_SPADES,
				Card.ACE_SPADES,
				Card.ACE_CLUBS);
		hand2 = EnumSet.of(
				Card.FOUR_HEARTS,
				Card.FOUR_SPADES,
				Card.FOUR_CLUBS,
				Card.SIX_HEARTS,
				Card.EIGHT_HEARTS,
				Card.ACE_HEARTS,
				Card.TEN_DIAMONDS);
		// test hand1>hand 2
		assertTrue(getRank(hand1)>getRank(hand2));
	}

	public void testAceHighStraightVSKingHighStraight() {
		EnumSet<Card> hand1 = EnumSet.of(
				Card.KING_HEARTS,
				Card.QUEEN_SPADES,
				Card.JACK_CLUBS,
				Card.TEN_SPADES,
				Card.NINE_SPADES,
				Card.TWO_SPADES,
				Card.THREE_DIAMONDS);
		EnumSet<Card> hand2 = EnumSet.of(
				Card.KING_HEARTS,
				Card.QUEEN_SPADES,
				Card.JACK_CLUBS,
				Card.TEN_SPADES,
				Card.NINE_SPADES,
				Card.ACE_SPADES,
				Card.THREE_DIAMONDS);
		assertTrue(getRank(hand1)<getRank(hand2));
	}

}
