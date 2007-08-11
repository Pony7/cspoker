package org.cspoker.server.game.elements.cards.hand.handQuality;

import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandType;

public class StraightFlushQualityCalculator extends HandQualityCalculator{

	@Override
	public double calculateQualityWithinType(Hand hand) {
		return HandType.STRAIGHT.getHandQualityCalculator().calculateQualityWithinType(hand);
	}

}
