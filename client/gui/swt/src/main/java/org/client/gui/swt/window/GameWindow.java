package org.cspoker.client.gui.swt.window;

import java.rmi.RemoteException;

import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.GameState;
import org.cspoker.client.gui.swt.control.SWTResourceManager;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.eventlisteners.game.RemoteAllGameEventsListener;
import org.cspoker.common.events.gameevents.*;
import org.cspoker.common.events.gameevents.playeractionevents.*;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;
import org.cspoker.common.player.SeatedPlayer;
import org.cspoker.common.player.Winner;
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
		implements RemoteAllGameEventsListener {
	
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
		if (getTableId() != null) {
			shell.setText("Logged in as " + clientCore.getUser().getUserName() + ", Table "
					+ gameState.getTableMemento().getName() + "(Id: " + getTableId() + ")");
		}
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
			
			@Override
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
	
	public TableId getTableId() {
		return getGameState().getTableMemento().getId();
	}
	
	public TableUserInputComposite getUserInputComposite() {
		return userInputComposite;
	}
	
	/**
	 * @param event
	 */
	public void handleActionChangedPot(final ActionChangedPotEvent e) {
		// Bring to top TODO See if this disturbs when
		// multitabling
		getShell().setActive();
		getPlayerSeatComposite(e.getPlayer()).handleActionChangedPot(e);
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
	
	@Override
	public void onAllInEvent(final ActionChangedPotEvent event)
			throws RemoteException {
		// Only if the allin player has put more in the pot than the player
		// before him he is the new reference player
		// Otherwise keep the other player. TODO Refactor?
		handleActionChangedPot(event);
	}
	
	@Override
	public void onBetEvent(final BetEvent event)
			throws RemoteException {
		handleActionChangedPot(event);
	}
	
	@Override
	public void onBigBlindEvent(final BigBlindEvent event)
			throws RemoteException {
		handleActionChangedPot(event);
	}
	
	@Override
	public void onBrokePlayerKickedOutEvent(final BrokePlayerKickedOutEvent event)
			throws RemoteException {
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	}
	
	@Override
	public void onCallEvent(final CallEvent event)
			throws RemoteException {
		handleActionChangedPot(event);
	}
	
	@Override
	public void onCheckEvent(final CheckEvent event)
			throws RemoteException {
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	}
	
	/**
	 * @param player
	 * @return
	 */
	private PlayerSeatComposite getPlayerSeatComposite(final SeatedPlayer player) {
		return tableComposite.getPlayerSeatComposite(player.getSeatId());
	}
	
	@Override
	public void onFoldEvent(final FoldEvent event)
			throws RemoteException {
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	}
	
	@Override
	public void onGameMessageEvent(final GameMessageEvent event)
			throws RemoteException {
	// Handled by Client Core
	}
	
	@Override
	public void onNewCommunityCardsEvent(final NewCommunityCardsEvent event)
			throws RemoteException {
		tableComposite.addCommunityCards(event.getCommonCards());
	}
	
	@Override
	public synchronized void onNewDealEvent(final NewDealEvent event)
			throws RemoteException {
		for (SeatedPlayer dealPlayer : event.getPlayers()) {
			event.dispatch(getPlayerSeatComposite(dealPlayer));
		}
		tableComposite.redraw();
	}
	
	@Override
	public void onNewPocketCardsEvent(final NewPocketCardsEvent event)
			throws RemoteException {
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	}
	
	@Override
	public void onNewRoundEvent(final NewRoundEvent event) {
		gameState.newRound(event.getRoundName());
		tableComposite.moveBetsToPot();
		if (isUser(event.getInitialPlayer())) {
			gameState.setUser(event.getInitialPlayer());
			userInputComposite.prepareForUserInput();
		}
		
	}
	
	/**
	 * @param event
	 * @return
	 */
	private boolean isUser(final SeatedPlayer player) {
		return player.getName().equalsIgnoreCase(clientCore.getUser().getUserName());
	}
	
	@Override
	public void onNextPlayerEvent(final NextPlayerEvent event)
			throws RemoteException {
		SeatedPlayer playerToAct = event.getPlayer();
		tableComposite.updateProgressBars(playerToAct);
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
		
		if (isUser(playerToAct) && playerToAct.getStackValue() > 0) {
			gameState.setUser(playerToAct);
			userInputComposite.prepareForUserInput();
		} else {
			userInputComposite.gameActionGroup.setVisible(false);
		}
	}
	
	@Override
	public void onPlayerJoinedTableEvent(final PlayerJoinedTableEvent event)
			throws RemoteException {
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	}
	
	@Override
	public void onPlayerLeftTableEvent(final PlayerLeftTableEvent event)
			throws RemoteException {
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
	}
	
	@Override
	public void onPlayerReboughtEvent(PlayerReboughtEvent event)
			throws RemoteException {
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
		
	}
	
	@Override
	public void onPlayerSatInEvent(PlayerSatInEvent event)
			throws RemoteException {
		if (isUser(event.getPlayer())) {
			userInputComposite.sitInOutButton.setText("Sit Out");
		}
		event.dispatch(getPlayerSeatComposite(event.getPlayer()));
		
	}
	
	@Override
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
	
	@Override
	public void onRaiseEvent(final RaiseEvent event) {
		handleActionChangedPot(event);
		
	}
	
	@Override
	public void onShowHandEvent(final ShowHandEvent event)
			throws RemoteException {
		event.dispatch(getPlayerSeatComposite(event.getShowdownPlayer().getPlayer()));
		
	}
	
	@Override
	public void onSmallBlindEvent(final SmallBlindEvent event) {
		handleActionChangedPot(event);
	}
	
	@Override
	public synchronized void onWinnerEvent(final WinnerEvent event)
			throws RemoteException {
		
		gameState.newRound("Showdown round");
		for (Winner winner : event.getWinners())
			event.dispatch(getPlayerSeatComposite(winner.getPlayer()));
		tableComposite.moveBetsToPot();
		getDisplay().timerExec(2000, new Runnable() {
			
			@Override
			public void run() {
				tableComposite.movePotsToWinners(event);
				
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
		Table thisTable = clientCore.joinTable(getTableId(), 100 * bigBlind);
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
