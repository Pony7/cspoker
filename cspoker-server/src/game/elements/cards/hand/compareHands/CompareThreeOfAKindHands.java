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
 * A class to compare two three-of-a-kind hands.
 *
 * @author Cedric & Kenzo(refactoring)
 *
 */
public class CompareThreeOfAKindHands extends CompareHands {

	/**
	 * Compares two three-of-a-kind hands.
	 *
	 * @param 	h1
	 *          The first hand
	 * @param 	h2
	 *          The second hand
	 *
	 * @throws	IllegalArgumentException
	 * 			if the given hands aren't three-of-a-kind hands
	 * 			| !HandTypeCalculator.calculateHandType(h1).equals(HandType.THREE_OF_A_KIND) ||
	 * 			|			!HandTypeCalculator.calculateHandType(h2).equals(HandType.THREE_OF_A_KIND)
	 * @pre	The given hands consist of maximally 5 cards
	 * 		| h1.getNBCards()<=5 || h2.getNBCards()<=5
	 * @pre	The given hands consist of minimally 5 cards
	 * 		| h1.getNBCards()>=3 || h2.getNBCards()>=3
	 * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	 * @return	One if the three cards of equal rank of the fist hand have a greater rank than the three cards
	 * 			of equal rank of the second hand
	 * 			| if(HandTypeCalculator.getDeterminatingCards(h1)[0].getRank().getValue()>
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[0].getRank().getValue())
	 * 			|	result==1
	 * @return	Minus one if the three cards of equal rank of the fist hand have a lower rank than the three cards
	 * 			of equal rank of the second hand
	 * 			| if(HandTypeCalculator.getDeterminatingCards(h1)[0].getRank().getValue()<
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[0].getRank().getValue())
	 * 			|	result==-1
	 * @return	Else the remaining cards are compared
	 * 			| return HandEvaluator.compareHighCardHands(new Hand(h1).removeCard(
	 * 			|	HandTypeCalculator.getDeterminatingCards(h1)),new Hand(h2).removeCard(
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)));
	 */
	@Override
	public int compareHands(Hand h1, Hand h2) {
		if (!HandTypeCalculator.calculateHandType(h1).equals(
				HandType.THREE_OF_A_KIND)
				|| !HandTypeCalculator.calculateHandType(h2).equals(
						HandType.THREE_OF_A_KIND))
			throw new IllegalArgumentException();
		Card[] three1 = new Card[3];
		Card[] three2 = new Card[3];
		three1 = HandTypeCalculator.getDeterminatingCards(h1);
		three2 = HandTypeCalculator.getDeterminatingCards(h2);

		if (three1[0].getRank().getValue() > three2[0].getRank().getValue())
			return 1;
		if (three1[0].getRank().getValue() < three2[0].getRank().getValue())
			return -1;
		// clone hands and remove three-of-a-kind cards
		Hand temp1 = new Hand(h1);
		Hand temp2 = new Hand(h2);
		temp1.removeCard(three1);
		temp2.removeCard(three2);
		// compare the remaining card
		return HandType.HIGH_CARD.getEqualRankHandsComparator().compareHands(temp1, temp2);
	}

}
