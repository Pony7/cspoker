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

package org.cspoker.common.elements.cards;


/**
 * An enumeration to represent the different suits a card can have.
 * 
 * @author Kenzo
 * 
 * 
 */
public enum Suit {

	CLUBS("Clubs", "c"), DIAMONDS("Diamonds", "d"), HEARTS("Hearts", "h"), SPADES(
			"Spades", "s");

	private String longDescription;
	private String shortDescription;

	private Suit(final String longDescription, final String shortDescription) {
		this.longDescription = longDescription;
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Returns a textual representation of this suit
	 */

	public String toString() {
		return getShortDescription();
	}

	/**
	 * Returns the relative value of this suit (clubs ranking the lowest,
	 * followed by diamonds, hearts, and spades as in bridge)
	 */
	public int getValue() {
		return ordinal();
	}

	public static Suit getSuit(String value) {
		for (Suit rank : Suit.values()) {
			if (rank.toString().equalsIgnoreCase(value)) {
				return rank;
			}
		}
		return null;
	}
}
