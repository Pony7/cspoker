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

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.cspoker.client.gui.swt.window.PlayerSeatComposite;
import org.cspoker.client.gui.swt.window.TableComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * A {@link PaintListener} for handling the redrawing of chips on the
 * {@link TableComposite}, i.e. after bet actions or chip animations.
 * <p>
 * 
 * @author Stephan Schmidt
 */
public class ChipPaintListener
		implements PaintListener {
	
	private final static Logger logger = Logger.getLogger(ChipPaintListener.class);
	
	private TableComposite tableComposite = null;
	
	/**
	 * Creates a new PaintListener for Chips
	 * 
	 * @param tc The {@link TableComposite}. This parameter suffices, the
	 *            relevant info from the {@link PlayerSeatComposite}s can be
	 *            retrieved via the tc.
	 */
	public ChipPaintListener(TableComposite tc) {
		tableComposite = tc;
	}
	
	/**
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	
	public void paintControl(PaintEvent e) {
		int size = Math.min(Chip.MAX_IMG_SIZE, tableComposite.getSize().x / 200);
		// Draw bet chips for each player who has put chips in front of him this
		// round
		try {
			for (PlayerSeatComposite pc : tableComposite.getPlayerSeatComposites(true)) {
				if (pc.getPlayer().getCurrentBetPile().size() > 0)
					drawChips(e.gc, pc.getBetChipsDisplayArea(), pc.getPlayer().getCurrentBetPile(), size, false);
				// Draw dealer chip
				if (pc.getPlayer().isDealer()) {
					e.gc.drawImage(Chip.DEALER.getImage(size), pc.getInitialChipDrawOffset().x - 8 * size, pc
							.getInitialChipDrawOffset().y
							- 6 * size);
				}
			}
			// Draw pot
			drawChips(e.gc, tableComposite.getPotDisplayArea(), Arrays.asList(Chip.getDistribution(tableComposite
					.getMoneyInPot())), size, true);
		} catch (FileNotFoundException fnf) {
			logger.error("Chip image to draw not found, skipping chip draw process", fnf);
		}
	}
	
	/**
	 * Do the actual drawing of chip images on the {@link TableComposite}
	 * surface
	 * 
	 * @param gc The {@link GC}
	 * @param area Where the chips are to be drawn
	 * @param chipPiles The chips to draw
	 * @param size The desired size of the chips
	 * @param putDifferentValuesOnSeparatePiles Whether you want to mix
	 *            different chip values in the same pile
	 */
	private void drawChips(GC gc, Rectangle area, List<NavigableMap<Chip, Integer>> chipPiles, int size,
			boolean putDifferentValuesOnSeparatePiles)
			throws FileNotFoundException {
		int amount = Chip.getValue(chipPiles);
		if (area == null || amount == 0)
			return;
		
		int xCoord = area.x;
		int standardXDistance = Chip.ONE_CENT_CHIP.getImage(size).getBounds().width + size;
		int standardYLocation = area.y + area.height - Chip.ONE_CENT_CHIP.getImage(size).getBounds().height;
		// Iterate over the chip piles.
		for (NavigableMap<Chip, Integer> chipPile : chipPiles) {
			
			int yCoord = standardYLocation;
			int totalChips = 0;
			// Iterate over the chip pile entries
			for (Entry<Chip, Integer> entry : chipPile.entrySet()) {
				
				if (entry.getValue() == 0)
					continue;
				
				Image chipImg = entry.getKey().getImage(size);
				for (int i = 0; i < entry.getValue(); i++) {
					if (totalChips > 7) {
						// Once we reach 8, shift to the right and open a new
						// pile
						// Update the offset
						xCoord += standardXDistance;
						yCoord = standardYLocation;
						totalChips = 0;
					}
					// While there are still chips of the same value in the
					// pile, stack them on top of each other
					gc.drawImage(chipImg, xCoord, yCoord);
					yCoord -= (size + 1);
					totalChips++;
					
				}
				if (putDifferentValuesOnSeparatePiles) {
					// If we dont want chips of different values on top of each
					// other, open up a new pile before going to the next chip
					// type
					// Update the offset
					xCoord += standardXDistance;
					yCoord = standardYLocation;
					totalChips = 0;
				}
			}
			xCoord += standardXDistance;
		}
		// Annotate the chip pile image with a textual display of the bet amount
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
		gc.drawText(ClientGUI.formatBet(amount), xCoord, standardYLocation);
		return;
	}
}
