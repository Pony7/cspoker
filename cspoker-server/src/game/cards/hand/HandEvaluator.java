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
import game.cards.Rank;

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
				          if(m != i && m!= j) 
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
	   /**
	    * Compares two hands of 5 cards against each other.
	    * 
	    * @param h1
	    *           The first hand
	    * @param h2
	    *           The second hand
	    * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	    */
	   public static int compareFiveCardHands(Hand h1, Hand h2) {
		   

		  Hand temp1=new Hand(h1);
		  Hand temp2=new Hand(h2);
		  temp1.sort();
	      temp2.sort();
	      int rank1= HandTypeCalculator.calculateHandType(temp1).getRanking();
	      int rank2=HandTypeCalculator.calculateHandType(temp2).getRanking();
	      
	      if(rank1>rank2)
	    	  return 1;
	      if(rank2>rank1)
	    	  return -1;
	      else{
	    	  switch (rank1) {
			case 0:
				//HIGH_CARD
				return compareHighCardHands(h1,h2);
			case 1:
				//PAIR
				return comparePairHands(h1,h2);
			case 2:
				//DOUBLE_PAIR
				return compareDoublePairHands(h1,h2);
			case 3:
				//THREE_OF_A_KIND
				return compareThreeOfAKindHands(h1,h2);
			case 4:
				//STRAIGHT
				return compareStraightHands(h1,h2);
			case 5:
				//FLUSH
				return compareFlushHands(h1,h2);
			case 6:
				//FULL_HOUSE
				return compareFullHouseHands(h1,h2);
			case 7:
				//FOUR_OF_A_KIND
				return compareFourOfAKindHands(h1,h2);
			case 8:
				//STRAIGHT_FLUSH
				return compareStraightFlushHands(h1,h2);
			}
	      }
	      //cannot occur
	      throw new IllegalStateException();
	   }
	   public static int compareDoublePairHands(Hand h1, Hand h2) {
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
   public static int compareStraightFlushHands(Hand h1, Hand h2) {
	   	return compareStraightHands(h1,h2);
   }
public static int compareFourOfAKindHands(Hand h1, Hand h2) {
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
	public static int compareFullHouseHands(Hand h1, Hand h2) {
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
	public static int compareFlushHands(Hand h1, Hand h2) {
		return compareHighCardHands(h1,h2);
	}
	public static int compareStraightHands(Hand h1, Hand h2) {
		Card[] cards1=HandTypeCalculator.getDeterminatingCards(h1);
		Card[] cards2=HandTypeCalculator.getDeterminatingCards(h2);
		
		if(cards1[0].getRank().getValue()>cards2[0].getRank().getValue())
			return 1;
		if(cards1[0].getRank().getValue()<cards2[0].getRank().getValue())
			return -1;
		return 0;
	}
	public static int compareThreeOfAKindHands(Hand h1, Hand h2) {
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
		  
		   Hand temp1=new Hand(h1);
		   Hand temp2=new Hand(h2);
		   temp1.sort();
		   temp2.sort();
		   for(int j=0;j<Math.min(h1.getNBCards(),5);j++){
			   if(temp1.getCard(j).getRank().getValue()>temp2.getCard(j).getRank().getValue())
				   return 1;
			   if(temp1.getCard(j).getRank().getValue()<temp2.getCard(j).getRank().getValue())
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
	   
	   /**
	    * Returns the quality of the given hand, expressed by a number:
	    * the higher the number the better the quality of the hand
	    * @param hand
	    * 			the given hand
	    */
	   public static double getHandQuality(Hand hand){
		   Hand temp=HandEvaluator.getBestHand(hand);
		   int primaryRank=HandTypeCalculator.calculateHandType(temp).getRanking();
		   double secondaryRank=0;
		   switch (primaryRank) {
			case 0:
				//HIGH_CARD
				secondaryRank= getHighCardHandQuality(temp);
				break;
			case 1:
				//PAIR
				secondaryRank= getPairHandQuality(temp);
				break;
			case 2:
				//DOUBLE_PAIR
				secondaryRank= getDoublePairHighCardHandQuality(temp);
				break;
			case 3:
				//THREE_OF_A_KIND
				secondaryRank= getThreeOfAKindHandQuality(temp);
				break;
			case 4:
				//STRAIGHT
				secondaryRank= getStraightHandQuality(temp);
				break;
			case 5:
				//FLUSH
				secondaryRank= getFlushHandQuality(temp);
				break;
			case 6:
				//FULL_HOUSE
				secondaryRank= getFullHouseHandQuality(temp);
				break;
			case 7:
				//FOUR_OF_A_KIND
				secondaryRank= getFourOfAKindHandQuality(temp);
				break;
			case 8:
				//STRAIGHT_FLUSH
				secondaryRank= getStraigthFlushHandQuality(temp);
				break;
			}
		   //cannot occur
		   return ((double)primaryRank)+secondaryRank;
	   }
	public static double getStraigthFlushHandQuality(Hand hand) {
		return getStraightHandQuality(hand);
	}
	public static double getFourOfAKindHandQuality(Hand hand) {
		Card[] fourCards=HandTypeCalculator.getDeterminatingCards(hand);
		double pairQuality=1/(getNumberCombinations(1)+1)*(fourCards[0].getRank().getValue()-1);
		Hand temp=new Hand(hand);
		temp.removeCard(fourCards[0]);
		temp.removeCard(fourCards[1]);
		temp.removeCard(fourCards[2]);
		temp.removeCard(fourCards[3]);
		
		double restHandQuality=1/(getNumberCombinations(1)+1)*getHighCardHandQuality(temp);
		
		return pairQuality+restHandQuality;
	}
	public static double getFullHouseHandQuality(Hand hand) {
		Card[] fullHouseCards=HandTypeCalculator.getDeterminatingCards(hand);
		
		double threeQuality=1/(getNumberCombinations(1)+1)*(fullHouseCards[0].getRank().getValue()-1);
		double pairQuality=1/(getNumberCombinations(1)+1)*1/(getNumberCombinations(1)+1)*(fullHouseCards[3].getRank().getValue()-1);
		
		return threeQuality+pairQuality;
	}
	public static double getFlushHandQuality(Hand hand) {
		return getHighCardHandQuality(hand);
	}
	public static double getStraightHandQuality(Hand hand) {
		Card[] straightCard=HandTypeCalculator.getDeterminatingCards(hand);
		return 0.1*((straightCard[0].getRank().getValue())-5.0);
	}
	public static double getThreeOfAKindHandQuality(Hand hand) {
		Card[] threeCards=HandTypeCalculator.getDeterminatingCards(hand);
		double pairQuality=1/(getNumberCombinations(1)+1)*(threeCards[0].getRank().getValue()-1);
		Hand temp=new Hand(hand);
		temp.removeCard(threeCards[0]);
		temp.removeCard(threeCards[1]);
		temp.removeCard(threeCards[2]);
		double restHandQuality=1/(getNumberCombinations(2)+1)*getHighCardHandQuality(temp);
		
		return pairQuality+restHandQuality;
	}
	public static double getDoublePairHighCardHandQuality(Hand hand) {
		Card[] doublePairCards=HandTypeCalculator.getDeterminatingCards(hand);

		double firstPairQuality=1/(getNumberCombinations(1)+1)*(doublePairCards[0].getRank().getValue()-1);
		double secondPairQuality=1/(getNumberCombinations(1)+1)*1/(getNumberCombinations(1)+2)*(doublePairCards[2].getRank().getValue()-1);
		double doublePairQuality=firstPairQuality+secondPairQuality;
		Hand temp=new Hand(hand);
		
		temp.removeCard(doublePairCards[0]);
		temp.removeCard(doublePairCards[1]);
		temp.removeCard(doublePairCards[2]);
		temp.removeCard(doublePairCards[3]);
		double restHandQuality=1/(Math.pow(getNumberCombinations(1)+1,5))*getHighCardHandQuality(temp);
		return doublePairQuality+restHandQuality;
	}
	public static double getPairHandQuality(Hand hand) {
		Card[] pairCards=HandTypeCalculator.getDeterminatingCards(hand);
		double pairQuality=1/(getNumberCombinations(1)+1)*(pairCards[0].getRank().getValue()-1);
		Hand temp=new Hand(hand);
		temp.removeCard(pairCards[0]);
		temp.removeCard(pairCards[1]);
		double restHandQuality=1/(getNumberCombinations(3)+1)*getHighCardHandQuality(temp);
		
		return pairQuality+restHandQuality;
	}
	public static double getHighCardHandQuality(Hand hand) {
		Hand temp=new Hand(hand);
		temp.sort();
		double quality=0;
		for(int j=0;j<temp.getNBCards();j++){
			quality+=1/(Math.pow(getNumberCombinations(1)+1,j+1))*(temp.getCard(j).getRank().getValue()-1);
		}
		return quality;
	}
	
	/**
	 * Returns the number of possible combinations of the given number of cards,
	 * the exact sequence doesn't matter
	 */
	public static double getNumberCombinations(int nBCards){
		return factorial(13)/(factorial(nBCards)*factorial(13-nBCards));
	}
	
    public static double factorial(int n) {
        if      (n <  0) throw new RuntimeException("Underflow error in factorial");
        else if (n > 20) throw new RuntimeException("Overflow error in factorial");
        else if (n == 0) return 1;
        else             return n * factorial(n-1);
    }
}
