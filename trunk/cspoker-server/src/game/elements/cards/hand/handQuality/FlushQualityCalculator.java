package game.elements.cards.hand.handQuality;

import game.elements.cards.hand.Hand;
import game.elements.cards.hand.HandType;

public class FlushQualityCalculator extends HandQualityCalculator{

	@Override
	public double calculateQualityWithinType(Hand hand) {
		return HandType.HIGH_CARD.getHandQualityCalculator().calculateQualityWithinType(hand);
	}

}
