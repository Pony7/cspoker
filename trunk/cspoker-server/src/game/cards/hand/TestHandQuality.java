package game.cards.hand;

import game.cards.CardImpl;
import game.cards.Rank;
import game.cards.Suit;
import junit.framework.TestCase;

public class TestHandQuality extends TestCase {

	protected Hand hand1=new Hand();
	protected Hand hand2=new Hand();
	public void testHighCardHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.NINE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand1).equals(HandType.HIGH_CARD));
		System.out.println(HandEvaluator.getHandQuality(hand1));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.NINE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand2).equals(HandType.HIGH_CARD));
		System.out.println(HandEvaluator.getHandQuality(hand2));
		
	}
}
