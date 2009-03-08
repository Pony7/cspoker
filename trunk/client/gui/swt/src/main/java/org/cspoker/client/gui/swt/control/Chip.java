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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Image;

/**
 * Represents a playing chip, associated with a value and an image.
 * <p>
 * Since <i>Chips</i> have attached image information, they will contain path
 * information and file information which is non-generic. Could be changed in
 * later versions.
 * 
 * @author Stephan Schmidt
 */
public class Chip
		implements Comparable<Chip> {
	
	/**
	 * Delaer Chip indicating visually the dealer of the current hand. Has a
	 * value of 0.
	 */
	public static final Chip DEALER = new Chip(0, "chip-d");
	
	/** 1c chip */
	public static final Chip ONE_CENT_CHIP = new Chip(1, "chip000001");
	/** 5c chip */
	public static final Chip FIVE_CENT_CHIP = new Chip(5, "chip000005");
	/** 25c chip */
	public static final Chip TWENTY_FIVE_CENT_CHIP = new Chip(25, "chip000025");
	/** 1$ chip */
	public static final Chip ONE_DOLLAR_CHIP = new Chip(100, "chip0001");
	/** 5$ chip */
	public static final Chip FIVE_DOLLAR_CHIP = new Chip(500, "chip0005");
	/** 25$ chip */
	public static final Chip TWENTY_FIVE_DOLLAR_CHIP = new Chip(2500, "chip0025");
	/** 100$ chip */
	public static final Chip HUNDRED_DOLLAR_CHIP = new Chip(10000, "chip0100");
	/** 500$ chip */
	public static final Chip FIVE_HUNDRED_DOLLAR_CHIP = new Chip(50000, "chip0500");
	/** 1,000$ chip */
	public static final Chip THOUSAND_DOLLAR_CHIP = new Chip(100000, "chip1000");
	/** 5,000$ chip */
	public static final Chip FIVE_THOUSAND_DOLLAR_CHIP = new Chip(500000, "chip5000");
	
	// Only for tournaments usually :D
	
	/** 25,000$ chip */
	public static final Chip TWENTY_FIVE_THOUSAND_DOLLAR_CHIP = new Chip(2500000, "chip25000");
	/** 100,000$ chip */
	public static final Chip ONE_HUNDRED_THOUSAND_DOLLAR_CHIP = new Chip(10000000, "chip100000");
	/** 500,000$ chip */
	public static final Chip FIVE_HUNDRED_THOUSAND_DOLLAR_CHIP = new Chip(50000000, "chip500000");
	/** 1,000,000$ chip */
	public static final Chip ONE_MILLION_DOLLAR_CHIP = new Chip(100000000, "chip1000000");
	/** 5,000,000$ chip */
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
	
	public static final int MAX_CHIPS_IN_PILE = 7;
	
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
		Image chipImg = null;
		try {
			chipImg = SWTResourceManager.getChipImage(this, size);
		} catch (IOException e) {
			throw new IllegalArgumentException("Chip image not found", e);
		}
		return chipImg;
		
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
		return ClientGUI.formatBet(value) + " Chip";
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
	
	/**
	 * Helper method
	 * 
	 * @param chipPiles A List of chip piles to evaluate
	 * @return The monetary value of the given chip piles
	 */
	public static int getValue(List<NavigableMap<Chip, Integer>> chipPiles) {
		int amount = 0;
		for (Map<Chip, Integer> chips : chipPiles) {
			for (Entry<Chip, Integer> chipEntry : chips.entrySet()) {
				amount += chipEntry.getKey().getValue() * chipEntry.getValue();
			}
		}
		return amount;
	}

	public InputStream getImageStream(int size) {
		String path = getImagePath(size);
		InputStream stream = ClientGUI.getResource(path);
		if (stream==null) {
			stream = ClientGUI.getResource(ClientGUI.Resources.ACTIVE_CHIP_DIR + fileId + ".bmp");
		}
		return stream;
	}

	public String getImagePath(int size) {
		return ClientGUI.Resources.ACTIVE_CHIP_DIR + size + "/" + fileId + ".bmp";
	}
	
	/**
	 * @param size The desired size
	 * @return The file location of the mask image, depending on the
	 *         {@link ClientGUI.Resources#ACTIVE_CHIP_DIR}
	 */
	public InputStream getMaskImageStream(int size) {
		String path = ClientGUI.Resources.ACTIVE_CHIP_DIR + size + "/" + fileId + ".a.bmp";
		InputStream stream = ClientGUI.getResource(path);
		if (stream==null) {
			stream = ClientGUI.getResource(ClientGUI.Resources.ACTIVE_CHIP_DIR + fileId + ".bmp");
		}
		return stream;
	}
	
	/**
	 * Returns <code>true</code> if the Chip values are the same
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Chip)) {
			return false;
		}
		return getValue() == ((Chip) obj).getValue();
		
	}
	
	/**
	 * Uses {@link #value} as the hash code
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return value;
	}
}
