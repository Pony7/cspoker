/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.gui.swt.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

/**
 * Class to manage SWT resources (Font, Color, Image and Cursor) There are no
 * restrictions on the use of this code. You may change this code and your
 * changes will not be overwritten, but if you change the version number below
 * then this class will be completely overwritten by Jigloo.
 * #SWTResourceManager:version4.0.0#
 */
public class SWTResourceManager {
	
	private final static Logger logger = Logger.getLogger(SWTResourceManager.class);
	
	private static HashMap<String, Resource> resources = new HashMap<String, Resource>();
	private static Vector<Widget> users = new Vector<Widget>();
	private static DisposeListener disposeListener = new DisposeListener() {
		
		public void widgetDisposed(DisposeEvent e) {
			users.remove(e.getSource());
			if (users.size() == 0)
				dispose();
		}
	};
	
	/**
	 * This method should be called by *all* Widgets which use resources
	 * provided by this SWTResourceManager. When widgets are disposed, they are
	 * removed from the "users" Vector, and when no more registered Widgets are
	 * left, all resources are disposed.
	 * <P>
	 * If this method is not called for all Widgets then it should not be called
	 * at all, and the "dispose" method should be explicitly called after all
	 * resources are no longer being used.
	 */
	public static void registerResourceUser(Widget widget) {
		if (users.contains(widget))
			return;
		users.add(widget);
		widget.addDisposeListener(disposeListener);
	}
	
	/**
	 * Disposes of all cached resources.
	 */
	public static void dispose() {
		for (Resource r : resources.values()) {
			if (r != null && !r.isDisposed())
				r.dispose();
		}
		resources.clear();
	}
	
	public static Font getFont(String name, int size, int style) {
		return getFont(name, size, style, false, false);
	}
	
	public static Font getFont(String name, int size, int style, boolean strikeout, boolean underline) {
		String fontName = name + "|" + size + "|" + style + "|" + strikeout + "|" + underline;
		if (resources.containsKey(fontName))
			return (Font) resources.get(fontName);
		FontData fd = new FontData(name, size, style);
		if (strikeout || underline) {
			try {
				Class<?> lfCls = Class.forName("org.eclipse.swt.internal.win32.LOGFONT");
				Object lf = FontData.class.getField("data").get(fd);
				if (lf != null && lfCls != null) {
					if (strikeout)
						lfCls.getField("lfStrikeOut").set(lf, Byte.valueOf((byte) 1));
					if (underline)
						lfCls.getField("lfUnderline").set(lf, Byte.valueOf((byte) 1));
				}
			} catch (Throwable e) {
				System.err.println("Unable to set underline or strikeout" + " (probably on a non-Windows platform). "
						+ e);
			}
		}
		Font font = new Font(Display.getDefault(), fd);
		resources.put(fontName, font);
		return font;
	}
	
	public static Color getColor(int red, int green, int blue) {
		String name = "COLOR:" + red + "," + green + "," + blue;
		if (resources.containsKey(name))
			return (Color) resources.get(name);
		Color color = new Color(Display.getDefault(), red, green, blue);
		resources.put(name, color);
		return color;
	}
	
	public static Cursor getCursor(int type) {
		String name = "CURSOR:" + type;
		if (resources.containsKey(name))
			return (Cursor) resources.get(name);
		Cursor cursor = new Cursor(Display.getDefault(), type);
		resources.put(name, cursor);
		return cursor;
	}
	
	/***************************************************************************
	 * Image methods
	 **************************************************************************/
	
	/**
	 * Delegates to {@link SWTResourceManager#getImage(String)}.
	 * <p>
	 * TODO Check whether this works with the path separator char needed by the
	 * classloader in the getImage(String) method
	 * 
	 * @param file The file representing an image file
	 * @return The image contained in the file
	 */
	public static Image getImage(File file) {
		
		if (resources.containsKey(file.toString()))
			return (Image) resources.get(file.toString());
		Image img = null;
		FileInputStream imgStream = null;
		try {
			try {
				imgStream = new FileInputStream(file.getAbsoluteFile());
			} catch (FileNotFoundException e) {
				logger.error("File not found", e);
			}
			img = new Image(Display.getDefault(), imgStream);
		} finally {
			if (imgStream != null)
				try {
					imgStream.close();
				} catch (IOException ignored) {
					// Ignore
				}
		}
		if (!file.isDirectory()) {
			resources.put(file.toString(), img);
		}
		return img;
		
	}
	
