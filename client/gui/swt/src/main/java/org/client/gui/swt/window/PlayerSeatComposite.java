package org.cspoker.client.gui.swt.window;

import java.rmi.RemoteException;
import java.util.*;

import org.cspoker.client.gui.swt.control.CardPaintListener;
import org.cspoker.client.gui.swt.control.Chip;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.eventlisteners.game.AllGameEventsListener;
import org.cspoker.common.eventlisteners.game.RemoteAllGameEventsListener;
import org.cspoker.common.events.gameevents.*;
import org.cspoker.common.events.gameevents.playeractionevents.*;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;
import org.cspoker.common.player.SeatedPlayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * Represents a composite in the TableComposite where the player is visualized
 * with his user name, optional avatar, stack and status information TODO Maybe
 * use an adapter for the {@link RemoteAllGameEventsListener} so we dont have to
 * implement all the methods which are handled by a generic case ...
 */
public class PlayerSeatComposite
		extends ClientComposite
		implements AllGameEventsListener {
	
	private final SeatId seatId;
	private SeatedPlayer player;
	private Label playerName;
	
	public Label getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(Label playerName) {
		this.playerName = playerName;
	}
	
	private final ProgressBar timeLeftBar = new ProgressBar(this, SWT.NONE);
	private final Runnable progressUpdater = new Runnable() {
		
		final int maximum = timeLeftBar.getMaximum();
		
		@Override
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
	private Thread currentProgressUpdaterThread = new Thread(progressUpdater);
	private Composite holeCardsComposite;
	private Label playerStack;
	private List<Card> holeCards = new ArrayList<Card>();
	private List<NavigableMap<Chip, Integer>> currentBetPile = new ArrayList<NavigableMap<Chip, Integer>>();
	
	public List<NavigableMap<Chip, Integer>> getCurrentBetPile() {
		return currentBetPile;
	}
	
	Rectangle betChipsArea;
	private boolean dealer;
	
	public boolean isDealer() {
		return dealer;
	}
	
	public PlayerSeatComposite(TableComposite parent, int style, SeatId seatId) {
		super(parent, parent.getGui(), parent.getClientCore(), style);
		player = SeatedPlayer.NULL_PLAYER;
		this.seatId = seatId;
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new GridLayout(1, true));
		setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
		// setSize(200, 200);
		{
			playerName = new Label(this, SWT.SHADOW_NONE | SWT.CENTER | SWT.BORDER);
			GridData player1NameLData = new GridData(80, 25);
			player1NameLData.horizontalAlignment = GridData.CENTER;
			playerName.setLayoutData(player1NameLData);
			playerName.setText(seatId.toString());
			playerName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		}
		{
			playerStack = new Label(this, SWT.CENTER);
			GridData player1StackLData = new GridData(60, 15);
			player1StackLData.horizontalAlignment = GridData.CENTER;
			player1StackLData.grabExcessHorizontalSpace = true;
			playerStack.setLayoutData(player1StackLData);
			playerStack.setText(Integer.toString(player.getStackValue()));
			playerStack.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		}
		{
			GridData timeLeftBarLData = new GridData(60, 10);
			timeLeftBarLData.horizontalAlignment = GridData.CENTER;
			timeLeftBar.setLayoutData(timeLeftBarLData);
		}
		{
			
			holeCardsComposite = new Composite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
			holeCardsComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
			GridData holeCardsLData = new GridData(SWT.FILL, SWT.FILL, true, true);
			holeCardsComposite.setLayoutData(holeCardsLData);
			holeCardsComposite.setSize(gameState.getNumberOfHoleCards() * ClientGUI.PREFERRED_CARD_WIDTH,
					ClientGUI.PREFERRED_CARD_HEIGHT);
			holeCardsLData.minimumWidth = gameState.getNumberOfHoleCards() * ClientGUI.PREFERRED_CARD_WIDTH;
			holeCardsLData.minimumHeight = ClientGUI.PREFERRED_CARD_HEIGHT;
			holeCardsComposite.addPaintListener(new CardPaintListener(holeCards, 2, SWT.CENTER, -10));
			
		}
		setVisible(!player.equals(SeatedPlayer.NULL_PLAYER));
	}
	
	void setStack(int amount) {
		if (amount == 0) {
			playerStack.setText("Busto");
		}
		playerStack.setText(ClientGUI.formatBet(amount));
	}
	
	public SeatedPlayer getPlayer() {
		return player;
	}
	
	/**
	 * @param cards The cards to display. If <code>null</code> is passed,
	 *            display the back of the cards
	 */
	public void setHoleCards(Set<Card> cards) {
		holeCards.clear();
		holeCards.addAll(cards);
		holeCardsComposite.redraw();
	}
	
	public void update(SeatedPlayer player) {
		
		assert (this.player == SeatedPlayer.NULL_PLAYER || this.player.getId().equals(player.getId())) : "Updated PlayerComposite with wrong player";
		this.player = player;
		playerName.setText(player.getName());
		if (player.isSittingOut()) {
			playerName.setText(playerName.getText().concat(" \n (Sitting Out)"));
			
		}
		setStack(player.getStackValue());
		// Update the table (with chips)
		getParent().redraw();
		setVisible(!player.equals(SeatedPlayer.NULL_PLAYER));
	}
	
	public void startTimer() {
		timeLeftBar.setEnabled(true);
		timeLeftBar.setVisible(true);
		timeLeftBar.setSelection(0);
		// Update Progress Bar
		currentProgressUpdaterThread.interrupt();
		currentProgressUpdaterThread = new Thread(progressUpdater);
		currentProgressUpdaterThread.start();
	}
	
	public void stopTimer() {
		if (currentProgressUpdaterThread != null)
			currentProgressUpdaterThread.interrupt();
		timeLeftBar.setEnabled(false);
		timeLeftBar.setVisible(false);
	}
	
	public void setHiddenHoleCards() {
		
		final Card unknownCard = new Card(null, null);
		final Card unknownCard2 = new Card(null, null);
		Set<Card> unknownCards = new TreeSet<Card>();
		if (!player.isSittingOut()) {
			unknownCards.add(unknownCard);
			unknownCards.add(unknownCard2);
		}
		setHoleCards(unknownCards);
	}
	
	public SeatId getSeatId() {
		return seatId;
	}
	
	/**
	 * A utility method to determine where to draw the bet chips on the table
	 * depending on the location of the composite in the grid TODO Make this
	 * generic for other than six-handed tables
	 * 
	 * @return A {@link Point} describing the preferred location to draw the
	 *         chips at
	 */
	public Point getInitialChipDrawOffset() {
		int x = getLocation().x;
		int y = getLocation().y;
		switch (seatId.getId()) {
			case 0:
			case 1:

				y += getClientArea().height + 50;
				break;
			case 2:
				x += getClientArea().width + 50;
				y += getClientArea().height;
				break;
			case 3:
				x += getClientArea().width - 50;
				y += getClientArea().height;
				break;
			case 4:
			case 5:
				y -= 30;
		}
		return new Point(x, y);
		
	}
	
	public Rectangle getBetChipsDisplayArea() {
		if (betChipsArea == null) {
			updateBetChipsDisplayArea();
		}
		return betChipsArea;
	}
	
	public void updateBetChipsDisplayArea() {
		betChipsArea = new Rectangle(getInitialChipDrawOffset().x, getInitialChipDrawOffset().y, 100, 80);
	}
	
	public void setDealer(boolean b) {
		dealer = b;
		
	}
	
	public void showAction(final String action) {
		final String name = playerName.getText();
		playerName.setText(action);
		playerName.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		getDisplay().timerExec(2000, new Runnable() {
			
			@Override
			public void run() {
				playerName.setForeground(Display.getDefault().getSystemColor(SWT.DEFAULT));
				// Check because this may have been outdated in the meantime
				// (sit out etc.)
				if (!playerName.isDisposed() && playerName.getText().equalsIgnoreCase(action)) {
					playerName.setText(name);
				}
			}
		});
	}
	
	@Override
	public void onAllInEvent(ActionChangedPotEvent event) {
	// Nothing to do
	
	}
	
	@Override
	public void onBetEvent(BetEvent event) {
	// Nothing to do
	
	}
	
	@Override
	public void onBigBlindEvent(BigBlindEvent event) {
	// Nothing to do
	
	}
	
	@Override
	public void onCallEvent(CallEvent event) {
	// Nothing to do
	
	}
	
	@Override
	public void onCheckEvent(CheckEvent event) {
		showAction("CHECK");
		ClientGUI.playAudio("snd4.wav");
		
	}
	
	@Override
	public void onFoldEvent(FoldEvent event) {
		Set<Card> noCards = Collections.emptySet();
		setHoleCards(noCards);
		ClientGUI.playAudio("snd6.wav");
		
	}
	
	@Override
	public void onRaiseEvent(RaiseEvent event) {
	// Nothing to do
	
	}
	
	@Override
	public void onSmallBlindEvent(SmallBlindEvent event) {
	// Nothing to do
	
	}
	
	@Override
	public void onNewPocketCardsEvent(NewPocketCardsEvent event) {
		setHoleCards(event.getPocketCards());
		
	}
	
	@Override
	public void onNewCommunityCardsEvent(NewCommunityCardsEvent event) {

	}
	
	@Override
	public void onNewDealEvent(NewDealEvent event) {
		setHiddenHoleCards();
		// Draw dealer button
		setDealer(player.getName().equalsIgnoreCase(event.getDealer().getName()));
		
	}
	
	@Override
	public void onNewRoundEvent(NewRoundEvent event) {
	// Nothing to do
	
	}
	
	@Override
	public void onNextPlayerEvent(NextPlayerEvent event) {
		player = event.getPlayer();
		startTimer();
	}
	
	@Override
	public void onPlayerJoinedTableEvent(PlayerJoinedTableEvent event) {
		update(event.getPlayer());
		
	}
	
	@Override
	public void onPlayerLeftTableEvent(PlayerLeftTableEvent event) {
		player = SeatedPlayer.NULL_PLAYER;
		setVisible(false);
		
	}
	
	@Override
	public void onShowHandEvent(ShowHandEvent event) {
		player = event.getShowdownPlayer().getPlayer();
		setHoleCards(event.getShowdownPlayer().getHandCards());
	}
	
	@Override
	public void onWinnerEvent(WinnerEvent event) {
	// TODO Auto-generated method stub
	
	}
	
	@Override
	public void onGameMessageEvent(GameMessageEvent event) {
	// TODO Auto-generated method stub
	
	}
	
	@Override
	public void onBrokePlayerKickedOutEvent(BrokePlayerKickedOutEvent event) {
		player = SeatedPlayer.NULL_PLAYER;
		onPlayerLeftTableEvent(null);
		
	}
	
	@Override
	public void onPlayerSatOutEvent(PlayerSatOutEvent event)
			throws RemoteException {
		playerName.setText(event.getPlayer().getName() + " \n (Sitting Out)");
	}
	
	@Override
	public void onPlayerSatInEvent(PlayerSatInEvent event)
			throws RemoteException {
		player = event.getPlayer();
		playerName.setText(player.getName());
		
	}
	
	@Override
	public void onPlayerReboughtEvent(PlayerReboughtEvent event)
			throws RemoteException {
		player = event.getPlayer();
		setStack(player.getStackValue());
		
	}
	
	/**
	 * @param e
	 * @param gameWindow TODO
	 */
	void handleActionChangedPot(final ActionChangedPotEvent e) {
		player = e.getPlayer();
		setStack(e.getPlayer().getStackValue());
		currentBetPile = new ArrayList<NavigableMap<Chip, Integer>>();
		currentBetPile.addAll(getGameState().addToCurrentBetPile(e));
		getGameState().setPots(e.getPots());
		showAction(e.getActionId());
		ClientGUI.playAudio("snd5.wav");
	}
	
	public void clearHoleCards() {
		holeCards.clear();
		holeCardsComposite.redraw();
		
	}
	
}
