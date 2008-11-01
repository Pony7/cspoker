/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.server.common.gamecontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.JoinTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.util.threading.ScheduledRequestExecutor;
import org.cspoker.server.common.HoldemTableContextImpl;
import org.cspoker.server.common.account.ExtendedAccountContext;
import org.cspoker.server.common.chat.ChatServer;
import org.cspoker.server.common.chat.room.TableChatRoom;
import org.cspoker.server.common.elements.chips.IllegalValueException;
import org.cspoker.server.common.elements.id.PlayerId;
import org.cspoker.server.common.elements.id.SeatId;
import org.cspoker.server.common.elements.id.TableId;
import org.cspoker.server.common.player.MutableSeatedPlayer;
import org.cspoker.server.common.player.MutablePlayer;

/**
 * A class of game mediators to decouple the game control from all users:
 * server, gui, logger, ...
 * 
 * 
 */
public class PokerTable {
	
	private final static ChatServer chatServer = ChatServer.getInstance();
	
	private TableChatRoom chatRoom;
	
	private static Logger logger = Logger.getLogger(PokerTable.class);
	
	private final TableId tableId;
	
	private String name;
	
	private transient TableState tableState;
	
	private final ExtendedAccountContext creator;
	
	private TableConfiguration configuration;
	
	private ConcurrentHashMap<PlayerId, HoldemTableListener> joinedPlayers = new ConcurrentHashMap<PlayerId, HoldemTableListener>();
	
	private ConcurrentHashMap<PlayerId, HoldemPlayerListener> sitInPlayers = new ConcurrentHashMap<PlayerId, HoldemPlayerListener>();
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	
	public PokerTable(TableId id, String name, TableConfiguration configuration, ExtendedAccountContext creator) {
		
		if (id == null)
			throw new IllegalArgumentException("The given table id is not effective.");
		if (creator == null)
			throw new IllegalArgumentException("The given creator is not effective.");
		if (configuration == null)
			throw new IllegalArgumentException("The given configuration is not effective.");
		
		tableId = id;
		setName(name);
		this.configuration = configuration;
		tableState = new WaitingTableState(this);
		this.creator = creator;
		this.chatRoom = chatServer.addTableChatRoom(this);
	}
	
	/***************************************************************************
	 * Name
	 **************************************************************************/
	