	/**
	 * @param card The playing card
	 * @return The image for the card
	 */
	public static Image getCardImage(Card card) {
		Image img = null;
		if (resources.containsKey(card.toString()))
			return (Image) resources.get(card.toString());
		if (ClientGUI.Resources.ACTIVE_DECK_IMG_FILE.isFile()) {
			Image deck = getImage(ClientGUI.Resources.ACTIVE_DECK_IMG_FILE);
			img = getCardFromDeck(deck, card);
		} else {
			img = getImage(new File(ClientGUI.Resources.ACTIVE_DECK_IMG_FILE, card.toString() + ".png"));
		}
		
		resources.put(card.toString(), img);
		return img;
		
	}
	
	public static Image getCardFromDeck(Image deck, Card card) {
		// Calculate drawing values
		int xLookUp, yLookUp;
		if (card.getRank() == null || card.getSuit() == null) {
			xLookUp = 14;
			yLookUp = 1;
		} else {
			logger.debug("Card: " + card + "x: " + card.getRank().ordinal() + ", y: " + card.getSuit().ordinal());
			xLookUp = Rank.values().length - card.getRank().ordinal();
			yLookUp = Suit.values().length - card.getSuit().ordinal();
		}
		Image cardImg = getImageFromCollection(deck, new Point(14, 6), new Point(xLookUp, yLookUp));
		
		return cardImg;
	}
	
	private static Image getChipFromPNG(Chip chip) {
		if (resources.containsKey(chip.toString()))
			return (Image) resources.get(chip.toString());
		Image chipPngImg = getImage(new File(ClientGUI.Resources.FREE_CHIPS, "Chips_Free.png"));
		if (chipPngImg == null)
			return null;
		// Calculate drawing values
		int xLookUp, yLookUp;
		int indexInSet = Chip.AVAILABLE_CHIPS.headSet(chip, false).size();
		
		xLookUp = indexInSet % 5 + 1;
		yLookUp = indexInSet / 5 + 1;
		Image chipImg = getImageFromCollection(chipPngImg, new Point(5, 3), new Point(xLookUp, yLookUp));
		
		resources.put(chip.toString(), chipImg);
		return chipImg;
	}
	
	/**
	 * @param chip Teh chip
	 * @param size Desired size (1-6)
	 * @return The chip image
	 * @throws IOException If some kind of error occurs while retrieving the
	 *             image resource
	 */
	public static Image getChipImage(Chip chip, int size)
			throws IOException {
		Image chipImg = null;
		if (ClientGUI.Resources.ACTIVE_CHIP_DIR.equals(ClientGUI.Resources.FREE_CHIPS)) {
			if (chip != Chip.DEALER) {
				chipImg = SWTResourceManager.getChipFromPNG(chip);
			}
		}
		if (chipImg != null)
			return chipImg;
		if (resources.containsKey(chip.getImageFile(size).toString()))
			return (Image) resources.get(chip.getImageFile(size).toString());
		
		Image img = null;
		Image mask = null;
		Image icon = null;
		FileInputStream imgStream = null;
		FileInputStream maskStream = null;
		try {
			imgStream = new FileInputStream(chip.getImageFile(size).getAbsoluteFile());
			maskStream = new FileInputStream(chip.getMaskImageFile(size).getAbsoluteFile());
			
			img = new Image(Display.getDefault(), imgStream);
			mask = new Image(Display.getDefault(), maskStream);
			icon = new Image(Display.getDefault(), img.getImageData(), mask.getImageData());
			
		} catch (SWTException swtEx) {
			throw new IOException(swtEx);
		} finally {
			if (imgStream != null)
				imgStream.close();
			if (maskStream != null)
				maskStream.close();
		}
		img.dispose();
		mask.dispose();
		resources.put(chip.getImageFile(size).toString(), icon);
		return icon;
	}
	
	private static Image getImageFromCollection(Image collectionImg, Point collectionDimensions, Point imageLocation) {
		ImageData data = collectionImg.getImageData();
		int srcX = data.width / collectionDimensions.x * (imageLocation.x - 1);
		int srcY = data.height / collectionDimensions.y * (imageLocation.y - 1);
		int srcWidth = data.width / collectionDimensions.x;
		int srcHeight = data.height / collectionDimensions.y;
		Image img = new Image(Display.getDefault(), srcWidth, srcHeight);
		GC gc = new GC(img);
		// Draw the image
		gc.drawImage(collectionImg, srcX, srcY, srcWidth, srcHeight, 0, 0, img.getImageData().width,
				img.getImageData().height);
		Image mask = new Image(Display.getDefault(), img.getBounds());
		img = new Image(Display.getDefault(), img.getImageData(), mask.getImageData());
		
		gc.dispose();
		mask.dispose();
		return img;
	}
}
