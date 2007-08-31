package org.cspoker.server.game.elements.cards.hand.handQuality;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandTypeCalculator;

public class StraightQualityCalculator extends HandQualityCalculator {

	@Override
	public double calculateQualityWithinType(Hand hand) {
		Card[] straightCard=HandTypeCalculator.getDeterminatingCards(hand);
		return 0.1*((straightCard[0].getRank().getValue())-5.0);
	}

}
