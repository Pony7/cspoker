package game.elements.cards.hand.handQuality;

import game.elements.cards.hand.Hand;
import game.elements.cards.hand.HandType;

public class StraightFlushQualityCalculator extends HandQualityCalculator{

	@Override
	public double calculateQualityWithinType(Hand hand) {
		return HandType.STRAIGHT.getHandQualityCalculator().calculateQualityWithinType(hand);
	}

}
