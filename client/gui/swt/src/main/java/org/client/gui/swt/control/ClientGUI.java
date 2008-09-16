package org.cspoker.client.gui.swt.control;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import org.cspoker.client.gui.swt.window.ClientComposite;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.client.gui.swt.window.LoginDialog;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.exceptions.IllegalActionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * A class for a gui of a client
 * 
 * @author Cedric, Stephan
 */
public class ClientGUI {
	
	/**
	 * The display of this clientGui
	 */
	public final Display display;
	/**
	 * The clientCore of this clientGui
	 */
	private final ClientCore clientCore;
	/**
	 * The current window of this client gui
	 */
	public LobbyWindow lobby;
	public List<GameWindow> gameWindows;
	
	// Some of these images are copyrighted so the standard settings use free images
	// Changes may cause errors because the respective files have not been uploaded to the repository
	public static final String CS_POKER_ICON = "images/cspoker10.jpg";
	public final static String SOUND_DIR = "Snd/";
	public final static String STARS_CHIP_IMG_DIR = "images/chips/stars/";
	public final static String EPT_CHIP_IMG_DIR = "images/chips/ept/";
	
	public final static String THEMES_IMG_DIR = "images/themes/";
	public final static String CARDS_IMG_DIR = "images/cards/";
	public static final String FTP_DECK_IMG_FILE = "Deck_FTP.png";
	public static final String STARS_DECK_IMG_FILE = "cards6ug3.png";
	public static String ACTIVE_DECK_IMG_FILE = STARS_DECK_IMG_FILE;
	public static final String FREE_CHIP_IMAGE_FILE = "images/chips/Chips3.png";
	public static String ACTIVE_CHIP_DIR = STARS_CHIP_IMG_DIR;
	
	public final static int PREFERRED_CARD_WIDTH = 60;
	public final static int PREFERRED_CARD_HEIGHT = 90;
	
	@SuppressWarnings("unused")
	private static final String BG_IMAGE_PATH = "bg.jpg";
	private static String TABLE_IMAGE_PATH = "Free_Simple_Table_Background.jpg";
	/**
	 * During development, set this to SWT.BORDER so we better see where the
	 * composites are
	 */
	public static final int COMPOSITE_BORDER_STYLE = SWT.NONE;
	
	public final static NumberFormat betFormatter = NumberFormat.getCurrencyInstance();
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	/**
	 * Creates a new clientGui with a given clientCore
	 * 
	 * @param clientCore the given clientCore
	 */
	public ClientGUI(ClientCore clientCore) {
		display = Display.getDefault();
		this.clientCore = clientCore;
		gameWindows = new ArrayList<GameWindow>();
		betFormatter.setMinimumFractionDigits(0);
	}
	
	/***************************************************************************
	 * Window & shell
	 **************************************************************************/
	/**
	 * Sets the given window as the current window
	 * 
	 * @param window the given window
	 */
	public void setAsCurrentWindow(ClientComposite window) {
	// this.currentWindow = window;
	}
	
	/**
	 * Disposes the current shell
	 */
	public void disposeCurrentShell() {
		try {
			display.getActiveShell().dispose();
		} catch (NullPointerException e) {}
	}
	
	/***************************************************************************
	 * LOGIN
	 **************************************************************************/
	/**
	 * Starts the new gui by creating and opening new login screen
	 */
	public LoginDialog createNewLoginDialog() {
		disposeCurrentShell();
		LoginDialog loginDialog = new LoginDialog(new Shell(display, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), SWT.NONE,
				this, clientCore);
		return loginDialog;
	}
	
	/***************************************************************************
	 * ERRORS
	 **************************************************************************/
	/**
	 * Displays a fresh window with the given error message
	 * 
	 * @param e the given error message
	 */
	public int displayErrorMessage(Exception e) {
		e.printStackTrace();
		System.err.println(e);
		MessageBox errorMsgBox = new MessageBox(new Shell(display), SWT.ICON_ERROR | SWT.RETRY | SWT.ABORT | SWT.IGNORE);
		StringBuffer sb = new StringBuffer(e.getMessage() + "\n");
		
		for (StackTraceElement ste : e.getStackTrace()) {
			sb.append(ste.toString() + "\n");
		}
		errorMsgBox.setMessage(sb.toString());
		return errorMsgBox.open();
	}
	
	/**
	 * @param tableId
	 * @return The {@link GameWindow} for the given id, or <code>null</code> if
	 *         it doesn't exist
	 */
	public GameWindow getGameWindow(TableId tableId) {
		for (GameWindow gw : gameWindows) {
			if (gw.getTableId().equals(tableId)) {
				return gw;
			}
		}
		// No Game Window for this table yet
		GameWindow newGameWindow;
		try {
			newGameWindow = new GameWindow(display, this, clientCore, clientCore.getCommunication().getTable(tableId));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		gameWindows.add(newGameWindow);
		return newGameWindow;
	}
	
	public static String formatBet(int amount) {
		return betFormatter.format(new Double(amount) / 100);
	}
	
	public Image generateSkin(Point size) {
		// Image result = SWTResourceManager.findImage(tableFile,
		// SWTResourceManager.getImage(new File(
		// ClientGUI.THEMES_IMG_DIR + BG_IMAGE_PATH)));
		Image result = SWTResourceManager.getImage(THEMES_IMG_DIR + TABLE_IMAGE_PATH + ".bmp");
		return new Image(Display.getDefault(), result.getImageData().scaledTo(size.x, size.y));
		
	}
	
	public static void playAudio(String file) {
		// if (!getShell().isFocusControl()) {
		// return;
		// }
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(ClientGUI.class.getClassLoader()
					.getResourceAsStream(SOUND_DIR + file));
			DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setActiveCardDeck(String deckImgFile) {
		SWTResourceManager.clearImageCache();
		ACTIVE_DECK_IMG_FILE = deckImgFile;
		
	}
	
	public static void setActiveChipsStyle(String chipType) {
		SWTResourceManager.clearImageCache();
		ACTIVE_CHIP_DIR = chipType;
		
	}
}
