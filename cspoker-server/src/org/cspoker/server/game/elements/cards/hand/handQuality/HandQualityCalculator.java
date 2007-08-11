package org.cspoker.server.game.elements.cards.hand.handQuality;

import org.cspoker.server.game.elements.cards.hand.Hand;

public abstract class HandQualityCalculator{

	public abstract double calculateQualityWithinType(Hand hand);
	
	/**
	 * Returns the number of possible combinations of the given number of cards,
	 * the exact sequence doesn't matter
	 */
	public static double getNumberCombinations(int nBCards){
		return factorial(13)/(factorial(nBCards)*factorial(13-nBCards));
	}

    public static double factorial(int n) {
        if      (n <  0) throw new RuntimeException("Underflow error in factorial");
        else if (n > 20) throw new RuntimeException("Overflow error in factorial");
        else if (n == 0) return 1;
        else             return n * factorial(n-1);
    }
}