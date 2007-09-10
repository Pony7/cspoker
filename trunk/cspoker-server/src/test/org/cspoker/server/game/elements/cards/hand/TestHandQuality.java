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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.game.elements.cards.CardImpl;
import org.cspoker.common.game.elements.cards.cardElements.Rank;
import org.cspoker.common.game.elements.cards.cardElements.Suit;
import org.cspoker.server.game.elements.cards.deck.randomGenerator.RandomUtils;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandEvaluator;
/**
 * A test class for the quality of a hand
 * @author Cedric
 *
 */
public class TestHandQuality extends TestCase {
	private static Logger logger = Logger.getLogger(TestHandQuality.class);

	protected Hand hand1=new Hand();
	protected Hand hand2=new Hand();
	public void testRandomCard(){
		for(int j=0;j<1000;j++){
			TestHandQuality.logger.info("random card " + RandomUtils.getRandomCard().toString());
		}
	}
	public void testHighCardHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.NINE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.THREE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.HIGH_CARD));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand1)));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.NINE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.HIGH_CARD));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand2)));
		
	}
	public void testPairHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SEVEN));
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.PAIR));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand1)));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.PAIR));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand2)));
	}
	public void testDoublePairHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.SEVEN));
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.TWO_PAIR));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand1)));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.TWO_PAIR));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand2)));
	}
	public void testThreeOfAKindHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.THREE_OF_A_KIND));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand1)));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.THREE_OF_A_KIND));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand2)));
	}
	public void testFourOfAKindHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.FOUR_OF_A_KIND));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand1)));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FOUR_OF_A_KIND));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand2)));
	}
	public void testFlushHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.THREE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.FLUSH));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand1)));
		
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.ACE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SEVEN));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FLUSH));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand2)));
	}
	public void testStraightHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.THREE));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.DEUCE));
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.STRAIGHT));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand1)));
		
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.NINE));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.SEVEN));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.EIGHT));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.STRAIGHT));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand2)));
	}
	public void testFullHouseHand(){
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FIVE));
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FIVE));
		assertTrue(HandEvaluator.getShortDescription(this.hand1).equals(HandType.FULL_HOUSE));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand1)));
		
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.SIX));
		hand2.addCard(new CardImpl(Suit.DIAMONDS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.HEARTS,Rank.KING));
		hand2.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		hand2.addCard(new CardImpl(Suit.CLUBS,Rank.SIX));
		assertTrue(HandEvaluator.getShortDescription(this.hand2).equals(HandType.FULL_HOUSE));
		TestHandQuality.logger.info((HandEvaluator.getShortDescription(this.hand2)));
	}
	
	public void testHandQuality(){
		boolean qualityGreater,qualitySmaller,qualityEqual;
		int compare;
		int quality1, quality2;
		hand1.makeEmpty();
		hand2.makeEmpty();
		Map<String, Integer> rankMap = new HashMap<String, Integer>();
		double totalTests=10000.0;
		for(int j=0;j<totalTests;j++){
			hand1=RandomUtils.getRandomHand(5);
			hand2=RandomUtils.getRandomHand(5);
			compare=HandEvaluator.compareHands(hand1,hand2);
			qualityGreater=(compare==1);
			qualitySmaller=(compare==-1);
			qualityEqual=(compare==0);
			quality1 = HandEvaluator.getRank(this.hand1.getAsList()).intValue();
			quality2 = HandEvaluator.getRank(this.hand2.getAsList()).intValue();

		
			String rank = HandEvaluator.getShortDescription(this.hand1);
			if (!rankMap.containsKey(rank)) {
				rankMap.put(rank, Integer.valueOf(1));
			}
			rankMap.put(rank, Integer.valueOf(rankMap.get(rank).intValue() + 1));

			if (qualityGreater != (quality1 < quality2) || qualitySmaller != (quality1 > quality2) || qualityEqual != (quality1 == quality2)) {
				TestHandQuality.logger.info("compareTo " + HandEvaluator.compareHands(this.hand1, this.hand2));
				TestHandQuality.logger.info(this.hand1.toString());
				TestHandQuality.logger.info(HandEvaluator.getShortDescription(this.hand1));
				TestHandQuality.logger.info("quality 1 " + quality1);
				TestHandQuality.logger.info(this.hand2.toString());
				TestHandQuality.logger.info(HandEvaluator.getShortDescription(this.hand2));
				TestHandQuality.logger.info("quality 2 " + quality2);
				fail("error");
			}
			hand1.makeEmpty();
			hand2.makeEmpty();
			if(j%(totalTests/10)==0)
				TestHandQuality.logger.info(j);
		}
		
		Set<Entry<String, Integer>> entrySet = rankMap.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			TestHandQuality.logger.info(entry.getKey() + " % " + 100 * entry.getValue().intValue() / totalTests);	
		}
	}
}
