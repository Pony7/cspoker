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

import java.util.List;

import org.cspoker.common.game.elements.cards.Card;

/**
 * A class for evaluating and comparing hands
 * @author Cedric
 */
public class HandEvaluator {

	/**********************************************************
	 * Auxiliary Methods
	 **********************************************************/
	   /**
	    * Returns the best hand of 5 cards out of a given hand of 7 cards
	    * @param hand
	    * 		the given hand of 7 cards
	    * @throws	IllegalArgumentException
	    * 			if the given hand doesn't consist of 7 cards
	    * 			| hand.getNBCards()!=7
	    * @return	the resulting hand consists of 5 cards
	    * 			| result.getNBCards()==5
	    */
	   public static Hand getBestHand(Hand hand){
		   if(hand.getNBCards()<=5)
			   return hand;
		   Hand five = new Hand();
		   Hand bestHand = new Hand();
		   if(hand.getNBCards()==7){
			   for(int i = 0; i<hand.getNBCards(); i++){
				      for(int j = i+1; j<hand.getNBCards(); j++){
				        // build a 5 card hand skipping cards i and j
				        for(int m = 0; m<hand.getNBCards(); m++){
				          if((m != i) && (m!= j))
				        	  five.addCard(hand.getCard(m));
				        }
				        // keep it if it is the new winner
				        if(bestHand.getNBCards()==0){
				        	bestHand=new Hand(five);
				        }else{
				        	if(compareFiveCardHands(five,bestHand)==1){
						          bestHand=new Hand(five);
						        }
				        }
				        five.makeEmpty();
				      }

				    }
		   }
		   if(hand.getNBCards()==6){
			   for(int i = 0; i<hand.getNBCards(); i++){
				        for(int m = 0; m<hand.getNBCards(); m++){
				          if(m != i)
				        	  five.addCard(hand.getCard(m));
				        }
				        // keep it if it is the new winner
				        if(bestHand.getNBCards()==0){
				        	bestHand=new Hand(five);
				        }else{
				        	if(compareFiveCardHands(five,bestHand)==1){
						          bestHand=new Hand(five);
						        }
				        }
				        five.makeEmpty();
				      }
		   }

		    return bestHand;
		  }
	   /**********************************************************
		 * Methods for comparing any hands
		 **********************************************************/
	   /**
	    * Compares two hands of minimum 5 cards against each other
	    * @param h1
	    * 			the first hand
	    * @param h2
	    * 			the second hand
	    * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	    */
	   public static int compareHands(Hand h1,Hand h2){
		   Hand best1,best2;
		   if(h1.getNBCards()!=5){
			   best1=getBestHand(h1);
		   }else
			   best1=h1;
		   if(h2.getNBCards()!=5){
			   best2=getBestHand(h2);
		   }else
			   best2=h2;

		   return compareFiveCardHands(best1,best2);
	   }
	   
	   public static int compareHands2(Hand h1, Hand h2) {
		   List<Card> hand1 = h1.getAsList();
		   List<Card> hand2 = h2.getAsList();
		   
		   if (hand1.size()!=5) {
			   hand1 = NewHandEvaluator.getBestFive(hand1);
		   }
		   
		   if (hand2.size()!=5) {
			   hand2 = NewHandEvaluator.getBestFive(hand2);
		   }
		   
		   Integer rank1 = Integer.valueOf(NewHandEvaluator.getRank(hand1));
		   Integer rank2 = Integer.valueOf(NewHandEvaluator.getRank(hand2));
		   return rank2.compareTo(rank1);
	   }
	   
	   
	   /**
	    * Compares two hands of maximally 5 cards against each other.
	    * @param h1
	    *           The first hand
	    * @param h2
	    *           The second hand
	    * @throws	IllegalArgumentException
	    * 			if the given hands don't consist of maximally 5 cards
	    * 			| h1.getNBCards()>6 || h2.getNBCards()>6
	    * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	    */
	   public static int compareFiveCardHands(Hand h1, Hand h2) {
		   if ((h1.getNBCards() > 6) || (h2.getNBCards() > 6))
			throw new IllegalArgumentException();

		Hand temp1 = new Hand(h1);
		Hand temp2 = new Hand(h2);
		temp1.sort();
		temp2.sort();
		HandType type1 = HandTypeCalculator.calculateHandType(temp1);
		HandType type2 = HandTypeCalculator.calculateHandType(temp2);

		int rank1 = type1.getRanking();
		int rank2 = type2.getRanking();

		if (rank1 > rank2)
			return 1;
		if (rank2 > rank1)
			return -1;

		// rank1==rank2
		return type1.getEqualRankHandsComparator().compareHands(temp1, temp2);
	}

	   /**********************************************************
		 * Methods expressing the quality of any hand
		 **********************************************************/
	   /**
	    * Returns the quality of the given hand, expressed by a number:
	    * the higher the number the better the quality of the hand
	    * @param hand
	    * 			the given hand
	    * @return	If the quality of one hand is greater than the quality of another hand,
	    * 			the first hand is better than the second.
	    * 			| if(getHandQuality(first)>getHandQuality(second)
	    * 			|	then first.compareTo(second)==1
	    * @return	If the quality of one hand is smaller than the quality of another hand,
	    * 			the first hand is worse than the second.
	    * 			| if(getHandQuality(first)<getHandQuality(second)
	    * 			|	then first.compareTo(second)==-1
	    * @return	If the quality of one hand is equal to the quality of another hand,
	    * 			the first hand is as good as the second.
	    * 			| if(getHandQuality(first)==getHandQuality(second)
	    * 			|	then first.compareTo(second)==0
	    */
	   public static double getHandQuality(Hand hand){
		   Hand temp=HandEvaluator.getBestHand(hand);
		   HandType type=HandTypeCalculator.calculateHandType(temp);
		   int primaryRank=type.getRanking();
		   double secondaryRank=type.getHandQualityCalculator().calculateQualityWithinType(temp);
		  
		   return (primaryRank)+secondaryRank;
	   }
}
