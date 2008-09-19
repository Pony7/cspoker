/**
 * 
 */
package org.cspoker.client.gui.swt.control;

import java.util.Collection;

import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.common.elements.cards.Card;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;

/**
 * A {@link PaintListener} for updating the cards displayed in a
 * {@link GameWindow}. Subject to refactoring (not very object-oriented
 * connection to the other composites
 * 
 * @author stephans
 */
public class CardPaintListener
		implements PaintListener {
	
	Collection<Card> cards;
	private int numberOfTotalCards;
	private int alignment;
	private int spacing;
	
	public CardPaintListener(Collection<Card> cards, int numberOfTotalCards, int alignment, int spacing) {
		this.cards = cards;
		this.numberOfTotalCards = numberOfTotalCards;
		this.alignment = alignment;
		this.spacing = spacing;
		
	}
	
	/**
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	public void paintControl(PaintEvent e) {
		if (cards.size() == 0)
			return;
		int x = -1;
		int cardHeight = e.height;
		int cardWidth = Math.max(0, (e.width - spacing * (numberOfTotalCards - 1)) / numberOfTotalCards);
		
		for (Card c : cards) {
			final Image cardImg = SWTResourceManager.getCardImage(c);
			int origHeight = cardImg.getBounds().height;
			int origWidth = cardImg.getBounds().width;
			
			double scaleFactor = Math.min((double) cardHeight / (double) origHeight, (double) cardWidth
					/ (double) origWidth);
			assert (scaleFactor > 0) : "Wrong scale factor computed";
			int scaledHeight = (int) (origHeight * scaleFactor);
			int scaledWidth = (int) (origWidth * scaleFactor);
			
			if (x == -1 && alignment == SWT.CENTER) {
				x = ((e.width - scaledWidth * cards.size()) - (spacing * (cards.size() - 1))) / 2;
			} else if (x == -1 && alignment == SWT.LEFT) {
				x = 0;
			}
			// Ensure x is not negative ...
			x = Math.max(0, x);
			try {
				e.gc.drawImage(cardImg, 0, 0, origWidth, origHeight, x, 0, scaledWidth, scaledHeight);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			}
			x += (scaledWidth + spacing);
			
		}
	}
}
