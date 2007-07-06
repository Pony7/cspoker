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
import game.cards.Suit;

public class HandTypeCalculator {


	/**
	 * Calculates the type of the given hand
	 * @param hand
	 * 			the given hand
	 */
	public static HandType calculateHandType(Hand hand){
		Hand best;
		if(hand.getNBCards()!=5){
			best=HandEvaluator.bestFiveOfSeven(hand);
		}else{
			best=hand;
		}
		if(checkForPair(best)){
			if(checkForFourOfAKind(best))
				return HandType.FOUR_OF_A_KIND;
			if(checkForFullHouse(best))
				return HandType.FULL_HOUSE;
			if(checkForThreeOfAKind(best))
				return HandType.THREE_OF_A_KIND;
			if(checkForDoublePair(best))
				return HandType.DOUBLE_PAIR;
			return HandType.PAIR;
		}else{
			boolean flush=checkForFlush(best);
			boolean straight=checkForStraigth(best);
			
			if(flush && straight){
				return HandType.STRAIGHT_FLUSH;
			}else{
				if(flush)
					return HandType.FLUSH;
				if(straight)
					return HandType.STRAIGHT;
			}
		}
		return HandType.HIGH_CARD;
	}
	/**
	 * Checks if the given hand contains a pair of cards with equal rank
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForPair(Hand hand) {
		if(hand.getNBCards()<2)
			return false;
		boolean pairFound=false;
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				if(hand.getCard(j).equalRank(hand.getCard(i))){
					pairFound=true;
				}
			}
		}
		return pairFound;
	}
	/**
	 * Checks if the given hand contains 2 different pairs of cards of equal rank
	 * @param hand
	 * 			the given rank
	 */
	public static boolean checkForDoublePair(Hand hand) {
		if(hand.getNBCards()<4)
			return false;
		boolean doublePairFound=false;
		boolean singlePairFound=false;
		Card firstPairCard=null;
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				if(hand.getCard(j).equalRank(hand.getCard(i))){
					singlePairFound=true;
					firstPairCard=hand.getCard(j);
				}
			}
		}
		if(singlePairFound){
			for(int j=0;j<hand.getNBCards();j++){
				for(int i=j+1;i<hand.getNBCards();i++){
					if(hand.getCard(j).equalRank(hand.getCard(i))&& !hand.getCard(j).equalRank(firstPairCard)){
						doublePairFound=true;
					}
				}
			}
		}
		return doublePairFound;
	}
	/**
	 * Checks if the given hand contains three cards with equal rank
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForThreeOfAKind(Hand hand) {
		if(hand.getNBCards()<3)
			return false;
		boolean threeOfAKindFound=false;
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				for(int k=i+1;k<hand.getNBCards();k++){
					if(hand.getCard(j).equalRank(hand.getCard(i)) && hand.getCard(k).equalRank(hand.getCard(i)) )
						threeOfAKindFound=true;
				}
			}
		}
		return threeOfAKindFound;
	}
	/**
	 * Checks if the given hand contains a straigth 
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForStraigth(Hand hand) {
		if(hand.getNBCards()<5)
			return false;
		boolean straightFound=false;
		boolean prevRankOk=true;
		Hand temp=new Hand(hand);
		temp.sort();
		int i=0;
		for(int j=0;j<temp.getNBCards();j++){
			i=j;
			while(i-j<5 && i<temp.getNBCards()-1 && prevRankOk){
				prevRankOk=(temp.getCard(i).getRank().getValue()==temp.getCard(i+1).getRank().getValue()+1);
				i++;
			}
			if(prevRankOk && i-j+1==5){
				straightFound=true;
				break;
			}
			prevRankOk=true;
		}
		if(!straightFound){
			Card tempCard=null;
			for(int k=0;k<Math.round(temp.getNBCards()/2);k++){
				tempCard=temp.getCard(k);
				//place ace at the back of the hand
				if(tempCard.getRank().equals(Rank.ACE)){
					temp.removeCard(tempCard);
					temp.sort();
					temp.addCard(tempCard);
				}
			}
			int l=0;
			for(int j=0;j<temp.getNBCards();j++){
				l=j;
				while(l-j<5 && l<temp.getNBCards()-1 && prevRankOk){
					prevRankOk=(temp.getCard(l).getRank().getValue()==(temp.getCard(l+1).getRank().getValue()%13)+1);
					l++;
				}
				if(prevRankOk && l-j+1==5){
					straightFound=true;
					break;
				}
				prevRankOk=true;
			}
		}
		return straightFound;
	}
	/**
	 * Checks if the given hand contains a flush, being 5 cards with equal suit
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForFlush(Hand hand) {
		if(hand.getNBCards()<5)
			return false;
		int suitCount=0;
		Suit flushSuit;
		for(int i=0;i<hand.getNBCards();i++){
			flushSuit=hand.getCard(i).getSuit();
			for(int j=0;j<hand.getNBCards();j++){
				if(j!= i && hand.getCard(j).getSuit().equals(flushSuit))
					suitCount++;
				if(suitCount==4)
					return true;
			}
			suitCount=0;
		}
		return false;
	}
	/**
	 * Checks if the given hand contains a full house, being one pair of cards with
	 * equal rank and three other cards with the same rank
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForFullHouse(Hand hand) {
		if(hand.getNBCards()<5)
			return false;
		boolean fullHouseFound;
		boolean threeOfAKindFound=false;
		boolean pairFound=false;
		Card threeOfAKindCard=null;
		
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				for(int k=i+1;k<hand.getNBCards();k++){
					if(hand.getCard(j).equalRank(hand.getCard(i)) && hand.getCard(k).equalRank(hand.getCard(i)) ){
						threeOfAKindFound=true;
						threeOfAKindCard=hand.getCard(j);
					}
						
				}
			}
		}
		if(threeOfAKindFound){
			for(int j=0;j<hand.getNBCards();j++){
				for(int i=j+1;i<hand.getNBCards();i++){
					if(hand.getCard(j).equalRank(hand.getCard(i)) && !hand.getCard(j).equalRank(threeOfAKindCard)){
						pairFound=true;
					}
				}
			}
		}
		
		fullHouseFound= threeOfAKindFound && pairFound;
		return fullHouseFound;
	}
	/**
	 * Checks if the given hand contains 4 cards with an equal rank
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForFourOfAKind(Hand hand) {
		if(hand.getNBCards()<4)
			return false;
		boolean fourOfAKindFound=false;
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				for(int k=i+1;k<hand.getNBCards();k++){
					for(int l=k+1;l<hand.getNBCards();l++){
						if(hand.getCard(j).equalRank(hand.getCard(i)) && hand.getCard(k).equalRank(hand.getCard(i))
								&& hand.getCard(k).equalRank(hand.getCard(l)))
							fourOfAKindFound=true;
					}
				}
			}
		}
		return fourOfAKindFound;
	}
	/**
	 * Checks if the given hand contains a straight flush
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForStraightFlush(Hand hand) {
		if(hand.getNBCards()<5)
			return false;
		if(checkForFlush(hand) && checkForStraigth(hand)){
			Card [] temp1=HandTypeCalculator.getStraightCard(hand);
			Card[] temp2=HandTypeCalculator.getFlushCard(hand);
			return temp1[0].getSuit().equals(temp2[0].getSuit());
		}
		return false;
	}
	/**
	 * Returns the determinating cards for a given hand
	 */
	public static Card[] getDeterminatingCards(Hand hand){
		HandType type=calculateHandType(hand);
		
		if(type.equals(HandType.PAIR))
			return getPairCards(hand);
		if(type.equals(HandType.DOUBLE_PAIR))
			return getDoublePairCards(hand);
		if(type.equals(HandType.THREE_OF_A_KIND))
			return getThreeOfAKindCards(hand);
		if(type.equals(HandType.FOUR_OF_A_KIND))
			return getFourOfAKindCards(hand);
		if(type.equals(HandType.FULL_HOUSE))
			return getFullHouseCards(hand);
		if(type.equals(HandType.STRAIGHT)|| type.equals(HandType.STRAIGHT_FLUSH))
			return getStraightCard(hand);
		if(type.equals(HandType.FLUSH))
			return getFlushCard(hand);
		return null;
	}
	public static Card[] getFlushCard(Hand hand) {
		int suitCount=0;
		Suit flushSuit;
		for(int i=0;i<hand.getNBCards();i++){
			flushSuit=hand.getCard(i).getSuit();
			for(int j=0;j<hand.getNBCards();j++){
				if(j!= i && hand.getCard(j).getSuit().equals(flushSuit))
					suitCount++;
				if(suitCount==5){
					Card[] result={hand.getCard(i)};
					return result;
				}
			}
		}
		return null;
	}
	public static Card[] getStraightCard(Hand hand) {
		Hand temp=new Hand(hand);
		temp.sort();
		Card tempCard=null;
		for(int i=0;i<Math.round(temp.getNBCards()/2);i++){
			tempCard=temp.getCard(i);
			//place ace at the back of the hand
			if(tempCard.getRank().equals(Rank.ACE)){
				temp.removeCard(tempCard);
				temp.sort();
				temp.addCard(tempCard);
			}
		}
		Card[] result={temp.getCard(0)};
		return result;
	}
	public static Card[] getFullHouseCards(Hand hand) {
		if(!checkForFullHouse(hand))
			throw new IllegalArgumentException();
		
		Card threeOfAKindCard1=null;
		Card threeOfAKindCard2=null;
		Card threeOfAKindCard3=null;
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				for(int k=i+1;k<hand.getNBCards();k++){
					if(hand.getCard(j).equalRank(hand.getCard(i)) && hand.getCard(k).equalRank(hand.getCard(i))){
						threeOfAKindCard1=hand.getCard(j);
						threeOfAKindCard2=hand.getCard(i);
						threeOfAKindCard3=hand.getCard(k);
					}
				}
			}
		}
		Card pairCard1=null;
		Card pairCard2=null;
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				if(hand.getCard(j).equalRank(hand.getCard(i)) && !hand.getCard(j).equalRank(threeOfAKindCard1)){
					pairCard1=hand.getCard(j);
					pairCard2=hand.getCard(i);
				}
			}
		}
		Card[] result={threeOfAKindCard1,threeOfAKindCard2,threeOfAKindCard3,pairCard1,pairCard2};
		return result;
	}
	/**
	 * Returns the 4 cards of equal rank of the given four-of-a-kind hand
	 * @param hand
	 * 			the given four-of-a-kind hand
	 */
	public static Card[] getFourOfAKindCards(Hand hand) {
		if(!checkForFourOfAKind(hand))
			throw new IllegalArgumentException();
		Card card1 = null,card2 = null,card3 = null,card4 = null;
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				for(int k=i+1;k<hand.getNBCards();k++){
					for(int l=k+1;l<hand.getNBCards();l++){
						if(hand.getCard(j).equalRank(hand.getCard(i)) && hand.getCard(k).equalRank(hand.getCard(i))
								&& hand.getCard(k).equalRank(hand.getCard(l))){
							card1=hand.getCard(j);
							card2=hand.getCard(i);
							card3=hand.getCard(k);
							card4=hand.getCard(l);
						}
					}
				}
			}
		}
		Card[] result={card1,card2,card3,card4};
		return result;
	}
	/**
	 * Returns the 3 cards of equal rank of the given three-of-a-kind hand
	 * @param hand
	 * 			the given three-of-a-kind hand
	 */
	public static Card[] getThreeOfAKindCards(Hand hand) {
		if(!checkForThreeOfAKind(hand))
			throw new IllegalArgumentException();
		Card card1=null,card2=null,card3=null;
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				for(int k=i+1;k<hand.getNBCards();k++){
					if(hand.getCard(j).equalRank(hand.getCard(i)) && hand.getCard(k).equalRank(hand.getCard(i)) ){
						card1=hand.getCard(j);
						card2=hand.getCard(i);
						card3=hand.getCard(k);
					}
				}
			}
		}
		Card[] result={card1,card2,card3};
		return result;
	}
	/**
	 * Returns the two pairs of a given double-pair hand
	 * @param hand
	 * 			the given double pair hand
	 */
	public static Card[] getDoublePairCards(Hand hand) {
		if(!checkForDoublePair(hand))
			throw new IllegalArgumentException();
		Card pairCard1=null;
		Card pairCard2=null;
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				if(hand.getCard(j).equalRank(hand.getCard(i))){
					pairCard1=hand.getCard(j);
					pairCard2=hand.getCard(i);
					break;
				}
			}
		}
		Card pairCard3=null;
		Card pairCard4=null;
		for(int j=0;j<hand.getNBCards();j++){
			for(int i=j+1;i<hand.getNBCards();i++){
				if(hand.getCard(j).equalRank(hand.getCard(i)) && ! hand.getCard(j).equalRank(pairCard1)){
					pairCard3=hand.getCard(j);
					pairCard4=hand.getCard(i);
					break;
				}
			}
		}
		Card[] result={pairCard1,pairCard2,pairCard3,pairCard4};
		return result;
	}
	/**
	 * Returns the pair cards of the given pair-hand
	 * @param hand
	 * 			the given pair-hand
	 */
	public static Card[] getPairCards(Hand hand) {
		if(!checkForPair(hand))
			throw new IllegalArgumentException();
		Card pairCard1,pairCard2;
		for(int j=0;j<hand.getNBCards();j++){
			pairCard1=hand.getCard(j);
			for(int i=j+1;i<hand.getNBCards();i++){
				pairCard2=hand.getCard(i);
				if(pairCard2.equalRank(pairCard1)){
					Card[] result={pairCard1,pairCard2};
					return result;
				}
			}
		}
		throw new IllegalArgumentException();
	}
}
