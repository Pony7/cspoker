/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.server.game.elements.cards.hand.compareHands;

import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandType;
import org.cspoker.server.game.elements.cards.hand.HandTypeCalculator;

/**
 * A class to compare two straight-flush hands.
 *
 * @author Cedric & Kenzo(refactoring)
 *
 */
public class CompareStraightFlushHands extends CompareHands {

	/**
	 * Compares two straight-flush hands.
	 *
	 * @param 	h1
	 *          The first hand
	 * @param 	h2
	 *          The second hand
	 *
	 * @throws	IllegalArgumentException
	 * 			if the given hands aren't flush hands
	 * 			| !HandTypeCalculator.calculateHandType(h1).equals(STRAIGHT_FLUSH) ||
	 * 			|			!HandTypeCalculator.calculateHandType(h2).equals(STRAIGHT_FLUSH)
	 * @pre		The given hands consist of 5 cards
	 * 			| h1.getNBCards()==5 || h2.getNBCards()==5
	 * @return 	1 = first hand is best, -1 = second hand is best, 0 = tie
	 * @return	the hands are compared as straight hands
	 * 			| result == compareStraightHands(h1,h2)
	 */
	@Override
	public int compareHands(Hand h1, Hand h2) {
		if (!HandTypeCalculator.calculateHandType(h1).equals(
				HandType.STRAIGHT_FLUSH)
				|| !HandTypeCalculator.calculateHandType(h2).equals(
						HandType.STRAIGHT_FLUSH))
			throw new IllegalArgumentException();
		return HandType.STRAIGHT.getEqualRankHandsComparator().compareHands(h1, h2);
	}

}
