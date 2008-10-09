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

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.gui.swt.control.*;
import org.cspoker.common.api.lobby.holdemtable.event.*;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.pots.Pots;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A Game window for a particular table the user is sitting at. Events from the
 * server for the corresponding table are delivered here and handled/dispatched
 * go children controls such as {@link TableComposite} or
 * {@link PlayerSeatComposite}s
 * 
 * @author Stephan Schmidt
 */
public class GameWindow
		extends ClientComposite
		implements HoldemTableListener, HoldemPlayerListener {
	
	/** Minimum width so everything will still reasonably displayed */
	public static final int MINIMUM_WIDTH = 550;
	/** Minimum height so everything will still reasonably displayed */
	public static final int MINIMUM_HEIGHT = 600;
	
	private final static Logger logger = Logger.getLogger(GameWindow.class);
	
	private final UserSeatedPlayer user;
	private TableUserInputComposite userInputComposite;
	private TableComposite tableComposite;
	
	/**
	 * Creates a new <code>GameWindow</code>.
	 * <p>
	 * Initializes the window and updates all the composites with the
	 * information passed in the {@link DetailedHoldemTable} information.
	 * 
	 * @param lobbyWindow The lobby reference
	 * @param table The table to display in the game window
	 */
	public GameWindow(LobbyWindow lobbyWindow, DetailedHoldemTable table) {
		super(new Shell(lobbyWindow.getDisplay(), SWT.CLOSE | SWT.RESIZE), SWT.NONE, lobbyWindow.getClientCore());
		gameState = new GameState(table);
		user = new UserSeatedPlayer(this, getClientCore().getUser(), gameState);
		
		initGUI();
		for (SeatedPlayer player : table.getPlayers()) {
			tableComposite.findPlayerSeatCompositeBySeatId(player.getSeatId()).occupy(player);
		}
		
	}
	
	/**
	 * Does initial shell configuration
	 * 
	 * @param shell The GameWindow's shell to configure
	 */
	private void configureShell(final Shell shell) {
		// Get table info for display purposes
		shell.setText("Logged in as " + getClientCore().getUser().getUserName() + ", Table "
				+ gameState.getTableMemento().getName() + "(Id: " + gameState.getTableMemento().getId() + ")");
		shell.setImage(SWTResourceManager.getImage(ClientGUI.Resources.CS_POKER_ICON));
		shell.setLayout(new GridLayout());
		logger.debug("gw size: " + getSize());
		shell.setMinimumSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
		// TODO Somehow lock fixed x:y relation
		logger.debug("Shell size: " + shell.getSize());
		createCloseListener();
		shell.addPaintListener(new PaintListener() {
			
			/**
			 * Make sure we handle the case when the shell is resized.
			 * <p>
			 * In this case, rescale the background image and recompute the
			 * areas where the chips are displayed on the table.
			 * 
			 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
			 */
			public void paintControl(PaintEvent e) {
				if (getBackgroundImage() != null) {
					getBackgroundImage().dispose();
				}
				Image skin = SWTResourceManager.getImage(ClientGUI.Resources.TABLE_IMAGE);
				Image scaled = new Image(Display.getDefault(), skin.getImageData().scaledTo(getShell().getSize().x,
						getShell().getSize().y));
				setBackgroundImage(scaled);
				tableComposite.resetPotChipsArea();
				for (PlayerSeatComposite psc : tableComposite.getPlayerSeatComposites(false)) {
					psc.resetBetChipsDisplayArea();
				}
			}
		});
	}
	
	/**
	 * @param player
	 * @return
	 */
	private PlayerSeatComposite getPlayerSeatComposite(final Player player) {
		return tableComposite.findPlayerSeatCompositeByPlayerId(player.getId());
	}
	
	/**
	 * Issue a redraw of the table (when chips have moved etc.)
	 */
	public void updateTableGraphics() {
		tableComposite.redraw();
		tableComposite.update();
	}
	
	private void initGUI() {
		
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.minimumWidth = 500;
		data.minimumHeight = 450;
		setLayout(new GridLayout(1, true));
		setLayoutData(data);
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		tableComposite = new TableComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
		userInputComposite = new TableUserInputComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
		configureShell(getShell());
	}
	
	/**
	 * Determines whether the player (from the event usually) is the user of the
	 * client. Does string comparison on the name (TODO: use id?)
	 * 
	 * @param player The player to check
	 * @return whether the given player is equal to the user
	 */
	private boolean isUser(final Player player) {
		return getClientCore().getUser().getUserName().equalsIgnoreCase(player.getName());
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onBet(org.cspoker.common.api.lobby.holdemtable.event.BetEvent)
	 */
	public void onBet(BetEvent betEvent) {
		handleActionChangedPot(betEvent.getPots(), betEvent.getAmount(), betEvent.getPlayer(), "Bet");
		userInputComposite.showDealerMessage(betEvent);
	}
	
	private void handleActionChangedPot(Pots pots, int amount, Player bettor, String action) {
		// Game State update
		// Set new reference bet pile in GameState
		gameState.betRaise(amount, pots);
		MutableSeatedPlayer player = getPlayerSeatComposite(bettor).getPlayer();
		
		// Update the chip stack of the player who changed the pot by
		// betting/raising/calling
		player.updateStackAndBetChips(amount);
		player.getCurrentBetPile().clear();
		player.getCurrentBetPile().addAll(gameState.getCurrentBetPile());
		getPlayerSeatComposite(bettor).showAction(action);
		
		ClientGUI.playAudio(ClientGUI.Resources.SOUND_FILE_BETRAISE);
		updateTableGraphics();
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onBigBlind(org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent)
	 */
	public void onBigBlind(final BigBlindEvent event) {
		handleActionChangedPot(event.getPots(), event.getAmount(), event.getPlayer(), "Big Blind");
		userInputComposite.showDealerMessage(event);
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onCall(org.cspoker.common.api.lobby.holdemtable.event.CallEvent)
	 */
	public void onCall(CallEvent callEvent) {
		handleActionChangedPot(callEvent.getPots(), 0, callEvent.getPlayer(), "Call");
		userInputComposite.showDealerMessage(callEvent);
		
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onCheck(org.cspoker.common.api.lobby.holdemtable.event.CheckEvent)
	 */
	public void onCheck(CheckEvent checkEvent) {
		getPlayerSeatComposite(checkEvent.getPlayer()).showAction("Check");
		userInputComposite.showDealerMessage(checkEvent);
		
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onFold(org.cspoker.common.api.lobby.holdemtable.event.FoldEvent)
	 */
	public void onFold(FoldEvent foldEvent) {
		getPlayerSeatComposite(foldEvent.getPlayer()).showAction("Fold");
		userInputComposite.showDealerMessage(foldEvent);
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onLeaveGame(org.cspoker.common.api.lobby.holdemtable.event.LeaveGameEvent)
	 */
	public void onLeaveGame(LeaveGameEvent leaveGameEvent) {
		getPlayerSeatComposite(leaveGameEvent.getPlayer()).reset();
		userInputComposite.showDealerMessage(leaveGameEvent);
		updateTableGraphics();
		
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onNewCommunityCards(org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent)
	 */
	public void onNewCommunityCards(NewCommunityCardsEvent newCommunityCardsEvent) {
		tableComposite.addCommunityCards(newCommunityCardsEvent.getCommonCards());
		userInputComposite.showDealerMessage(newCommunityCardsEvent);
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onNewDeal(org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent)
	 */
	public void onNewDeal(NewDealEvent newDealEvent) {
		gameState.newRound();
		gameState.setPots(new Pots(0));
		
		for (PlayerSeatComposite psc : tableComposite.getPlayerSeatComposites(true)) {
			psc.setHoleCards(Arrays.asList(ClientGUI.UNKNOWN_CARD, ClientGUI.UNKNOWN_CARD));
			// Draw dealer button
			psc.getPlayer().setDealer(newDealEvent.getDealer().getId() == psc.getPlayer().getId());
			psc.resetBetChipsDisplayArea();
			psc.getPlayer().setBetChipsValue(0);
		}
		userInputComposite.showDealerMessage(newDealEvent);
		tableComposite.redraw();
		
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener#onNewPocketCards(org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent)
	 */
	public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {
		getPlayerSeatComposite(getClientCore().getUser().getPlayer())
				.setHoleCards(newPocketCardsEvent.getPocketCards());
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onNewRound(org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent)
	 */
	public void onNewRound(NewRoundEvent newRoundEvent) {
		tableComposite.moveBetsToPot();
		gameState.newRound();
		for (PlayerSeatComposite psc : tableComposite.getPlayerSeatComposites(true)) {
			psc.getPlayer().setBetChipsValue(0);
		}
		if (isUser(newRoundEvent.getInitialPlayer())) {
			userInputComposite.prepareForUserInput();
		}
		userInputComposite.showDealerMessage(newRoundEvent);
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onNextPlayer(org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent)
	 */
	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		Player playerToAct = nextPlayerEvent.getPlayer();
		tableComposite.updateProgressBars(playerToAct);
		getPlayerSeatComposite(nextPlayerEvent.getPlayer()).startTimer();
		userInputComposite.getGameActionGroup().setVisible(isUser(playerToAct));
		if (isUser(playerToAct)) {
			userInputComposite.prepareForUserInput();
		}
		userInputComposite.update();
		
	}
	
	// FIXME Old API methods
	// /**
	// * @deprecated Old API, replaced onSitIn ?
	// * @param event
	// * @throws RemoteException
	// */
	// @Deprecated
	// public void onPlayerJoinedTableEvent(final SitInEvent event)
	// throws RemoteException {
	// event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	// }
	
	// /**
	// * @deprecated Old API ? Maybe with CashierListener?
	// * @param event
	// * @throws RemoteException
	// */
	// @Deprecated
	// public void onPlayerReboughtEvent(PlayerReboughtEvent event)
	// throws RemoteException {
	// event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	//		
	// }
	
	// /**
	// * @deprecated Old API ? But we need this to know the player has sat out
	// and
	// * may sit in again but has not left
	// * @param event
	// * @throws RemoteException
	// */
	// @Deprecated
	// public void onPlayerSatOutEvent(PlayerSatOutEvent event)
	// throws RemoteException {
	//		
	// // User might automatically sit out if busto, set the button selection
	// // accordingly
	// if (isUser(event.getPlayer())) {
	// userInputComposite.sitInOutButton.setText("Sit In");
	// if (!userInputComposite.sitInOutButton.isFocusControl()) {
	// userInputComposite.sitInOutButton.setSelection(false);
	// }
	// }
	// event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	// }
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onRaise(org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent)
	 */
	public void onRaise(RaiseEvent raiseEvent) {
		handleActionChangedPot(raiseEvent.getPots(), raiseEvent.getAmount(), raiseEvent.getPlayer(), "Raise");
		userInputComposite.showDealerMessage(raiseEvent);
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onShowHand(org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent)
	 */
	public void onShowHand(ShowHandEvent showHandEvent) {
		getPlayerSeatComposite(showHandEvent.getShowdownPlayer()).setHoleCards(
				showHandEvent.getShowdownPlayer().getHandCards());
		// userInputComposite.showDealerMessage(showHandEvent);
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onSitIn(org.cspoker.common.api.lobby.holdemtable.event.SitInEvent)
	 */
	public void onSitIn(SitInEvent sitInEvent) {
		if (isUser(sitInEvent.getPlayer())) {
			userInputComposite.sitInOutButton.setText("Sit Out");
		}
		tableComposite.findPlayerSeatCompositeBySeatId(sitInEvent.getPlayer().getSeatId()).occupy(
				sitInEvent.getPlayer());
		userInputComposite.showDealerMessage(sitInEvent);
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onSmallBlind(org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent)
	 */
	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		handleActionChangedPot(smallBlindEvent.getPots(), smallBlindEvent.getAmount(), smallBlindEvent.getPlayer(),
				"Small Blind");
		userInputComposite.showDealerMessage(smallBlindEvent);
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onWinner(org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent)
	 */
	public void onWinner(final WinnerEvent winnerEvent) {
		tableComposite.moveBetsToPot();
		// FIXME XXX Make sure that this is better , i.e. new round
		// doesnt start until animation stuff is complete
		getDisplay().timerExec(2000, new Runnable() {
			
			/**
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				tableComposite.movePotsToWinners(winnerEvent.getWinners());
				
			}
		});
		
		tableComposite.clearCommunityCards();
		tableComposite.getCommunityCardsComposite().setVisible(false);
		for (PlayerSeatComposite psc : tableComposite.getPlayerSeatComposites(true)) {
			Set<Card> noCards = Collections.emptySet();
			psc.setHoleCards(noCards);
		}
		userInputComposite.showDealerMessage(winnerEvent);
		redraw();
	}
	
	/**
	 * PRE: Window is initialized Open the window in a new shell Open the
	 * GameWindow inside a new shell
	 */
	public void show() {
		Shell containingShell = getShell();
		containingShell.open();
		// Listen to events
		while (!containingShell.isDisposed()) {
			if (!containingShell.getDisplay().readAndDispatch())
				containingShell.getDisplay().sleep();
		}
	}
	
	/**
	 * @return The {@link LobbyWindow} (Parent of all GameWindows)
	 * @see org.eclipse.swt.widgets.Control#getParent()
	 */
	@Override
	public LobbyWindow getParent() {
		return (LobbyWindow) super.getParent();
	}
	
	/**
	 * @return The contained {@link TableUserInputComposite}
	 */
	public TableUserInputComposite getUserInputComposite() {
		return userInputComposite;
	}
	
	/**
	 * @return The contained {@link TableComposite}
	 */
	public TableComposite getTableComposite() {
		return tableComposite;
	}
	
	/**
	 * @return This GameWindow's user. The user is the access point for all
	 *         callbacks to the Server!
	 */
	public UserSeatedPlayer getUser() {
		return user;
	}
}
