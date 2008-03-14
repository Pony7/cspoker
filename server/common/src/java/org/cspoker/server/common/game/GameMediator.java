/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.server.common.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.eventlisteners.EventListener;
import org.cspoker.common.eventlisteners.game.AllGameEventsListener;
import org.cspoker.common.eventlisteners.game.BrokePlayerKickedOutListener;
import org.cspoker.common.eventlisteners.game.GameEventListener;
import org.cspoker.common.eventlisteners.game.GameMessageListener;
import org.cspoker.common.eventlisteners.game.NewCommunityCardsListener;
import org.cspoker.common.eventlisteners.game.NewDealListener;
import org.cspoker.common.eventlisteners.game.NewRoundListener;
import org.cspoker.common.eventlisteners.game.NextPlayerListener;
import org.cspoker.common.eventlisteners.game.PlayerJoinedTableListener;
import org.cspoker.common.eventlisteners.game.PlayerLeftTableListener;
import org.cspoker.common.eventlisteners.game.ShowHandListener;
import org.cspoker.common.eventlisteners.game.WinnerListener;
import org.cspoker.common.eventlisteners.game.actions.AllInListener;
import org.cspoker.common.eventlisteners.game.actions.BetListener;
import org.cspoker.common.eventlisteners.game.actions.BigBlindListener;
import org.cspoker.common.eventlisteners.game.actions.CallListener;
import org.cspoker.common.eventlisteners.game.actions.CheckListener;
import org.cspoker.common.eventlisteners.game.actions.FoldListener;
import org.cspoker.common.eventlisteners.game.actions.RaiseListener;
import org.cspoker.common.eventlisteners.game.actions.SmallBlindListener;
import org.cspoker.common.eventlisteners.game.privatelistener.NewPocketCardsListener;
import org.cspoker.common.events.Event;
import org.cspoker.common.events.gameevents.BrokePlayerKickedOutEvent;
import org.cspoker.common.events.gameevents.GameEvent;
import org.cspoker.common.events.gameevents.GameMessageEvent;
import org.cspoker.common.events.gameevents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameevents.NewDealEvent;
import org.cspoker.common.events.gameevents.NewRoundEvent;
import org.cspoker.common.events.gameevents.NextPlayerEvent;
import org.cspoker.common.events.gameevents.PlayerJoinedTableEvent;
import org.cspoker.common.events.gameevents.PlayerLeftTableEvent;
import org.cspoker.common.events.gameevents.ShowHandEvent;
import org.cspoker.common.events.gameevents.WinnerEvent;
import org.cspoker.common.events.gameevents.playeractionevents.AllInEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BetEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BigBlindEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CallEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CheckEvent;
import org.cspoker.common.events.gameevents.playeractionevents.FoldEvent;
import org.cspoker.common.events.gameevents.playeractionevents.RaiseEvent;
import org.cspoker.common.events.gameevents.playeractionevents.SmallBlindEvent;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.player.Player;
import org.cspoker.common.player.PlayerId;
import org.cspoker.server.common.game.gamecontrol.GameControl;
import org.cspoker.server.common.game.player.GamePlayer;
import org.cspoker.server.common.util.threading.ScheduledRequestExecutor;

/**
 * A class of game mediators to decouple the game control from all users:
 * server, gui, logger, ...
 * 
 * @author Kenzo
 * 
 */
public class GameMediator {
	
	private static Logger logger = Logger
	.getLogger(GameMediator.class);

	/**
	 * This variable contains the game control to mediate to.
	 */
	private GameControl gameControl;
	
	private final TableId id;

	/**
	 * Construct a new game mediator.
	 */
	public GameMediator(TableId id) {
		this.id = id;
	}
	
	public TableId getId(){
		return id;
	}

	/***************************************************************************
	 * Player Actions
	 **************************************************************************/

