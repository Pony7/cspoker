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
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.cspoker.client.gui.swt.control.CardPaintListener;
import org.cspoker.client.gui.swt.control.Chip;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.MutableSeatedPlayer;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.player.Winner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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
	private List<Card> communityCards = new ArrayList<Card>();
	
	private List<PlayerSeatComposite> playerSeatComposites = new ArrayList<PlayerSeatComposite>();
	private List<Canvas> playerBetAreas = new ArrayList<Canvas>();
	private Rectangle dealerChipLocation;
	private Canvas potChipsArea;
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
		
		GridLayout layout = new GridLayout(5, false);
		// layout.horizontalSpacing = 50;
		// layout.verticalSpacing = 50;
		setLayout(layout);
		GridData tableCompositeLData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableCompositeLData.heightHint = 750;
		tableCompositeLData.minimumHeight = 300;
		tableCompositeLData.minimumWidth = 600;
		setLayoutData(tableCompositeLData);
		setBackgroundMode(SWT.INHERIT_FORCE);
		insertHolderLabel();
		insertPlayerSeatComposite(0);
		insertHolderLabel();
		insertPlayerSeatComposite(1);
		insertHolderLabel();
		insertHolderLabel();
		insertBetArea();
		insertHolderLabel();
		insertBetArea();
		insertHolderLabel();
		// End second row
		insertPlayerSeatComposite(5);
		insertBetArea();
		communityCardsComposite = new Composite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
		communityCardsComposite.setLayout(new GridLayout(5, true));
		GridData communityCardsLayoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		communityCardsLayoutData.horizontalSpan = 1;
		communityCardsLayoutData.widthHint = 5 * (ClientGUI.PREFERRED_CARD_WIDTH + 5);
		communityCardsLayoutData.heightHint = ClientGUI.PREFERRED_CARD_HEIGHT;
		communityCardsLayoutData.minimumWidth = 5 * (ClientGUI.MINIMUM_CARD_WIDTH + 5);
		communityCardsLayoutData.minimumHeight = ClientGUI.MINIMUM_CARD_HEIGHT;
		communityCardsComposite.setLayoutData(communityCardsLayoutData);
		communityCardsComposite.setBackgroundMode(SWT.INHERIT_NONE);
		communityCardsComposite.addPaintListener(new CardPaintListener(communityCards, 5, SWT.LEFT, 5));
		insertBetArea();
		insertPlayerSeatComposite(2);
		insertHolderLabel();
		insertBetArea();
		potChipsArea = new Canvas(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
		GridData betAreaLayoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		betAreaLayoutData.widthHint = communityCardsLayoutData.widthHint;
		betAreaLayoutData.heightHint = 100;
		betAreaLayoutData.minimumWidth = communityCardsLayoutData.minimumWidth;
		betAreaLayoutData.minimumHeight = 50;
		potChipsArea.setLayout(new FillLayout());
		potChipsArea.setLayoutData(betAreaLayoutData);
		potChipsArea.setVisible(false);
		insertBetArea();
		insertHolderLabel();
		// End fourth row
		insertHolderLabel();
		insertPlayerSeatComposite(4);
		insertHolderLabel();
		insertPlayerSeatComposite(3);
		insertHolderLabel();
		
		for (int i = 0; i < playerSeatComposites.size(); i++) {
			Canvas chipsForPlayerArea = playerBetAreas.get(i);
			PlayerSeatComposite psc = playerSeatComposites.get(i);
			psc.setChipsArea(chipsForPlayerArea);
			
		}
		
		addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				Rectangle redrawArea = new Rectangle(e.x, e.y, e.width, e.height);
				for (PlayerSeatComposite pc : getPlayerSeatComposites(true)) {
					if (pc.getPlayer().getCurrentBetPile().size() > 0
							&& redrawArea.intersects(pc.getChipsArea().getBounds())) {
						drawChips(e.gc, pc.getChipsArea().getBounds(), pc.getPlayer().getCurrentBetPile(), false, false);
						
					}
				}
				// Draw dealer chip
				NavigableMap<Chip, Integer> dealerChip = new TreeMap<Chip, Integer>();
				dealerChip.put(Chip.DEALER, 1);
				Rectangle dealerChipLocation = getDealerChipLocation();
				if (dealerChipLocation != null && redrawArea.intersects(dealerChipLocation)) {
					drawChips(e.gc, dealerChipLocation, Arrays.asList(dealerChip), false, true);
				}
				if (redrawArea.intersects(potChipsArea.getBounds()) && redrawArea.intersects(potChipsArea.getBounds())) {
					drawChips(e.gc, potChipsArea.getBounds(), Arrays.asList(Chip.getDistribution(getMoneyInPot())),
							true, false);
				}
			}
		});
	}
	
	/**
	 * 
	 */
	private void insertBetArea() {
		Canvas betArea = new Canvas(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
		GridData betAreaLayoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		betAreaLayoutData.widthHint = 150;
		betAreaLayoutData.heightHint = 100;
		betAreaLayoutData.minimumWidth = 60;
		betAreaLayoutData.minimumHeight = 50;
		betArea.setLayout(new FillLayout());
		betArea.setLayoutData(betAreaLayoutData);
		playerBetAreas.add(betArea);
		betArea.setVisible(false);
	}
	
	/**
	 * 
	 */
	private void insertPlayerSeatComposite(int seatId) {
		playerSeatComposites.add(new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, seatId));
	}
	
	private void insertHolderLabel() {
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
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
	void proceedToNextPlayer(Player next) {
		for (PlayerSeatComposite pc : getPlayerSeatComposites(true)) {
			if (pc.isActive()) {
				pc.setActive(false);
			}
		}
		findPlayerSeatCompositeByPlayerId(next.getId()).setActive(true);
	}
	
	/**
	 * Move the current bets of each player into the pot via an animation
	 */
	public void moveBetsToPot() {
		List<PlayerSeatComposite> allPlayers = getPlayerSeatComposites(true);
		// Determine locations of the chip piles on the table
		for (PlayerSeatComposite pc : allPlayers) {
			if (pc.getPlayer().getCurrentBetPile().size() != 0) {

			} else {
				allPlayers.remove(pc);
			}
		}
		
		animateChips(allPlayers, true);
		for (PlayerSeatComposite pc : getPlayerSeatComposites(true)) {
			pc.getPlayer().setBetChipsValue(0);
		}
		
		moneyInPot = gameState.getPots().getTotalValue();
		// Reset all the bet piles and the display areas
		// lol redraw
		redraw();
		update();
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
				if (pc.getPlayer() == MutableSeatedPlayer.UNOCCUPIED && onlyOccupied) {
					continue;
				}
				result.add(pc);
			}
		}
		return result;
	}
	
	private void animateChips(final Rectangle from, final Rectangle to) {
		logger.debug("Starting animation");
		
		final int timerInterval = 10;
		final int steps = 30;
		
		getDisplay().syncExec(new Runnable() {
			
			double i = 0;
			
			public void run() {
				
				i++;
				
				final double xStep = (double) (from.x - to.x) / steps;
				final double yStep = (double) ((from.y + from.height) - (to.y + to.height)) / steps;
				Rectangle newBounds = new Rectangle(from.x - (int) (i * xStep), from.y - (int) (i * yStep), from.width,
						from.height);
				Rectangle redrawArea = dealerChipLocation.union(newBounds);
				from.x = newBounds.x;
				from.y = newBounds.y;
				redraw(redrawArea.x, redrawArea.y, redrawArea.width, redrawArea.height, false);
				update();
				try {
					Thread.sleep(timerInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (i < steps) {
					getDisplay().syncExec(this);
				}
			}
		});
		// Layout to reset the locations
		layout();
	}
	
	/**
	 * TODO Redraw certain areas to minimize flicker
	 * 
	 * @param pcs
	 * @param toPot
	 */
	private void animateChips(final List<PlayerSeatComposite> pcs, final boolean toPot) {
		logger.debug("Starting animation");
		
		final int timerInterval = 10;
		final int steps = 30;
		final Canvas to = toPot ? getPotChipsArea() : pcs.get(0).getChipsArea();
		
		getDisplay().syncExec(new Runnable() {
			
			double i = 0;
			
			public void run() {
				
				i++;
				
				for (PlayerSeatComposite pc : pcs) {
					Canvas from = toPot ? pc.getChipsArea() : getPotChipsArea();
					Rectangle fromBounds = from.getBounds();
					
					Rectangle toBounds = to.getBounds();
					final double xStep = (double) (fromBounds.x - toBounds.x) / steps;
					final double yStep = (double) ((fromBounds.y + fromBounds.height) - (toBounds.y + toBounds.height))
							/ steps;
					
					Rectangle newBounds = new Rectangle(fromBounds.x - (int) (i * xStep), fromBounds.y
							- (int) (i * yStep), fromBounds.width, fromBounds.height);
					from.setBounds(newBounds);
					Rectangle redrawArea = fromBounds.union(newBounds);
					redraw(redrawArea.x, redrawArea.y, redrawArea.width, redrawArea.height, false);
				}
				update();
				try {
					Thread.sleep(timerInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (i < steps) {
					getDisplay().syncExec(this);
				}
			}
		});
		// Layout to reset the locations
		layout();
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
		List<PlayerSeatComposite> winnerPCs = new ArrayList<PlayerSeatComposite>();
		// Ship it
		for (Winner winner : winners) {
			PlayerSeatComposite winnerPC = findPlayerSeatCompositeByPlayerId(winner.getPlayer().getId());
			int newStackValue = winnerPC.getPlayer().getStackValue() + winner.getGainedAmount();
			winnerPC.getPlayer().setStackValue(newStackValue);
			ClientGUI.playAudio(ClientGUI.Resources.SOUND_FILE_SLIDE_CHIPS);
			winnerPCs.add(winnerPC);
			
		}
		animateChips(winnerPCs, false);
		moneyInPot = 0;
		redraw();
		update();
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
	
	public Canvas getPotChipsArea() {
		return potChipsArea;
	}
	
	/**
	 * Do the actual drawing of chip images on the {@link TableComposite}
	 * surface
	 * 
	 * @param area Where the chips are to be drawn
	 * @param chipPiles The chips to draw
	 * @param size The desired size of the chips
	 * @param putDifferentValuesOnSeparatePiles Whether you want to mix
	 *            different chip values in the same pile
	 */
	public void drawChips(GC gc, Rectangle area, List<NavigableMap<Chip, Integer>> chipPiles,
			boolean putDifferentValuesOnSeparatePiles, boolean dealerButton) {
		int amount = Chip.getValue(chipPiles);
		int size = 1;
		for (int i = 2; i <= 7; i++) {
			Image chipImage = Chip.ONE_CENT_CHIP.getImage(i);
			int imgHeight = chipImage.getBounds().height;
			if (imgHeight + Chip.MAX_CHIPS_IN_PILE * i > area.height) {
				size = i - 1;
				break;
			}
		}
		
		if (amount == 0 && !dealerButton) {
			return;
		}
		
		int xCoord = area.x;
		int standardXDistance = Chip.ONE_CENT_CHIP.getImage(size).getBounds().width + size;
		int standardYLocation = area.y + area.height - Chip.ONE_CENT_CHIP.getImage(size).getBounds().height;
		// Iterate over the chip piles.
		for (NavigableMap<Chip, Integer> chipPile : chipPiles) {
			
			int yCoord = standardYLocation;
			int totalChips = 0;
			// Iterate over the chip pile entries
			for (Entry<Chip, Integer> entry : chipPile.entrySet()) {
				
				if (entry.getValue() == 0)
					continue;
				
				Image chipImg = entry.getKey().getImage(size);
				for (int i = 0; i < entry.getValue(); i++) {
					if (totalChips > Chip.MAX_CHIPS_IN_PILE) {
						// Once we reach the maximum, shift to the right and
						// open a new pile
						// Update the offset
						xCoord += standardXDistance;
						yCoord = standardYLocation;
						totalChips = 0;
					}
					// While there are still chips of the same value in the
					// pile, stack them on top of each other
					gc.drawImage(chipImg, xCoord, yCoord);
					yCoord -= (size + 1);
					totalChips++;
					
				}
				if (putDifferentValuesOnSeparatePiles) {
					// If we dont want chips of different values on top of each
					// other, open up a new pile before going to the next chip
					// type
					// Update the offset
					xCoord += standardXDistance;
					yCoord = standardYLocation;
					totalChips = 0;
				}
			}
			xCoord += standardXDistance;
		}
		// Annotate the chip pile image with a textual display of the bet amount
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
		if (!dealerButton) {
			gc.drawText(ClientGUI.formatBet(amount), xCoord, standardYLocation);
		}
		if (ClientGUI.COMPOSITE_BORDER_STYLE == SWT.BORDER)
			gc.drawRectangle(area);
		return;
	}
	
	/**
	 * @param psc
	 * @param findPlayerSeatCompositeByPlayerId
	 */
	public void moveDealerButton(PlayerSeatComposite from, PlayerSeatComposite to) {
		
		Rectangle dealerButtonLocation = from.getDealerChipLocation();
		dealerChipLocation = dealerButtonLocation;
		Rectangle toLocation = to.getDealerChipLocation();
		animateChips(dealerButtonLocation, toLocation);
	}
	
	public Rectangle getDealerChipLocation() {
		return dealerChipLocation;
	}
	
	public void setDealerChipLocation(Rectangle dealerChipLocation) {
		this.dealerChipLocation = dealerChipLocation;
	}
	
	/**
	 * Issue a redraw of the table (when chips have moved etc.)
	 */
	public void updateTableGraphics() {
		for (PlayerSeatComposite psc : getPlayerSeatComposites(true)) {
			if (psc.getPlayer().isDealer()) {
				setDealerChipLocation(psc.getDealerChipLocation());
			}
			redraw();
			update();
		}
	}
}