	/**
	 * Return the name of this table.
	 * 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Check whether tables can have the given name as their name.
	 * 
	 * @param name The name to check.
	 * @return The given name should be effective | name!=null
	 * @return The given name should be at least one character long. |
	 *         name!=null && name.length()>0
	 */
	public static boolean canHaveAsName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() > 0;
	}
	
	/**
	 * Set the name of this table to the given name.
	 * 
	 * @param name The new name for this table.
	 * @post If the given name is a valid name for this table the name of this
	 *       table is set to the given name, else the default name is used. |
	 *       if(canHaveAsName(name)) | then new.getName().equals(name) | else
	 *       new.getName().equals(getDefaultName())
	 */
	private void setName(String name) {
		if (!canHaveAsName(name)) {
			this.name = getDefaultName();
		} else {
			this.name = name;
		}
	}
	
	/**
	 * Change the name of this table to the given name.
	 * 
	 * @param name The new name for this table.
	 * @post If the given name is a valid name for this table the name of this
	 *       table is set to the given name, else the previous name is kept. |
	 *       if(canHaveAsName(name)) | then new.getName().equals(name) | else
	 *       new.getName().equals(getName())
	 */
	public void changeName(String name) {
		if (!canHaveAsName(name)) {
			setName(name);
		}
	}
	
	protected String getDefaultName() {
		return "default table";
	}
	
	/**
	 * Returns the table id of this table.
	 * 
	 * @return The table id of this table.
	 */
	public TableId getTableId() {
		return tableId;
	}
	
	/**
	 * Returns a short description for this table: #id and name.
	 * 
	 * @return A short description for this table: #id and name.
	 */
	public Table getShortTableInformation() {
		return new Table(getTableId().getId(), getName());
	}
	
	public DetailedHoldemTable getTableInformation() {
		return new DetailedHoldemTable(getTableId().getId(), getName(), tableState.getSeatedPlayers(), tableState
				.isPlaying(), configuration);
	}
	
	/**
	 * Returns the table configuration of this table.
	 * 
	 * @return The table configuration of this table.
	 */
	public TableConfiguration getTableConfiguration() {
		return configuration;
	}
	
	public boolean isEmpty() {
		return false; // TODO
	}
	
	public synchronized boolean terminate() {
		// TODO unsubscribe all if can terminate (check if isEmpty())
		return true;
	}
	
	public synchronized void startGame(ExtendedAccountContext accountContext)
			throws IllegalActionException {
		if (!creator.equals(accountContext))
			throw new IllegalActionException("Only the creator of the table can start a game.");
		if (tableState.isPlaying())
			throw new IllegalActionException("The game has already started.");
		tableState = tableState.getNextState();
	}
	
	public synchronized void startGame()
	throws IllegalActionException {
		if (tableState.isPlaying())
			throw new IllegalActionException("The game has already started.");
		tableState = tableState.getNextState();
}
	
	/***************************************************************************
	 * Player Actions
	 **************************************************************************/
	
	/**
	 * The given player goes all-in.
	 * 
	 * @param player The player who goes all-in.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	public void allIn(MutableSeatedPlayer player)
			throws IllegalActionException {
		tableState.allIn(player);
		cancelOldTimeOut();
	}
	
	/**
	 * The player puts money in the pot.
	 * 
	 * @param player The player who puts a bet.
	 * @param amount The amount of the bet.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	public void bet(MutableSeatedPlayer player, int amount)
			throws IllegalActionException {
		tableState.bet(player, amount);
		cancelOldTimeOut();
	}
	
	/**
	 * To put into the pot an amount of money equal to the most recent bet or
	 * raise.
	 * 
	 * @param player The player who calls.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	public void call(MutableSeatedPlayer player)
			throws IllegalActionException {
		tableState.call(player);
		cancelOldTimeOut();
	}
	
	/**
	 * If there is no bet on the table and you do not wish to place a bet. You
	 * may only check when there are no prior bets.
	 * 
	 * @param player The player who checks.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	public void check(MutableSeatedPlayer player)
			throws IllegalActionException {
		tableState.check(player);
		cancelOldTimeOut();
	}
	
	/**
	 * The given player folds the cards.
	 * 
	 * The player will not be able to take any actions in the coming rounds of
	 * the current deal.
	 * 
	 * @param player The player who folds.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	public void fold(MutableSeatedPlayer player)
			throws IllegalActionException {
		tableState.fold(player);
		cancelOldTimeOut();
	}
	
	/**
	 * Raise the bet with given amount.
	 * 
	 * @param player The player who raises the current bet.
	 * @param amount The amount with which to raise the bet.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	public void raise(MutableSeatedPlayer player, int amount)
			throws IllegalActionException {
		tableState.raise(player, amount);
		cancelOldTimeOut();
	}
	
	public HoldemTableContext joinTable(MutablePlayer player, HoldemTableListener holdemTableListener)
			throws IllegalActionException {
		if (player == null)
			throw new IllegalArgumentException("The given player should be effective.");
		if (holdemTableListener == null)
			throw new IllegalArgumentException("The given holdem table listener should be effective.");
		
		if (joinedPlayers.putIfAbsent(player.getId(), holdemTableListener) != null)
			throw new IllegalActionException(player.toString() + " is already joined at this table.");
		
		publishJoinTableEvent(new JoinTableEvent(player.getMemento()));
		subscribeHoldemTableListener(holdemTableListener);
		return new HoldemTableContextImpl(player, this);
	}
	
	public boolean hasAsJoinedPlayer(MutablePlayer player) {
		return player == null ? false : hasAsJoinedPlayer(player.getId());
	}
	
	public boolean hasAsJoinedPlayer(PlayerId id) {
		return id == null ? false : joinedPlayers.containsKey(id);
	}
	
	public void leaveTable(MutablePlayer player) {
		if (player == null)
			throw new IllegalArgumentException("The given player should be effective.");
		HoldemTableListener listener = joinedPlayers.remove(player.getId());
		if (listener != null)
			unsubscribeHoldemTableListener(listener);
	}
	
	public synchronized HoldemPlayerContext sitIn(SeatId seatId, int buyIn, MutablePlayer player,
			HoldemPlayerListener holdemPlayerListener)
	throws IllegalActionException {
		try {
			HoldemPlayerContext toReturn = tableState.sitIn(seatId, new MutableSeatedPlayer(player, buyIn));
			sitInPlayers.put(player.getId(), holdemPlayerListener);
			subscribeHoldemPlayerListener(player.getId(), holdemPlayerListener);
			if (sitInPlayers.size() > 1) {
				tableState = tableState.getNextState();
				tableState.deal(tableState.getGame().getDealer());
			}
			return toReturn;
		} catch (IllegalValueException e) {
			throw new IllegalActionException("You can not sit in to this table with the given buy-in of " + buyIn
					+ "chips.");
		}

	}


	public synchronized HoldemPlayerContext sitIn(int buyIn, MutablePlayer player,
			HoldemPlayerListener holdemPlayerListener) throws IllegalActionException {
		try {
			HoldemPlayerContext toReturn = tableState.sitIn(new MutableSeatedPlayer(player, buyIn));
			sitInPlayers.put(player.getId(), holdemPlayerListener);
			subscribeHoldemPlayerListener(player.getId(), holdemPlayerListener);
			if (sitInPlayers.size() > 1) {
				tableState = tableState.getNextState();
				tableState.deal(tableState.getGame().getDealer());
			}
			return toReturn;
		} catch (IllegalValueException e) {
			throw new IllegalActionException("You can not sit in to this table with the given buy-in of " + buyIn
					+ "chips.");
		}
	}
	
	public synchronized void sitOut(MutableSeatedPlayer player) {
		tableState.sitOut(player);
		unsubscribeHoldemPlayerListener(player.getId(), sitInPlayers.get(player.getId()));
		sitInPlayers.remove(player.getId());
	}
	
	public boolean isPlaying() {
		return tableState.isPlaying();
	}
	
	/***************************************************************************
	 * Holdem Table Events
	 **************************************************************************/
	
	/**
	 * This list contains all holdem table listeners that should be alerted on a
	 * new event.
	 */
	private List<HoldemTableListener> holdemTableListeners = new CopyOnWriteArrayList<HoldemTableListener>();
	
	/**
	 * Subscribe the given holdem table listener for holdem table events.
	 * 
	 * @param listener The listener to subscribe.
	 */
	public void subscribeHoldemTableListener(HoldemTableListener listener) {
		holdemTableListeners.add(listener);
	}
	
	/**
	 * Unsubscribe the given holdem table listener for holdem table events.
	 * 
	 * @param listener The listener to unsubscribe.
	 */
	public void unsubscribeHoldemTableListener(HoldemTableListener listener) {
		holdemTableListeners.remove(listener);
	}
	
	/**
	 * Inform all subscribed holdem table listeners a fold event has occurred.
	 * 
	 * Each subscribed holdem table listener is updated by calling their
	 * onFold() method.
	 * 
	 */
	public synchronized void publishFoldEvent(FoldEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onFold(event);
		}
	}
	
	/**
	 * Inform all subscribed holdem table listeners a raise event has occurred.
	 * 
	 * Each subscribed holdem table listener is updated by calling their
	 * onRaise() method.
	 * 
	 */
	public synchronized void publishRaiseEvent(RaiseEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onRaise(event);
		}
	}
	
	/**
	 * Inform all subscribed holdem table listeners a check event has occurred.
	 * 
	 * Each subscribed holdem table listener is updated by calling their
	 * onCheck() method.
	 * 
	 */
	public synchronized void publishCheckEvent(CheckEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onCheck(event);
		}
	}
	
	/**
	 * Inform all subscribed holdem table listeners a call event has occurred.
	 * 
	 * Each subscribed holdem table listener is updated by calling their
	 * onCall() method.
	 * 
	 */
	public synchronized void publishCallEvent(CallEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onCall(event);
		}
	}
	
	/**
	 * Inform all subscribed holdem table listeners a bet event has occurred.
	 * 
	 * Each subscribed holdem table listener is updated by calling their onBet()
	 * method.
	 * 
	 */
	public synchronized void publishBetEvent(BetEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onBet(event);
		}
	}
	
	/**
	 * Inform all subscribed winner listeners a winner event has occurred.
	 * 
	 * Each subscribed winner listener is updated by calling their
	 * onWinnerEvent() method.
	 * 
	 */
	public void publishAllInEvent(AllInEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onAllIn(event);
		}
	}
	
	/**
	 * Inform all subscribed small blind listeners a small blind event has
	 * occurred.
	 * 
	 * Each subscribed small blind listener is updated by calling their
	 * onSmallBlind() method.
	 * 
	 */
	public synchronized void publishSmallBlindEvent(SmallBlindEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onSmallBlind(event);
		}
	}
	
	/**
	 * Inform all subscribed big blind listeners a big blind event has occurred.
	 * 
	 * Each subscribed big blind listener is updated by calling their
	 * onBigBlind() method.
	 * 
	 */
	public synchronized void publishBigBlindEvent(BigBlindEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onBigBlind(event);
		}
	}
	
	/**
	 * Inform all subscribed new round listeners a new round event has occurred.
	 * 
	 * Each subscribed new round listener is updated by calling their
	 * onNewRoundEvent() method.
	 * 
	 */
	public synchronized void publishNewRoundEvent(NewRoundEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onNewRound(event);
		}
	}
	
	/**
	 * Inform all subscribed new common cards listeners a new common cards event
	 * has occurred.
	 * 
	 * Each subscribed new common cards listener is updated by calling their
	 * onNewCommonCardsEvent() method.
	 * 
	 */
	public synchronized void publishNewCommonCardsEvent(NewCommunityCardsEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onNewCommunityCards(event);
		}
	}
	
	/**
	 * Inform all subscribed new deal listeners a new deal event has occurred.
	 * 
	 * Each subscribed new deal listener is updated by calling their
	 * onNewDealEvent() method.
	 * 
	 */
	public synchronized void publishNewDealEvent(NewDealEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onNewDeal(event);
		}
	}
	
	/**
	 * Inform all subscribed next player listeners a next player event has
	 * occurred.
	 * 
	 * Each subscribed next player listener is updated by calling their
	 * onNextPlayerEvent() method.
	 * 
	 */
	public void publishNextPlayerEvent(NextPlayerEvent event) {
		cancelOldTimeOut();
		submitTimeOutHandler(event.getPlayer());
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onNextPlayer(event);
		}
	}
	
	/**
	 * Inform all subscribed winner listeners a winner event has occurred.
	 * 
	 * Each subscribed winner listener is updated by calling their
	 * onWinnerEvent() method.
	 * 
	 */
	public void publishWinnerEvent(WinnerEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onWinner(event);
		}
	}
	
	/**
	 * Inform all subscribed show hand listeners a show hand event has occurred.
	 * 
	 * Each subscribed show hand listener is updated by calling their
	 * onShowHandEvent() method.
	 * 
	 */
	public void publishShowHandEvent(ShowHandEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onShowHand(event);
		}
	}
	
	/**
	 * Inform all subscribed player joined game listeners a player joined game
	 * event has occurred.
	 * 
	 * Each subscribed player joined game listener is updated by calling their
	 * onJoinTable() method.
	 * 
	 */
	public void publishJoinTableEvent(JoinTableEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onJoinTable(event);
		}
	}
	
	/**
	 * Inform all subscribed player left table listeners a player left table
	 * event has occurred.
	 * 
	 * Each subscribed player left table listener is updated by calling their
	 * onLeaveTable() method.
	 * 
	 */
	public void publishLeaveTableEvent(LeaveTableEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onLeaveTable(event);
		}
	}
	
	public void publishSitInEvent(SitInEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onSitIn(event);
		}
	}
	
	public void publishSitOutEvent(SitOutEvent event) {
		for (HoldemTableListener listener : holdemTableListeners) {
			listener.onSitOut(event);
		}
	}
	
	/***************************************************************************
	 * Holdem Player Events
	 **************************************************************************/
	
	/**
	 * Inform all subscribed new private cards listeners a new private cards
	 * event event has occurred.
	 * 
	 * Each subscribed new private cards listener is updated by calling their
	 * onNewPrivateCards() method.
	 * 
	 */
	public synchronized void publishNewPocketCardsEvent(PlayerId id, NewPocketCardsEvent event) {
		List<HoldemPlayerListener> listeners = holdemPlayerListeners.get(id);
		if (listeners != null) {
			for (HoldemPlayerListener listener : listeners) {
				listener.onNewPocketCards(event);
			}
		}
	}
	
	/**
	 * Subscribe the given new private cards listener for new private cards
	 * events.
	 * 
	 * @param id The id of the player to get the new private cards events from.
	 * @param listener The listener to subscribe.
	 * 
	 * @note This method is both non-blocking and thread-safe.
	 */
	public void subscribeHoldemPlayerListener(PlayerId id, HoldemPlayerListener listener) {
		
		List<HoldemPlayerListener> currentListeners;
		List<HoldemPlayerListener> newListeners;
		
		boolean notAdded = false;
		
		do {
			currentListeners = holdemPlayerListeners.get(id);
			if (currentListeners == null) {
				newListeners = new ArrayList<HoldemPlayerListener>();
			} else {
				newListeners = new ArrayList<HoldemPlayerListener>(currentListeners);
			}
			newListeners.add(listener);
			if (currentListeners == null) {
				notAdded = (holdemPlayerListeners.putIfAbsent(id, Collections.unmodifiableList(newListeners)) != null);
			} else {
				notAdded = !holdemPlayerListeners.replace(id, currentListeners, Collections
						.unmodifiableList(newListeners));
			}
		} while (notAdded);
	}
	
	/**
	 * Unsubscribe the given new private cards listener for new private cards
	 * events.
	 * 
	 * @param listener The listener to unsubscribe.
	 */
	public void unsubscribeHoldemPlayerListener(PlayerId id, HoldemPlayerListener listener) {
		List<HoldemPlayerListener> currentListeners;
		List<HoldemPlayerListener> newListeners;
		
		boolean removed;
		
		do {
			currentListeners = holdemPlayerListeners.get(id);
			if (currentListeners == null) {
				return;
			}
			newListeners = new ArrayList<HoldemPlayerListener>(currentListeners);
			newListeners.remove(listener);
			if (newListeners.size() == 0) {
				removed = holdemPlayerListeners.remove(id, currentListeners);
			} else {
				removed = holdemPlayerListeners.replace(id, currentListeners, Collections
						.unmodifiableList(newListeners));
			}
		} while (!removed);
	}
	
	/**
	 * This list contains all new private cards listeners that should be alerted
	 * on a new private cards.
	 */
	private final ConcurrentMap<PlayerId, List<HoldemPlayerListener>> holdemPlayerListeners = new ConcurrentHashMap<PlayerId, List<HoldemPlayerListener>>();
	
	private synchronized void submitTimeOutHandler(Player player) {
		currentTimeOut = new PlayerActionTimeOut(player);
		oldFuture = currentFuture;
		cancelOldTimeOut();
		currentFuture = ScheduledRequestExecutor.getInstance().schedule(currentTimeOut, 30, TimeUnit.SECONDS);
		PokerTable.logger.info(player.getName() + " action time out submitted.");
	}
	
	private PlayerActionTimeOut currentTimeOut;
	
	private ScheduledFuture<?> currentFuture;
	
	private ScheduledFuture<?> oldFuture;
	
	private synchronized void cancelOldTimeOut() {
		if (oldFuture != null) {
			oldFuture.cancel(false);
		}
	}
	
	private synchronized PlayerActionTimeOut getCurrentTimeOut() {
		return currentTimeOut;
	}
	
	private class PlayerActionTimeOut
			implements Runnable {
		
		private Player player;
		
		public PlayerActionTimeOut(Player player) {
			this.player = player;
		}
		
		public void run() {
			try {
				PokerTable.logger.info(player.getName() + " auto-fold called.");
				
				if (getCurrentTimeOut() == this && tableState.getGame() != null) {
					MutableSeatedPlayer gcPlayer = tableState.getGame().getCurrentPlayer();
					if ((gcPlayer.getId().equals(new PlayerId(player.getId())))) {
						PokerTable.logger.info(player.getName() + " automatically folded.");
						tableState.fold(gcPlayer);
					}
				}
			} catch (IllegalActionException e) {}
		}
	}
}
