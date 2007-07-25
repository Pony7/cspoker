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
 * A class to compare two double-pair hands.
 *
 * @author Cedric & Kenzo(refactoring)
 *
 */
public class CompareDoublePairHands extends CompareHands{

	/**
	 * Compares two given double-pair hands.
	 *
	 * @param 	h1
	 *          The first hand
	 * @param 	h2
	 *          The second hand
	 *
	 * @throws	IllegalArgumentException
	 * 			if the given hands aren't double-pair hands
	 * 			| !HandTypeCalculator.calculateHandType(h1).equals(HandType.DOUBLE_PAIR) ||
	 * 			|			!HandTypeCalculator.calculateHandType(h2).equals(HandType.DOUBLE_PAIR)
	 * @pre		The given hands consist of maximally 5 cards
	 * 			| h1.getNBCards()<=5 || h2.getNBCards()<=5
	 * @pre 	The given hands consist of minimally 4 cards
	 * 			| h1.getNBCards()>=4 || h2.getNBCards()>=4
	 * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	 * @return	One if the first pair of the fist hand has a greater rank than the first pair of the second hand
	 * 			| if(HandTypeCalculator.getDeterminatingCards(h1)[0].getRank().getValue()>
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[0].getRank().getValue())
	 * 			|	result==1
	 * @return	Minus one if the first pair of the fist hand has a lower rank than the first pair of the second hand
	 * 			| if(HandTypeCalculator.getDeterminatingCards(h1)[0].getRank().getValue()<
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[0].getRank().getValue())
	 * 			|	result==-1
	 * @return	One if the first pair of the fist hand has an equal rank as the first pair of the second hand
	 * 			and the second pair of the first hand has a greater rank than the second pair of the second hand
	 * 			| if(HandTypeCalculator.getDeterminatingCards(h1)[0].getRank().getValue()==
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[0].getRank().getValue()
	 * 			|		&& HandTypeCalculator.getDeterminatingCards(h1)[2].getRank().getValue()>
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[2].getRank().getValue())
	 * 			|	result==1
	 * @return	Minus one if the first pair of the fist hand has an equal rank as the first pair of the second hand
	 * 			and the second pair of the first hand has a lower rank than the second pair of the second hand
	 * 			| if(HandTypeCalculator.getDeterminatingCards(h1)[0].getRank().getValue()==
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[0].getRank().getValue()
	 * 			|		&& HandTypeCalculator.getDeterminatingCards(h1)[2].getRank().getValue()<
	 * 			|		HandTypeCalculator.getDeterminatingCards(h2)[2].getRank().getValue())
	 * 			|	result==1
	 * @return	Else the non-pair cards are compared if the given hands have 5 cards
	 * 			| else if(h1.getNBCards()==5 && h1.getNBCards()==5)
	 * 			|		return (h1.removeCard(HandTypeCalculator.getDeterminatingCards(h1))).getCard(0).compareTo(
	 * 			|				h2.removeCard(HandTypeCalculator.getDeterminatingCards(h2))).getCard(0))
	 * @return	Else return 0
	 * 			| else result == 0
	 */
	@Override
	public int compareHands(Hand h1, Hand h2) {
		if (!HandTypeCalculator.calculateHandType(h1).equals(
				HandType.DOUBLE_PAIR)
				|| !HandTypeCalculator.calculateHandType(h2).equals(
						HandType.DOUBLE_PAIR))
			throw new IllegalArgumentException();
		Card[] card1 = new Card[4];
		Card[] card2 = new Card[4];

		card1 = HandTypeCalculator.getDeterminatingCards(h1);
		card2 = HandTypeCalculator.getDeterminatingCards(h2);

		if (card1[0].getRank().getValue() > card2[0].getRank().getValue())
			return 1;
		if (card1[0].getRank().getValue() < card2[0].getRank().getValue())
			return -1;
		if (card1[2].getRank().getValue() > card2[2].getRank().getValue())
			return 1;
		if (card1[2].getRank().getValue() < card2[2].getRank().getValue())
			return -1;
		// clone hands and remove pair cards
		Hand temp1 = new Hand(h1);
		Hand temp2 = new Hand(h2);
		temp1.removeCard(card1);
		temp2.removeCard(card2);
		// compare the remaining card
		return temp1.getCard(0).compareTo(temp2.getCard(0));
	}

}
