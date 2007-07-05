package game.cards.hand;

import game.cards.Card;
import game.cards.CardImpl;
import game.cards.Rank;
import game.cards.Suit;
import junit.framework.TestCase;

public class TestHandEvaluator extends TestCase {

	protected Hand hand1=new Hand();
	protected Hand hand2=new Hand();
	
//	public void testCompareHighCardHands(){
//		//test hand1 > hand2
//		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
//		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.DEUCE));
//		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
//		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
//		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
//		
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.KING));
//		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.DEUCE));
//		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.THREE));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
//		
//		assertTrue(HandEvaluator.compareHighCardHands(hand1,hand2)==1);
//		
//		//test hand2>hand1
//		hand2.makeEmpty();
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
//		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
//		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
//		
//		assertTrue(HandEvaluator.compareHighCardHands(hand1,hand2)==-1);
//		//test hand2==hand1
//		hand2.makeEmpty();
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
//		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.DEUCE));
//		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
//		
//		assertTrue(HandEvaluator.compareHighCardHands(hand1,hand2)==0);
//	}
//	public void testComparePairHands(){
//		//test hand1>hand2
//		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
//		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
//		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
//		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
//		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
//		
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.THREE));
//		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
//		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.FIVE));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
//		
//		assertTrue(HandEvaluator.comparePairHands(hand1,hand2)==1);
//		
//		hand2.makeEmpty();
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
//		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
//		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.KING));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
//		assertTrue(HandEvaluator.comparePairHands(hand1,hand2)==1);
//		
//		//test hand2>hand1
//		hand2.makeEmpty();
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
//		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
//		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.THREE));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
//		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
//		
//		assertTrue(HandEvaluator.comparePairHands(hand1,hand2)==-1);
//		
//		//test hand2==hand1
//		
//		hand2.makeEmpty();
//	}
	public void testGetPairHands() {
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		Card[] pairCards=HandTypeCalculator.getPairCards(hand1);
		assertTrue(pairCards.length==2);
		assertTrue(pairCards[0].equals(pairCards[1]));
		assertTrue(pairCards[0].getRank().getValue()==4);
	}
}
