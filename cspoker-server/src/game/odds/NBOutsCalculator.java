package game.odds;

import game.elements.cards.Card;
import game.elements.cards.hand.Hand;

import java.util.List;

public abstract class NBOutsCalculator {

	/**
	 * Returns the number of TRUE outs for the given hand and the given list of
	 * open cards; true in the meaning that an out doens't help another players hand
	 * @param hand
	 * 			the given hand
	 * @param openCards
	 * 			the given open cards
	 */
	public abstract int getNBOuts(Hand hand, List<Card> openCards);
}
