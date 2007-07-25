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
package game.elements.cards.hand.compareHands;

import game.elements.cards.Card;
import game.elements.cards.hand.Hand;
import game.elements.cards.hand.HandType;
import game.elements.cards.hand.HandTypeCalculator;

/**
 * A class to compare two pair hands.
 *
 * @author Cedric & Kenzo(refactoring)
 *
 */
public class ComparePairHands extends CompareHands {

	/**
	 * Compares two pair hands.
	 *
	 * @param 	h1
	 *          The first hand
	 * @param 	h2
	 *          The second hand
	 *
	 * @throws	IllegalArgumentException
	 * 			if the given hands aren't pair hands
	 * 			| !HandTypeCalculator.calculateHandType(h1).equals(HandType.PAIR) ||
	 * 			|			!HandTypeCalculator.calculateHandType(h2).equals(HandType.PAIR)
	 * @pre		The given hands consist of maximally 5 cards
	 * 			| h1.getNBCards()<=5 || h2.getNBCards()<=5
	 * @pre		The given hands consist of minimally 2 cards
	 * 			| h1.getNBCards()>=2 || h2.getNBCards()>=2
	 * @return 	1 = first hand is best, -1 = second hand is best, 0 = tie
	 * @return	One if the pair of the fist hand has a greater rank than the pair of the second hand
	 * 			| if(HandTypeCalculator.getDeterminatingCards(h1)[0].getRank().getValue()>
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[0].getRank().getValue())
	 * 			|	result==1
	 * @return	Minus one if the pair of the fist hand has a lower rank than the pair of the second hand
	 * 			| if(HandTypeCalculator.getDeterminatingCards(h1)[0].getRank().getValue()<
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[0].getRank().getValue())
	 * 			|	result==-1
	 * @return	Else the remaining cards are compared
	 * 			| return HandEvaluator.compareHighCardHands(new Hand(h1).removeCard(pair1),new Hand(h2).removeCard(pair2));
	 */
	@Override
	public int compareHands(Hand h1, Hand h2) {
		if (!HandTypeCalculator.calculateHandType(h1).equals(HandType.PAIR)
				|| !HandTypeCalculator.calculateHandType(h2).equals(
						HandType.PAIR))
			throw new IllegalArgumentException();
		Card[] pair1 = new Card[2];

		Card[] pair2 = new Card[2];
		pair1 = HandTypeCalculator.getDeterminatingCards(h1);
		pair2 = HandTypeCalculator.getDeterminatingCards(h2);

		if (pair1[0].getRank().getValue() > pair2[0].getRank().getValue())
			return 1;
		if (pair1[0].getRank().getValue() < pair2[0].getRank().getValue())
			return -1;

		Hand temp1 = new Hand(h1);
		Hand temp2 = new Hand(h2);
		temp1.removeCard(pair1);
		temp2.removeCard(pair2);

		return HandType.HIGH_CARD.getEqualRankHandsComparator().compareHands(temp1, temp2);
	}
}
