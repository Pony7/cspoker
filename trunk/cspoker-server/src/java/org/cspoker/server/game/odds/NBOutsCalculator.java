package org.cspoker.server.game.odds;

import java.util.List;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.server.game.elements.cards.hand.Hand;

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
