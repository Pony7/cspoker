package org.cspoker.client.gui.swt.window;

import java.util.*;

import org.cspoker.client.gui.swt.control.CardPaintListener;
import org.cspoker.client.gui.swt.control.Chip;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.common.api.lobby.holdemtable.event.*;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.player.SeatedPlayer;
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
		implements HoldemTableListener {
	
	private int seatId;
	private Player player;
	private Label playerName;
	
	private final ProgressBar timeLeftBar = new ProgressBar(this, SWT.NONE);
	
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
	
	private Thread currentProgressUpdaterThread = new Thread(progressUpdater);
	private Composite holeCardsComposite;
	private Label playerStack;
	private List<Card> holeCards = new ArrayList<Card>();
	private List<NavigableMap<Chip, Integer>> currentBetPile = new ArrayList<NavigableMap<Chip, Integer>>();
	Rectangle betChipsArea;
	private boolean dealer;
	
	private int currentStack;
	
	public PlayerSeatComposite(TableComposite parent, int style, int seatId) {
		super(parent, style, parent.getClientCore());
		initGUI();
	}
	
	public void clearHoleCards() {
		holeCards.clear();
		holeCardsComposite.redraw();
		
	}
	
	public Rectangle getBetChipsDisplayArea() {
		if (betChipsArea == null) {
			updateBetChipsDisplayArea();
		}
		return betChipsArea;
	}
	
	public List<NavigableMap<Chip, Integer>> getCurrentBetPile() {
		return currentBetPile;
	}
	
	public int getCurrentStack() {
		return currentStack;
	}
	
	public HoldemPlayerListener getHoldemPlayerListener() {
		// TODO Auto-generated method stub
		return null;
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
		switch (seatId) {
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
	
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @param e
	 * @param gameWindow TODO
	 */
	void handleActionChangedPot(int amount) {
		updateStack(-amount);
		currentBetPile = new ArrayList<NavigableMap<Chip, Integer>>();
		currentBetPile.addAll(getGameState().addToCurrentBetPile(amount));
		ClientGUI.playAudio("snd5.wav");
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
			playerName.setText("Empty");
			playerName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		}
		{
			playerStack = new Label(this, SWT.CENTER);
			GridData player1StackLData = new GridData(60, 15);
			player1StackLData.horizontalAlignment = GridData.CENTER;
			player1StackLData.grabExcessHorizontalSpace = true;
			playerStack.setLayoutData(player1StackLData);
			playerStack.setText("No monies");
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
		setVisible(player != null);
	}
	
	public boolean isDealer() {
		return dealer;
	}
	
	public void onBet(BetEvent betEvent) {
		gameState.setPots(betEvent.getPots());
		handleActionChangedPot(betEvent.getAmount());
		showAction("Bet");
	}
	
	public void onBigBlind(BigBlindEvent bigBlindEvent) {
		gameState.getCurrentBetPile().clear();
		handleActionChangedPot(bigBlindEvent.getAmount());
		showAction("Big Blind");
	}
	
	public void onCall(CallEvent callEvent) {
		gameState.setPots(callEvent.getPots());
		showAction("Call");
	}
	
	public void onCheck(CheckEvent checkEvent) {
		showAction("Check");
		ClientGUI.playAudio("snd4.wav");
		
	}
	
	public void onFold(FoldEvent foldEvent) {
		Set<Card> noCards = Collections.emptySet();
		setHoleCards(noCards);
		ClientGUI.playAudio("snd6.wav");
		
	}
	
	public void onLeaveGame(LeaveGameEvent leaveGameEvent) {
		player = null;
		setVisible(false);
		
	}
	
	public void onNewCommunityCards(NewCommunityCardsEvent newCommunityCardsEvent) {

	}
	
	public void onNewDeal(NewDealEvent newDealEvent) {
		clearHoleCards();
		setHiddenHoleCards();
		// Draw dealer button
		setDealer(player.getName().equalsIgnoreCase(newDealEvent.getDealer().getName()));
		
	}
	
	public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {
		setHoleCards(newPocketCardsEvent.getPocketCards());
		
	}
	
	public void onNewRound(NewRoundEvent newRoundEvent) {
	// TODO Auto-generated method stub
	
	}
	
	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		player = nextPlayerEvent.getPlayer();
		startTimer();
		
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
	
	public void onRaise(RaiseEvent raiseEvent) {
		getGameState().setPots(raiseEvent.getPots());
		handleActionChangedPot(raiseEvent.getAmount());
		
	}
	
	public void onShowHand(ShowHandEvent showHandEvent) {
		player = showHandEvent.getShowdownPlayer().getPlayer();
		setHoleCards(showHandEvent.getShowdownPlayer().getHandCards());
	}
	
	public void onSitIn(SitInEvent sitInEvent) {
		update(sitInEvent.getPlayer());
	}
	
	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
	// TODO Auto-generated method stub
	
	}
	
	public void onWinner(WinnerEvent winnerEvent) {
	// TODO Auto-generated method stub
	
	}
	
	public void setDealer(boolean b) {
		dealer = b;
		
	}
	
	public void setHiddenHoleCards() {
		
		final Card unknownCard = new Card(null, null);
		final Card unknownCard2 = new Card(null, null);
		Set<Card> unknownCards = new TreeSet<Card>();
		// TODO Sit out?
		unknownCards.add(unknownCard);
		unknownCards.add(unknownCard2);
		setHoleCards(unknownCards);
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
	
	public void setPlayerName(Label playerName) {
		this.playerName = playerName;
	}
	
	void setStack(int amount) {
		if (amount == 0) {
			playerStack.setText("Busto");
		}
		playerStack.setText(ClientGUI.formatBet(amount));
	}
	
	public void showAction(final String action) {
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
	
	public void updateBetChipsDisplayArea() {
		betChipsArea = new Rectangle(getInitialChipDrawOffset().x, getInitialChipDrawOffset().y, 100, 80);
	}
	
	private void updateStack(int amount) {

	}
	
	/**
	 * Update with detailed information (for example after opening the table)
	 * 
	 * @param player
	 */
	public void update(SeatedPlayer detailedPlayer) {
		player = detailedPlayer;
		playerName.setForeground(Display.getDefault().getSystemColor(SWT.DEFAULT));
		playerName.setText(detailedPlayer.getName());
		setStack(detailedPlayer.getStackValue());
		// TODO Cast correct? (Should seat ids really be longs??)
		seatId = (int) detailedPlayer.getSeatId();
		
	}
	
}
