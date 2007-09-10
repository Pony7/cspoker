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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.common.game.elements.cards.cardElements.Suit;

/**
 * @author Craig Motlin
 * 
 */
public final class HandEvaluator {
	private HandEvaluator() {
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
	public static Collection<Card> getBestFive(final Collection<Card> hand) {
		if (hand.size() == 5) {
			return hand;
		}
		
		assert hand.size() == 7 || hand.size() == 6 || hand.size() == 5: "Illegal argument, hand size != 7 or 6 or 5";

		int minRank = Integer.MAX_VALUE;
		List<Card> bestHand = null;

		if (hand.size() == 7) {
			for (int i = 0; i < 6; i++) {
				for (int j = i + 1; j < 7; j++) {
					final List<Card> fiveCardHand = new ArrayList<Card>(hand);
					fiveCardHand.remove(j);
					fiveCardHand.remove(i);

					final int rank = HandEvaluator.getRank(fiveCardHand);
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

				final int rank = HandEvaluator.getRank(fiveCardHand);
				if (rank < minRank) {
					minRank = rank;
					bestHand = fiveCardHand;
				}
			}
		}

		return bestHand;
	}

	public static String getDescription(final Collection<Card> hand) {
		return HandRanks.getInstance().getLongDescription(valueOf(hand));
	}

	public static String getShortDescription(final Collection<Card> hand) {
		return HandRanks.getInstance().getShortDescription(valueOf(hand));
	}
	
	public static Integer getRank(final Collection<Card> hand) {
		return HandRanks.getInstance().getHandRank(valueOf(hand));
	}

	private static int getProduct(final Collection<Card> hand) {
		assert hand.size() == 5 : "Illegal argument, hand size != 5";

		int product = 1;
		for (final Card card : hand) {
			product *= card.getRank().getPrime();
		}
		return product;
	}

	public static boolean isFlush(final Collection<Card> hand) {
		assert hand.size() == 5 : "Illegal argument, hand size != 5";

		Iterator<Card> iterator = hand.iterator();
		final Suit suit = iterator.next().getSuit();

		while (iterator.hasNext()) {
			if (suit != iterator.next().getSuit()) {
				return false;
			}
		}

		return true;
	}

	public static int compareHands(Hand h1, Hand h2) {
		Collection<Card> hand1 = h1.getAsList();
		Collection<Card> hand2 = h2.getAsList();

		if (hand1.size() != 5) {
			hand1 = HandEvaluator.getBestFive(hand1);
		}

		if (hand2.size() != 5) {
			hand2 = HandEvaluator.getBestFive(hand2);
		}

		Integer rank1 = HandEvaluator.getRank(hand1);
		Integer rank2 = HandEvaluator.getRank(hand2);
		return rank2.compareTo(rank1);
	}

	public static Hand getBestFive(Hand hand) {
		return new Hand(HandEvaluator.getBestFive(hand.getAsList()));
	}

	public static HandInfo valueOf(final Collection<Card> hand) {
		assert hand.size() == 5 : "Illegal argument, hand size != 5";

		int product = HandEvaluator.getProduct(hand);
		boolean flush = HandEvaluator.isFlush(hand);
		return new HandInfo(product, flush);
	}

	// TODO delete this method
	public static String getDescription(Hand hand) {
		return getDescription(hand.getAsList());
	}
	
	// TODO delete this method
	public static String getShortDescription(Hand hand) {
		return getShortDescription(hand.getAsList());
	}
}
