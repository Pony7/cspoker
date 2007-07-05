package game.cards.hand;

import game.cards.Card;

public class HandTypeCalculator {


	/**
	 * Calculates the type of the given hand
	 * @param hand
	 * 			the given hand
	 */
	public static HandType calculateHandType(Hand hand){
		if(hand.getNBCards()!=5)
			return null;
		
		if(checkForStraightFlush(hand))
			return HandType.STRAIGHT_FLUSH;
		if(checkForFourOfAKind(hand))
			return HandType.FOUR_OF_A_KIND;
		if(checkForFullHouse(hand))
			return HandType.FULL_HOUSE;
		if(checkForFlush(hand))
			return HandType.FLUSH;
		if(checkForStraigth(hand))
			return HandType.STRAIGHT;
		if(checkForThreeOfAKind(hand))
			return HandType.THREE_OF_A_KIND;
		if(checkForDoublePair(hand))
			return HandType.DOUBLE_PAIR;
		if(checkForPair(hand))
			return HandType.PAIR;
		return HandType.HIGH_CARD;
	}
	public static boolean checkForPair(Hand hand) {
		if(hand.getNBCards()!=2)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	public static boolean checkForDoublePair(Hand hand) {
		if(hand.getNBCards()!=4)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	public static boolean checkForThreeOfAKind(Hand hand) {
		if(hand.getNBCards()!=3)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	public static boolean checkForStraigth(Hand hand) {
		if(hand.getNBCards()!=5)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	public static boolean checkForFlush(Hand hand) {
		if(hand.getNBCards()!=5)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	public static boolean checkForFullHouse(Hand hand) {
		if(hand.getNBCards()!=5)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	public static boolean checkForFourOfAKind(Hand hand) {
		if(hand.getNBCards()!=5)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
		return false;
	}
	public static boolean checkForStraightFlush(Hand hand) {
		if(hand.getNBCards()!=5)
			throw new IllegalArgumentException();
		// TODO Auto-generated method stub
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
		return hand.getCards();
	}
	public static Card[] getFourOfAKindCards(Hand hand) {
		// TODO Auto-generated method stub
		return null;
	}
	public static Card[] getThreeOfAKindCards(Hand hand) {
		// TODO Auto-generated method stub
		return null;
	}
	public static Card[] getDoublePairCards(Hand hand) {
		Card pairCard1=null;
		Card pairCard2=null;
		for(int j=0;j<hand.getNBCards();j++){
			pairCard1=hand.getCard(j);
			for(int i=j;i<hand.getNBCards();i++){
				pairCard2=hand.getCard(i);
				if(pairCard2.equals(pairCard2)){
					break;
				}
			}
		}
		Card pairCard3=null;
		Card pairCard4=null;
		for(int j=0;j<hand.getNBCards();j++){
			pairCard3=hand.getCard(j);
			for(int i=j;i<hand.getNBCards();i++){
				pairCard4=hand.getCard(i);
				if(pairCard3.equals(pairCard4) && ! pairCard3.equals(pairCard2)){
					break;
				}
			}
		}
		Card[] result={pairCard1,pairCard2,pairCard3,pairCard4};
		return result;
	}
	public static Card[] getPairCards(Hand hand) {
		Card pairCard1,pairCard2;
		for(int j=0;j<hand.getNBCards();j++){
			pairCard1=hand.getCard(j);
			for(int i=j;i<hand.getNBCards();i++){
				pairCard2=hand.getCard(i);
				if(pairCard2.equals(pairCard2)){
					Card[] result={pairCard1,pairCard2};
					return result;
				}
			}
		}
		throw new IllegalArgumentException();
	}
}
