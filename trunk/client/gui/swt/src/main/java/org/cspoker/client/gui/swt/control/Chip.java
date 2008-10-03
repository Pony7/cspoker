/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.gui.swt.control;

import java.util.*;

import org.eclipse.swt.graphics.Image;

/**
 * Represents a playing chip, associated with a value and an image.
 * 
 * @author stephans
 */
public class Chip
		implements Comparable<Chip> {
	
	public static final Chip DEALER = new Chip(0, "chip-d");
	
	public static final Chip ONE_CENT_CHIP = new Chip(1, "chip000001");
	public static final Chip FIVE_CENT_CHIP = new Chip(5, "chip000005");
	public static final Chip TWENTY_FIVE_CENT_CHIP = new Chip(25, "chip000025");
	public static final Chip ONE_DOLLAR_CHIP = new Chip(100, "chip0001");
	public static final Chip FIVE_DOLLAR_CHIP = new Chip(500, "chip0005");
	public static final Chip TWENTY_FIVE_DOLLAR_CHIP = new Chip(2500, "chip0025");
	public static final Chip HUNDRED_DOLLAR_CHIP = new Chip(10000, "chip0100");
	public static final Chip FIVE_HUNDRED_DOLLAR_CHIP = new Chip(50000, "chip0500");
	public static final Chip THOUSAND_DOLLAR_CHIP = new Chip(100000, "chip1000");
	public static final Chip FIVE_THOUSAND_DOLLAR_CHIP = new Chip(500000, "chip5000");
	public static final Chip TWENTY_FIVE_THOUSAND_DOLLAR_CHIP = new Chip(2500000, "chip25000");
	public static final Chip ONE_HUNDRED_THOUSAND_DOLLAR_CHIP = new Chip(10000000, "chip100000");
	public static final Chip FIVE_HUNDRED_THOUSAND_DOLLAR_CHIP = new Chip(50000000, "chip500000");
	public static final Chip ONE_MILLION_DOLLAR_CHIP = new Chip(100000000, "chip1000000");
	public static final Chip FIVE_MILLION_DOLLAR_CHIP = new Chip(500000000, "chip5000000");
	// Add more as necessary
	
	private final int value;
	
	/**
	 * The maximum image size. Possible image sizes are {@code 1 -
	 * #MAX_IMG_SIZE}
	 */
	public static final int MAX_IMG_SIZE = 6;
	private final String fileId;
	/** The chips that are available at the table as a NavigableSet */
	public static final NavigableSet<Chip> AVAILABLE_CHIPS = new TreeSet<Chip>(Arrays.asList(Chip.ONE_CENT_CHIP,
			Chip.FIVE_CENT_CHIP, Chip.TWENTY_FIVE_CENT_CHIP, Chip.ONE_DOLLAR_CHIP, Chip.FIVE_DOLLAR_CHIP,
			Chip.TWENTY_FIVE_DOLLAR_CHIP, Chip.HUNDRED_DOLLAR_CHIP, Chip.FIVE_HUNDRED_DOLLAR_CHIP,
			Chip.THOUSAND_DOLLAR_CHIP, Chip.FIVE_THOUSAND_DOLLAR_CHIP, Chip.TWENTY_FIVE_THOUSAND_DOLLAR_CHIP,
			Chip.ONE_HUNDRED_THOUSAND_DOLLAR_CHIP, Chip.FIVE_HUNDRED_THOUSAND_DOLLAR_CHIP,
			Chip.ONE_MILLION_DOLLAR_CHIP, Chip.FIVE_MILLION_DOLLAR_CHIP));
	
	/**
	 * Private chip creation constructor.
	 * 
	 * @param value The value of the chip (in cents)
	 * @param fileId The fileName (without mask end string ".a.bmp")
	 */
	private Chip(int value, String fileId) {
		this.fileId = fileId;
		this.value = value;
	}
	
	/**
	 * @param size Parameter indicating the desired size of the returned image.
	 * @return The corresponding chip image.
	 */
	public Image getImage(int size) {
		
		return SWTResourceManager.getChipImage(this, size);
		
	}
	
	/**
	 * @return The value of this chip in cents.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Returns a human-readable version of the chip. A typical example would be
	 * {@code $25.00 Chip}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ClientGUI.betFormatter.format(value) + " Chip";
	}
	
	/**
	 * Performs a comparison on the value of this chip.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Chip o) {
		return Integer.valueOf(getValue()).compareTo(Integer.valueOf(o.getValue()));
	}
	
	/**
	 * Given an amount returns a <i>Chip stack</i> in the form of a {@code
	 * NavigableMap}, where the keys are the Chips and the Values are the number
	 * of the given key chip. <br>
	 * <br>
	 * For example, a possible return value for {@code Chip#getDistribution(17)}
	 * would be {@code ($5.00 Chip=3),($1.00 Chip=2)}
	 * 
	 * @param amount The value of the chips
	 * @return A chip stack representing the given value
	 */
	public static NavigableMap<Chip, Integer> getDistribution(int amount) {
		assert (amount >= 0) : "Amount should be non-negative";
		NavigableMap<Chip, Integer> result = new TreeMap<Chip, Integer>();
		if (amount == 0) {
			return result;
		}
		int remainingToDistribute = amount;
		
		for (Chip c : AVAILABLE_CHIPS.descendingSet()) {
			int chipValue = c.getValue();
			int numberOfChips = 0;
			while (remainingToDistribute != 0 && (!(chipValue > remainingToDistribute) || chipValue == 1)) {
				numberOfChips++;
				remainingToDistribute -= chipValue;
			}
			result.put(c, numberOfChips);
		}
		return result.descendingMap();
	}
	
	public String getFileId() {
		return fileId;
	}
}
