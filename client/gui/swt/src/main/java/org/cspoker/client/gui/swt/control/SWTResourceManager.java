package org.cspoker.client.gui.swt.control;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.*;
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
			InputStream ips = instance.getClass().getClassLoader().getResourceAsStream(fileAsString);
			if (ips == null)
				return null;
			Image img = new Image(Display.getDefault(), ips);
			resources.put(fileAsString, img);
			return img;
		} catch (Exception e) {
			System.err.println("SWTResourceManager.getImage: Error getting image " + fileAsString + ", " + e);
			return null;
		}
	}
	
	public static Image getCardImage(Card card) {
		
		if (resources.containsKey(card.toString()))
			return (Image) resources.get(card.toString());
		Image deck = getImage(ClientGUI.CARDS_IMG_DIR + ClientGUI.ACTIVE_DECK_IMG_FILE);
		if (deck == null) {
			System.err.println("Deck img not found");
			return null;
		}
		Image img = getCardFromDeck(deck, card);
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
			System.out.println("Card: " + card + "x: " + card.getRank().ordinal() + ", y: " + card.getSuit().ordinal());
			xLookUp = Rank.values().length - card.getRank().ordinal();
			yLookUp = Suit.values().length - card.getSuit().ordinal();
		}
		Image cardImg = getImageFromCollection(deck, new Point(14, 6), new Point(xLookUp, yLookUp));
		
		return cardImg;
	}
	
	public static Image getChipFromPNG(Chip chip, int size) {
		if (resources.containsKey(chip.toString() + size))
			return (Image) resources.get(chip.toString() + size);
		Image chipPngImg = getImage(ClientGUI.ACTIVE_CHIP_DIR);
		if (chipPngImg == null)
			return null;
		// Calculate drawing values
		int xLookUp, yLookUp;
		int indexInSet = Chip.AVAILABLE_CHIPS.headSet(chip, false).size();
		
		xLookUp = indexInSet % 5;
		yLookUp = indexInSet / 5;
		Image chipImg = getImageFromCollection(chipPngImg, new Point(5, 3), new Point(xLookUp, yLookUp));
		
		resources.put(chip.toString() + size, chipImg);
		return chipImg;
	}
	
	public static Image getChipImage(Chip chip, int size) {
		Image chipImg = null;
		if (ClientGUI.ACTIVE_CHIP_DIR == ClientGUI.FREE_CHIP_IMAGE_FILE) {
			chipImg = SWTResourceManager.getChipFromPNG(chip, size);
		}
		if (chipImg != null)
			return chipImg;
		if (resources.containsKey(chip.toString() + size))
			return (Image) resources.get(chip.toString() + size);
		
		String fileAsString = ClientGUI.ACTIVE_CHIP_DIR + size + "/" + chip.getFileId();
		fileAsString = fileAsString + ".";
		String imgUrl = fileAsString + "bmp";
		String maskUrl = fileAsString + "a.bmp";
		Image img = new Image(Display.getDefault(), ClientGUI.class.getClassLoader().getResourceAsStream(imgUrl));
		Image mask = new Image(Display.getDefault(), ClientGUI.class.getClassLoader().getResourceAsStream(maskUrl));
		Image icon = new Image(Display.getDefault(), img.getImageData(), mask.getImageData());
		img.dispose();
		mask.dispose();
		resources.put(chip.toString() + size, icon);
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
