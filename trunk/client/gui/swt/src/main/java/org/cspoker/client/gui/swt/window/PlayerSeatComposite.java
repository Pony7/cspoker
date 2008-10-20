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
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;
import org.cspoker.client.gui.swt.control.CardPaintListener;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.MutableSeatedPlayer;
import org.cspoker.client.gui.swt.control.UserSeatedPlayer;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * Represents a composite in the TableComposite where the player is visualized
 * with his user name, optional avatar, stack and status information
 */
public class PlayerSeatComposite
		extends ClientComposite {
	
	private final static Logger logger = Logger.getLogger(PlayerSeatComposite.class);
	
	// Game-relevant fields
	private MutableSeatedPlayer player;
	
	private int numberOfHoleCards = 2;
	private final long seatId;
	
	// SWT fields
	private Label playerName;
	Rectangle betChipsArea;
	private final ProgressBar timeLeftBar = new ProgressBar(this, SWT.SMOOTH);
	
	private final Runnable progressUpdater = new Runnable() {
		
		final int maximum = timeLeftBar.getMaximum();
		
		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			for (final int[] i = new int[1]; i[0] <= maximum; i[0]++) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					return;
				}
				if (Display.getDefault().isDisposed())
					return;
				Display.getDefault().asyncExec(new Runnable() {
					
					public void run() {
						// TODO Blink here (border?!)...
						if (timeLeftBar.isDisposed() || !timeLeftBar.isEnabled())
							return;
						timeLeftBar.setSelection(i[0]);
					}
				});
			}
		}
	};
	
	private Composite holeCardsComposite;
	private Label playerStack;
	
	private Future<?> timerAction = new FutureTask<Object>(progressUpdater, null);
	private List<Card> holeCards = new ArrayList<Card>();
	
	/**
	 * @param parent
	 * @param style
	 * @param seatId
	 */
	public PlayerSeatComposite(TableComposite parent, int style, int seatId) {
		super(parent, style);
		initGUI();
		numberOfHoleCards = gameState.getNumberOfHoleCards();
		this.seatId = seatId;
		reset();
		
	}
	
	/**
	 * Clears all hole cards
	 */
	public void clearHoleCards() {
		holeCards.clear();
		holeCardsComposite.redraw();
		
	}
	
	/**
	 * @return The {@link Rectangle} where the chips the player has bet in this
	 *         round are displayed.
	 *         <p>
	 *         May be modified during animation threads. Reset to initial
	 *         location via {@link #resetBetChipsDisplayArea()}
	 */
	public Rectangle getBetChipsDisplayArea() {
		if (betChipsArea == null) {
			resetBetChipsDisplayArea();
		}
		return betChipsArea;
	}
	
	/**
	 * Resets the area where the chips the player has bet are displayed to its
	 * default value.
	 * <p>
	 * By default, the chips in the pot are displayed below the community cards.
	 */
	public void resetBetChipsDisplayArea() {
		betChipsArea = getInitialChipDrawOffset();
	}
	
	/**
	 * A utility method to determine where to draw the bet chips on the table
	 * depending on the location of the composite in the grid
	 * <p>
	 * TODO Holy meatballs what a hack :-), Make this generic for other than
	 * six-handed tables
	 * 
	 * @return A {@link Point} describing the preferred location to draw the
	 *         chips at
	 */
	public Rectangle getInitialChipDrawOffset() {
		int x = getLocation().x;
		int y = getLocation().y;
		Rectangle communityCardsLocation = getParent().getCommunityCardsComposite().getBounds();
		switch ((int) seatId) {
			case 0:
			case 1:
				return new Rectangle(x, y + getBounds().height, getBounds().width, communityCardsLocation.y - y
						- getBounds().height);
				// Attention: Seat id 3 is on the left, 2 on the right (clock
				// wise seat ids)
			case 3:
				return new Rectangle(x + getBounds().width, communityCardsLocation.y, communityCardsLocation.x - x
						+ getBounds().width, communityCardsLocation.height);
				
			case 2:
				return new Rectangle(communityCardsLocation.x + communityCardsLocation.width, communityCardsLocation.y,
						x - communityCardsLocation.x - communityCardsLocation.width, communityCardsLocation.height);
			case 4:
			case 5:
				return new Rectangle(x, communityCardsLocation.y + communityCardsLocation.height, getBounds().width, y
						- communityCardsLocation.y - communityCardsLocation.height);
			default:
				throw new IllegalArgumentException("Unsupported seat id for computing bet chips area: " + seatId);
		}
		
	}
	
	/**
	 * @return The player currently occupying this seat, or <code>null</code> if
	 *         the seat is currently available
	 */
	public MutableSeatedPlayer getPlayer() {
		return player;
	}
	
	private void initGUI() {
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 20;
		layout.verticalSpacing = 20;
		setLayout(new GridLayout(1, false));
		GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		data.widthHint = 150;
		data.heightHint = 250;
		data.minimumWidth = 100;
		data.minimumHeight = 60;
		
		setLayoutData(data);
		{
			playerName = new Label(this, SWT.SHADOW_NONE | SWT.CENTER | SWT.BORDER);
			GridData player1NameLData = new GridData(SWT.DEFAULT, SWT.DEFAULT);
			player1NameLData.horizontalAlignment = GridData.CENTER;
			player1NameLData.widthHint = 100;
			player1NameLData.minimumWidth = 80;
			player1NameLData.grabExcessHorizontalSpace = true;
			playerName.setLayoutData(player1NameLData);
			playerName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		}
		{
			playerStack = new Label(this, SWT.CENTER);
			GridData player1StackLData = new GridData(SWT.DEFAULT, SWT.DEFAULT);
			player1StackLData.horizontalAlignment = GridData.CENTER;
			player1StackLData.grabExcessHorizontalSpace = true;
			player1StackLData.minimumWidth = 70;
			playerStack.setLayoutData(player1StackLData);
			playerStack.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		}
		{
			GridData timeLeftBarLData = new GridData(SWT.DEFAULT, SWT.DEFAULT);
			timeLeftBarLData.horizontalAlignment = GridData.CENTER;
			timeLeftBarLData.widthHint = 80;
			timeLeftBar.setLayoutData(timeLeftBarLData);
		}
		{
			
			holeCardsComposite = new Composite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
			holeCardsComposite.setLayout(new GridLayout(numberOfHoleCards, true));
			
			GridData holeCardsCompositeLayoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
			holeCardsCompositeLayoutData.minimumWidth = numberOfHoleCards * ClientGUI.MINIMUM_CARD_WIDTH;
			holeCardsCompositeLayoutData.minimumHeight = ClientGUI.MINIMUM_CARD_HEIGHT;
			holeCardsCompositeLayoutData.widthHint = numberOfHoleCards * ClientGUI.PREFERRED_CARD_WIDTH;
			holeCardsCompositeLayoutData.heightHint = ClientGUI.PREFERRED_CARD_HEIGHT;
			holeCardsComposite.setLayoutData(holeCardsCompositeLayoutData);
			holeCardsComposite.addPaintListener(new CardPaintListener(holeCards, 2, SWT.CENTER, -10));
			
		}
		
		this.addMouseListener(new MouseAdapter() {
			
			/**
			 * Upon clicking on the PlayerSeatComposite, the user sits in.
			 * 
			 * @param evt MouseEvent (ignored)
			 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
			 */
			@Override
			public void mouseDown(MouseEvent evt) {
				// Check if seat is occupied
				if (getPlayer() != null) {
					// If not, we can probably sit in
					// TODO Buyin, reserve seat in the meantime
					logger.debug("Clicked on PlayerSeatComposite, sit in if empty ...");
					GameWindow containingGameWindow = getParent().getParent();
					
					player = containingGameWindow.getUser();
					new BuyinDialog(getClientCore(), containingGameWindow.getUser().getCashierContext(), gameState
							.getTableMemento().getGameProperty().getBigBlind() * 100);
					
				}
			}
		});
	}
	
	// @Override
	// public void onPlayerReboughtEvent(PlayerReboughtEvent event) {
	// player = event.getPlayer();
	// setStack(player.getStackValue());
	//		
	// }
	
	// @Override
	// public void onPlayerSatOutEvent(PlayerSatOutEvent event)
	// throws RemoteException {
	// playerName.setText(event.getPlayer().getName() + " \n (Sitting Out)");
	// }
	/**
	 * @param cards The cards to display. If <code>null</code> is passed,
	 *            display the back of the cards
	 */
	public void setHoleCards(Collection<Card> cards) {
		holeCards.clear();
		holeCards.addAll(cards);
		holeCardsComposite.redraw();
	}
	
	/**
	 * Briefly displays the action the player has taken in place of his user
	 * name.
	 * 
	 * @param action The action to display, i.e. <i>"Check"</i> or <i>"Fold"</i>
	 */
	public void showAction(final String action) {
		playerStack.setText(ClientGUI.formatBet(player.getStackValue()));
		final String name = player.getName();
		playerName.setText(action);
		playerName.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		getDisplay().timerExec(2000, new Runnable() {
			
			/**
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				playerName.setForeground(Display.getDefault().getSystemColor(SWT.DEFAULT));
				// Check because this may have been outdated in the meantime
				// (sit out etc.)
				if (!playerName.isDisposed()) {
					playerName.setText(name);
				}
			}
		});
	}
	
	/**
	 * Resets and starts the progress bar above the player's name, indicating
	 * that it is this player's turn to act.
	 */
	public void startTimer() {
		timeLeftBar.setEnabled(true);
		timeLeftBar.setVisible(true);
		timeLeftBar.setSelection(0);
		// Update Progress Bar
		timerAction.cancel(true);
		timerAction = executor.submit(progressUpdater);
	}
	
	/**
	 * Stops the Progress Bar timer (if it is running)
	 */
	public void stopTimer() {
		timerAction.cancel(true);
		timeLeftBar.setEnabled(false);
		timeLeftBar.setVisible(false);
	}
	
	/**
	 * Update with detailed information after a player sits in.
	 * 
	 * @param detailedPlayer The player to use as an update.
	 */
	public void occupy(SeatedPlayer detailedPlayer) {
		// Check is necessary because the variable might already be set to a
		// UserSeatedPlayer
		if (player == null) {
			if (detailedPlayer.getId() == getClientCore().getUser().getPlayer().getId()) {
				UserSeatedPlayer user = getParent().getParent().getUser();
				user.setPlayer(detailedPlayer);
				player = user;
			} else {
				player = new MutableSeatedPlayer(detailedPlayer, gameState);
			}
		}
		playerName.setForeground(Display.getDefault().getSystemColor(SWT.DEFAULT));
		playerName.setVisible(true);
		playerName.setText(detailedPlayer.getName());
		playerStack.setVisible(true);
		playerStack.setText(ClientGUI.formatBet(player.getStackValue()));
		update();
	}
	
	/**
	 * Reset this {@link PlayerSeatComposite}, indicating an empty seat which
	 * may be occupied by clicking on it
	 */
	public void reset() {
		
		player = null;
		playerName.setText("Empty Seat");
		playerStack.setText("");
		playerStack.setVisible(false);
		holeCardsComposite.redraw();
		timerAction.cancel(true);
	}
	
	/**
	 * @return The immutable seat id associated with this PlayerSeatComposite
	 */
	public long getSeatId() {
		return seatId;
	}
	
	@Override
	public TableComposite getParent() {
		return (TableComposite) super.getParent();
	}
}