	/**
	 * The given player goes all-in.
	 * 
	 * @param player
	 *            The player who goes all-in.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void allIn(GamePlayer player) throws IllegalActionException {
		gameControl.allIn(player);
		cancelOldTimeOut();
	}

	/**
	 * The player puts money in the pot.
	 * 
	 * @param player
	 *            The player who puts a bet.
	 * @param amount
	 *            The amount of the bet.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void bet(GamePlayer player, int amount)
			throws IllegalActionException {
		gameControl.bet(player, amount);
		cancelOldTimeOut();
	}

	/**
	 * To put into the pot an amount of money equal to the most recent bet or
	 * raise.
	 * 
	 * @param player
	 *            The player who calls.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void call(GamePlayer player) throws IllegalActionException {
		gameControl.call(player);
		cancelOldTimeOut();
	}

	/**
	 * If there is no bet on the table and you do not wish to place a bet. You
	 * may only check when there are no prior bets.
	 * 
	 * @param player
	 *            The player who checks.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void check(GamePlayer player) throws IllegalActionException {
		gameControl.check(player);
		cancelOldTimeOut();
	}

	/**
	 * The given player folds the cards.
	 * 
	 * The player will not be able to take any actions in the coming rounds of
	 * the current deal.
	 * 
	 * @param player
	 *            The player who folds.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void fold(GamePlayer player) throws IllegalActionException {
		gameControl.fold(player);
		cancelOldTimeOut();
	}

	/**
	 * Raise the bet with given amount.
	 * 
	 * @param player
	 *            The player who raises the current bet.
	 * @param amount
	 *            The amount with which to raise the bet.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void raise(GamePlayer player, int amount)
			throws IllegalActionException {
		gameControl.raise(player, amount);
		cancelOldTimeOut();
	}

	public void joinGame(SeatId seatId, GamePlayer player) throws IllegalActionException {
			gameControl.joinGame(seatId, player);
	}

	public void leaveGame(GamePlayer player) throws IllegalActionException {
		gameControl.leaveGame(player);
	}

	/***************************************************************************
	 * set Game Control
	 **************************************************************************/

	/**
	 * Set the game control of this game mediator to the given game control.
	 * 
	 */
	public void setGameControl(GameControl gameControl) {
		this.gameControl = gameControl;
	}

	/***************************************************************************
	 * Player Action Events
	 **************************************************************************/

