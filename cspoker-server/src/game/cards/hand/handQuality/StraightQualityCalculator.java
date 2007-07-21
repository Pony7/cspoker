package game.cards.hand.handQuality;

import game.cards.Card;
import game.cards.hand.Hand;
import game.cards.hand.HandTypeCalculator;

public class StraightQualityCalculator extends HandQualityCalculator {

	@Override
	public double calculateQualityWithinType(Hand hand) {
		Card[] straightCard=HandTypeCalculator.getDeterminatingCards(hand);
		return 0.1*((straightCard[0].getRank().getValue())-5.0);
	}

}
