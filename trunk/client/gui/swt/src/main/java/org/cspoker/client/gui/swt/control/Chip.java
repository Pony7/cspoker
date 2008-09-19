package org.cspoker.client.gui.swt.control;

import java.util.*;

import org.eclipse.swt.graphics.Image;

public class Chip
		implements Comparable<Chip> {
	
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
	
	public static final int MAX_IMG_SIZE = 6;
	private final String fileId;
	private Hashtable<Integer, Image> imagesForDiffSizes = new Hashtable<Integer, Image>();
	public static final NavigableSet<Chip> AVAILABLE_CHIPS = new TreeSet<Chip>(Arrays.asList(Chip.ONE_CENT_CHIP,
			Chip.FIVE_CENT_CHIP, Chip.TWENTY_FIVE_CENT_CHIP, Chip.ONE_DOLLAR_CHIP, Chip.FIVE_DOLLAR_CHIP,
			Chip.TWENTY_FIVE_DOLLAR_CHIP, Chip.HUNDRED_DOLLAR_CHIP, Chip.FIVE_HUNDRED_DOLLAR_CHIP,
			Chip.THOUSAND_DOLLAR_CHIP, Chip.FIVE_THOUSAND_DOLLAR_CHIP, Chip.TWENTY_FIVE_THOUSAND_DOLLAR_CHIP,
			Chip.ONE_HUNDRED_THOUSAND_DOLLAR_CHIP, Chip.FIVE_HUNDRED_THOUSAND_DOLLAR_CHIP,
			Chip.ONE_MILLION_DOLLAR_CHIP, Chip.FIVE_MILLION_DOLLAR_CHIP));
	
	/**
	 * @param value The value of the chip (in cents)
	 * @param fileId The fileName (without mask end string ".a.bmp")
	 */
	private Chip(int value, String fileId) {
		this.fileId = fileId;
		this.value = value;
	}
	
	public Image getImage(int size) {
		
		if (imagesForDiffSizes.get(size) != null) {
			return imagesForDiffSizes.get(size);
		} else {
			Image chipImg = null;
			try {
				chipImg = SWTResourceManager.getChipFromPNG(ClientGUI.ACTIVE_CHIP_DIR, this);
			} catch (Exception e) {
				e.printStackTrace();
				String chipFile = ClientGUI.ACTIVE_CHIP_DIR + size + "/" + fileId;
				chipImg = SWTResourceManager.getChipImage(chipFile);
			}
			
			imagesForDiffSizes.put(size, chipImg);
			
			return chipImg;
		}
	}
	
	/**
	 * @param size The desired size of the image (1-6)
	 * @return A dealer chip image
	 */
	public static Image getDealerChip(int size) {
		String chipFile;
		Image img;
		try {
			chipFile = ClientGUI.ACTIVE_CHIP_DIR + size + "/" + "chip-d";
			img = SWTResourceManager.getChipImage(chipFile);
		} catch (Exception e) {
			chipFile = ClientGUI.STARS_CHIP_IMG_DIR + size + "/" + "chip-d";
			img = SWTResourceManager.getChipImage(chipFile);
		}
		img = SWTResourceManager.getChipImage(chipFile);
		return img;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return ClientGUI.formatBet(value) + " Chip ";
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Chip o) {
		return Integer.valueOf(getValue()).compareTo(Integer.valueOf(o.getValue()));
	}
	
	/**
	 * Example: getDistribution(32) returns {25=1, 5=1, 1=2}
	 * 
	 * @param amount The value of the chips
	 * @return A "chip stack" representing the value of the given amount in
	 *         chips
	 */
	public static NavigableMap<Chip, Integer> getDistribution(int amount) {
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
}
