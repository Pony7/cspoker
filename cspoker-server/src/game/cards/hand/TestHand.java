package game.cards.hand;

import game.cards.Card;
import game.cards.CardImpl;
import game.cards.Rank;
import game.cards.Suit;
import junit.framework.TestCase;

public class TestHand extends TestCase {

	protected Hand hand1;
	public void test1(){
		hand1=new Hand();
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		assertTrue(hand1.getNBCards()==1);
		assertTrue(hand1.getIndexOf(new CardImpl(Suit.HEARTS,Rank.THREE))==0);
		
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		assertTrue(hand1.getNBCards()==2);
		assertTrue(hand1.getIndexOf(new CardImpl(Suit.HEARTS,Rank.THREE))==0);
		
		assertTrue(hand1.getCard(0).equals(new CardImpl(Suit.HEARTS,Rank.THREE)));
		assertTrue(hand1.getCard(1).equals(new CardImpl(Suit.SPADES,Rank.FOUR)));
		
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.EIGHT));
		assertTrue(hand1.getNBCards()==3);
		
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		assertTrue(hand1.getNBCards()==4);
		
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		assertTrue(hand1.getNBCards()==5);
		assertTrue(hand1.getIndexOf(new CardImpl(Suit.HEARTS,Rank.THREE))==0);
		assertTrue(hand1.getIndexOf(new CardImpl(Suit.HEARTS,Rank.ACE))==4);
		
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		assertTrue(hand1.getNBCards()==6);
		
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.QUEEN));
		assertTrue(hand1.getNBCards()==7);
		
		try{
			hand1.addCard(new CardImpl(Suit.CLUBS,Rank.QUEEN));
			assert(false);
		}catch(IllegalArgumentException e){
			assertTrue(hand1.getNBCards()==7);
		}
		
		assertTrue(hand1.contains(new CardImpl(Suit.CLUBS,Rank.QUEEN)));
		assertFalse(hand1.contains(new CardImpl(Suit.HEARTS,Rank.QUEEN)));
		assertTrue(hand1.isFull());
		
		hand1.removeCard(new CardImpl(Suit.CLUBS,Rank.QUEEN));
		assertFalse(hand1.contains(new CardImpl(Suit.CLUBS,Rank.QUEEN)));
		assertFalse(hand1.isFull());
		assertTrue(hand1.getNBCards()==6);
		
		try {
			hand1.removeCard(new CardImpl(Suit.CLUBS,Rank.QUEEN));
			assert(false);
		} catch (IllegalArgumentException e) {
			assertFalse(hand1.contains(new CardImpl(Suit.CLUBS,Rank.QUEEN)));
			assertFalse(hand1.isFull());
			assertTrue(hand1.getNBCards()==6);
		}
		
		assertTrue(new CardImpl(Suit.CLUBS,Rank.ACE).compareTo(new CardImpl(Suit.DIAMONDS,Rank.KING))==1);
		assertTrue(new CardImpl(Suit.HEARTS,Rank.DEUCE).compareTo(new CardImpl(Suit.SPADES,Rank.KING))==-1);
		assertTrue(new CardImpl(Suit.DIAMONDS,Rank.DEUCE).compareTo(new CardImpl(Suit.CLUBS,Rank.DEUCE))==0);
		assertFalse(new CardImpl(Suit.DIAMONDS,Rank.DEUCE).equals(new CardImpl(Suit.CLUBS,Rank.DEUCE)));
		assertTrue(new CardImpl(Suit.CLUBS,Rank.DEUCE).equals(new CardImpl(Suit.CLUBS,Rank.DEUCE)));
		
		System.out.println("voor sort "+"\n"+hand1.toString());
		hand1.sort();
		System.out.println("aantal kaarten "+hand1.getNBCards());
		System.out.println("na sort "+"\n"+hand1.toString());
		
	}
}
