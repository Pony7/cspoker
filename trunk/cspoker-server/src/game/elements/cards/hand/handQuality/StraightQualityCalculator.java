package game.elements.cards.hand.handQuality;

import game.elements.cards.Card;
import game.elements.cards.hand.Hand;
import game.elements.cards.hand.HandTypeCalculator;

public class StraightQualityCalculator extends HandQualityCalculator {

	@Override
	public double calculateQualityWithinType(Hand hand) {
		Card[] straightCard=HandTypeCalculator.getDeterminatingCards(hand);
		return 0.1*((straightCard[0].getRank().getValue())-5.0);
	}

}
