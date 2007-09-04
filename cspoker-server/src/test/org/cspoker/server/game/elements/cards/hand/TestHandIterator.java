package org.cspoker.server.game.elements.cards.hand;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.common.game.elements.cards.CardImpl;
import org.cspoker.common.game.elements.cards.cardElements.Rank;
import org.cspoker.common.game.elements.cards.cardElements.Suit;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandEvaluator;

public class TestHandIterator extends TestCase {
	private static Logger logger = Logger.getLogger(TestHandIterator.class);

	protected Hand hand1;
	@Override
	protected void setUp() throws Exception {
		hand1=new Hand();
		
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.SIX));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.SEVEN));
		
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.THREE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
	}
	
	public void testHandIterator1(){
		Iterator<Card> iterator=hand1.iterator();
		while(iterator.hasNext()){
			TestHandIterator.logger.info(iterator.next().toString());
		}
		try {
			iterator.next();
			assert(false);
		} catch (IndexOutOfBoundsException e) {
		}
	}
	public void testHandIterator2(){
		Iterator<Card> iterator=HandEvaluator.getBestHand(hand1).iterator();
		while(iterator.hasNext()){
			TestHandIterator.logger.info(iterator.next().toString());
		}
		try {
			iterator.next();
			assert(false);
		} catch (IndexOutOfBoundsException e) {
		}
	}
}
