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
import game.deck.Deck;

/**
 * A class for evaluating and comparing hands
 * @author Cedric
 *
 */
public class HandEvaluator {
	
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
	   public static Hand bestFiveOfSeven(Hand hand){ 
		   Hand five = new Hand();
		   Hand bestHand = new Hand();
		    int bestValue = -1;
		    for(int i = 0; i<hand.getNBCards()-1; i++){
		      for(int j = i+1; j<hand.getNBCards(); j++){
		        // build a 5 card hand skipping cards i and j
		        for(int m = 0; m<hand.getNBCards(); m++){
		          if(m != i && m!= j) 
		        	  five.addCard(hand.getCard(m)); 
		        }
		       
		        // keep it if it is the new winner
		        if(compareHands(five,bestHand)==1){
		          bestHand=new Hand(five);  
		        }
		      }
		    }
		    return bestHand;
		  }
	   /**
	    * Compares two hands against each other.
	    * 
	    * @param h1
	    *           The first hand
	    * @param h2
	    *           The second hand
	    * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	    */
	   public static int compareHands(Hand h1, Hand h2) {
	      Hand best1=HandEvaluator.bestFiveOfSeven(h1);
	      Hand best2=HandEvaluator.bestFiveOfSeven(h2);
		  best1.sort();
	      best2.sort();
	      int rank1= best1.getHandType().getRanking();
	      int rank2=best2.getHandType().getRanking();
	      
	      if(rank1>rank2)
	    	  return 1;
	      if(rank2>rank1)
	    	  return -1;
	      else{
	    	  switch (rank1) {
			case 0:
				//HIGH_CARD
				return compareHighCardHands(best1,best2);
			case 1:
				//PAIR
				return comparePairHands(best1,best2);
			case 2:
				//DOUBLE_PAIR
				return compareDoublePairHands(best1,best2);
			case 3:
				//THREE_OF_A_KIND
				return compareThreeOfAKindHands(best1,best2);
			case 4:
				//STRAIGHT
				return compareStraightHands(best1,best2);
			case 5:
				//FLUSH
				return compareFlushHands(best1,best2);
			case 6:
				//FULL_HOUSE
				return compareFullHouseHands(best1,best2);
			case 7:
				//FOUR_OF_A_KIND
				return compareFourOfAKindHands(best1,best2);
			case 8:
				//STRAIGHT_FLUSH
				return compareStraightFlushHands(best1,best2);
			}
	      }
	      //cannot occur
	      throw new IllegalStateException();
	   }
   private static int compareDoublePairHands(Hand h1, Hand h2) {
	   Card[] card1=new Card[4];
		Card[] card2=new Card[4];
		
		card1=HandTypeCalculator.getDeterminatingCards(h1);
		card2=HandTypeCalculator.getDeterminatingCards(h2);
		
		if(card1[0].getRank().getValue()>card2[0].getRank().getValue())
			   return 1;
		if(card1[0].getRank().getValue()<card2[0].getRank().getValue())
			   return -1;
		if(card1[2].getRank().getValue()>card2[2].getRank().getValue())
			   return 1;
		if(card1[2].getRank().getValue()<card2[2].getRank().getValue())
			   return -1;
		 Hand temp1=new Hand(h1);
		   Hand temp2=new Hand(h2);
		   
		   temp1.removeCard(card1[0]);
		   temp1.removeCard(card1[1]);
		   temp1.removeCard(card1[2]);
		   temp1.removeCard(card1[3]);
		   temp2.removeCard(card2[0]);
		   temp2.removeCard(card2[1]);
		   temp2.removeCard(card2[2]);
		   temp2.removeCard(card2[3]);
		   
		   return HandEvaluator.compareHighCardHands(temp1,temp2);
	}
private static int compareStraightFlushHands(Hand h1, Hand h2) {
	   	return compareStraightHands(h1,h2);
   }
	private static int compareFourOfAKindHands(Hand h1, Hand h2) {
		Card[] four1=new Card[4];
		   Card[] four2=new Card[4];
		   four1=HandTypeCalculator.getDeterminatingCards(h1);
		   four2=HandTypeCalculator.getDeterminatingCards(h2);
		   
		   if(four1[0].getRank().getValue()>four2[0].getRank().getValue())
			   return 1;
		   if(four1[0].getRank().getValue()<four2[0].getRank().getValue())
			   return -1;
		   
		   Hand temp1=new Hand(h1);
		   Hand temp2=new Hand(h2);
		   
		   temp1.removeCard(four1[0]);
		   temp1.removeCard(four1[1]);
		   temp1.removeCard(four1[2]);
		   temp1.removeCard(four1[3]);
		   temp2.removeCard(four2[0]);
		   temp2.removeCard(four2[1]);
		   temp2.removeCard(four2[2]);
		   temp2.removeCard(four2[3]);
		   
		   return HandEvaluator.compareHighCardHands(temp1,temp2);
	}
	private static int compareFullHouseHands(Hand h1, Hand h2) {
		Card[] card1=new Card[5];
		Card[] card2=new Card[5];
		
		card1=HandTypeCalculator.getDeterminatingCards(h1);
		card2=HandTypeCalculator.getDeterminatingCards(h2);
		
		if(card1[0].getRank().getValue()>card2[0].getRank().getValue())
			   return 1;
		if(card1[0].getRank().getValue()<card2[0].getRank().getValue())
			   return -1;
		if(card1[3].getRank().getValue()>card2[3].getRank().getValue())
			   return 1;
		if(card1[3].getRank().getValue()<card2[3].getRank().getValue())
			   return -1;
		return 0;
	}
	private static int compareFlushHands(Hand h1, Hand h2) {
		return compareHighCardHands(h1,h2);
	}
	private static int compareStraightHands(Hand h1, Hand h2) {
		h1.sort();
		h2.sort();
		
		if(h1.getCard(0).getRank().getValue()>h2.getCard(0).getRank().getValue())
			return 1;
		if(h1.getCard(0).getRank().getValue()<h2.getCard(0).getRank().getValue())
			return -1;
		return 0;
	}
	private static int compareThreeOfAKindHands(Hand h1, Hand h2) {
		Card[] three1=new Card[3];
		   Card[] three2=new Card[3];
		   three1=HandTypeCalculator.getDeterminatingCards(h1);
		   three2=HandTypeCalculator.getDeterminatingCards(h2);
		   
		   if(three1[0].getRank().getValue()>three2[0].getRank().getValue())
			   return 1;
		   if(three1[0].getRank().getValue()<three2[0].getRank().getValue())
			   return -1;
		   
		   Hand temp1=new Hand(h1);
		   Hand temp2=new Hand(h2);
		   
		   temp1.removeCard(three1[0]);
		   temp1.removeCard(three1[1]);
		   temp1.removeCard(three1[2]);
		   temp2.removeCard(three2[0]);
		   temp2.removeCard(three2[1]);
		   temp2.removeCard(three2[2]);
		   
		   return HandEvaluator.compareHighCardHands(temp1,temp2);
	}
	
