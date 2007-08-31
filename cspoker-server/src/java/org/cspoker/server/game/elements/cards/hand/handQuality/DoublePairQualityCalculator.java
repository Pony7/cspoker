package org.cspoker.server.game.elements.cards.hand.handQuality;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandTypeCalculator;

public class DoublePairQualityCalculator extends HandQualityCalculator {

	@Override
	public double calculateQualityWithinType(Hand hand) {
		Card[] doublePairCards=HandTypeCalculator.getDeterminatingCards(hand);

		double firstPairQuality=(1.0/14)*(doublePairCards[0].getRank().getValue()-1);
		double secondPairQuality=(1.0/14)*(1.0/14)*(doublePairCards[2].getRank().getValue()-1);
		double doublePairQuality=firstPairQuality+secondPairQuality;
		Hand temp=new Hand(hand);

		temp.removeCard(doublePairCards[0]);
		temp.removeCard(doublePairCards[1]);
		temp.removeCard(doublePairCards[2]);
		temp.removeCard(doublePairCards[3]);
		double restHandQuality=(temp.getCard(0).getRank().getValue()*1.0)/14*(1.0/14)*(1.0/14);
		
		return doublePairQuality+restHandQuality;
	}

}
