package org.cspoker.server.game.elements.cards.hand.handQuality;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandType;
import org.cspoker.server.game.elements.cards.hand.HandTypeCalculator;

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
