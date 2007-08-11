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

/**
 * A class to compare two hands.
 *
 * @author Cedric & Kenzo(refactoring)
 *
 */
public abstract class CompareHands {

	/**
	 * Compares 2 hands from the same hand type.
	 *
	 * @param 	h1
	 *          The first hand
	 * @param 	h2
	 *          The second hand
	 *
	 * @throws  IllegalArgumentException [can]
	 * 			The given hands are not from the same hand type.
	 *
	 * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	 */
	public abstract int compareHands(Hand h1,Hand h2);

}
