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
			if(checkForDoublePair(best))
				return HandType.DOUBLE_PAIR;
			if(checkForThreeOfAKind(best))
				return HandType.THREE_OF_A_KIND;
			if(checkForFullHouse(best))
				return HandType.FULL_HOUSE;
			if(checkForFourOfAKind(best))
				return HandType.FULL_HOUSE;
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
			throw new IllegalArgumentException();
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
			throw new IllegalArgumentException();
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
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * Checks if the given hand contains a straigth 
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForStraigth(Hand hand) {
		if(hand.getNBCards()<5)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * Checks if the given hand contains a flush, being 5 cards with equal suit
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForFlush(Hand hand) {
		if(hand.getNBCards()<5)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
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
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * Checks if the given hand contains 4 cards with an equal rank
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForFourOfAKind(Hand hand) {
		if(hand.getNBCards()<4)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * Checks if the given hand contains a straight flush
	 * @param hand
	 * 			the given hand to check
	 */
	public static boolean checkForStraightFlush(Hand hand) {
		if(hand.getNBCards()<5)
			throw new IllegalArgumentException();
		return checkForFlush(hand) && checkForStraigth(hand);
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
		return hand.getCards();
	}
	/**
	 * Returns the 4 cards of equal rank of the given four-of-a-kind hand
	 * @param hand
	 * 			the given four-of-a-kind hand
	 */
	public static Card[] getFourOfAKindCards(Hand hand) {
		if(!checkForFourOfAKind(hand))
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Returns the 3 cards of equal rank of the given three-of-a-kind hand
	 * @param hand
	 * 			the given three-of-a-kind hand
	 */
	public static Card[] getThreeOfAKindCards(Hand hand) {
		if(!checkForThreeOfAKind(hand))
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return null;
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
