package game.cards.hand;

import game.cards.Card;
import game.cards.CardImpl;
import game.cards.Rank;
import game.cards.Suit;
import game.deck.randomGenerator.RandomOrgSeededRandomGenerator;
import junit.framework.TestCase;

public class TestHandQuality extends TestCase {

	protected Hand hand1=new Hand();
	protected Hand hand2=new Hand();
	public void testCombinations(){
		System.out.println(HandEvaluator.getNumberCombinations(1));
		System.out.println(HandEvaluator.getNumberCombinations(2));
		System.out.println(HandEvaluator.getNumberCombinations(3));
		System.out.println(HandEvaluator.getNumberCombinations(4));
		System.out.println(HandEvaluator.getNumberCombinations(5));
		System.out.println(HandEvaluator.getNumberCombinations(6));
		System.out.println(HandEvaluator.getNumberCombinations(7));
		System.out.println(HandEvaluator.getNumberCombinations(8));
		System.out.println(HandEvaluator.getNumberCombinations(9));
		
	}
	public void testRandomCard(){
		RandomOrgSeededRandomGenerator rng=new RandomOrgSeededRandomGenerator();
		for(int j=0;j<1000;j++){
			System.out.println("random card "+rng.getRandomCard().toString()+"\n");
		}
	}
	public void testHighCardHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.NINE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.THREE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand1).equals(HandType.HIGH_CARD));
		System.out.println(HandEvaluator.getHandQuality(hand1));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.NINE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand2).equals(HandType.HIGH_CARD));
		System.out.println(HandEvaluator.getHandQuality(hand2));
		
	}
	public void testPairHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SEVEN));
		assertTrue(HandTypeCalculator.calculateHandType(hand1).equals(HandType.PAIR));
		System.out.println(HandEvaluator.getHandQuality(hand1));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand2).equals(HandType.PAIR));
		System.out.println(HandEvaluator.getHandQuality(hand2));
	}
	public void testDoublePairHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SEVEN));
		assertTrue(HandTypeCalculator.calculateHandType(hand1).equals(HandType.DOUBLE_PAIR));
		System.out.println(HandEvaluator.getHandQuality(hand1));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand2).equals(HandType.DOUBLE_PAIR));
		System.out.println(HandEvaluator.getHandQuality(hand2));
	}
	public void testThreeOfAKindHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand1).equals(HandType.THREE_OF_A_KIND));
		System.out.println(HandEvaluator.getHandQuality(hand1));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand2).equals(HandType.THREE_OF_A_KIND));
		System.out.println(HandEvaluator.getHandQuality(hand2));
	}
	public void testFourOfAKindHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		assertTrue(HandTypeCalculator.calculateHandType(hand1).equals(HandType.FOUR_OF_A_KIND));
		System.out.println(HandEvaluator.getHandQuality(hand1));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand2).equals(HandType.FOUR_OF_A_KIND));
		System.out.println(HandEvaluator.getHandQuality(hand2));
	}
	public void testFlushHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.THREE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		assertTrue(HandTypeCalculator.calculateHandType(hand1).equals(HandType.FLUSH));
		System.out.println(HandEvaluator.getHandQuality(hand1));
		
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SEVEN));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand2).equals(HandType.FLUSH));
		System.out.println(HandEvaluator.getHandQuality(hand2));
	}
	public void testStraightHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.THREE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		assertTrue(HandTypeCalculator.calculateHandType(hand1).equals(HandType.STRAIGHT));
		System.out.println(HandEvaluator.getHandQuality(hand1));
		
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.NINE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SEVEN));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.EIGHT));
		assertTrue(HandTypeCalculator.calculateHandType(hand2).equals(HandType.STRAIGHT));
		System.out.println(HandEvaluator.getHandQuality(hand2));
	}
	public void testFullHouseHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		assertTrue(HandTypeCalculator.calculateHandType(hand1).equals(HandType.FULL_HOUSE));
		System.out.println(HandEvaluator.getHandQuality(hand1));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.SIX));
		assertTrue(HandTypeCalculator.calculateHandType(hand2).equals(HandType.FULL_HOUSE));
		System.out.println(HandEvaluator.getHandQuality(hand2));
	}
	
	public void testHandQuality(){
		RandomOrgSeededRandomGenerator rng=new RandomOrgSeededRandomGenerator();
		boolean qualityGreater;
		double quality1,quality2;
		hand1.makeEmpty();
		hand2.makeEmpty();
		Hand best1;
		Hand best2;
		for(int j=0;j<1000000;j++){
			hand1=rng.getRandomHand(7);
			hand2=rng.getRandomHand(7);
			
			qualityGreater=(HandEvaluator.compareHands(hand1,hand2)==1);
			quality1=HandEvaluator.getHandQuality(hand1);
			quality2=HandEvaluator.getHandQuality(hand2);
			best1=HandEvaluator.getBestHand(hand1);
			best2=HandEvaluator.getBestHand(hand2);
			best1.sort();
			best2.sort();
			
			//System.out.println("hand1 "+best1.toString()+" type "+HandTypeCalculator.calculateHandType(hand1).toString()+" quality "+quality1);
			//System.out.println("hand2 "+best2.toString()+" type "+HandTypeCalculator.calculateHandType(hand2).toString()+" quality "+quality1);
			assertTrue(qualityGreater==(quality1>quality2));
			
			hand1.makeEmpty();
			hand2.makeEmpty();
		}
	}
}
