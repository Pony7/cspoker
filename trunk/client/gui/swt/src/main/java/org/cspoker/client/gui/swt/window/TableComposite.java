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
	
	private Composite communityCardsComposite;
	Rectangle potChipsDisplayArea;
	private List<Card> communityCards = new ArrayList<Card>();
	// TODO The chipPaintListener is implemented in a not very object-oriented
	// fashion, could be done better
	private ChipPaintListener chipPaintListener;
	public boolean updateChipLocations = true;
	
	public Composite getCommunityCardsComposite() {
		return communityCardsComposite;
	}
	
	public TableComposite(GameWindow parent, int style) {
		super(parent, style, parent.getClientCore());
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new GridLayout(5, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// Add first row, placeholder labels and player composites
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 0);
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 1);
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		// End first row
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 2);
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		communityCardsComposite = new Composite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
		communityCardsComposite.setLayout(new GridLayout(1, true));
		GridData communityCardsLayoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		communityCardsLayoutData.widthHint = 5 * ClientGUI.PREFERRED_CARD_WIDTH;
		communityCardsLayoutData.heightHint = ClientGUI.PREFERRED_CARD_HEIGHT;
		communityCardsComposite.setLayoutData(communityCardsLayoutData);
		communityCardsComposite.addPaintListener(new CardPaintListener(communityCards, 5, SWT.LEFT, 10));
		
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 3);
		// End second row
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 4);
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, 5);
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		chipPaintListener = new ChipPaintListener(this);
		this.addPaintListener(chipPaintListener);
	}
	
	/**
	 * Returns the PlayerSeatComposite if a player with the same id is sitting
	 * at the table
	 * 
	 * @param seatId The seat id
	 * @return The associated {@link PlayerSeatComposite}, or <code>null</code>,
	 *         if the player is not at the table
	 */
	public PlayerSeatComposite getPlayerSeatComposite(long playerId) {
		for (PlayerSeatComposite pc : getPlayerSeatComposites()) {
			if (pc.getPlayer().getId() == playerId) {
				return pc;
			}
		}
		return null;
	}
	
	/**
	 * Updates the Progress bars to indicate who's turn it is to act
	 * 
	 * @param playerToAct The player who's turn it is
	 */
	void updateProgressBars(Player playerToAct) {
		for (PlayerSeatComposite pc : getPlayerSeatComposites()) {
			pc.stopTimer();
		}
		getPlayerSeatComposite(playerToAct.getId()).startTimer();
	}
	
	/**
	 * Move the current bets of each player into the pot via an animation
	 */
	public void moveBetsToPot() {
		// Determine locations of the chip piles on the table
		List<Rectangle> chipLocList = new ArrayList<Rectangle>();
		for (PlayerSeatComposite pc : getPlayerSeatComposites()) {
			
			if (pc.getCurrentBetPile().size() != 0) {
				chipLocList.add(pc.getBetChipsDisplayArea());
			}
		}
		// Use locations as parameter for the animation to the pot
		// TODO Make this stuff more robusto
		animateChips(chipLocList, getPotOffset());
		// Reset all the bet piles and the display areas
		for (PlayerSeatComposite pc : getPlayerSeatComposites()) {
			pc.getCurrentBetPile().clear();
			pc.betChipsArea = null;
			potChipsDisplayArea = null;
		}
		// lol redraw
		redraw();
	}
	
	/**
	 * @return A list of all children player seat composites
	 */
	public List<PlayerSeatComposite> getPlayerSeatComposites() {
		List<PlayerSeatComposite> result = new ArrayList<PlayerSeatComposite>();
		for (Control c : getChildren()) {
			if (c instanceof PlayerSeatComposite) {
				PlayerSeatComposite pc = (PlayerSeatComposite) c;
				result.add(pc);
			}
		}
		return result;
	}
	
	void clearCommunityCards() {
		communityCards.clear();
		communityCardsComposite.redraw();
	}
	
	public void addCommunityCards(Set<Card> commonCards) {
		communityCardsComposite.setVisible(true);
		communityCards.addAll(commonCards);
		communityCardsComposite.redraw();
	}
	
	private void shipPot(PlayerSeatComposite winner) {
		communityCardsComposite.setVisible(false);
		final Rectangle potLocation = getPotOffset();
		winner.updateBetChipsDisplayArea();
		final Rectangle winnerLocation = winner.getBetChipsDisplayArea();
		animateChips(potLocation, winnerLocation);
	}
	
	private void animateChips(final List<Rectangle> list, final Rectangle toLocation) {
		if (list.size() == 0) {
			return;
		}
		System.out.println("Ship from" + list + " to " + toLocation);
		final int timerInterval = 10;
		updateChipLocations = false;
		
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
					try {
						Thread.sleep(timerInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getDisplay().syncExec(this);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			
		});
		
		updateChipLocations = true;
		// Reset locations after animation
		updateGetPotChipsArea();
		for (PlayerSeatComposite psc : getPlayerSeatComposites()) {
			psc.updateBetChipsDisplayArea();
		}
	}
	
	private void animateChips(Rectangle offset, Rectangle offset2) {
		animateChips(Arrays.asList(offset), offset2);
	}
	
	public Rectangle getPotOffset() {
		if (potChipsDisplayArea == null) {
			updateGetPotChipsArea();
		}
		return potChipsDisplayArea;
	}
	
	public void updateGetPotChipsArea() {
		int x = communityCardsComposite.getLocation().x;
		int y = communityCardsComposite.getLocation().y + communityCardsComposite.getClientArea().height + 50;
		potChipsDisplayArea = new Rectangle(x, y, communityCardsComposite.getSize().x, communityCardsComposite
				.getSize().y);
		
	}
	
	/**
	 * Reverse method to {@link #moveBetsToPot()} TODO What if there are more
	 * than one winner, the bet pile needs to be split accordingly first ...
	 * 
	 * @param winners The winners in the hand
	 */
	void movePotsToWinners(final Set<Winner> winners) {
		// Ship it
		for (Winner winner : winners) {
			PlayerSeatComposite winnerPC = getPlayerSeatComposite(winner.getPlayer().getId());
			winnerPC.updateStack(winner.getGainedAmount());
			shipPot(winnerPC);
			ClientGUI.playAudio(ClientGUI.Resources.SOUND_FILE_SLIDE_CHIPS);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clearCommunityCards();
	}
}
