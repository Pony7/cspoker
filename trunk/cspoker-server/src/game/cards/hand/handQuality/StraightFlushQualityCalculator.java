package game.cards.hand.handQuality;

import game.cards.hand.Hand;
import game.cards.hand.HandType;

public class StraightFlushQualityCalculator extends HandQualityCalculator{

	@Override
	public double calculateQualityWithinType(Hand hand) {
		return HandType.STRAIGHT.getHandQualityCalculator().calculateQualityWithinType(hand);
	}

}
