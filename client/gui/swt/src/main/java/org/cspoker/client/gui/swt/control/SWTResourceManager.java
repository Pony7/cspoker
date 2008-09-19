package org.cspoker.client.gui.swt.control;

import java.util.HashMap;
import java.util.Vector;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Control;
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
	
	private static HashMap<String, Resource> resources = new HashMap<String, Resource>();
	private static Vector<Widget> users = new Vector<Widget>();
	private static SWTResourceManager instance = new SWTResourceManager();
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
	
	public static void dispose() {
		for (Resource r : resources.values()) {
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
						lfCls.getField("lfStrikeOut").set(lf, new Byte((byte) 1));
					if (underline)
						lfCls.getField("lfUnderline").set(lf, new Byte((byte) 1));
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
	public static Image getImage(String fileAsString) {
		try {
			if (resources.containsKey(fileAsString))
				return (Image) resources.get(fileAsString);
			Image img = new Image(Display.getDefault(), instance.getClass().getClassLoader().getResourceAsStream(
					fileAsString));
			resources.put(fileAsString, img);
			return img;
		} catch (Exception e) {
			System.err.println("SWTResourceManager.getImage: Error getting image " + fileAsString + ", " + e);
			return null;
		}
	}
	
	public static Image getCardImage(Card card) {
		try {
			if (resources.containsKey(card.toString()))
				return (Image) resources.get(card.toString());
			Image img = getCardFromDeck(ClientGUI.CARDS_IMG_DIR + ClientGUI.ACTIVE_DECK_IMG_FILE, card);
			resources.put(card.toString(), img);
			return img;
		} catch (Exception e) {
			System.err.println("SWTResourceManager.getImage: Error getting image " + card + ", " + e);
			return null;
		}
		
	}
	
	public static Image getScaledCardImage(Card card, Control control) {
		Image cardImg = getCardImage(card);
		final Image scaled = new Image(control.getDisplay(), cardImg.getImageData().scaledTo(control.getSize().x,
				control.getSize().y));
		return scaled;
	}
	
	public static Image getScaledCardImage(Card card, Point size) {
		Image cardImg = getCardImage(card);
		final Image scaled = new Image(Display.getCurrent(), cardImg.getImageData().scaledTo(size.x, size.y));
		return scaled;
	}
	
	public static Image getCardFromDeck(String deckImgFile, Card card) {
		Image deck = getImage(deckImgFile);
		ImageData data = deck.getImageData();
		// Calculate drawing values
		int xLookUp, yLookUp;
		if (card.getRank() == null || card.getSuit() == null) {
			xLookUp = -1;
			yLookUp = 0;
		} else {
			System.out.println("Card: " + card + "x: " + card.getRank().ordinal() + ", y: " + card.getSuit().ordinal());
			xLookUp = card.getRank().ordinal();
			yLookUp = card.getSuit().ordinal();
		}
		int srcX = data.width / 14 * (Rank.values().length - xLookUp - 1);
		int srcY = data.height / 6 * (Suit.values().length - yLookUp - 1);
		int srcWidth = data.width / 14;
		int srcHeight = data.height / 6;
		Image deckImg = new Image(Display.getDefault(), data);
		Image cardImg = new Image(Display.getDefault(), srcWidth, srcHeight);
		GC cardGC = new GC(cardImg);
		// Draw the image
		cardGC.drawImage(deckImg, srcX, srcY, srcWidth, srcHeight, 0, 0, cardImg.getImageData().width, cardImg
				.getImageData().height);
		Image cardMask = new Image(Display.getDefault(), cardImg.getBounds());
		cardImg = new Image(Display.getDefault(), cardImg.getImageData(), cardMask.getImageData());
		cardGC.dispose();
		return cardImg;
	}
	
	public static Image getChipFromPNG(String chipPngFile, Chip chip) {
		if (resources.containsKey(chip.toString()))
			return (Image) resources.get(chip.toString());
		Image chipPngImg = getImage(chipPngFile);
		ImageData data = chipPngImg.getImageData();
		// Calculate drawing values
		int xLookUp, yLookUp;
		int indexInSet = Chip.AVAILABLE_CHIPS.headSet(chip, false).size();
		
		xLookUp = indexInSet % 5;
		yLookUp = indexInSet / 5;
		
		int srcX = data.width / 5 * xLookUp;
		int srcY = data.height / 3 * yLookUp;
		int srcWidth = data.width / 5;
		int srcHeight = data.height / 3;
		Image deckImg = new Image(Display.getDefault(), data);
		Image cardImg = new Image(Display.getDefault(), srcWidth, srcHeight);
		GC cardGC = new GC(cardImg);
		// Draw the image
		cardGC.drawImage(deckImg, srcX, srcY, srcWidth, srcHeight, 0, 0, cardImg.getImageData().width, cardImg
				.getImageData().height);
		Image cardMask = new Image(Display.getDefault(), cardImg.getBounds());
		cardImg = new Image(Display.getDefault(), cardImg.getImageData(), cardMask.getImageData());
		cardGC.dispose();
		resources.put(chip.toString(), cardImg);
		return cardImg;
	}
	
	public static Image getChipImage(String fileAsString) {
		if (resources.containsKey(fileAsString))
			return (Image) resources.get(fileAsString);
		Image icon = null;
		
		fileAsString = fileAsString + ".";
		String imgUrl = fileAsString + "bmp";
		String maskUrl = fileAsString + "a.bmp";
		Image img = new Image(Display.getDefault(), ClientGUI.class.getClassLoader().getResourceAsStream(imgUrl));
		Image mask = new Image(Display.getDefault(), ClientGUI.class.getClassLoader().getResourceAsStream(maskUrl));
		icon = new Image(Display.getDefault(), img.getImageData(), mask.getImageData());
		
		resources.put(fileAsString, icon);
		return icon;
	}
	
	public static void clearImageCache() {
		resources.clear();
	}
}
