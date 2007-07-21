package game.cards.hand.handQuality;

import game.cards.hand.Hand;
import game.cards.hand.HandType;

public class FlushQualityCalculator extends HandQualityCalculator{

	@Override
	public double calculateQualityWithinType(Hand hand) {
		return HandType.HIGH_CARD.getHandQualityCalculator().calculateQualityWithinType(hand);
	}

}
