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

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandTypeCalculator;

/**
 * A class to compare two straight hands.
 *
 * @author Cedric & Kenzo(refactoring)
 *
 */
public class CompareStraightHands extends CompareHands {

	/**
	 * Compares two straight hands.
	 *
	 * @param 	h1
	 *          The first hand
	 * @param 	h2
	 *          The second hand
	 *
	 * @throws	IllegalArgumentException
	 * 			if the given hands aren't straight hands
	 * 			| !HandTypeCalculator.checkForStraight(h1) || !HandTypeCalculator.checkForStraight(h2)
	 * @pre		The given hands consist of 5 cards
	 * 			| h1.getNBCards()==5 || h2.getNBCards()==5
	 * @return	1 = first hand is best, -1 = second hand is best, 0 = tie
	 * @return	One if the rank of the starting card of the straight of the first hand is greater than
	 * 			the rank of the starting card of the straight of the second hand
	 * 			| if(HandTypeCalculator.getDeterminatingCards(h1)[0].getRank().getValue()>
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[0].getRank().getValue())
	 * 			|	result==1
	 * @return	Minus one if the rank of the starting card of the straight of the first hand is smaller than
	 * 			the rank of the starting card of the straight of the second hand
	 * 			| if(HandTypeCalculator.getDeterminatingCards(h1)[0].getRank().getValue()<
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[0].getRank().getValue())
	 * 			|	result==1
	 * @return	Else result is zero
	 * 			| else result==0
	 */
	@Override
	public int compareHands(Hand h1, Hand h2) {
		if (!HandTypeCalculator.checkForStraight(h1)
				|| !HandTypeCalculator.checkForStraight(h2))
			throw new IllegalArgumentException();
		Card[] cards1 = HandTypeCalculator.getDeterminatingCards(h1);
		Card[] cards2 = HandTypeCalculator.getDeterminatingCards(h2);
		
		if (cards1[0].getRank().getValue() > cards2[0].getRank().getValue())
			return 1;
		if (cards1[0].getRank().getValue() < cards2[0].getRank().getValue())
			return -1;
		return 0;
	}

}
