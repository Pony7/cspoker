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
package org.cspoker.client.gui.swt.window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.gui.swt.control.CardPaintListener;
import org.cspoker.client.gui.swt.control.ChipPaintListener;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.player.Winner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * The main composite in the game window. Contains the
 * {@link PlayerSeatComposite}s and draws the chips on the table etc.
 * 
 * @author stephans
 */
public class TableComposite
		extends ClientComposite {
	
	private final static Logger logger = Logger.getLogger(TableComposite.class);
	
	private Composite communityCardsComposite;
	Rectangle potChipsDisplayArea;
	private List<Card> communityCards = new ArrayList<Card>();
	private ChipPaintListener chipPaintListener;
	
	private int moneyInPot;
	
	/**
	 * Creates and initializes a new Table composite. The table composite
	 * contains all {@link PlayerSeatComposite}s.
	 * <p>
	 * It is in particular responsible for drawing chips on the table surface.
	 * 
	 * @param parent The containing {@link GameWindow}
	 * @param style Relevant style bits
	 */
	public TableComposite(GameWindow parent, int style) {
		super(parent, style);
		initGUI();
	}
	
	/**
	 * @see org.eclipse.swt.widgets.Control#getParent()
	 */
	@Override
	public GameWindow getParent() {
		return (GameWindow) super.getParent();
	}
	
	/**
	 * Initialization of SWT components.
	 */
	private void initGUI() {
		GridLayout layout = new GridLayout(4, true);
		layout.horizontalSpacing = 50;
		layout.verticalSpacing = 50;
		setLayout(layout);
		GridData tableCompositeLData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableCompositeLData.heightHint = 750;
		tableCompositeLData.minimumHeight = 300;
		setLayoutData(tableCompositeLData);
		setBackgroundMode(SWT.INHERIT_FORCE);
		// Add first row, placeholder labels and player composites
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 0);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 1);
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		// End first row
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 3);
		communityCardsComposite = new Composite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
		communityCardsComposite.setLayout(new GridLayout(5, true));
		GridData communityCardsLayoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		communityCardsLayoutData.horizontalSpan = 2;
		communityCardsLayoutData.widthHint = 5 * (ClientGUI.PREFERRED_CARD_WIDTH + 5);
		communityCardsLayoutData.heightHint = ClientGUI.PREFERRED_CARD_HEIGHT;
		communityCardsLayoutData.minimumWidth = 5 * (ClientGUI.MINIMUM_CARD_WIDTH + 5);
		communityCardsLayoutData.minimumHeight = ClientGUI.MINIMUM_CARD_HEIGHT;
		communityCardsComposite.setLayoutData(communityCardsLayoutData);
		communityCardsComposite.setBackgroundMode(SWT.INHERIT_NONE);
		communityCardsComposite.addPaintListener(new CardPaintListener(communityCards, 5, SWT.LEFT, 5));
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 2);
		// End second row
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 5);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 4);
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		chipPaintListener = new ChipPaintListener(this);
		this.addPaintListener(chipPaintListener);
	}
	
	/**
	 * Returns the PlayerSeatComposite if a player with the same id is sitting
	 * at the table
	 * 
	 * @param playerId The id of the player (must be seated at the table)
	 * @return The associated {@link PlayerSeatComposite}, or <code>null</code>,
	 *         if the player is not at the table
	 * @throws IllegalArgumentException If no player with the given id is
	 *             sitting at the table
	 */
	public PlayerSeatComposite findPlayerSeatCompositeByPlayerId(long playerId)
			throws IllegalArgumentException {
		for (PlayerSeatComposite pc : getPlayerSeatComposites(true)) {
			if (pc.getPlayer().getId() == playerId) {
				return pc;
			}
		}
		throw new IllegalArgumentException("Player with id " + playerId + " not found at table");
	}
	
	/**
	 * Returns the PlayerSeatComposite for the given seat id
	 * 
	 * @param seatId The seat id
	 * @return The associated {@link PlayerSeatComposite}, or <code>null</code>,
	 *         if the player is not at the table
	 * @throws IllegalArgumentException If no seat with the given id is
	 *             available at the table (i.e. seatId 10 for a 6-handed table)
	 */
	public PlayerSeatComposite findPlayerSeatCompositeBySeatId(long seatId)
			throws IllegalArgumentException {
		for (PlayerSeatComposite pc : getPlayerSeatComposites(false)) {
			if (pc.getSeatId() == seatId) {
				return pc;
			}
		}
		throw new IllegalArgumentException("Seat id " + seatId + " not found at table");
	}
	
	/**
	 * Updates the Progress bars to indicate who's turn it is to act
	 * 
	 * @param playerToAct The player who's turn it is
	 */
	void updateProgressBars(Player playerToAct) {
		for (PlayerSeatComposite pc : getPlayerSeatComposites(true)) {
			pc.stopTimer();
		}
		findPlayerSeatCompositeByPlayerId(playerToAct.getId()).startTimer();
	}
	
	/**
	 * Move the current bets of each player into the pot via an animation
	 */
	public void moveBetsToPot() {
		// Determine locations of the chip piles on the table
		List<Rectangle> chipLocList = new ArrayList<Rectangle>();
		for (PlayerSeatComposite pc : getPlayerSeatComposites(true)) {
			if (pc.getPlayer().getCurrentBetPile().size() != 0) {
				chipLocList.add(pc.getBetChipsDisplayArea());
			}
		}
		// Use locations as parameter for the animation to the pot
		// TODO Make this stuff more robusto
		resetPotChipsArea();
		moneyInPot = gameState.getPots().getTotalValue();
		animateChips(chipLocList, getPotDisplayArea());
		resetPotChipsArea();
		// Reset all the bet piles and the display areas
		for (PlayerSeatComposite pc : getPlayerSeatComposites(true)) {
			pc.getPlayer().getCurrentBetPile().clear();
			pc.resetBetChipsDisplayArea();
		}
		
		// lol redraw
		redraw();
	}
	
	/**
	 * @param onlyOccupied Whether only these {@link PlayerSeatComposite}s
	 *            should be returned where a player is sitting
	 * @return A list of all children player seat composites
	 */
	public List<PlayerSeatComposite> getPlayerSeatComposites(boolean onlyOccupied) {
		List<PlayerSeatComposite> result = new ArrayList<PlayerSeatComposite>();
		for (Control c : getChildren()) {
			if (c instanceof PlayerSeatComposite) {
				PlayerSeatComposite pc = (PlayerSeatComposite) c;
				if (pc.getPlayer() == null && onlyOccupied) {
					continue;
				}
				result.add(pc);
			}
		}
		return result;
	}
	
	private void animateChips(final List<Rectangle> list, final Rectangle toLocation) {
		if (list.size() == 0) {
			return;
		}
		logger.info("Send Chips from" + list + " to " + toLocation);
		final int timerInterval = 30;
		
		getDisplay().syncExec(new Runnable() {
			
			double i = 0;
			
			public void run() {
				try {
					i++;
					for (Rectangle newLocation : list) {
						final double xStep = (double) (newLocation.x - toLocation.x) / 20;
						final double yStep = (double) (newLocation.y - toLocation.y) / 20;
						
						newLocation.x = (int) (newLocation.x - i * xStep);
						newLocation.y = (int) (newLocation.y - i * yStep);
						
						if (Math.abs(newLocation.x - toLocation.x) > Math.abs(xStep)
								|| Math.abs(newLocation.y - toLocation.y) > Math.abs(yStep)) {

						} else {
							redraw();
							update();
							return;
						}
						
					}
					redraw();
					update();
					Thread.sleep(timerInterval);
					getDisplay().syncExec(this);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			
		});
		// Reset locations after animation
		resetPotChipsArea();
		for (PlayerSeatComposite psc : getPlayerSeatComposites(false)) {
			psc.resetBetChipsDisplayArea();
		}
	}
	
	private void animateChips(Rectangle offset, Rectangle offset2) {
		animateChips(Arrays.asList(offset), offset2);
	}
	
	/**
	 * @return The {@link Rectangle} where the Chips in the Pot should be
	 *         displayed.
	 *         <p>
	 *         May be modified during animation threads. Reset to initial
	 *         location via {@link #resetPotChipsArea()}
	 */
	public Rectangle getPotDisplayArea() {
		return potChipsDisplayArea;
	}
	
	/**
	 * Resets the area where the chips in the pot are displayed to its default
	 * value.
	 * <p>
	 * By default, the chips in the pot are displayed below the community cards.
	 */
	public void resetPotChipsArea() {
		int x = communityCardsComposite.getLocation().x;
		int y = communityCardsComposite.getLocation().y + communityCardsComposite.getClientArea().height;
		potChipsDisplayArea = new Rectangle(x, y, communityCardsComposite.getSize().x, findPlayerSeatCompositeBySeatId(
				5).getBounds().y
				- y);
		
	}
	
	/**
	 * @param commonCards Adds these community cards and displays them
	 */
	public void addCommunityCards(Set<Card> commonCards) {
		communityCardsComposite.setVisible(true);
		communityCards.addAll(commonCards);
		communityCardsComposite.redraw();
	}
	
	/**
	 * Reverse method to {@link #moveBetsToPot()} TODO What if there are more
	 * than one winner, the bet pile needs to be split accordingly first ...
	 * 
	 * @param winners The winners in the hand
	 */
	public void movePotsToWinners(final Set<Winner> winners) {
		// Ship it
		for (Winner winner : winners) {
			PlayerSeatComposite winnerPC = findPlayerSeatCompositeByPlayerId(winner.getPlayer().getId());
			winnerPC.getPlayer().setStackValue(winnerPC.getPlayer().getStackValue() + winner.getGainedAmount());
			animateChips(getPotDisplayArea(), winnerPC.getBetChipsDisplayArea());
			ClientGUI.playAudio(ClientGUI.Resources.SOUND_FILE_SLIDE_CHIPS);
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			logger.warn("Chip animation interrupted", e);
		}
		moneyInPot = 0;
		redraw();
	}
	
	/**
	 * TODO Change for side pots etc.
	 * 
	 * @return The money in the pot
	 */
	public int getMoneyInPot() {
		return moneyInPot;
	}
	
	void clearCommunityCards() {
		communityCards.clear();
		communityCardsComposite.redraw();
	}
	
	/**
	 * @return the Composite holding the Community Card Images
	 */
	public Composite getCommunityCardsComposite() {
		return communityCardsComposite;
	}
}
