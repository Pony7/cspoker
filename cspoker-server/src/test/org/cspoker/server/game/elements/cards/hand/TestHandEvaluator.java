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
package org.cspoker.server.game.elements.cards.hand;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.game.elements.cards.CardImpl;
import org.cspoker.common.game.elements.cards.cardElements.Rank;
import org.cspoker.common.game.elements.cards.cardElements.Suit;
import org.cspoker.server.game.elements.cards.deck.Deck;

/**
 * A test class for all the methods in the HandEvaluator class
 * @author Cedric
 *
 */
public class TestHandEvaluator extends TestCase {
	private static Logger logger = Logger.getLogger(TestHandEvaluator.class); 

	protected Hand hand1=new Hand();
	protected Hand hand2=new Hand();
	
	public void testHighCardHands(){
		//test hand1 > hand2
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.DEUCE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.THREE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.HIGH_CARD));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.HIGH_CARD));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		
		//test hand2>hand1
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.HIGH_CARD));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		//test hand2==hand1
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.HIGH_CARD));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	public void testPairHands(){
		//test hand1>hand2
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.THREE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.PAIR));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.PAIR));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		
		
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.PAIR));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);

		//test hand2>hand1
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.THREE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.PAIR));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		
		//test hand2==hand1
		
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.PAIR));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	public void testDoublePairHands(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.TWO_PAIR));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertFalse(HandEvaluator.getShortDescription(this.hand2).equals(HandType.TWO_PAIR));
		
		//hand1 >hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.TWO_PAIR));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		
		
		//hand1 <hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.TWO_PAIR));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		
		//hand1 =hand 2
		hand2=new Hand(hand1);
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.TWO_PAIR));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	public void testThreeOfAKindHands(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.THREE_OF_A_KIND));
	
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertFalse(HandEvaluator.getShortDescription(this.hand2).equals(HandType.THREE_OF_A_KIND));
		
		
		//hand1 >hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.THREE_OF_A_KIND));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		
		
		//hand1 <hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.THREE_OF_A_KIND));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		
		//hand1 =hand 2
		hand2=new Hand(hand1);
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.THREE_OF_A_KIND));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	public void testFourOfAKindHands(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.FOUR_OF_A_KIND));
	
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertFalse(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FOUR_OF_A_KIND));
		
		
		//hand1 >hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FOUR_OF_A_KIND));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		
		
		//hand1 <hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FOUR_OF_A_KIND));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		
		//hand1 =hand 2
		hand2=new Hand(hand1);
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FOUR_OF_A_KIND));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	public void testFullHouseHands(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.FULL_HOUSE));
	
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertFalse(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FULL_HOUSE));
		
		
		//hand1 >hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FULL_HOUSE));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		
		
		//hand1 <hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FULL_HOUSE));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		
		//hand1 =hand 2
		hand2=new Hand(hand1);
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FULL_HOUSE));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	public void testFlushHands(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.NINE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SEVEN));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.FLUSH));
	
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertFalse(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FLUSH));
		
		
		//hand1 >hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.SEVEN));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FLUSH));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		
		
		//hand1 <hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.NINE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FLUSH));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		
		//hand1 =hand 2
		hand2=new Hand(hand1);
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FLUSH));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	public void testStraightHands(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.SEVEN));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.SIX));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.STRAIGHT));
	
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertFalse(HandEvaluator.getShortDescription(this.hand2).equals(HandType.STRAIGHT));
		
		
		//hand1 >hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.THREE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.STRAIGHT));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		
		
		//hand1 <hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.QUEEN));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.JACK));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.TEN));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.STRAIGHT));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		
		//hand1 =hand 2
		hand2=new Hand(hand1);
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.STRAIGHT));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	public void testStraightFlushHands(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SEVEN));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.STRAIGHT_FLUSH));
	
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SEVEN));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.EIGHT));
		
		assertFalse(HandEvaluator.getShortDescription(this.hand2).equals(HandType.STRAIGHT_FLUSH));
		
		
		//hand1 >hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.DEUCE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.STRAIGHT_FLUSH));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		
		
		//hand1 <hand 2
		hand2.makeEmpty();
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.QUEEN));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.JACK));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.TEN));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.STRAIGHT_FLUSH));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		
		//hand1 =hand 2
		hand2=new Hand(hand1);
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.STRAIGHT_FLUSH));
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	
	public void testSixCardHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SEVEN));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		
		assertTrue(HandEvaluator.getShortDescription(HandEvaluator.getBestFive(hand1)).equals(HandType.STRAIGHT_FLUSH));
	
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		
		assertTrue(HandEvaluator.getShortDescription(HandEvaluator.getBestFive(hand2)).equals(HandType.THREE_OF_A_KIND));
		
		//test hand1>hand 2
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		 
		//test hand1<hand 2
		
		hand1.makeEmpty();
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.DEUCE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.SEVEN));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.SEVEN));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		
		//test hand1==hand2
		hand1.makeEmpty();
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SEVEN));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	public void testSevenCardHand(){
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SEVEN));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.NINE));
		
		assertTrue(HandEvaluator.getShortDescription(HandEvaluator.getBestFive(hand1)).equals(HandType.STRAIGHT_FLUSH));
	
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.KING));
		
		assertTrue(HandEvaluator.getShortDescription(HandEvaluator.getBestFive(hand2)).equals(HandType.THREE_OF_A_KIND));
		
		//test hand1>hand 2
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==1);
		
		//test hand1<hand 2
		
		hand1.makeEmpty();
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.DEUCE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.SEVEN));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.SEVEN));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
		
		//test hand1==hand2
		hand1.makeEmpty();
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SEVEN));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==0);
	}
	public void testAceHighStraightVSKingHighStraight(){
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.QUEEN));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.JACK));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.TEN));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.NINE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.THREE));
		
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.QUEEN));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.JACK));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.TEN));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.NINE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.THREE));
		
		assertTrue(HandEvaluator.compareHands(hand1,hand2)==-1);
	}
	
	public void testOldEvaluatorPerformance() {
		Deck deck = new Deck();
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < 1000; i++) {
			deck.newDeal();
			hand1.makeEmpty();
			hand2.makeEmpty();
			for (int j = 0; j < 7; j++) {
				hand1.addCard(deck.drawCard());
				hand2.addCard(deck.drawCard());
			}

			HandEvaluator.compareHands(hand1, hand2);
		}

		long endTime = System.currentTimeMillis();
		TestHandEvaluator.logger.info("Elapsed: " + (endTime - startTime));
	}

}
