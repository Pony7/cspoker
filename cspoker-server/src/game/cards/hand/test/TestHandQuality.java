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
package game.cards.hand.test;

import game.cards.Card;
import game.cards.CardImpl;
import game.cards.Rank;
import game.cards.Suit;
import game.cards.hand.Hand;
import game.cards.hand.HandEvaluator;
import game.cards.hand.HandType;
import game.cards.hand.HandTypeCalculator;
import game.deck.randomGenerator.RandomOrgSeededRandomGenerator;
import junit.framework.TestCase;
/**
 * A test class for the quality of a hand
 * @author Cedric
 *
 */
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
		boolean qualityGreater,qualitySmaller,qualityEqual;
		int compare;
		double quality1,quality2;
		hand1.makeEmpty();
		hand2.makeEmpty();
		double[] numbers=new double[9];
		double totalTests=10000.0;
		for(int j=0;j<totalTests;j++){
			hand1=rng.getRandomHand(5);
			hand2=rng.getRandomHand(5);
			compare=HandEvaluator.compareHands(hand1,hand2);
			qualityGreater=(compare==1);
			qualitySmaller=(compare==-1);
			qualityEqual=(compare==0);
			quality1=HandEvaluator.getHandQuality(hand1);
			quality2=HandEvaluator.getHandQuality(hand2);

		
			int rank1= HandTypeCalculator.calculateHandType(hand1).getRanking();
			numbers[rank1]++;
			
			assertTrue(qualityGreater==(quality1>quality2));
			assertTrue(qualitySmaller==(quality1<quality2));
			assertTrue(qualityEqual==(quality1==quality2));
			hand1.makeEmpty();
			hand2.makeEmpty();
			if(j%(totalTests/10)==0)
				System.out.println(j);
		}
		System.out.println("high cards % "+numbers[0]/totalTests);
		System.out.println("pair % "+numbers[1]/totalTests);
		System.out.println("double pair % "+numbers[2]/totalTests);
		System.out.println("three of a kind % "+numbers[3]/totalTests);
		System.out.println("straight% "+numbers[4]/totalTests);
		System.out.println("flush % "+numbers[5]/totalTests);
		System.out.println("full house % "+numbers[6]/totalTests);
		System.out.println("four of a kind % "+numbers[7]/totalTests);
		System.out.println("straight flush % "+numbers[8]/totalTests);
		
	}
	
}
