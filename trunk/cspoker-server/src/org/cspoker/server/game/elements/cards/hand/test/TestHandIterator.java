package org.cspoker.server.game.elements.cards.hand.test;

import java.util.Iterator;

import org.cspoker.server.game.elements.cards.CardImpl;
import org.cspoker.server.game.elements.cards.cardElements.Rank;
import org.cspoker.server.game.elements.cards.cardElements.Suit;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandEvaluator;

import junit.framework.TestCase;

public class TestHandIterator extends TestCase {

	protected Hand hand1;
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
		Iterator iterator=hand1.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next().toString());
		}
		try {
			iterator.next();
			assert(false);
		} catch (IndexOutOfBoundsException e) {
		}
	}
	public void testHandIterator2(){
		Iterator iterator=HandEvaluator.getBestHand(hand1).iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next().toString());
		}
		try {
			iterator.next();
			assert(false);
		} catch (IndexOutOfBoundsException e) {
		}
	}
}
