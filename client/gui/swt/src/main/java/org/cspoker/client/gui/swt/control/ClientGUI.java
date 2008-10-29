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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Hashtable;

import javax.sound.sampled.*;

import org.apache.log4j.Logger;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.client.gui.swt.window.LoginDialog;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
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
		
		public static final boolean ADDITIONAL_RESOURCES = false;
		
		private static final File IMAGE_DIR = new File("target/classes/images");
		
		/** Icon to be used for Shell images */
		public static final File CS_POKER_ICON = new File(Resources.IMAGE_DIR, "csicon.png");
		
		private static final File CHIP_DIR = new File(IMAGE_DIR, "chips");
		/**
		 * Contains PokerStars chip images. May not be available in open-source
		 * version
		 */
		public final static File STARS_CHIP_IMG_DIR = new File(Resources.CHIP_DIR, "stars");
		/**
		 * Contains EPT chip images. May not be available in open-source version
		 */
		public final static File EPT_CHIP_IMG_DIR = new File(Resources.CHIP_DIR, "ept");
		/** Contains Chip images from PokerWikia (free!) */
		public static final File FREE_CHIP_IMAGE_FILE = new File(Resources.CHIP_DIR, "Chips_Free.png");
		/**
		 * Chip resource currently in use (that's where the images are retrieved
		 * from during play)
		 */
		static File ACTIVE_CHIP_DIR = FREE_CHIP_IMAGE_FILE;
		
		private final static File THEMES_IMG_DIR = new File(Resources.IMAGE_DIR, "themes");
		private final static File CARDS_IMG_DIR = new File(Resources.IMAGE_DIR, "cards");
		/**
		 * Contains FTP card images. May not be available in open-source version
		 */
		public static final File FTP_DECK_IMG_FILE = new File(CARDS_IMG_DIR, "Deck_FTP.png");
		/**
		 * Contains PokerStars card images. May not be available in open-source
		 * version
		 */
		public static final File FOUR_COLOR_DECK_IMG_FILE = new File(CARDS_IMG_DIR, "Deck_Free_2.png");
		/**
		 * Card image resource currently in use (that's where the images are
		 * retrieved from during play). Initialized to use free Four color
		 * deck-style cards
		 */
		static File ACTIVE_DECK_IMG_FILE = FOUR_COLOR_DECK_IMG_FILE;
		
		/** Default table background image */
		public static final File TABLE_IMAGE = new File(THEMES_IMG_DIR, "table1.jpg");
		
		private static final File SOUND_DIR = new File("target/classes/Snd");
		/** Plays a <i>Check</i> sound */
		public static final File SOUND_FILE_CHECK = new File(SOUND_DIR, "snd4.wav");
		/** Plays a <i>Fold</i> sound */
		public static final File SOUND_FILE_FOLD = new File(SOUND_DIR, "snd6.wav");
		/** Plays a <i>Chip clink</i> sound */
		public static final File SOUND_FILE_BETRAISE = new File(SOUND_DIR, "snd5.wav");
		/** Plays a <i>Chip sliding</i> sound */
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
	
	/** Preferred width at which a card is best displayed */
	public final static int PREFERRED_CARD_WIDTH = 50;
	
	/** What do you think this is? */
	public final static int MINIMUM_CARD_WIDTH = 30;
	/** What do you think this is? */
	public final static int MINIMUM_CARD_HEIGHT = (int) Math.round(MINIMUM_CARD_WIDTH * 1.5);
	
	/**
	 * Preferred height at which a card is best displayed. Set to
	 * <code>1.5 * PREFERRED_CARD_WIDTH</code>
	 */
	public final static int PREFERRED_CARD_HEIGHT = (int) Math.round(PREFERRED_CARD_WIDTH * 1.5);
	/**
	 * Final reference for an unknown card (i.e. the image is the back of the
	 * card)
	 */
	public final static Card UNKNOWN_CARD = new Card(null, null);
	
	/**
	 * During development, set this to SWT.BORDER so we better see where the
	 * composites are
	 */
	public static final int COMPOSITE_BORDER_STYLE = SWT.NONE;
	
	/**
	 * A {@link NumberFormat} to use when converting from a bare
	 * <code>int</code> chip value to a human-readable representation and
	 * vice-versa
	 */
	private final static NumberFormat betFormatter = NumberFormat.getNumberInstance();
	
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
		betFormatter.setMaximumFractionDigits(2);
		betFormatter.setGroupingUsed(false);
	}
	
	/**
	 * Formatting bets by hand since {@link NumberFormat#getCurrencyInstance()}s
	 * from Java sucks
	 * 
	 * @param bet The bet in the lowest denominator currency unit (cents)
	 * @return A String representing the bet amount
	 * @see #parseBet(String)
	 */
	public static String formatBet(int bet) {
		int dollar = bet / 100;
		int cent = bet % 100;
		String result = "$ " + Integer.toString(dollar);
		
		if (cent != 0) {
			result = result + ".";
			if (cent / 10 == 0) {
				result = result + "0";
			}
			result = result + Integer.toString(cent);
		}
		return result;
	}
	
	/**
	 * Formatting bets by hand since {@link NumberFormat#getCurrencyInstance()}s
	 * from Java sucks
	 * 
	 * @param bet The bet as a string
	 * @return The bet in cents
	 * @throws NumberFormatException if the bet does not contain a parsable
	 *             double
	 * @see #formatBet(int)
	 */
	public static int parseBet(String bet) {
		bet = bet.replaceAll("[^0-9.,]", "");
		double result = Double.parseDouble(bet);
		return (int) Math.round(result * 100);
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
	 * 
	 * @return A new {@link LoginDialog}
	 */
	public LoginDialog createNewLoginDialog() {
		disposeCurrentShell();
		return new LoginDialog(new Shell(display, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), SWT.NONE, clientCore);
	}
	
	/**
	 * Displays a fresh {@link MessageBox} with the given error message
	 * 
	 * @param e the given error message
	 * @return {@link MessageBox#open()}
	 */
	public static int displayException(Exception e) {
		logger.error("Unexpected error during client execution", e);
		
		StringBuffer sb = new StringBuffer(e.getMessage() + "\n\n");
		
		for (StackTraceElement ste : e.getStackTrace()) {
			sb.append(ste.toString() + "\n");
		}
		return displayMessage("Unexpected exception, everything was reset", sb.toString(), SWT.ICON_ERROR);
	}
	
	/**
	 * Displays a fresh {@link MessageBox} with the given message
	 * 
	 * @param message The message to display
	 * @param style The desired style bits (see {@link MessageBox} style bit
	 *            info)
	 * @return {@link MessageBox#open()}
	 */
	public static int displayMessage(String title, String message, int style) {
		logger.info(message);
		MessageBox infoBox = new MessageBox(new Shell(Display.getCurrent()), style | SWT.OK);
		infoBox.setText(title);
		infoBox.setMessage(message);
		return infoBox.open();
	}
	
	/**
	 * @param file Plays the given audio file
	 */
	public static void playAudio(File file) {
		AudioInputStream stream = null;
		Clip clip = null;
		try {
			stream = AudioSystem.getAudioInputStream((new FileInputStream(file)));
			DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
			clip.addLineListener(new LineListener() {
				
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP)
						event.getLine().close();
				}
			});
		} catch (Exception e) {
			logger.warn("Could not play sound", e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ignored) {
					// Do nothing
				}
			}
		}
		
	}
	
	/**
	 * Sets the visual cards to be used in all {@link GameWindow}s.
	 * <p>
	 * This method disposes of all image resources so that no images cached by
	 * the {@link SWTResourceManager} can be reused.
	 * 
	 * @param cardFileResource The file system resource where the cards can be
	 *            found. May either be a directory containing single images or a
	 *            single image file containing all images.
	 * @throws FileNotFoundException If the file resource does not exist
	 */
	public static void setActiveCardDeck(File cardFileResource)
			throws FileNotFoundException {
		if (!cardFileResource.exists()) {
			throw new FileNotFoundException(cardFileResource.toString());
		}
		SWTResourceManager.dispose();
		Resources.ACTIVE_DECK_IMG_FILE = cardFileResource;
		
	}
	
	/**
	 * Sets the visual type of chips used in all {@link GameWindow}s.
	 * <p>
	 * This method disposes of all image resources so that no images cached by
	 * the {@link SWTResourceManager} can be reused.
	 * 
	 * @param chipFileResource The file system resource where the cards can be
	 *            found. May either be a directory containing single images or a
	 *            single image file containing all images.
	 * @throws FileNotFoundException If the file resource does not exist
	 */
	public static void setActiveChipsStyle(File chipFileResource)
			throws FileNotFoundException {
		if (!chipFileResource.exists()) {
			throw new FileNotFoundException(chipFileResource.toString());
		}
		SWTResourceManager.dispose();
		Resources.ACTIVE_CHIP_DIR = chipFileResource;
		
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
			
			DetailedHoldemTable table;
			try {
				table = getLobby().getContext().getHoldemTableInformation(tableId);
				w = new GameWindow(getLobby(), table);
			} catch (RemoteException e) {
				throw new IllegalStateException("Could not retrieve remote table information", e);
			} catch (IllegalActionException exception) {
				logger.error("This should not happen", exception);
			}
		}
		gameWindows.put(tableId, w);
		
		return w;
	}
}
