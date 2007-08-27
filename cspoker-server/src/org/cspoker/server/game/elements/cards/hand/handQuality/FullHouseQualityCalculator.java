package org.cspoker.server.game.elements.cards.hand.handQuality;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandTypeCalculator;

public class FullHouseQualityCalculator extends HandQualityCalculator {

	@Override
	public double calculateQualityWithinType(Hand hand) {
		Card[] fullHouseCards=HandTypeCalculator.getDeterminatingCards(hand);

		double threeQuality=1/(getNumberCombinations(1)+1)*(fullHouseCards[0].getRank().getValue()-1);
		double pairQuality=1/(getNumberCombinations(1)+1)*1/(getNumberCombinations(1)+1)*(fullHouseCards[3].getRank().getValue()-1);

		return threeQuality+pairQuality;
	}

}
