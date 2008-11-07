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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;
import org.cspoker.client.gui.swt.control.CardPaintListener;
import org.cspoker.client.gui.swt.control.Chip;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.UserSeatedPlayer;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.table.SeatId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * Represents a composite in the TableComposite where the player is visualized
 * with his user name, optional avatar, stack and status information
 */
public class PlayerSeatComposite
		extends ClientComposite {
	
	MouseAdapter sitInMouseAdapter = new SitInMouseAdapter();
	
	private final class SitInMouseAdapter
			extends MouseAdapter {
		
		/**
		 * Upon clicking on the PlayerSeatComposite, the user sits in.
		 * 
		 * @param evt MouseEvent (ignored)
		 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
		 */
		@Override
		public void mouseDown(MouseEvent evt) {
			// Check if seat is occupied
			if (player != null) {
				return;
			}
			// If not, we can probably sit in
			// TODO Buyin, reserve seat in the meantime
			logger.debug("Clicked on PlayerSeatComposite, sit in if empty ...");
			GameWindow containingGameWindow = getParent().getParent();
			UserSeatedPlayer user = containingGameWindow.getUser();
			if (user.isSittingIn()) {
				// Dont do anything if the user is already sitting in
				return;
			}
			int amount = new BuyinDialog(getClientCore(), user.getCashierContext(), gameState.getTableMemento()
					.getGameProperty().getBigBlind() * 100, true).open();
			if (amount <= 0)
				return;
			try {
				player = user;
				user.sitIn(seatId, amount);
				// Update the button accordingly with which the user can sit
				// in and out
				Button sitInOutButton = getParent().getParent().getUserInputComposite().sitInOutButton;
				sitInOutButton.setText("Sit Out");
				sitInOutButton.setSelection(true);
				sitInOutButton.setVisible(true);
			} catch (RemoteException e) {
				getClientCore().handleRemoteException(e);
			} catch (IllegalActionException e) {
				logger.error("This should not happen", e);
			}
		}
	}
	
	private final static Logger logger = Logger.getLogger(PlayerSeatComposite.class);
	
	// Game-relevant fields
	private MutableSeatedPlayer player;
	
	private int numberOfHoleCards = 2;
	private final SeatId seatId;
	/** Indicates whether this player is currently to act */
	private boolean toAct;
	
	// SWT fields
	private Label playerName;
	
	private final ProgressBar timeLeftBar = new ProgressBar(this, SWT.SMOOTH);
	
	private final Runnable progressUpdater = new Runnable() {
		
		private final Color colorDefault = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		private final Color colorAlternative = Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW);
		final int maximum = timeLeftBar.getMaximum();
		
		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			for (final int[] i = new int[1]; i[0] <= maximum; i[0]++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					playerName.setBackground(colorDefault);
					playerName.update();
					return;
				}
				if (getDisplay().isDisposed())
					return;
				getDisplay().syncExec(new Runnable() {
					
					public void run() {
						// TODO Blink here (border?!)...
						if (timeLeftBar.isDisposed() || !timeLeftBar.isEnabled())
							return;
						timeLeftBar.setSelection(i[0]);
						Color color = i[0] % 2 == 0 ? colorDefault : colorAlternative;
						playerName.setBackground(color);
						playerName.update();
					}
				});
			}
		}
	};
	
	private Composite holeCardsComposite;
	private Label playerStack;
	private Canvas chipsArea;
	
	private Future<?> timerAction = new FutureTask<Object>(progressUpdater, null);
	private List<Card> holeCards = new ArrayList<Card>();
	
	/**
	 * @param parent
	 * @param style
	 * @param seatId
	 */
	public PlayerSeatComposite(TableComposite parent, int style, SeatId seatId) {
		super(parent, style);
		numberOfHoleCards = gameState.getNumberOfHoleCards();
		player = null;
		this.seatId = seatId;
		initGUI();
		updatePlayerInfo();
		
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
	public Rectangle getDealerChipLocation() {
		int size = Math.min(Chip.MAX_IMG_SIZE, getParent().getSize().x / 200);
		Image dealerChip = Chip.DEALER.getImage(size);
		Rectangle pcLocation = getBounds();
		int x = 0;
		int y = 0;
		int width = dealerChip.getImageData().width;
		int height = dealerChip.getImageData().height;
		switch ((int) seatId.getId()) {
			case 0:
			case 5:
				x = pcLocation.x + pcLocation.width;
				y = pcLocation.y + pcLocation.height;
				break;
			case 1:
				x = pcLocation.x - width;
				y = pcLocation.y + pcLocation.height;
				break;
			case 2:
			case 3:
				x = pcLocation.x - width;
				y = pcLocation.y;
				break;
			case 4:
				x = pcLocation.x + pcLocation.width;
				y = pcLocation.y;
				break;
			
		}
		return new Rectangle(x, y, width, height);
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
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		setLayout(new GridLayout(1, false));
		GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		data.widthHint = 150;
		data.heightHint = 200;
		data.minimumWidth = 80;
		data.minimumHeight = 100;
		setLayoutData(data);
		{
			playerName = new Label(this, SWT.SHADOW_NONE | SWT.CENTER | SWT.BORDER);
			GridData player1NameLData = new GridData(SWT.DEFAULT, SWT.DEFAULT);
			player1NameLData.horizontalAlignment = GridData.CENTER;
			player1NameLData.widthHint = 100;
			player1NameLData.minimumWidth = 55;
			player1NameLData.grabExcessHorizontalSpace = true;
			playerName.setLayoutData(player1NameLData);
			playerName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		}
		{
			playerStack = new Label(this, SWT.SHADOW_NONE | SWT.CENTER | SWT.BORDER);
			GridData player1StackLData = new GridData(SWT.DEFAULT, SWT.DEFAULT);
			player1StackLData.horizontalAlignment = GridData.CENTER;
			player1StackLData.grabExcessHorizontalSpace = true;
			player1StackLData.minimumWidth = 55;
			playerStack.setLayoutData(player1StackLData);
			playerStack.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		}
		{
			GridData timeLeftBarLData = new GridData(SWT.DEFAULT, SWT.DEFAULT);
			timeLeftBarLData.horizontalAlignment = GridData.CENTER;
			timeLeftBarLData.widthHint = 80;
			timeLeftBarLData.heightHint = 10;
			timeLeftBarLData.minimumWidth = 40;
			timeLeftBarLData.grabExcessHorizontalSpace = true;
			timeLeftBar.setLayoutData(timeLeftBarLData);
		}
		{
			
			holeCardsComposite = new Composite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
			holeCardsComposite.setLayout(new GridLayout(numberOfHoleCards, true));
			
			GridData holeCardsCompositeLayoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
			holeCardsCompositeLayoutData.minimumWidth = numberOfHoleCards * ClientGUI.MINIMUM_CARD_WIDTH;
			holeCardsCompositeLayoutData.minimumHeight = ClientGUI.MINIMUM_CARD_HEIGHT;
			holeCardsCompositeLayoutData.widthHint = numberOfHoleCards * ClientGUI.PREFERRED_CARD_WIDTH - 10;
			holeCardsCompositeLayoutData.heightHint = ClientGUI.PREFERRED_CARD_HEIGHT;
			holeCardsComposite.setLayoutData(holeCardsCompositeLayoutData);
			holeCardsComposite.addPaintListener(new CardPaintListener(holeCards, 2, SWT.CENTER, -10));
			
		}
		
		this.addMouseListener(sitInMouseAdapter);
		for (Control c : getChildren()) {
			c.addMouseListener(sitInMouseAdapter);
		}
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
	 * Refreshes chips and briefly displays the action the player has taken in
	 * place of his user name.
	 * 
	 * @param action The action to display, i.e. <i>"Check"</i> or <i>"Fold"</i>
	 */
	public void showAction(final String action) {
		playerStack.setText(ClientGUI.formatBet(player.getStack().getValue()));
		playerStack.pack(true);
		final String name = player.getName();
		playerName.setText(action);
		playerName.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		getDisplay().timerExec(3000, new Runnable() {
			
			/**
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				
				// Check because this may have been outdated in the meantime
				// (sit out etc.)
				if (!playerName.isDisposed()) {
					playerName.setText(name);
					playerName.setForeground(Display.getDefault().getSystemColor(SWT.DEFAULT));
				}
			}
		});
		layout(true);
	}
	
	/**
	 * Resets and starts the progress bar above the player's name, indicating
	 * that it is this player's turn to act.
	 */
	private void startTimer() {
		timeLeftBar.setEnabled(true);
		timeLeftBar.setVisible(true);
		timeLeftBar.setSelection(0);
		timeLeftBar.setMaximum(30);
		// Update Progress Bar
		timerAction.cancel(true);
		timerAction = executor.submit(progressUpdater);
	}
	
	/**
	 * Stops the Progress Bar timer (if it is running)
	 */
	private void stopTimer() {
		timerAction.cancel(true);
		playerName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		playerName.update();
		timeLeftBar.setEnabled(false);
		timeLeftBar.setVisible(false);
	}
	
	public void setActive(boolean active) {
		this.toAct = active;
		if (active) {
			startTimer();
		} else {
			stopTimer();
		}
	}
	
	/**
	 * Update with detailed information after a player sits in.
	 * 
	 * @param detailedPlayer The player to use as an update.
	 */
	public void occupy(MutableSeatedPlayer detailedPlayer) {
		// Check is necessary because the variable might already be set to a
		// UserSeatedPlayer
		if (player == null) {
			player = detailedPlayer;
		} else {
			logger.warn("Trying to occupy seat with " + detailedPlayer + ", " + player + " already sitting here");
		}
		playerName.setForeground(Display.getDefault().getSystemColor(SWT.DEFAULT));
		playerName.setVisible(true);
		playerName.setText(detailedPlayer.getName());
		playerStack.setVisible(true);
		playerStack.setText(ClientGUI.formatBet(detailedPlayer.getStack().getValue()));
		update();
	}
	
	/**
	 * Reset this {@link PlayerSeatComposite}, indicating an empty seat which
	 * may be occupied by clicking on it
	 */
	public void updatePlayerInfo() {
		String displayedName = (player == null) ? "Empty Seat" : player.getName();
		if (player != null && !player.isSittingIn()) {
			displayedName = displayedName.concat(" (Sitting Out)");
		}
		playerName.setText(displayedName);
		if (player == null) {
			return;
		}
		playerStack.setText(ClientGUI.formatBet(player.getStack().getValue()));
		playerStack.pack(true);
		holeCardsComposite.redraw();
		timerAction.cancel(true);
		layout(true);
	}
	
	/**
	 * @return The immutable seat id associated with this PlayerSeatComposite
	 */
	public SeatId getSeatId() {
		return seatId;
	}
	
	@Override
	public TableComposite getParent() {
		return (TableComposite) super.getParent();
	}
	
	public Canvas getChipsArea() {
		return chipsArea;
	}
	
	public void setChipsArea(Canvas chipsArea) {
		this.chipsArea = chipsArea;
	}
	
	public boolean isToAct() {
		return toAct;
	}
}
