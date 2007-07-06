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
	public void testPairHand() {
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandTypeCalculator.checkForPair(hand1));
		
		Card[] pairCards=HandTypeCalculator.getPairCards(hand1);
		assertTrue(pairCards.length==2);
		assertTrue(pairCards[0].equalRank(pairCards[1]));
		assertTrue(pairCards[0].getRank().getValue()==4);
		
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertFalse(HandTypeCalculator.checkForPair(hand2));
		try {
			HandTypeCalculator.getPairCards(hand2);
			assert(false);
		} catch (IllegalArgumentException e) {
		}
	}
	public void testDoublePairHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandTypeCalculator.checkForDoublePair(hand1));
		
		Card[] pairCards=HandTypeCalculator.getDoublePairCards(hand1);
		assertTrue(pairCards.length==4);
		assertTrue(pairCards[0].equalRank(pairCards[1]));
		assertTrue(pairCards[2].equalRank(pairCards[3]));
		assertTrue(pairCards[0].getRank().getValue()==5);
		assertTrue(pairCards[2].getRank().getValue()==4);
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertFalse(HandTypeCalculator.checkForDoublePair(hand2));
		try {
			HandTypeCalculator.getDoublePairCards(hand2);
			assert(false);
		} catch (IllegalArgumentException e) {
		}
	}
}
