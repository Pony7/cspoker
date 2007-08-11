package org.cspoker.server.game.elements.cards.hand.handQuality;

import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandType;

public class FlushQualityCalculator extends HandQualityCalculator{

	@Override
	public double calculateQualityWithinType(Hand hand) {
		return HandType.HIGH_CARD.getHandQualityCalculator().calculateQualityWithinType(hand);
	}

}
