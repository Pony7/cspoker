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
import java.io.FileInputStream;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Hashtable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import org.apache.log4j.Logger;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.client.gui.swt.window.LoginDialog;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * SWT Client GUI Provides access to all <code>GameWindow</code>s and manages
 * Display preference constants
 * 
 * @author Cedric, Stephan
 */
public class ClientGUI {
	
	/**
	 * Encapsulates File resources accessed by the GUI (images, sound files
	 * etc.)
	 * 
	 * @author Stephan
	 */
	public static class Resources {
		
		private static final File IMAGE_DIR = new File("images");
		
		public static final File CS_POKER_ICON = new File(Resources.IMAGE_DIR, "csicon.png");
		
		private static final File CHIP_DIR = new File(IMAGE_DIR, "chips");
		public final static File STARS_CHIP_IMG_DIR = new File(Resources.CHIP_DIR, "stars");
		public final static File EPT_CHIP_IMG_DIR = new File(Resources.CHIP_DIR, "ept");
		public static final File FREE_CHIP_IMAGE_FILE = new File(Resources.CHIP_DIR, "Chips3.png");
		public static File ACTIVE_CHIP_DIR = STARS_CHIP_IMG_DIR;
		
		public final static File THEMES_IMG_DIR = new File(Resources.IMAGE_DIR, "themes");
		public final static File CARDS_IMG_DIR = new File(Resources.IMAGE_DIR, "cards");
		public static final File FTP_DECK_IMG_FILE = new File(CARDS_IMG_DIR, "Deck_FTP.png");
		public static final File STARS_DECK_IMG_FILE = new File(CARDS_IMG_DIR, "cards6ug3.png");
		public static File ACTIVE_DECK_IMG_FILE = STARS_DECK_IMG_FILE;
		
		public static final File BACKGROUND_IMAGE = new File(THEMES_IMG_DIR, "bg.jpg");
		public static File TABLE_IMAGE = new File(THEMES_IMG_DIR, "Free_Simple_Table_Background.jpg");
		
		public static final File SOUND_DIR = new File("Snd");
		public static final File SOUND_FILE_CHECK = new File(SOUND_DIR, "snd4.wav");
		public static final File SOUND_FILE_FOLD = new File(SOUND_DIR, "snd6.wav");
		public static final File SOUND_FILE_BETRAISE = new File(SOUND_DIR, "snd5.wav");
		public static final File SOUND_FILE_SLIDE_CHIPS = new File(SOUND_DIR, "snd3.wav");
		
	}
	
	private final Display display;
	private final ClientCore clientCore;
	private LobbyWindow lobby;
	private final static Logger logger = Logger.getLogger(ClientGUI.class);
	// TODO Better way of storing/retrieving the open GameWindows?
	private Hashtable<Long, GameWindow> gameWindows;
	
	/**
	 * @return The UI {@link Display} used throughout. All windows created by
	 *         the client should use this display.
	 */
	public Display getDisplay() {
		assert (!display.isDisposed()) : "The requested display has been disposed";
		return display;
	}
	
	/**
	 * @return The list with all {@link GameWindow}s currently opened
	 */
	public Collection<GameWindow> getGameWindows() {
		return gameWindows.values();
	}
	
	// Some of these images are copyrighted so the standard settings use free
	// images
	// Changes may cause errors because the respective files have not been
	// uploaded to the repository
	
	public final static int PREFERRED_CARD_WIDTH = 60;
	public final static int PREFERRED_CARD_HEIGHT = (int) Math.round(PREFERRED_CARD_WIDTH * 1.5);
	
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
	
	/**
	 * @param file Plays the given audio file
	 */
	public static void playAudio(File file) {
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream((new FileInputStream(file)));
			DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch (Exception e) {
			logger.warn("Could not play sound", e);
		}
		
	}
	
	public static void setActiveCardDeck(String deckImgFile) {
		SWTResourceManager.dispose();
		Resources.ACTIVE_DECK_IMG_FILE = new File(Resources.CARDS_IMG_DIR, deckImgFile);
		
	}
	
	public static void setActiveChipsStyle(String chipType) {
		SWTResourceManager.dispose();
		Resources.ACTIVE_CHIP_DIR = new File(Resources.CHIP_DIR, chipType);
		
	}
	
	/**
	 * @param lobby the lobby to set
	 */
	public void setLobby(LobbyWindow lobby) {
		if (getLobby() != null) {
			display.syncExec(new Runnable() {
				
				public void run() {
					getLobby().dispose();
				}
			});
		}
		this.lobby = lobby;
	}
	
	/**
	 * @return the lobby
	 */
	public LobbyWindow getLobby() {
		return lobby;
	}
	
	/**
	 * Searches for the given {@link GameWindow}.
	 * 
	 * @param tableId The table id
	 * @param createNew if <code>true</code>, a new {@link GameWindow} will be
	 *            created if it doesn't exist, otherwise the existing GameWindow
	 *            is returned
	 * @return The {@link GameWindow} for the given id. If it doesn't exist, it
	 *         is created when <code>createNew</code> is <code>true</code>,
	 *         otherwise <code>null</code> is returned
	 */
	public GameWindow getGameWindow(long tableId, boolean createNew) {
		GameWindow w = gameWindows.get(tableId);
		if (w == null && createNew) {
			// No Game Window for this table yet
			DetailedHoldemTable table = getLobby().getContext().joinTable(tableId);
			w = new GameWindow(getLobby(), table);
			gameWindows.put(tableId, w);
		}
		return w;
	}
}
