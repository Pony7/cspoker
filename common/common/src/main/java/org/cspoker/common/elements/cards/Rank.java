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

import java.math.BigInteger;

/**
 * An enumeration to represent the different ranks a card can have.
 * 
 * @author Kenzo
 * 
 */
public enum Rank {
	DEUCE("Two", "2"), THREE("Three", "3"), FOUR("Four", "4"), FIVE("Five", "5"), SIX(
			"Six", "6"), SEVEN("Seven", "7"), EIGHT("Eight", "8"), NINE("Nine",
			"9"), TEN("Ten", "10"), JACK("Jack", "J"), QUEEN("Queen", "Q"), KING(
			"King", "K"), ACE("Ace", "A");

	private int prime;
	private String longDescription;
	private String shortDescription;

	private Rank(final String longDescription, final String shortDescription) {
		prime = PrimeInitializer.getNextPrime();
		this.longDescription = longDescription;
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public int getPrime() {
		return prime;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Returns a textual representation of this rank
	 */

	@Override
	public String toString() {
		return getShortDescription();
	}

	private static final class PrimeInitializer {
		private static BigInteger nextPrime = BigInteger.valueOf(1);

		public static int getNextPrime() {
			PrimeInitializer.nextPrime = PrimeInitializer.nextPrime
					.nextProbablePrime();
			return PrimeInitializer.nextPrime.intValue();
		}

		private PrimeInitializer() {
			// Utility class, should not be instantiated
		}
	}

	/**
	 * Returns the numeral value of this rank.
	 * 
	 * @return The numeral value of this rank.
	 */
	public int getValue() {
		return ordinal() + 2;
	}

	public static Rank getRank(String value) {
		for (Rank rank : Rank.values()) {
			if (rank.toString().equalsIgnoreCase(value)) {
				return rank;
			}
		}
		return null;
	}
}
