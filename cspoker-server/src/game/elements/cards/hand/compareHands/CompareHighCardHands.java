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

import game.elements.cards.hand.Hand;

/**
 * A class to compare two high cards hands.
 *
 * @author Cedric & Kenzo(refactoring)
 *
 */
public class CompareHighCardHands extends CompareHands {

	/**
	 * Compares two high cards hands.
	 *
	 * @param 	h1
	 *          The first hand
	 * @param 	h2
	 *          The second hand
	 *
	 * @pre	The given hands consist of maximally 5 cards
	 * 		| h1.getNBCards()<=5 || h2.getNBCards()<=5
	 * @pre	The given hands consist of minimally 1 card
	 * 		| h1.getNBCards()>=1 || h2.getNBCards()>=1
	 * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	 */
	@Override
	public int compareHands(Hand h1, Hand h2) {
		Hand temp1 = new Hand(h1);
		Hand temp2 = new Hand(h2);
		temp1.sort();
		temp2.sort();
		for (int j = 0; j < Math.min(Math.min(h1.getNBCards(), 5), h2
				.getNBCards()); j++) {
			if (temp1.getCard(j).getRank().getValue() > temp2.getCard(j)
					.getRank().getValue())
				return 1;
			if (temp1.getCard(j).getRank().getValue() < temp2.getCard(j)
					.getRank().getValue())
				return -1;
		}
		return 0;
	}

}
