package org.cspoker.server.game.elements.cards.deck.randomGenerator;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.common.game.elements.cards.CardImpl;
import org.cspoker.common.game.elements.cards.cardElements.Rank;
import org.cspoker.common.game.elements.cards.cardElements.Suit;
import org.cspoker.server.game.elements.cards.hand.Hand;

public class RandomUtils {
	private RandomUtils() {
		// Utility class - should not be implemented
	}

	/**
	 * Returns a random card.
	 * 
	 * @return A random card.
	 */
	public static Card getRandomCard() {
		int suitIndex = RandomOrgSeededRandomGenerator.getInstance().getRandom().nextInt(4);
		int rankIndex = RandomOrgSeededRandomGenerator.getInstance().getRandom().nextInt(13);
	
		return new CardImpl(Suit.values()[suitIndex], Rank.values()[rankIndex]);
	}

	/**
	 * Returns a random hand.
	 * 
	 * @param nBCards
	 *            The number of cards in the hand.
	 * @return A random hand.
	 */
	public static Hand getRandomHand(int nBCards) {
		Hand result = new Hand();
		while (result.getNBCards() < nBCards) {
			Card randomCard = getRandomCard();
			if (!result.contains(randomCard))
				result.addCard(randomCard);
		}
		return result;
	}
}
