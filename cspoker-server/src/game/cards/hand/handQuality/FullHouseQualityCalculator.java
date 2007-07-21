package game.cards.hand.handQuality;

import game.cards.Card;
import game.cards.hand.Hand;
import game.cards.hand.HandTypeCalculator;

public class FullHouseQualityCalculator extends HandQualityCalculator {

	@Override
	public double calculateQualityWithinType(Hand hand) {
		Card[] fullHouseCards=HandTypeCalculator.getDeterminatingCards(hand);

		double threeQuality=1/(getNumberCombinations(1)+1)*(fullHouseCards[0].getRank().getValue()-1);
		double pairQuality=1/(getNumberCombinations(1)+1)*1/(getNumberCombinations(1)+1)*(fullHouseCards[3].getRank().getValue()-1);

		return threeQuality+pairQuality;
	}

}
