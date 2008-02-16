package org.cspoker.server.common.game.elements.cards.hand;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.cspoker.server.common.game.elements.cards.hand.Hand;
import org.cspoker.server.common.game.utilities.TestExactCard;

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
	    assert (false);
	} catch (NoSuchElementException e) {
	}
    }

    public void testHandIterator2() {
	Iterator<Card> iterator = this.hand1.getBestFive().iterator();
	while (iterator.hasNext()) {
	    TestHandIterator.logger.info(iterator.next().toString());
	}
	try {
	    iterator.next();
	    assert (false);
	} catch (NoSuchElementException e) {
	}
    }
}
