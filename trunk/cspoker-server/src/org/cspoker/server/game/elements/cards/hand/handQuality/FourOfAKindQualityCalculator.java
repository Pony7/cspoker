package org.cspoker.server.game.elements.cards.hand.handQuality;

import org.cspoker.server.game.elements.cards.Card;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandType;
import org.cspoker.server.game.elements.cards.hand.HandTypeCalculator;

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
