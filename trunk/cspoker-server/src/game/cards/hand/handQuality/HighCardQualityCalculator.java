package game.cards.hand.handQuality;

import game.cards.hand.Hand;

public class HighCardQualityCalculator extends HandQualityCalculator {

	@Override
	public double calculateQualityWithinType(Hand hand) {
		Hand temp=new Hand(hand);
		temp.sort();
		double quality=0;
		for(int j=0;j<temp.getNBCards();j++){
			quality+=1/(Math.pow(getNumberCombinations(1)+1,j+1))*(temp.getCard(j).getRank().getValue()-1);
		}
		return quality;
	}

}