	/**
	 * Inform all subscribed fold listeners a fold event has occurred.
	 * 
	 * Each subscribed fold listener is updated by calling their onFoldEvent()
	 * method.
	 * 
	 */
	public synchronized void publishFoldEvent(FoldEvent event) {
		for (FoldListener listener : foldListeners) {
			listener.onFoldEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given fold listener for fold events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeFoldListener(FoldListener listener) {
		foldListeners.add(listener);
	}

	/**
	 * Unsubscribe the given fold listener for fold events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeFoldListener(FoldListener listener) {
		foldListeners.remove(listener);
	}

	/**
	 * This list contains all fold listeners that should be alerted on a fold.
	 */
	private final List<FoldListener> foldListeners = new CopyOnWriteArrayList<FoldListener>();

	/**
	 * Inform all subscribed raise listeners a raise event has occurred.
	 * 
	 * Each subscribed raise listener is updated by calling their onRaiseEvent()
	 * method.
	 * 
	 */
	public synchronized void publishRaiseEvent(RaiseEvent event) {
		for (RaiseListener listener : raiseListeners) {
			listener.onRaiseEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given raise listener for raise events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeRaiseListener(RaiseListener listener) {
		raiseListeners.add(listener);
	}

	/**
	 * Unsubscribe the given raise listener for raise events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeRaiseListener(RaiseListener listener) {
		raiseListeners.remove(listener);
	}

	/**
	 * This list contains all raise listeners that should be alerted on a raise.
	 */
	private final List<RaiseListener> raiseListeners = new CopyOnWriteArrayList<RaiseListener>();

	/**
	 * Inform all subscribed check listeners a check event has occurred.
	 * 
	 * Each subscribed check listener is updated by calling their onCheckEvent()
	 * method.
	 * 
	 */
	public synchronized void publishCheckEvent(CheckEvent event) {
		for (CheckListener listener : checkListeners) {
			listener.onCheckEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given check listener for check events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeCheckListener(CheckListener listener) {
		checkListeners.add(listener);
	}

	/**
	 * Unsubscribe the given check listener for check events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeCheckListener(CheckListener listener) {
		checkListeners.remove(listener);
	}

	/**
	 * This list contains all check listeners that should be alerted on a check.
	 */
	private final List<CheckListener> checkListeners = new CopyOnWriteArrayList<CheckListener>();

	/**
	 * Inform all subscribed call listeners a call event has occurred.
	 * 
	 * Each subscribed call listener is updated by calling their onCallEvent()
	 * method.
	 * 
	 */
	public synchronized void publishCallEvent(CallEvent event) {
		for (CallListener listener : callListeners) {
			listener.onCallEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given call listener for call events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeCallListener(CallListener listener) {
		callListeners.add(listener);
	}

	/**
	 * Unsubscribe the given call listener for call events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeCallListener(CallListener listener) {
		callListeners.remove(listener);
	}

	/**
	 * This list contains all call listeners that should be alerted on a call.
	 */
	private final List<CallListener> callListeners = new CopyOnWriteArrayList<CallListener>();

	/**
	 * Inform all subscribed bet listeners a bet event has occurred.
	 * 
	 * Each subscribed bet listener is updated by calling their onBetEvent()
	 * method.
	 * 
	 */
	public synchronized void publishBetEvent(BetEvent event) {
		for (BetListener listener : betListeners) {
			listener.onBetEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given bet listener for bet events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeBetListener(BetListener listener) {
		betListeners.add(listener);
	}

	/**
	 * Unsubscribe the given bet listener for bet events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeBetListener(BetListener listener) {
		betListeners.remove(listener);
	}

	/**
	 * This list contains all bet listeners that should be alerted on a bet.
	 */
	private final List<BetListener> betListeners = new CopyOnWriteArrayList<BetListener>();

	/**
	 * Inform all subscribed all-in listeners a all-in event has occurred.
	 * 
	 * Each subscribed all-in listener is updated by calling their
	 * onAllInEvent() method.
	 * 
	 */
	public synchronized void publishAllInEvent(AllInEvent event) {
		for (AllInListener listener : allInListeners) {
			listener.onAllInEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given all-in listener for all-in events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeAllInListener(AllInListener listener) {
		allInListeners.add(listener);
	}

	/**
	 * Unsubscribe the given all-in listener for all-in events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeAllInListener(AllInListener listener) {
		allInListeners.remove(listener);
	}

	/**
	 * This list contains all all-in listeners that should be alerted on a
	 * all-in.
	 */
	private final List<AllInListener> allInListeners = new CopyOnWriteArrayList<AllInListener>();

	/**
	 * Inform all subscribed small blind listeners a small blind event has
	 * occurred.
	 * 
	 * Each subscribed small blind listener is updated by calling their
	 * onSmallBlindEvent() method.
	 * 
	 */
	public synchronized void publishSmallBlindEvent(SmallBlindEvent event) {
		for (SmallBlindListener listener : smallBlindListeners) {
			listener.onSmallBlindEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given small blind listener for small blind events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeSmallBlindListener(SmallBlindListener listener) {
		smallBlindListeners.add(listener);
	}

	/**
	 * Unsubscribe the given small blind listener for small blind events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeSmallBlindListener(SmallBlindListener listener) {
		smallBlindListeners.remove(listener);
	}

	/**
	 * This list contains all small blind listeners that should be alerted on a
	 * small blind event.
	 */
	private final List<SmallBlindListener> smallBlindListeners = new CopyOnWriteArrayList<SmallBlindListener>();

	/**
	 * Inform all subscribed big blind listeners a big blind event has occurred.
	 * 
	 * Each subscribed big blind listener is updated by calling their
	 * onBigBlindEvent() method.
	 * 
	 */
	public synchronized void publishBigBlindEvent(BigBlindEvent event) {
		for (BigBlindListener listener : bigBlindListeners) {
			listener.onBigBlindEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given big blind listener for big blind events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeBigBlindListener(BigBlindListener listener) {
		bigBlindListeners.add(listener);
	}

	/**
	 * Unsubscribe the given big blind listener for big blind events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeBigBlindListener(BigBlindListener listener) {
		bigBlindListeners.remove(listener);
	}

	/**
	 * This list contains all big blind listeners that should be alerted on a
	 * big blind.
	 */
	private final List<BigBlindListener> bigBlindListeners = new CopyOnWriteArrayList<BigBlindListener>();

	/**
	 * Inform all subscribed new round listeners a new round event has occurred.
	 * 
	 * Each subscribed new round listener is updated by calling their
	 * onNewRoundEvent() method.
	 * 
	 */
	public synchronized void publishNewRoundEvent(NewRoundEvent event) {
		if(event.getPlayer()!=null)
			submitTimeOutHandler(event.getPlayer());
		for (NewRoundListener listener : newRoundListeners) {
			listener.onNewRoundEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given new round listener for new round events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeNewRoundListener(NewRoundListener listener) {
		newRoundListeners.add(listener);
	}

	/**
	 * Unsubscribe the given new round listener for new round events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeNewRoundListener(NewRoundListener listener) {
		newRoundListeners.remove(listener);
	}

	/**
	 * This list contains all new round listeners that should be alerted on a
	 * new round.
	 */
	private final List<NewRoundListener> newRoundListeners = new CopyOnWriteArrayList<NewRoundListener>();

	/**
	 * Inform all subscribed new common cards listeners a new common cards event
	 * has occurred.
	 * 
	 * Each subscribed new common cards listener is updated by calling their
	 * onNewCommonCardsEvent() method.
	 * 
	 */
	public synchronized void publishNewCommonCardsEvent(
			NewCommunityCardsEvent event) {
		for (NewCommunityCardsListener listener : newCommunityCardsListeners) {
			listener.onNewCommunityCardsEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given new common cards listener for new common cards
	 * events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeNewCommonCardsListener(
			NewCommunityCardsListener listener) {
		newCommunityCardsListeners.add(listener);
	}

	/**
	 * Unsubscribe the given new common cards listener for new common cards
	 * events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeNewCommonCardsListener(
			NewCommunityCardsListener listener) {
		newCommunityCardsListeners.remove(listener);
	}

	/**
	 * This list contains all new common cards listeners that should be alerted
	 * on new common cards.
	 */
	private final List<NewCommunityCardsListener> newCommunityCardsListeners = new CopyOnWriteArrayList<NewCommunityCardsListener>();

	/**
	 * Inform all subscribed new deal listeners a new deal event has occurred.
	 * 
	 * Each subscribed new deal listener is updated by calling their
	 * onNewDealEvent() method.
	 * 
	 */
	public synchronized void publishNewDealEvent(NewDealEvent event) {
		for (NewDealListener listener : newDealListeners) {
			listener.onNewDealEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given new deal listener for new deal events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeNewDealListener(NewDealListener listener) {
		newDealListeners.add(listener);
	}

	/**
	 * Unsubscribe the given new deal listener for new deal events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeNewDealListener(NewDealListener listener) {
		newDealListeners.remove(listener);
	}

	/**
	 * This list contains all new deal listeners that should be alerted on a new
	 * deal.
	 */
	private final List<NewDealListener> newDealListeners = new CopyOnWriteArrayList<NewDealListener>();

	/**
	 * Inform all subscribed next player listeners a next player event has
	 * occurred.
	 * 
	 * Each subscribed next player listener is updated by calling their
	 * onNextPlayerEvent() method.
	 * 
	 */
	public synchronized void publishNextPlayerEvent(NextPlayerEvent event) {
		submitTimeOutHandler(event.getPlayer());
		for (NextPlayerListener listener : nextPlayerListeners) {
			listener.onNextPlayerEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given next player listener for next player events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeNextPlayerListener(NextPlayerListener listener) {
		nextPlayerListeners.add(listener);
	}

	/**
	 * Unsubscribe the given next player listener for next player events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeNextPlayerListener(NextPlayerListener listener) {
		nextPlayerListeners.remove(listener);
	}

	/**
	 * This list contains all next player listeners that should be alerted on a
	 * next player.
	 */
	private final List<NextPlayerListener> nextPlayerListeners = new CopyOnWriteArrayList<NextPlayerListener>();

	/**
	 * Inform all subscribed winner listeners a winner event has occurred.
	 * 
	 * Each subscribed winner listener is updated by calling their
	 * onWinnerEvent() method.
	 * 
	 */
	public synchronized void publishWinner(WinnerEvent event) {
		for (WinnerListener listener : winnerListeners) {
			listener.onWinnerEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given winner listener for winner events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeWinnerListener(WinnerListener listener) {
		winnerListeners.add(listener);
	}

	/**
	 * Unsubscribe the given winner listener for winner events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeWinnerListener(WinnerListener listener) {
		winnerListeners.remove(listener);
	}

	/**
	 * This list contains all winner listeners that should be alerted on a
	 * winner.
	 */
	private final List<WinnerListener> winnerListeners = new CopyOnWriteArrayList<WinnerListener>();

	/**
	 * Inform all subscribed show hand listeners a show hand event has occurred.
	 * 
	 * Each subscribed show hand listener is updated by calling their
	 * onShowHandEvent() method.
	 * 
	 */
	public synchronized void publishShowHand(ShowHandEvent event) {
		for (ShowHandListener listener : showHandListeners) {
			listener.onShowHandEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given show hand listener for show hand events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeShowHandListener(ShowHandListener listener) {
		showHandListeners.add(listener);
	}

	/**
	 * Unsubscribe the given show hand listener for show hand events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeShowHandListener(ShowHandListener listener) {
		showHandListeners.remove(listener);
	}

	/**
	 * This list contains all show hand listeners that should be alerted on a
	 * show hand.
	 */
	private final List<ShowHandListener> showHandListeners = new CopyOnWriteArrayList<ShowHandListener>();

	/**
	 * Inform all subscribed player joined game listeners a player joined game
	 * event has occurred.
	 * 
	 * Each subscribed player joined game listener is updated by calling their
	 * onPlayerJoinedGameEvent() method.
	 * 
	 */
	public synchronized void publishPlayerJoinedTable(PlayerJoinedTableEvent event) {
		for (PlayerJoinedTableListener listener : playerJoinedGameListeners) {
			listener.onPlayerJoinedTableEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given player joined game listener for player joined game
	 * events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribePlayerJoinedGameListener(
			PlayerJoinedTableListener listener) {
		playerJoinedGameListeners.add(listener);
	}

	/**
	 * Unsubscribe the given player joined game listener for player joined game
	 * events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribePlayerJoinedGameListener(
			PlayerJoinedTableListener listener) {
		playerJoinedGameListeners.remove(listener);
	}

	/**
	 * This list contains all player joined game listeners that should be
	 * alerted on a player joined game.
	 */
	private final List<PlayerJoinedTableListener> playerJoinedGameListeners = new CopyOnWriteArrayList<PlayerJoinedTableListener>();

	/**
	 * Inform all subscribed player left table listeners a player left table
	 * event has occurred.
	 * 
	 * Each subscribed player left table listener is updated by calling their
	 * onPlayerLeftTableEvent() method.
	 * 
	 */
	public synchronized void publishPlayerLeftTable(PlayerLeftTableEvent event) {
		for (PlayerLeftTableListener listener : playerLeftTableListeners) {
			listener.onPlayerLeftTableEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given player left table listener for player left table
	 * events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribePlayerLeftTableListener(
			PlayerLeftTableListener listener) {
		playerLeftTableListeners.add(listener);
	}

	/**
	 * Unsubscribe the given player left table listener for player left table
	 * events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribePlayerLeftTableListener(
			PlayerLeftTableListener listener) {
		playerLeftTableListeners.remove(listener);
	}

	/**
	 * This list contains all player left table listeners that should be alerted
	 * on a player left table.
	 */
	private final List<PlayerLeftTableListener> playerLeftTableListeners = new CopyOnWriteArrayList<PlayerLeftTableListener>();

	/**
	 * Inform all subscribed message listeners a message event has occurred.
	 * 
	 * Each subscribed message listener is updated by calling their
	 * onMessageEvent() method.
	 * 
	 */
	public synchronized void publishGameMessageEvent(GameMessageEvent event) {
		for (GameMessageListener listener : gameMessageListeners) {
			listener.onGameMessageEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given message listener for message events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeGameMessageListener(GameMessageListener listener) {
		gameMessageListeners.add(listener);
	}

	/**
	 * Unsubscribe the given message listener for message events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeGameMessageListener(GameMessageListener listener) {
		gameMessageListeners.remove(listener);
	}

	/**
	 * This list contains all message listeners that should be alerted on a
	 * message.
	 */
	private final List<GameMessageListener> gameMessageListeners = new CopyOnWriteArrayList<GameMessageListener>();
	
	/**
	 * Inform all subscribed broke player kicked out listeners a broke player kicked out event has occurred.
	 * 
	 * Each subscribed broke player kicked out listener is updated by calling their
	 * onBrokePlayerKickedOut() method.
	 * 
	 */
	public synchronized void publishBrokePlayerKickedOutEvent(BrokePlayerKickedOutEvent event) {
		for (BrokePlayerKickedOutListener listener : brokePlayerKickedOutListeners) {
			listener.onBrokePlayerKickedOutEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given broke player kicked out listener for broke player kicked out events.
	 * 
	 * @param 	listener
	 *      	The listener to subscribe.
	 */
	public void subscribeBrokePlayerKickedOutListener(BrokePlayerKickedOutListener listener) {
		brokePlayerKickedOutListeners.add(listener);
	}

	/**
	 * Unsubscribe the given broke player kicked out listener for broke player kicked out events.
	 * 
	 * @param 	listener
	 *  		The listener to unsubscribe.
	 */
	public void unsubscribeBrokePlayerKickedOutListener(BrokePlayerKickedOutListener listener) {
		brokePlayerKickedOutListeners.remove(listener);
	}

	/**
	 * This list contains all broke player kicked out listeners that should be alerted on a
	 * broke player kicked out.
	 */
	private final List<BrokePlayerKickedOutListener> brokePlayerKickedOutListeners = new CopyOnWriteArrayList<BrokePlayerKickedOutListener>();

	/***************************************************************************
	 * Personal Events
	 **************************************************************************/

	/**
	 * Inform all subscribed new private cards listeners a new private cards
	 * event event has occurred.
	 * 
	 * Each subscribed new private cards listener is updated by calling their
	 * onNewPrivateCards() method.
	 * 
	 */
	public synchronized void publishNewPocketCardsEvent(PlayerId id,
			NewPocketCardsEvent event) {
		List<NewPocketCardsListener> listeners = newPocketCardsListeners
				.get(id);
		if (listeners != null) {
			for (NewPocketCardsListener listener : listeners) {
				listener.onNewPocketCardsEvent(event);
			}
		}
		publishPersonalGameEvent(id, event);
	}

	/**
	 * Subscribe the given new private cards listener for new private cards
	 * events.
	 * 
	 * @param id
	 *            The id of the player to get the new private cards events from.
	 * @param listener
	 *            The listener to subscribe.
	 * 
	 * @note This method is both non-blocking and thread-safe.
	 */
	public void subscribeNewPocketCardsListener(PlayerId id,
			NewPocketCardsListener listener) {
		List<NewPocketCardsListener> currentListeners;
		List<NewPocketCardsListener> newListeners;

		boolean notAdded;

		do {
			notAdded = false;
			currentListeners = newPocketCardsListeners.get(id);
			if (currentListeners == null) {
				newListeners = new ArrayList<NewPocketCardsListener>();
			} else {
				newListeners = new ArrayList<NewPocketCardsListener>(
						currentListeners);
			}
			newListeners.add(listener);
			if (currentListeners == null) {
				notAdded = (newPocketCardsListeners.putIfAbsent(id, Collections
						.unmodifiableList(newListeners)) != null);
			} else {
				notAdded = !newPocketCardsListeners.replace(id,
						currentListeners, Collections
								.unmodifiableList(newListeners));
			}
		} while (notAdded);
	}

	/**
	 * Unsubscribe the given new private cards listener for new private cards
	 * events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeNewPocketCardsListener(PlayerId id,
			NewPocketCardsListener listener) {
		List<NewPocketCardsListener> currentListeners;
		List<NewPocketCardsListener> newListeners;

		boolean removed;

		do {
			currentListeners = newPocketCardsListeners.get(id);
			if (currentListeners == null) {
				return;
			}
			newListeners = new ArrayList<NewPocketCardsListener>(
					currentListeners);
			newListeners.remove(listener);
			if (newListeners.size() == 0) {
				removed = newPocketCardsListeners.remove(id, currentListeners);
			} else {
				removed = newPocketCardsListeners.replace(id, currentListeners,
						Collections.unmodifiableList(newListeners));
			}
		} while (!removed);
	}

	/**
	 * This list contains all new private cards listeners that should be alerted
	 * on a new private cards.
	 */
	private final ConcurrentMap<PlayerId, List<NewPocketCardsListener>> newPocketCardsListeners = new ConcurrentHashMap<PlayerId, List<NewPocketCardsListener>>();

	/***************************************************************************
	 * All game events listener
	 **************************************************************************/

	/**
	 * Inform all subscribed game event listeners a game event has occurred.
	 * 
	 * Each subscribed game event listener is updated by calling their
	 * onGameEvent() method.
	 * 
	 */
	private synchronized void publishGameEvent(Event event) {
		for (EventListener listener : gameEventListeners) {
			listener.onEvent(event);
		}
	}

	/**
	 * Subscribe the given game event listener for game events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeGameEventListener(EventListener listener) {
		gameEventListeners.add(listener);
	}

	/**
	 * Unsubscribe the given game event listener for game events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeGameEventListener(EventListener listener) {
		gameEventListeners.remove(listener);
	}

	/**
	 * This list contains all game event listeners that should be alerted on a
	 * game event.
	 */
	private final List<EventListener> gameEventListeners = new CopyOnWriteArrayList<EventListener>();

	/***************************************************************************
	 * Personal game events listener
	 * 
	 * All personal events for one player can easily be collected.
	 **************************************************************************/

	/**
	 * Inform all subscribed personal event listeners a personal event event has
	 * occurred.
	 * 
	 * Each subscribed personal event listener is updated by calling their
	 * onGameEvent() method.
	 * 
	 */
	public synchronized void publishPersonalGameEvent(PlayerId id,
			GameEvent event) {
		List<EventListener> listeners = personalEventsListeners.get(id);
		if (listeners != null) {
			for (EventListener listener : listeners) {
				listener.onEvent(event);
			}
		}
		publishAllPersonalEvents(event);
	}

	/**
	 * Subscribe the given personal event listener for personal event events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribePersonalGameEventListener(PlayerId id,
			EventListener listener) {
		List<EventListener> currentListeners;
		List<EventListener> newListeners;

		boolean notAdded;

		do {
			notAdded = false;
			currentListeners = personalEventsListeners.get(id);
			if (currentListeners == null) {
				newListeners = new ArrayList<EventListener>();
			} else {
				newListeners = new ArrayList<EventListener>(currentListeners);
			}
			newListeners.add(listener);
			if (currentListeners == null) {
				notAdded = (personalEventsListeners.putIfAbsent(id, Collections
						.unmodifiableList(newListeners)) != null);
			} else {
				notAdded = !personalEventsListeners.replace(id,
						currentListeners, Collections
								.unmodifiableList(newListeners));
			}
		} while (notAdded);
	}

	/**
	 * Unsubscribe the given personal event listener for personal event events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribePersonalGameEventListener(PlayerId id,
			EventListener listener) {
		List<EventListener> currentListeners;
		List<EventListener> newListeners;

		boolean removed;

		do {
			currentListeners = personalEventsListeners.get(id);
			if (currentListeners == null) {
				return;
			}
			newListeners = new ArrayList<EventListener>(currentListeners);
			newListeners.remove(listener);
			if (newListeners.size() == 0) {
				removed = personalEventsListeners.remove(id, currentListeners);
			} else {
				removed = personalEventsListeners.replace(id, currentListeners,
						Collections.unmodifiableList(newListeners));
			}
		} while (!removed);
	}

	/**
	 * This hash map contains all personal event listeners that should be
	 * alerted on a personal event for a given id.
	 */
	private final ConcurrentMap<PlayerId, List<EventListener>> personalEventsListeners = new ConcurrentHashMap<PlayerId, List<EventListener>>();

	/***************************************************************************
	 * All personal game events listener
	 * 
	 * Game loggers can also obtain private events all other players can only
	 * receive personally.
	 **************************************************************************/

	/**
	 * Inform all subscribed personal listeners a personal event has occurred.
	 * 
	 * Each subscribed personal listener is updated by calling their
	 * onGameEvent() method.
	 * 
	 */
	public synchronized void publishAllPersonalEvents(GameEvent event) {
		for (GameEventListener listener : allPersonalEventsListeners) {
			listener.onGameEvent(event);
		}
	}

	/**
	 * Subscribe the given personal listener for personal events.
	 * 
	 * @param listener
	 *            The listener to subscribe.
	 */
	public void subscribeAllPersonalEventsListener(GameEventListener listener) {
		allPersonalEventsListeners.add(listener);
	}

	/**
	 * Unsubscribe the given personal listener for personal events.
	 * 
	 * @param listener
	 *            The listener to unsubscribe.
	 */
	public void unsubscribeAllPersonalEventsListener(GameEventListener listener) {
		allPersonalEventsListeners.remove(listener);
	}

	/**
	 * This list contains all personal listeners that should be alerted on a
	 * personal.
	 */
	private final List<GameEventListener> allPersonalEventsListeners = new CopyOnWriteArrayList<GameEventListener>();

	public void subscribeAllGameEventsListener(PlayerId id,
			AllGameEventsListener listener) {
		subscribeAllInListener(listener);
		subscribeBetListener(listener);
		subscribeBigBlindListener(listener);
		subscribeCallListener(listener);
		subscribeCheckListener(listener);
		subscribeFoldListener(listener);
		subscribeGameMessageListener(listener);
		subscribeNewCommonCardsListener(listener);
		subscribeNewDealListener(listener);
		subscribeNewPocketCardsListener(id, listener);
		subscribeNewRoundListener(listener);
		subscribeNextPlayerListener(listener);
		subscribePlayerJoinedGameListener(listener);
		subscribePlayerLeftTableListener(listener);
		subscribeRaiseListener(listener);
		subscribeShowHandListener(listener);
		subscribeSmallBlindListener(listener);
		subscribeWinnerListener(listener);
		subscribeBrokePlayerKickedOutListener(listener);
	}

	public void unsubscribeAllGameEventsListener(PlayerId id,
			AllGameEventsListener listener) {
		unsubscribeAllInListener(listener);
		unsubscribeBetListener(listener);
		unsubscribeBigBlindListener(listener);
		unsubscribeCallListener(listener);
		unsubscribeCheckListener(listener);
		unsubscribeFoldListener(listener);
		unsubscribeGameMessageListener(listener);
		unsubscribeNewCommonCardsListener(listener);
		unsubscribeNewDealListener(listener);
		unsubscribeNewPocketCardsListener(id, listener);
		unsubscribeNewRoundListener(listener);
		unsubscribeNextPlayerListener(listener);
		unsubscribePlayerJoinedGameListener(listener);
		unsubscribePlayerLeftTableListener(listener);
		unsubscribeRaiseListener(listener);
		unsubscribeShowHandListener(listener);
		unsubscribeSmallBlindListener(listener);
		unsubscribeWinnerListener(listener);
		unsubscribeBrokePlayerKickedOutListener(listener);
	}
	
	private void submitTimeOutHandler(Player player){
		currentTimeOut = new PlayerActionTimeOut(player);
		oldFuture = currentFuture;
		currentFuture = ScheduledRequestExecutor.getInstance().schedule(currentTimeOut, 30, TimeUnit.SECONDS);
		GameMediator.logger.info(player.getName()+" action time out submitted.");
	}
	
	private PlayerActionTimeOut currentTimeOut;
	
	private ScheduledFuture<Object> currentFuture;
	
	private ScheduledFuture<Object> oldFuture;
	
	private void cancelOldTimeOut(){
		if(oldFuture!=null)
			oldFuture.cancel(false);
	}
	
	
	private class PlayerActionTimeOut implements Callable<Object>{
				
		private Player player;
				
		public PlayerActionTimeOut(Player player){
			this.player = player;
		}

		@Override
		public Object call() {
			try {
				if(GameMediator.this.currentTimeOut==this){
					GamePlayer gcPlayer = gameControl.getGame().getCurrentPlayer();
					if(gcPlayer.getId().equals(player.getId())){
						GameMediator.logger.info(player.getName()+" automatically folded.");
						GameMediator.this.gameControl.fold(gcPlayer);
					}
				}
			} catch (IllegalActionException e) {
			}
			return null;
		}
	}
}
