package org.cspoker.client.gui.swt.control;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Hashtable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.client.gui.swt.window.LoginDialog;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.elements.table.DetailedTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * SWT Client GUI Provides access to all <code>GameWindow</code>s, manages
 * Display preference constants
 * 
 * @author Cedric, Stephan
 */
public class ClientGUI {
	
	/**
	 * The display of this clientGui
	 */
	private final Display display;
	
	public Display getDisplay() {
		return display;
	}
	
	/**
	 * The clientCore of this clientGui
	 */
	private final ClientCore clientCore;
	/**
	 * The current window of this client gui
	 */
	private LobbyWindow lobby;
	
	// TODO Better way of storing/retrieving the open GameWindows?
	private Hashtable<Long, GameWindow> gameWindows;
	
	public Collection<GameWindow> getGameWindows() {
		return gameWindows.values();
	}
	
	// Some of these images are copyrighted so the standard settings use free
	// images
	// Changes may cause errors because the respective files have not been
	// uploaded to the repository
	
	// TODO Modularize Resource management, not all as static final Strings and
	// later building paths from it by concatenation
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
		gameWindows = new Hashtable<Long, GameWindow>();
		betFormatter.setMinimumFractionDigits(0);
	}
	
	/***************************************************************************
	 * Window & shell
	 **************************************************************************/
	
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
		return new LoginDialog(new Shell(display, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), SWT.NONE, this, clientCore);
	}
	
	/***************************************************************************
	 * ERRORS
	 **************************************************************************/
	/**
	 * Displays a fresh window with the given error message
	 * 
	 * @param e the given error message
	 */
	public static int displayErrorMessage(Exception e) {
		e.printStackTrace();
		System.err.println(e);
		MessageBox errorMsgBox = new MessageBox(new Shell(Display.getDefault()), SWT.ICON_ERROR | SWT.RETRY | SWT.ABORT
				| SWT.IGNORE);
		StringBuffer sb = new StringBuffer(e.getMessage() + "\n");
		
		for (StackTraceElement ste : e.getStackTrace()) {
			sb.append(ste.toString() + "\n");
		}
		errorMsgBox.setMessage(sb.toString());
		return errorMsgBox.open();
	}
	
	public static String formatBet(int amount) {
		return betFormatter.format(new Double(amount) / 100);
	}
	
	public static Image generateSkin(Point size) {
		// Image result = SWTResourceManager.findImage(tableFile,
		// SWTResourceManager.getImage(new File(
		// ClientGUI.THEMES_IMG_DIR + BG_IMAGE_PATH)));
		
		Image skin = SWTResourceManager.getImage(THEMES_IMG_DIR + TABLE_IMAGE_PATH + ".bmp");
		Image scaled = new Image(Display.getDefault(), skin.getImageData().scaledTo(size.x, size.y));
		skin.dispose();
		return scaled;
		
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
		SWTResourceManager.dispose();
		ACTIVE_DECK_IMG_FILE = deckImgFile;
		
	}
	
	public static void setActiveChipsStyle(String chipType) {
		SWTResourceManager.dispose();
		ACTIVE_CHIP_DIR = chipType;
		
	}
	
	/**
	 * @param tableId
	 * @return The {@link GameWindow} for the given id, or <code>null</code> if
	 *         it doesn't exist
	 */
	public GameWindow getGameWindow(LobbyContext context, long tableId) {
		GameWindow gw = gameWindows.get(tableId);
		if (gw != null) {
			return gw;
		}
		// No Game Window for this table yet
		DetailedTable table = context.joinTable(tableId);
		GameWindow newGameWindow = new GameWindow(getLobby(), table);
		gameWindows.put(tableId, newGameWindow);
		return newGameWindow;
	}
	
	/**
	 * @param lobby the lobby to set
	 */
	public void setLobby(LobbyWindow lobby) {
		this.lobby = lobby;
	}
	
	/**
	 * @return the lobby
	 */
	public LobbyWindow getLobby() {
		return lobby;
	}
}
