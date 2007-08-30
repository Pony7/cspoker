package org.cspoker.server.game.elements.cards.hand;

/**
 * Copyright (C) 2007 Craig Motlin
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.util.ArrayList;
import java.util.List;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.common.game.elements.cards.cardElements.Suit;

/**
 * @author Craig Motlin
 * 
 */
public final class NewHandEvaluator {
	private NewHandEvaluator() {
		// Utility class, should not be instantiated
	}

	/**
	 * This function takes a seven card hand and returns the best five card hand
	 * that is a subset
	 * 
	 * @param hand
	 *            The seven card hand
	 * @return The best five card hand that is a subset
	 */
	public static List<Card> getBestFive(final List<Card> hand) {
		assert hand.size() == 7 || hand.size() == 6 : "Illegal argument, hand size != 7 or 6";

		int minRank = Integer.MAX_VALUE;
		List<Card> bestHand = null;

		if (hand.size() == 7) {
			for (int i = 0; i < 6; i++) {
				for (int j = i + 1; j < 7; j++) {
					final List<Card> fiveCardHand = new ArrayList<Card>(hand);
					fiveCardHand.remove(j);
					fiveCardHand.remove(i);

					final int rank = NewHandEvaluator.getRank(fiveCardHand);
					if (rank < minRank) {
						minRank = rank;
						bestHand = fiveCardHand;
					}
				}
			}
		} else if (hand.size() == 6) {
			for (int i = 0; i < 6; i++) {
				final List<Card> fiveCardHand = new ArrayList<Card>(hand);
				fiveCardHand.remove(i);

				final int rank = NewHandEvaluator.getRank(fiveCardHand);
				if (rank < minRank) {
					minRank = rank;
					bestHand = fiveCardHand;
				}
			}
		}

		return bestHand;
	}

	public static String getDescription(final List<Card> hand) {
		return HandRanks.getInstance().getLongDescription(Integer.valueOf(NewHandEvaluator.getProduct(hand)), NewHandEvaluator.isFlush(hand));
	}

	public static int getRank(final List<Card> hand) {
		return HandRanks.getInstance().getHandRank(Integer.valueOf(NewHandEvaluator.getProduct(hand)), NewHandEvaluator.isFlush(hand)).intValue();
	}

	// TODO decide if this should be API - I'm leaning toward no
	protected static int getProduct(final List<Card> hand) {
		assert hand.size() == 5 : "Illegal argument, hand size != 5";

		int product = 1;
		for (final Card card : hand) {
			product *= card.getRank().getPrime();
		}
		return product;
	}

	// TODO decide if this should be API - I'm leaning toward yes
	protected static boolean isFlush(final List<Card> hand) {
		assert hand.size() == 5 : "Illegal argument, hand size != 5";

		final Suit suit = hand.get(0).getSuit();
		for (final Card card : hand.subList(1, 5)) {
			if (suit != card.getSuit()) {
				return false;
			}
		}

		return true;
	}
}
