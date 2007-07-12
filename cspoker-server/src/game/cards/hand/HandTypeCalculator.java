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
import game.cards.cardElements.Rank;
import game.cards.cardElements.Suit;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class for calculating the HandType of any hand
 * @author Cedric
 */
public class HandTypeCalculator {
	/**********************************************************
	 * Calculate HandType
	 **********************************************************/
	/**
	 * Calculates the type of any given hand
	 * @param hand
	 * 			the given hand
	 */
	public static HandType calculateHandType(Hand hand){
		Hand best;
		if(hand.getNBCards()!=5){
			best=HandEvaluator.getBestHand(hand);
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
			boolean straight=checkForStraight(best);
			
			if(flush && straight)
				return HandType.STRAIGHT_FLUSH;
			else{
				if(flush)
					return HandType.FLUSH;
				if(straight)
					return HandType.STRAIGHT;
			}
		}
		return HandType.HIGH_CARD;
	}
	/**********************************************************
	 * Checkers for different HandTypes
	 **********************************************************/
	/**
	 * Checks if the given hand contains a pair of cards with equal rank
	 * @param hand
	 * 			the given hand to check
	 * @result	True of this hand contains at least a pair, being two cards of the same rank; false otherwise
	 * 			| result== (x.equalRank(y) && hand.contains(x) && hand.contains(y))
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
	 * @result	True if the given hand contains at least two different pairs; false otherwise
	 * 			| result== ( (x.equalRank(y) && hand.contains(x) && hand.contains(y))
	 * 			|			&& (z.equalRank(u) && hand.contains(z) && hand.contains(u))
	 * 			|				&& !x.equalRank(z))
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
	 * @result	True if the given hand contains at least three cards with the same rank; false otherwise
	 * 			| result == (x.equalRank(y)&& x.equalRank(z) && hand.contains(x) && hand.contains(y)&& hand.contains(z))
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
	 * Checks if the given hand contains a straight 
	 * @param hand
	 * 			the given hand to check
	 * @result	True if this hand contains at least a sequence of five cards with consequetive ranks; false otherwise
	 * 			|result == ( hand.contains(x) && hand.contains(y) && hand.contains(z) && hand.contains(u) && hand.contains(v)
	 * 			|				&& x.getRank()=y.getRank()+1 && y.getRank()=z.getRank()+1 && z.getRank()=u.getRank()+1
	 * 			|				&& u.getRank()=v.getRank()+1 )
	 */
	public static boolean checkForStraight(Hand hand) {
		if(hand.getNBCards()<5)
			return false;
		boolean straightFound=false;
		boolean prevRankOk=true;
		Hand temp=new Hand(hand);
		temp.sort();
		int i=0;
		for(int j=0;j<temp.getNBCards();j++){
			i=j;
			while((i-j<5) && (i<temp.getNBCards()-1) && prevRankOk){
				prevRankOk=(temp.getCard(i).getRank().getValue()==temp.getCard(i+1).getRank().getValue()+1);
				i++;
			}
			if(prevRankOk && (i-j+1==5)){
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
				while((l-j<5) && (l<temp.getNBCards()-1) && prevRankOk){
					prevRankOk=(temp.getCard(l).getRank().getValue()==(temp.getCard(l+1).getRank().getValue()%13)+1);
					l++;
				}
				if(prevRankOk && (l-j+1==5)){
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
	 * @result	True if this hand contains at least five cards with the same suit; false otherwise
	 * 			|result == ( hand.contains(x) && hand.contains(y) && hand.contains(z) && hand.contains(u) && hand.contains(v)
	 * 			|				&& x.equalSuit(y)&& x.equalSuit(z)&& x.equalSuit(u)&& x.equalSuit(v))
	 */
	public static boolean checkForFlush(Hand hand) {
		if(hand.getNBCards()<5)
			return false;
		int suitCount=0;
		Suit flushSuit;
		for(int i=0;i<hand.getNBCards();i++){
			flushSuit=hand.getCard(i).getSuit();
			for(int j=0;j<hand.getNBCards();j++){
				if((j!= i) && hand.getCard(j).getSuit().equals(flushSuit))
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
	 * @result	True if the given hand contains at least three cards of the same rank and two cards of the same rank
	 * 			; false otherwise
	 * 			|result == ( hand.contains(x) && hand.contains(y) && hand.contains(z) && hand.contains(u) && hand.contains(v)
	 * 			|				&& x.equalRank(y) && x.equalRank(z) && !x.equalRank(u)&& u.equalRank(v))
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
	 * @result	True if the given hand contains four cards with equal rank
	 * 			| result == (x.equalRank(y)&& x.equalRank(z) && x.equalRank(u) && hand.contains(x) 
	 * 			|				&& hand.contains(y)&& hand.contains(z))&& hand.contains(u))
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
	 * @result	True if this hand contains five cards with the same suit and consequitive ranks
	 * 			|result == ( hand.contains(x) && hand.contains(y) && hand.contains(z) && hand.contains(u) && hand.contains(v)
	 * 			|				&& x.getRank()=y.getRank()+1 && y.getRank()=z.getRank()+1 && z.getRank()=u.getRank()+1
	 * 			|				&& u.getRank()=v.getRank()+1 && x.equalSuit(y) && x.equalSuit(z) && x.equalSuit(u)
	 * 			|				&& x.equalSuit(v))
	 */
	public static boolean checkForStraightFlush(Hand hand) {
		if(hand.getNBCards()<5)
			return false;
		if(checkForFlush(hand) && checkForStraight(hand)){
			Card [] temp1=HandTypeCalculator.getStraightCard(hand);
			Card[] temp2=HandTypeCalculator.getFlushCard(hand);
			return temp1[0].getSuit().equals(temp2[0].getSuit());
		}
		return false;
	}
	/**********************************************************
	 * Get the determinating cards for any HandType
	 **********************************************************/
	/**
	 * Returns the determinating cards for a given hand
	 * @return	The resulting array isn't null
	 * 			| result != null
	 * @return	The given hand contains the resulting array
	 * 			| hand.contains(result)
	 */
	public static Card[] getDeterminatingCards(Hand hand){
		Hand best=HandEvaluator.getBestHand(hand);
		HandType type=calculateHandType(best);
		
		if(type.equals(HandType.PAIR))
			return getPairCards(best);
		if(type.equals(HandType.DOUBLE_PAIR))
			return getDoublePairCards(best);
		if(type.equals(HandType.THREE_OF_A_KIND))
			return getThreeOfAKindCards(best);
		if(type.equals(HandType.FOUR_OF_A_KIND))
			return getFourOfAKindCards(best);
		if(type.equals(HandType.FULL_HOUSE))
			return getFullHouseCards(best);
		if(type.equals(HandType.STRAIGHT)|| type.equals(HandType.STRAIGHT_FLUSH))
			return getStraightCard(best);
		if(type.equals(HandType.FLUSH))
			return getFlushCard(best);
		//cannot occur
		throw new IllegalStateException();
	}
	/**********************************************************
	 * Getters for the determinating cards of the different HandTypes
	 **********************************************************/
	/**
	 * Returns the pair cards of the given pair-hand
	 * @param hand
	 * 			the given pair-hand
	 * @throws	IllegalArgumentException
	 * 			if the given hand doesn't contain a pair
	 * 			| !checkForPair(hand)
	 * @return	The resulting array isn't null
	 * 			| result != null
	 * @result	There are two cards in the resulting array
	 * 			| result.length==2
	 * @result	The two cards in the resulting array have the same rank but are not equal
	 * 			| result[0].equalRank(result[1]) && ! result[0].equals(result[1])
	 * @return	The given hand contains the resulting array
	 * 			| hand.contains(result)
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
	/**
	 * Returns the two pairs of a given double-pair hand
	 * @param hand
	 * 			the given double pair hand
	 * @throws	IllegalArgumentException
	 * 			if the given hand doesn't contain a double pair
	 * 			| !checkForDoublePair(hand)
	 * @return	The resulting array isn't null
	 * 			| result != null
	 * @result	There are 4 cards in the resulting array
	 * 			| result.length==4
	 * @result	The first two cards in the resulting array have the same rank but are not equal
	 * 			| result[0].equalRank(result[1]) && ! result[0].equals(result[1])
	 * @result	The last two cards in the resulting array have the same rank but are not equal
	 * 			| result[2].equalRank(result[3]) && ! result[2].equals(result[3])
	 * @result	The first pair has a greater rank than the last pair
	 * 			| result[0].getRank().getValue()>result[2].getRank().getValue()
	 * @return	The given hand contains the resulting array
	 * 			| hand.contains(result)
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
		Card temp1,temp2;
		if(pairCard1.getRank().getValue()<pairCard3.getRank().getValue()){
			temp1=pairCard1;
			temp2=pairCard2;
			pairCard1=pairCard3;
			pairCard2=pairCard4;
			pairCard3=temp1;
			pairCard4=temp2;
		}
		Card[] result={pairCard1,pairCard2,pairCard3,pairCard4};
		return result;
	}
	/**
	 * Returns the 3 cards of equal rank of the given three-of-a-kind hand
	 * @param hand
	 * 			the given three-of-a-kind hand
	 * @throws	IllegalArgumentException
	 * 			if the give hand doesn't contain a three-of-a-kind
	 * 			| !checkForThreeOfAKind(hand)
	 * @return	The resulting array isn't null
	 * 			| result != null
	 * @result	There are 3 cards in the resulting array
	 * 			| result.length==3
	 * @result	The three cards in the resulting array have the same rank but are not equal
	 * 			| result[0].equalRank(result[1])&& result[0].equalRank(result[2]) 
	 * 			|	&& !result[0].equals(result[1]) && !result[0].equals(result[2])
	 * @return	The given hand contains the resulting array
	 * 			| hand.contains(result)
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
	 * Returns the starting card (the one with the greatest rank) of the straight of the given hand
	 * @param hand
	 * 			the given hand
	 * @throws	IllegalArgumentException
	 * 			if the give hand doesn't contain a straigth
	 * 			| !checkForStraigth(hand)
	 * @return	The resulting array contains only one card
	 * 			| result.length==1
	 * @return	The resulting array isn't null
	 * 			| result != null
	 * @return	The given hand contains the resulting array
	 * 			| hand.contains(result)
	 */
	public static Card[] getStraightCard(Hand hand) {
		if(!checkForStraight(hand))
			throw new IllegalArgumentException();
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
	/**
	 * Returns the sorted array with the 5 cards that determine the given flush-hand
	 * @param hand
	 * 			the given hand
	 * @throws	IllegalArgumentException
	 * 			if the give hand doesn't contain a flush
	 * 			| !checkForFlush(hand)
	 * @return	The resulting array contains five cards
	 * 			| result.length==5
	 * @return	The resulting array isn't null
	 * 			| result != null
	 * @return	The resulting array contains 5 cards of equal suit
	 * 			| result[0].equalSuit(result[1]) && result[0].equalSuit(result[2]) result[0].equalSuit(result[3])
	 * 			|	&& result[0].equalSuit(result[4])
	 * @return	The resulting array is sorted by rank, from high to low
	 * 			| (result[0].compareTo(result[0])!=-1) && (result[1].compareTo(result[2])!=-1)
	 * 			|	&& (result[2].compareTo(result[3])!=-1) && (result[3].compareTo(result[4])!=-1)
	 * @return	The given hand contains the resulting array
	 * 			| hand.contains(result)
	 */
	public static Card[] getFlushCard(Hand hand) {
		if(!checkForFlush(hand))
			throw new IllegalArgumentException();
		int suitCount=0;
		Suit flushSuit = null;
		for(int i=0;i<hand.getNBCards();i++){
			for(int j=0;j<hand.getNBCards();j++){
				if((j!= i) && hand.getCard(j).getSuit().equals(hand.getCard(i).getSuit()))
					suitCount++;
				if(suitCount==5){
					flushSuit=hand.getCard(j).getSuit();
					break;
				}
			}
		}
		Hand temp=new Hand(hand);
		ArrayList<Card> cardsToRemove=new ArrayList<Card>();
		for(int j=0;j<temp.getNBCards();j++){
			if(! temp.getCard(j).getSuit().equals(flushSuit))
				cardsToRemove.add(temp.getCard(j));
		}
		Iterator<Card> iterator=cardsToRemove.iterator();
		while(iterator.hasNext()){
			temp.removeCard(iterator.next());
		}
		temp.sort();
		if(temp.getNBCards()==5)
			return temp.getCards();
		Hand result=new Hand();
		for(int k=0;k<5;k++){
			result.addCard(temp.getCard(k));
		}
		return result.getCards();
	}
	/**
	 * Returns the 5 cards that determine the given full-house hand
	 * @param hand
	 * 			the given hand
	 * @throws	IllegalArgumentException
	 * 			if the give hand doesn't contain a flush
	 * 			| !checkForFullHouse(hand)
	 * @return	The resulting array contains five cards
	 * 			| result.length==5
	 * @return	The resulting array isn't null
	 * 			| result != null
	 * @return	The first three cards have equal rank
	 * 			| result[0].equalRank(result[1]) && result[1].equalRank(result[2])
	 * @return	The last two cards have equal rank but not the same as the first three cards
	 * 			| result[3].equalRank(result[4]) && !result[0].equalRank(result[3])
	 * @return	The given hand contains the resulting array
	 * 			| hand.contains(result)
	 */
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
	 * @throws	IllegalArgumentException
	 * 			if the give hand doesn't contain a flush
	 * 			| !checkForFourOfAKind(hand)
	 * @return	The resulting array contains four cards
	 * 			| result.length==4
	 * @return	The resulting array isn't null
	 * 			| result != null
	 * @return	The resulting cards have equal rank
	 * 			| result[0].equalRank(result[1]) && result[1].equalRank(result[2]) && result[2].equalRank(result[3])
	 * @return	The given hand contains the resulting array
	 * 			| hand.contains(result)
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
}
