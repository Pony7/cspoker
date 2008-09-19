package org.cspoker.client.gui.swt.control;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Map.Entry;

import org.cspoker.client.gui.swt.window.PlayerSeatComposite;
import org.cspoker.client.gui.swt.window.TableComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

public class ChipPaintListener
		implements PaintListener {
	
	private TableComposite tableComposite = null;
	
	public ChipPaintListener(TableComposite tc) {
		tableComposite = tc;
	}
	
	@Override
	public void paintControl(PaintEvent e) {
		int size = Math.min(Chip.MAX_IMG_SIZE, tableComposite.getSize().x / 200);
		for (PlayerSeatComposite pc : tableComposite.getPlayerSeatComposites()) {
			if (pc.getPlayer().getBetChipsValue() > 0)
				drawChipImage(e.gc, pc, size);
			// Draw dealer chip
			if (pc.isDealer()) {
				e.gc.drawImage(Chip.getDealerChip(size), pc.getInitialChipDrawOffset().x - 8 * size, pc
						.getInitialChipDrawOffset().y
						- 6 * size);
			}
		}
		// Draw pot
		try {
			drawChipImage(e.gc, null, size);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
	}
	
	public void drawChipImage(GC gc, PlayerSeatComposite pc, int size) {
		List<NavigableMap<Chip, Integer>> chipPiles = new ArrayList<NavigableMap<Chip, Integer>>();
		Rectangle area;
		int amount = 0;
		if (pc != null) {
			if (tableComposite.updateChipLocations) {
				pc.updateBetChipsDisplayArea();
			}
			area = pc.getBetChipsDisplayArea();
			chipPiles = pc.getCurrentBetPile();
			amount = GameState.getValue(chipPiles);
		} else {
			if (tableComposite.updateChipLocations) {
				tableComposite.updateGetPotChipsArea();
			}
			area = tableComposite.getPotOffset();
			
			amount = tableComposite.getGameState().getMoneyInMiddle();
			chipPiles.add(Chip.getDistribution(amount));
		}
		if (area == null || amount == 0)
			return;
		
		int xCoord = area.x;
		int standardXDistance = Chip.ONE_CENT_CHIP.getImage(size).getBounds().width + size;
		for (NavigableMap<Chip, Integer> chipDistro : chipPiles) {
			int yCoord = area.y;
			int totalChips = 0;
			for (Entry<Chip, Integer> entry : chipDistro.entrySet()) {
				if (entry.getValue() == 0)
					continue;
				Image chipImg = entry.getKey().getImage(size);
				for (int i = 0; i < entry.getValue(); i++) {
					if (totalChips > 8) {
						xCoord += standardXDistance;
						yCoord = area.y;
						totalChips = 0;
					}
					gc.drawImage(chipImg, xCoord, yCoord);
					yCoord -= (size + 1);
					totalChips++;
				}
				
				if (pc == null) {
					xCoord += standardXDistance;
					totalChips = 0;
					yCoord = area.y;
				}
			}
			// Update the offset
			xCoord += standardXDistance;
			totalChips = 0;
		}
		
		gc.drawText(ClientGUI.formatBet(amount), area.x, area.y + 50);
		
		return;
	}
}
