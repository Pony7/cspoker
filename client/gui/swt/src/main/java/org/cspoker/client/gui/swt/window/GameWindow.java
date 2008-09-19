package org.cspoker.client.gui.swt.window;

import java.rmi.RemoteException;

import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.GameState;
import org.cspoker.client.gui.swt.control.SWTResourceManager;
import org.cspoker.common.api.lobby.holdemtable.event.*;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.player.Winner;
import org.cspoker.common.elements.table.DetailedTable;
import org.cspoker.common.elements.table.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
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
 * @author stephans
 */
public class GameWindow
		extends ClientComposite
		implements HoldemTableListener {
	
	private final ClientCore clientCore;
	private TableUserInputComposite userInputComposite;
	private TableComposite tableComposite;
	
	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}
	
	public GameWindow(Display display, ClientGUI clientGUI, ClientCore clientCore, Table table) {
		super(new Shell(display, SWT.CLOSE | SWT.RESIZE), clientGUI, clientCore, SWT.NONE);
		this.clientCore = clientCore;
		this.gameState = new GameState(table);
		initGUI();
	}
	
	private void configureShell(final Shell shell) {
		// Get table info for display purposes
		
		shell.setText("Logged in as " + clientCore.getUser().getUserName() + ", Table "
				+ gameState.getTableMemento().getName() + "(Id: " + getTableId() + ")");
		shell.setImage(SWTResourceManager.getImage(ClientGUI.CS_POKER_ICON));
		shell.setLayout(new GridLayout());
		System.out.println("gw size: " + getSize());
		shell.setMinimumSize(700, 600);
		// TODO Somehow lock fixed x:y relation
		System.out.println("Shell size: " + shell.getSize());
		shell.addShellListener(new ShellAdapter() {
			
			@Override
			public void shellClosed(ShellEvent evt) {
				System.out.println("Left table " + gameState.getTableMemento().getName());
			}
		});
		
		shell.addPaintListener(new PaintListener() {
			
			/**
			 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
			 */
			public void paintControl(PaintEvent e) {
				setBackgroundImage(getGui().generateSkin(getShell().getSize()));
				
			}
		});
		
	}
	
	/**
	 * @return the gameState
	 */
	@Override
	public GameState getGameState() {
		return gameState;
	}
	
	public HoldemPlayerListener getHoldemPlayerListener() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @param player
	 * @return
	 */
	private PlayerSeatComposite getPlayerSeatComposite(final Player player) {
		return tableComposite.getPlayerSeatComposite(player.getId());
	}
	
	public long getTableId() {
		return getGameState().getTableMemento().getId();
	}
	
	public TableUserInputComposite getUserInputComposite() {
		return userInputComposite;
	}
	
	/**
	 * Issue a redraw of the table (when chips have moved etc.)
	 */
	public void updateTableGraphics() {
		tableComposite.redraw();
		tableComposite.update();
	}
	
	private void initGUI() {
		try {
			
			setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			setLayout(new GridLayout(1, true));
			setBackgroundMode(SWT.INHERIT_DEFAULT);
			tableComposite = new TableComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
			userInputComposite = new TableUserInputComposite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
			configureShell(getShell());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param event
	 * @return
	 */
	private boolean isUser(final Player player) {
		return player.getId() == clientCore.getUser().getPlayer().getId();
	}
	
	public void onBet(BetEvent betEvent) {
		betEvent.dispatch(getPlayerSeatComposite(betEvent.getPlayer()));
		updateTableGraphics();
		
	}
	
	public void onBigBlind(final BigBlindEvent event) {
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
		updateTableGraphics();
	}
	
	public void onCall(CallEvent callEvent) {
		callEvent.dispatch(getPlayerSeatComposite(callEvent.getPlayer()));
		updateTableGraphics();
		
	}
	
	public void onCheck(CheckEvent checkEvent) {
		checkEvent.dispatch(getPlayerSeatComposite(checkEvent.getPlayer()));
		updateTableGraphics();
		
	}
	
	public void onFold(FoldEvent foldEvent) {
		foldEvent.dispatch(getPlayerSeatComposite(foldEvent.getPlayer()));
		updateTableGraphics();
	}
	
	public void onLeaveGame(LeaveGameEvent leaveGameEvent) {
		leaveGameEvent.dispatch(getPlayerSeatComposite(leaveGameEvent.getPlayer()));
		updateTableGraphics();
		
	}
	
	public void onNewCommunityCards(NewCommunityCardsEvent newCommunityCardsEvent) {
		tableComposite.addCommunityCards(newCommunityCardsEvent.getCommonCards());
		
	}
	
	public void onNewDeal(NewDealEvent newDealEvent) {
		newDealEvent.dispatch(getPlayerSeatComposite(newDealEvent.getDealer()));
		tableComposite.redraw();
		
	}
	
	public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {
		newPocketCardsEvent.dispatch(getPlayerSeatComposite(clientCore.getUser().getPlayer()));
		
	}
	
	public void onNewRound(NewRoundEvent newRoundEvent) {
		gameState.newRound(newRoundEvent.getRoundName());
		tableComposite.moveBetsToPot();
		if (isUser(newRoundEvent.getInitialPlayer())) {
			gameState.setUser(newRoundEvent.getInitialPlayer());
			userInputComposite.prepareForUserInput();
		}
		
	}
	
	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		Player playerToAct = nextPlayerEvent.getPlayer();
		tableComposite.updateProgressBars(playerToAct);
		nextPlayerEvent.dispatch(getPlayerSeatComposite(nextPlayerEvent.getPlayer()));
		
		if (isUser(playerToAct)) {
			gameState.setUser(playerToAct);
			userInputComposite.prepareForUserInput();
		} else {
			userInputComposite.gameActionGroup.setVisible(false);
		}
		
	}
	
	/**
	 * @deprecated Old API, replaced onSitIn ?
	 * @param event
	 * @throws RemoteException
	 */
	@Deprecated
	public void onPlayerJoinedTableEvent(final SitInEvent event)
			throws RemoteException {
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	}
	
	/**
	 * @deprecated Old API ? Maybe with CashierListener?
	 * @param event
	 * @throws RemoteException
	 */
	@Deprecated
	public void onPlayerReboughtEvent(PlayerReboughtEvent event)
			throws RemoteException {
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
		
	}
	
	/**
	 * @deprecated Old API ? But we need this to know the player has sat out and
	 *             may sit in again but has not left
	 * @param event
	 * @throws RemoteException
	 */
	@Deprecated
	public void onPlayerSatOutEvent(PlayerSatOutEvent event)
			throws RemoteException {
		
		// User might automatically sit out if busto, set the button selection
		// accordingly
		if (isUser(event.getPlayer())) {
			userInputComposite.sitInOutButton.setText("Sit In");
			if (!userInputComposite.sitInOutButton.isFocusControl()) {
				userInputComposite.sitInOutButton.setSelection(false);
			}
		}
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	}
	
	public void onRaise(RaiseEvent raiseEvent) {
		raiseEvent.dispatch(getPlayerSeatComposite(raiseEvent.getPlayer()));
		updateTableGraphics();
		
	}
	
	public void onShowHand(ShowHandEvent showHandEvent) {
		showHandEvent.dispatch(getPlayerSeatComposite(showHandEvent.getShowdownPlayer()));
		updateTableGraphics();
		
	}
	
	public void onSitIn(SitInEvent sitInEvent) {
		if (isUser(sitInEvent.getPlayer())) {
			userInputComposite.sitInOutButton.setText("Sit Out");
		}
		sitInEvent.dispatch(getPlayerSeatComposite(sitInEvent.getPlayer()));
		
	}
	
	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		smallBlindEvent.dispatch(getPlayerSeatComposite(smallBlindEvent.getPlayer()));
		updateTableGraphics();
		
	}
	
	public void onWinner(final WinnerEvent winnerEvent) {
		gameState.newRound("Showdown round");
		for (Winner winner : winnerEvent.getWinners())
			winnerEvent.dispatch(getPlayerSeatComposite(winner.getPlayer()));
		tableComposite.moveBetsToPot();
		getDisplay().timerExec(2000, new Runnable() {
			
			/**
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				tableComposite.movePotsToWinners(winnerEvent.getWinners());
				
			}
		});
	}
	
	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */
	public void show() {
		if (isDisposed()) {
			return;
		}
		// TODO Get Bankroll
		// TODO move joinTable to other place (do not join automatically when
		// double-clicking in lobby)
		int bigBlind = gameState.getTableMemento().getGameProperty().getBigBlind();
		DetailedTable thisTable = clientCore.joinTable(getTableId(), 100 * bigBlind);
		gameState.setTableMemento(thisTable);
		Shell containingShell = getShell();
		
		containingShell.open();
		for (SeatedPlayer player : gameState.getTableMemento().getPlayers()) {
			getPlayerSeatComposite(player).update(player);
		}
		// Listen to events
		while (!containingShell.isDisposed()) {
			if (!containingShell.getDisplay().readAndDispatch())
				containingShell.getDisplay().sleep();
		}
	}
}
