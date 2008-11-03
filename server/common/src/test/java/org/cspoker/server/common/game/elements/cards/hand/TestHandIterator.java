/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.server.common.game.elements.cards.hand;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.cspoker.common.elements.hand.Hand;

public class TestHandIterator extends TestCase {
	private static Logger logger = Logger.getLogger(TestHandIterator.class);

	private static TestExactCard testExactCard = new TestExactCard();

	protected Hand hand1;

	@Override
	protected void setUp() throws Exception {
		hand1 = new Hand();

		hand1.add(testExactCard.getExactCard(Rank.THREE, Suit.HEARTS));
		hand1.add(testExactCard.getExactCard(Rank.FOUR, Suit.HEARTS));
		hand1.add(testExactCard.getExactCard(Rank.FIVE, Suit.HEARTS));
		hand1.add(testExactCard.getExactCard(Rank.SIX, Suit.HEARTS));
		hand1.add(testExactCard.getExactCard(Rank.SEVEN, Suit.HEARTS));

		hand1.add(testExactCard.getExactCard(Rank.THREE, Suit.SPADES));
		hand1.add(testExactCard.getExactCard(Rank.ACE, Suit.HEARTS));
	}

	public void testHandIterator1() {
		Iterator<Card> iterator = hand1.iterator();
		while (iterator.hasNext()) {
			TestHandIterator.logger.info(iterator.next().toString());
		}
		try {
			iterator.next();
			fail("Exception Expected.");
		} catch (NoSuchElementException e) {
		}
	}

	public void testHandIterator2() {
		Iterator<Card> iterator = hand1.getBestFive().iterator();
		while (iterator.hasNext()) {
			TestHandIterator.logger.info(iterator.next().toString());
		}
		try {
			iterator.next();
			fail("Exception Expected.");
		} catch (NoSuchElementException e) {
		}
	}
}
