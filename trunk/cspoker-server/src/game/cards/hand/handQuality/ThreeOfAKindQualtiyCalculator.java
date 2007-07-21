package game.cards.hand.handQuality;

import game.cards.Card;
import game.cards.hand.Hand;
import game.cards.hand.HandType;
import game.cards.hand.HandTypeCalculator;

public class ThreeOfAKindQualtiyCalculator extends HandQualityCalculator {

	@Override
	public double calculateQualityWithinType(Hand hand) {
		Card[] threeCards=HandTypeCalculator.getDeterminatingCards(hand);
		double pairQuality=1/(getNumberCombinations(1)+1)*(threeCards[0].getRank().getValue()-1);
		Hand temp=new Hand(hand);
		temp.removeCard(threeCards[0]);
		temp.removeCard(threeCards[1]);
		temp.removeCard(threeCards[2]);
		double restHandQuality=1/(getNumberCombinations(2)+1)*HandType.HIGH_CARD.getHandQualityCalculator().calculateQualityWithinType(temp);

		return pairQuality+restHandQuality;
	}

}
