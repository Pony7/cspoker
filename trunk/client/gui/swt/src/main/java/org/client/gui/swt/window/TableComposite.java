package org.cspoker.client.gui.swt.window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.cspoker.client.gui.swt.control.CardPaintListener;
import org.cspoker.client.gui.swt.control.ChipPaintListener;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.events.gameevents.WinnerEvent;
import org.cspoker.common.player.SeatedPlayer;
import org.cspoker.common.player.Winner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class TableComposite
		extends ClientComposite {
	
	private Composite communityCardsComposite;
	Rectangle potChipsDisplayArea;
	private List<Card> communityCards = new ArrayList<Card>();
	private ChipPaintListener chipPaintListener;
	public boolean updateChipLocations = true;
	
	public Composite getCommunityCardsComposite() {
		return communityCardsComposite;
	}
	
	public TableComposite(GameWindow parent, int style) {
		super(parent, parent.getGui(), parent.getClientCore(), style);
		this.gameState = parent.getGameState();
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new GridLayout(5, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// Add first row, placeholder labels and player composites
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, new SeatId(0));
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, new SeatId(1));
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		// End first row
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, new SeatId(2));
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		communityCardsComposite = new Composite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
		communityCardsComposite.setLayout(new GridLayout(1, true));
		GridData communityCardsLayoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		communityCardsLayoutData.widthHint = 5 * ClientGUI.PREFERRED_CARD_WIDTH;
		communityCardsLayoutData.heightHint = ClientGUI.PREFERRED_CARD_HEIGHT;
		communityCardsComposite.setLayoutData(communityCardsLayoutData);
		communityCardsComposite.addPaintListener(new CardPaintListener(communityCards, 5, SWT.LEFT, 10));
		
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, new SeatId(3));
		// End second row
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, new SeatId(4));
		new Label(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE).setVisible(false);
		new PlayerSeatComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE, new SeatId(5));
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
	public PlayerSeatComposite getPlayerSeatComposite(SeatId seatId) {
		for (PlayerSeatComposite pc : getPlayerSeatComposites()) {
			if (pc.getSeatId().getId() == seatId.getId()) {
				return pc;
			}
		}
		return null;
	}
	
	void updateProgressBars(SeatedPlayer seatedPlayer) {
		for (PlayerSeatComposite pc : getPlayerSeatComposites()) {
			pc.stopTimer();
		}
		getPlayerSeatComposite(seatedPlayer.getSeatId()).startTimer();
	}
	
	public void moveBetsToPot() {
		List<Rectangle> chipLocList = new ArrayList<Rectangle>();
		// Put it in the pot
		// Set all player chips to 0 and redraw table
		for (PlayerSeatComposite pc : getPlayerSeatComposites()) {
			SeatedPlayer oldPlayer = pc.getPlayer();
			if (oldPlayer.getBetChipsValue() > 0) {
				chipLocList.add(pc.getBetChipsDisplayArea());
			}
		}
		// Animate chips before resetting value
		// TODO Make this stuff more robusto
		animateChips(chipLocList, getPotOffset());
		for (PlayerSeatComposite pc : getPlayerSeatComposites()) {
			SeatedPlayer oldPlayer = pc.getPlayer();
			if (oldPlayer == SeatedPlayer.NULL_PLAYER) {
				continue;
			}
			pc.getCurrentBetPile().clear();
			pc.betChipsArea = null;
			potChipsDisplayArea = null;
		}
		redraw();
	}
	
	/**
	 * @return A list of player seat composites where the index is the seat id
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
	
	public void shipPot(PlayerSeatComposite winner) {
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
	 * @param gameWindow TODO
	 * @param event
	 */
	void movePotsToWinners(final WinnerEvent event) {
		// Ship it
		for (Winner winner : event.getWinners()) {
			PlayerSeatComposite winnerPC = getPlayerSeatComposite(winner.getPlayer().getSeatId());
			winnerPC.setStack(winner.getPlayer().getStackValue() + winner.getGainedAmount());
			shipPot(winnerPC);
			ClientGUI.playAudio("snd3.wav");
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clearCommunityCards();
		for (PlayerSeatComposite psc : getPlayerSeatComposites()) {
			psc.clearHoleCards();
			
		}
		gameState.setMoneyInMiddle(0);
		redraw();
	}
}