	/**
	    * Compares two high cards hands
	    * @param h1
	    * 			the first high card hand
	    * @param h2
	    * 			the second high card hand
	    * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	    */
	   public static int compareHighCardHands(Hand h1,Hand h2){
		   if(h1.getNBCards()!=h2.getNBCards())
			   throw new IllegalArgumentException();
		   h1.sort();
		   h2.sort();
		   for(int j=0;j<5;j++){
			   if(h1.getCard(j).getRank().getValue()>h2.getCard(j).getRank().getValue())
				   return 1;
			   if(h1.getCard(j).getRank().getValue()<h2.getCard(j).getRank().getValue())
				   return -1;
		   }
		   return 0;
	   }
	
	   /**
	    * Compares two pair hands
	    * @param h1
	    * 			the first high card hand
	    * @param h2
	    * 			the second high card hand
	    * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	    * @pre	the given hands both consist of 5 cards
	    * 		| h1.getNBCards()==5 && h2.getNBCards()==5
	    */
	   public static int comparePairHands(Hand h1,Hand h2){
		   Card[] pair1=new Card[2];
		   Card[] pair2=new Card[2];
		   pair1=HandTypeCalculator.getDeterminatingCards(h1);
		   pair2=HandTypeCalculator.getDeterminatingCards(h2);
		   
		   if(pair1[0].getRank().getValue()>pair2[0].getRank().getValue())
			   return 1;
		   if(pair1[0].getRank().getValue()<pair2[0].getRank().getValue())
			   return -1;
		   
		   Hand temp1=new Hand(h1);
		   Hand temp2=new Hand(h2);
		   
		   temp1.removeCard(pair1[0]);
		   temp1.removeCard(pair1[1]);
		   temp2.removeCard(pair2[0]);
		   temp2.removeCard(pair2[1]);
		   
		   return HandEvaluator.compareHighCardHands(temp1,temp2);
	   }
}
