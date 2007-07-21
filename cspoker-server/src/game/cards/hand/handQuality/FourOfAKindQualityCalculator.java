package game.cards.hand.handQuality;

import game.cards.Card;
import game.cards.hand.Hand;
import game.cards.hand.HandType;
import game.cards.hand.HandTypeCalculator;

public class FourOfAKindQualityCalculator extends HandQualityCalculator {

	@Override
	public double calculateQualityWithinType(Hand hand) {
		Card[] fourCards=HandTypeCalculator.getDeterminatingCards(hand);
		double pairQuality=1/(getNumberCombinations(1)+1)*(fourCards[0].getRank().getValue()-1);
		Hand temp=new Hand(hand);
		temp.removeCard(fourCards[0]);
		temp.removeCard(fourCards[1]);
		temp.removeCard(fourCards[2]);
		temp.removeCard(fourCards[3]);

		double restHandQuality=1/(getNumberCombinations(1)+1)*HandType.HIGH_CARD.getHandQualityCalculator().calculateQualityWithinType(temp);

		return pairQuality+restHandQuality;
	}

}
