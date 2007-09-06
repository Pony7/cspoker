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

package org.cspoker.server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.server.game.elements.table.PlayerListFullException;
import org.cspoker.server.game.events.Event;
import org.cspoker.server.game.events.EventListener;
import org.cspoker.server.game.events.MessageEvent;
import org.cspoker.server.game.events.MessageListener;
import org.cspoker.server.game.events.gameEvents.GameEvent;
import org.cspoker.server.game.events.gameEvents.GameEventListener;
import org.cspoker.server.game.events.gameEvents.NewCommunityCardsEvent;
import org.cspoker.server.game.events.gameEvents.NewCommunityCardsListener;
import org.cspoker.server.game.events.gameEvents.NewDealEvent;
import org.cspoker.server.game.events.gameEvents.NewDealListener;
import org.cspoker.server.game.events.gameEvents.NewRoundEvent;
import org.cspoker.server.game.events.gameEvents.NewRoundListener;
import org.cspoker.server.game.events.gameEvents.NextPlayerEvent;
import org.cspoker.server.game.events.gameEvents.NextPlayerListener;
import org.cspoker.server.game.events.gameEvents.PotChangedEvent;
import org.cspoker.server.game.events.gameEvents.PotChangedListener;
import org.cspoker.server.game.events.gameEvents.ShowHandEvent;
import org.cspoker.server.game.events.gameEvents.ShowHandListener;
import org.cspoker.server.game.events.gameEvents.StackChangedEvent;
import org.cspoker.server.game.events.gameEvents.StackChangedListener;
import org.cspoker.server.game.events.gameEvents.WinnerEvent;
import org.cspoker.server.game.events.gameEvents.WinnerListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.AllInEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.AllInListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BetEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BetListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BigBlindEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BigBlindListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.CallEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.CallListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.CheckEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.CheckListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.DealEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.DealListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.FoldEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.FoldListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.RaiseEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.RaiseListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.SmallBlindEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.SmallBlindListener;
import org.cspoker.server.game.events.gameEvents.privateEvents.NewPocketCardsEvent;
import org.cspoker.server.game.events.gameEvents.privateEvents.NewPrivateCardsListener;
import org.cspoker.server.game.gameControl.GameControl;
import org.cspoker.server.game.gameControl.IllegalActionException;
import org.cspoker.server.game.gameControl.PlayerAction;
import org.cspoker.server.game.player.Player;

/**
 * A class of game mediators to decouple
 * the game control from all users: server, gui, logger, ...
 *
 * @author Kenzo
 *
 */
public class GameMediator implements PlayerAction{

	/**
	 * This variable contains the game control to mediate to.
	 */
	private GameControl gameControl;

	/**
	 * Construct a new game mediator.
	 */
	public GameMediator(){

	}


	/**********************************************************
	 * Player Actions
	 **********************************************************/


	/**
	 * The given player goes all-in.
	 *
	 * @param 	player
	 * 			The player who goes all-in.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 */
	public void allIn(Player player) throws IllegalActionException {
		gameControl.allIn(player);
	}

	/**
	 * The player puts money in the pot.
	 *
	 * @param 	player
	 * 			The player who puts a bet.
	 * @param 	amount
	 * 			The amount of the bet.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 */
	public void bet(Player player, int amount) throws IllegalActionException {
		gameControl.bet(player, amount);
	}

	/**
	 * To put into the pot an amount of money equal to
	 * the most recent bet or raise.
	 *
	 * @param 	player
	 * 			The player who calls.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 */
	public void call(Player player) throws IllegalActionException {
		gameControl.call(player);
	}

	/**
	 * If there is no bet on the table and you do not wish to place a bet.
	 * You may only check when there are no prior bets.
	 *
	 * @param	player
	 * 			The player who checks.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 */
	public void check(Player player) throws IllegalActionException {
		gameControl.check(player);
	}

	/**
	 * The player who the dealer-button has been dealt to
	 * can choose to start the deal.
	 * From that moment, new players can not join the on-going deal.
	 *
	 * @param 	player
	 * 			The player who deals.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 */
	public void deal(Player player) throws IllegalActionException {
		gameControl.deal(player);
	}

	/**
	 * The given player folds the cards.
	 *
	 * The player will not be able to take any actions
	 * in the coming rounds of the current deal.
	 *
	 * @param 	player
	 * 			The player who folds.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 */
	public void fold(Player player) throws IllegalActionException {
		gameControl.fold(player);
	}

	/**
	 * Raise the bet with given amount.
	 *
	 * @param	player
	 * 			The player who raises the current bet.
	 * @param 	amount
	 * 			The amount with which to raise the bet.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 */
	public void raise(Player player, int amount) throws IllegalActionException {
		gameControl.raise(player, amount);
	}

	public void joinGame(Player player) throws IllegalActionException{
		try {
			gameControl.joinGame(player);
		} catch (PlayerListFullException e) {
			throw new IllegalActionException(e.getMessage());
		}
	}

	public void leaveGame(Player player) throws IllegalActionException{
		gameControl.leaveGame(player);
	}

	/**********************************************************
	 * set Game Control
	 **********************************************************/

	/**
	 * Set the game control of this game mediator
	 * to the given game control.
	 *
	 */
	public void setGameControl(GameControl gameControl){
		this.gameControl = gameControl;
	}



	/**********************************************************
	 * Player Action Events
	 **********************************************************/

	/**
	 * Inform all subscribed fold listeners a fold event has occurred.
	 *
	 * Each subscribed fold listener is updated
	 * by calling their onFoldEvent() method.
	 *
	 */
	public void publishFoldEvent(FoldEvent event){
		for(FoldListener listener:foldListeners){
			listener.onFoldEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given fold listener for fold events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeFoldListener(FoldListener listener) {
		foldListeners.add(listener);
	}

	/**
	 * Unsubscribe the given fold listener for fold events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeFoldListener(FoldListener listener) {
		foldListeners.remove(listener);
	}

	/**
	 * This list contains all fold listeners that
	 * should be alerted on a fold.
	 */
	private final List<FoldListener> foldListeners = new CopyOnWriteArrayList<FoldListener>();



	/**
	 * Inform all subscribed raise listeners a raise event has occurred.
	 *
	 * Each subscribed raise listener is updated
	 * by calling their onRaiseEvent() method.
	 *
	 */
	public void publishRaiseEvent(RaiseEvent event){
		for(RaiseListener listener:raiseListeners){
			listener.onRaiseEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given raise listener for raise events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeRaiseListener(RaiseListener listener) {
		raiseListeners.add(listener);
	}

	/**
	 * Unsubscribe the given raise listener for raise events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeRaiseListener(RaiseListener listener) {
		raiseListeners.remove(listener);
	}

	/**
	 * This list contains all raise listeners that
	 * should be alerted on a raise.
	 */
	private final List<RaiseListener> raiseListeners = new CopyOnWriteArrayList<RaiseListener>();



	/**
	 * Inform all subscribed check listeners a check event has occurred.
	 *
	 * Each subscribed check listener is updated
	 * by calling their onCheckEvent() method.
	 *
	 */
	public void publishCheckEvent(CheckEvent event){
		for(CheckListener listener:checkListeners){
			listener.onCheckEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given check listener for check events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeCheckListener(CheckListener listener) {
		checkListeners.add(listener);
	}

	/**
	 * Unsubscribe the given check listener for check events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeCheckListener(CheckListener listener) {
		checkListeners.remove(listener);
	}

	/**
	 * This list contains all check listeners that
	 * should be alerted on a check.
	 */
	private final List<CheckListener> checkListeners = new CopyOnWriteArrayList<CheckListener>();



	/**
	 * Inform all subscribed call listeners a call event has occurred.
	 *
	 * Each subscribed call listener is updated
	 * by calling their onCallEvent() method.
	 *
	 */
	public void publishCallEvent(CallEvent event){
		for(CallListener listener:callListeners){
			listener.onCallEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given call listener for call events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeCallListener(CallListener listener) {
		callListeners.add(listener);
	}

	/**
	 * Unsubscribe the given call listener for call events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeCallListener(CallListener listener) {
		callListeners.remove(listener);
	}

	/**
	 * This list contains all call listeners that
	 * should be alerted on a call.
	 */
	private final List<CallListener> callListeners = new CopyOnWriteArrayList<CallListener>();



	/**
	 * Inform all subscribed bet listeners a bet event has occurred.
	 *
	 * Each subscribed bet listener is updated
	 * by calling their onBetEvent() method.
	 *
	 */
	public void publishBetEvent(BetEvent event){
		for(BetListener listener:betListeners){
			listener.onBetEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given bet listener for bet events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeBetListener(BetListener listener) {
		betListeners.add(listener);
	}

	/**
	 * Unsubscribe the given bet listener for bet events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeBetListener(BetListener listener) {
		betListeners.remove(listener);
	}

	/**
	 * This list contains all bet listeners that
	 * should be alerted on a bet.
	 */
	private final List<BetListener> betListeners = new CopyOnWriteArrayList<BetListener>();



	/**
	 * Inform all subscribed all-in listeners a all-in event has occurred.
	 *
	 * Each subscribed all-in listener is updated
	 * by calling their onAllInEvent() method.
	 *
	 */
	public void publishAllInEvent(AllInEvent event){
		for(AllInListener listener:allInListeners){
			listener.onAllInEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given all-in listener for all-in events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeAllInListener(AllInListener listener) {
		allInListeners.add(listener);
	}

	/**
	 * Unsubscribe the given all-in listener for all-in events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeAllInListener(AllInListener listener) {
		allInListeners.remove(listener);
	}

	/**
	 * This list contains all all-in listeners that
	 * should be alerted on a all-in.
	 */
	private final List<AllInListener> allInListeners = new CopyOnWriteArrayList<AllInListener>();



	/**
	 * Inform all subscribed deal listeners a deal event has occurred.
	 *
	 * Each subscribed deal listener is updated
	 * by calling their onDealEvent() method.
	 *
	 */
	public void publishDealEvent(DealEvent event){
		for(DealListener listener:dealListeners){
			listener.onDealEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given deal listener for deal events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeDealListener(DealListener listener) {
		dealListeners.add(listener);
	}

	/**
	 * Unsubscribe the given deal listener for deal events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeDealListener(DealListener listener) {
		dealListeners.remove(listener);
	}

	/**
	 * This list contains all deal listeners that
	 * should be alerted on a deal.
	 */
	private final List<DealListener> dealListeners = new CopyOnWriteArrayList<DealListener>();

	/**
	 * Inform all subscribed small blind listeners a small blind event has occurred.
	 *
	 * Each subscribed small blind listener is updated
	 * by calling their onSmallBlindEvent() method.
	 *
	 */
	public void publishSmallBlindEvent(SmallBlindEvent event){
		for(SmallBlindListener listener: smallBlindListeners){
			listener.onSmallBlindEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given small blind listener for small blind events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeSmallBlindListener(SmallBlindListener listener) {
		smallBlindListeners.add(listener);
	}

	/**
	 * Unsubscribe the given small blind listener for small blind events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeSmallBlindListener(SmallBlindListener listener) {
		smallBlindListeners.remove(listener);
	}

	/**
	 * This list contains all small blind listeners that
	 * should be alerted on a small blind event.
	 */
	private final List<SmallBlindListener> smallBlindListeners = new CopyOnWriteArrayList<SmallBlindListener>();


	/**
	 * Inform all subscribed big blind listeners a big blind event has occurred.
	 *
	 * Each subscribed big blind listener is updated
	 * by calling their onBigBlindEvent() method.
	 *
	 */
	public void publishBigBlindEvent(BigBlindEvent event) {
		for (BigBlindListener listener : bigBlindListeners) {
			listener.onBigBlindEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given big blind listener for big blind events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeBigBlindListener(BigBlindListener listener) {
		bigBlindListeners.add(listener);
	}

	/**
	 * Unsubscribe the given big blind listener for big blind events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeBigBlindListener(BigBlindListener listener) {
		bigBlindListeners.remove(listener);
	}

	/**
	 * This list contains all big blind listeners that
	 * should be alerted on a big blind.
	 */
	private final List<BigBlindListener> bigBlindListeners = new CopyOnWriteArrayList<BigBlindListener>();

	/**
	 * Inform all subscribed new round listeners a new round event has occurred.
	 *
	 * Each subscribed new round listener is updated
	 * by calling their onNewRoundEvent() method.
	 *
	 */
	public void publishNewRoundEvent(NewRoundEvent event){
		for(NewRoundListener listener: newRoundListeners){
			listener.onNewRoundEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given new round listener for new round events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeNewRoundListener(NewRoundListener listener) {
		newRoundListeners.add(listener);
	}

	/**
	 * Unsubscribe the given new round listener for new round events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeNewRoundListener(NewRoundListener listener) {
		newRoundListeners.remove(listener);
	}

	/**
	 * This list contains all new round listeners that
	 * should be alerted on a new round.
	 */
	private final List<NewRoundListener> newRoundListeners = new CopyOnWriteArrayList<NewRoundListener>();



	/**
	 * Inform all subscribed new common cards listeners a new common cards event has occurred.
	 *
	 * Each subscribed new common cards listener is updated
	 * by calling their onNewCommonCardsEvent() method.
	 *
	 */
	public void publishNewCommonCardsEvent(NewCommunityCardsEvent event){
		for(NewCommunityCardsListener listener:newCommonCardsListeners){
			listener.onNewCommunityCardsEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given new common cards listener for new common cards events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeNewCommonCardsListener(NewCommunityCardsListener listener) {
		newCommonCardsListeners.add(listener);
	}

	/**
	 * Unsubscribe the given new common cards listener for new common cards events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeNewCommonCardsListener(NewCommunityCardsListener listener) {
		newCommonCardsListeners.remove(listener);
	}

	/**
	 * This list contains all new common cards listeners that
	 * should be alerted on new common cards.
	 */
	private final List<NewCommunityCardsListener> newCommonCardsListeners = new CopyOnWriteArrayList<NewCommunityCardsListener>();



	/**
	 * Inform all subscribed new deal listeners a new deal event has occurred.
	 *
	 * Each subscribed new deal listener is updated
	 * by calling their onNewDealEvent() method.
	 *
	 */
	public void publishNewDealEvent(NewDealEvent event){
		for(NewDealListener listener:newDealListeners){
			listener.onNewDealEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given new deal listener for new deal events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeNewDealListener(NewDealListener listener) {
		newDealListeners.add(listener);
	}

	/**
	 * Unsubscribe the given new deal listener for new deal events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeNewDealListener(NewDealListener listener) {
		newDealListeners.remove(listener);
	}

	/**
	 * This list contains all new deal listeners that
	 * should be alerted on a new deal.
	 */
	private final List<NewDealListener> newDealListeners = new CopyOnWriteArrayList<NewDealListener>();

	/**
	 * Inform all subscribed next player listeners a next player event has occurred.
	 *
	 * Each subscribed next player listener is updated
	 * by calling their onNextPlayerEvent() method.
	 *
	 */
	public void publishNextPlayerEvent(NextPlayerEvent event) {
		for (NextPlayerListener listener : nextPlayerListeners) {
			listener.onNextPlayerEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given next player listener for next player events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeNextPlayerListener(NextPlayerListener listener) {
		nextPlayerListeners.add(listener);
	}

	/**
	 * Unsubscribe the given next player listener for next player events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeNextPlayerListener(NextPlayerListener listener) {
		nextPlayerListeners.remove(listener);
	}

	/**
	 * This list contains all next player listeners that
	 * should be alerted on a next player.
	 */
	private final List<NextPlayerListener> nextPlayerListeners = new CopyOnWriteArrayList<NextPlayerListener>();


	/**
	 * Inform all subscribed winner listeners a winner event has occurred.
	 *
	 * Each subscribed winner listener is updated
	 * by calling their onWinnerEvent() method.
	 *
	 */
	public void publishWinner(WinnerEvent event) {
		for (WinnerListener listener : winnerListeners) {
			listener.onWinnerEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given winner listener for winner events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeWinnerListener(WinnerListener listener) {
		winnerListeners.add(listener);
	}

	/**
	 * Unsubscribe the given winner listener for winner events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeWinnerListener(WinnerListener listener) {
		winnerListeners.remove(listener);
	}

	/**
	 * This list contains all winner listeners that
	 * should be alerted on a winner.
	 */
	private final List<WinnerListener> winnerListeners = new CopyOnWriteArrayList<WinnerListener>();


	/**
	 * Inform all subscribed show hand listeners a show hand event has occurred.
	 *
	 * Each subscribed show hand listener is updated
	 * by calling their onShowHandEvent() method.
	 *
	 */
	public void publishShowHand(ShowHandEvent event) {
		for (ShowHandListener listener : showHandListeners) {
			listener.onShowHandEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given show hand listener for show hand events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeShowHandListener(ShowHandListener listener) {
		showHandListeners.add(listener);
	}

	/**
	 * Unsubscribe the given show hand listener for show hand events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeShowHandListener(ShowHandListener listener) {
		showHandListeners.remove(listener);
	}

	/**
	 * This list contains all show hand listeners that
	 * should be alerted on a show hand.
	 */
	private final List<ShowHandListener> showHandListeners = new CopyOnWriteArrayList<ShowHandListener>();


	/**
	 * Inform all subscribed stack changed listeners a stack changed event has occurred.
	 *
	 * Each subscribed stack changed listener is updated
	 * by calling their onStackChangedEvent() method.
	 *
	 */
	public void publishStackChanged(StackChangedEvent event) {
		for (StackChangedListener listener : stackChangedListeners) {
			listener.onStackChangedEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given stack changed listener for stack changed events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeStackChangedListener(StackChangedListener listener) {
		stackChangedListeners.add(listener);
	}

	/**
	 * Unsubscribe the given stack changed listener for stack changed events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeStackChangedListener(StackChangedListener listener) {
		stackChangedListeners.remove(listener);
	}

	/**
	 * This list contains all stack changed listeners that
	 * should be alerted on a stack changed.
	 */
	private final List<StackChangedListener> stackChangedListeners = new CopyOnWriteArrayList<StackChangedListener>();

	/**
	 * Inform all subscribed pot changed listeners a pot changed event has occurred.
	 *
	 * Each subscribed pot changed listener is updated
	 * by calling their onPotChangedEvent() method.
	 *
	 */
	public void publishPotChangedEvent(PotChangedEvent event) {
		for (PotChangedListener listener : potChangedListeners) {
			listener.onPotChangedEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given pot changed listener for pot changed events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribePotChangedListener(PotChangedListener listener) {
		potChangedListeners.add(listener);
	}

	/**
	 * Unsubscribe the given pot changed listener for pot changed events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribePotChangedListener(PotChangedListener listener) {
		potChangedListeners.remove(listener);
	}

	/**
	 * This list contains all pot changed listeners that
	 * should be alerted on a pot changed.
	 */
	private final List<PotChangedListener> potChangedListeners = new CopyOnWriteArrayList<PotChangedListener>();

	/**
	 * Inform all subscribed message listeners a message event has occurred.
	 *
	 * Each subscribed message listener is updated
	 * by calling their onMessageEvent() method.
	 *
	 */
	public void publishMessageEvent(MessageEvent event) {
		for (MessageListener listener : messageListeners) {
			listener.onMessageEvent(event);
		}
		publishGameEvent(event);
	}

	/**
	 * Subscribe the given message listener for message events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeMessageListener(MessageListener listener) {
		messageListeners.add(listener);
	}

	/**
	 * Unsubscribe the given message listener for message events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeMessageListener(MessageListener listener) {
		messageListeners.remove(listener);
	}

	/**
	 * This list contains all message listeners that
	 * should be alerted on a message.
	 */
	private final List<MessageListener> messageListeners = new CopyOnWriteArrayList<MessageListener>();


	/**********************************************************
	 * Personal Events
	 **********************************************************/

	/**
	 * Inform all subscribed new private cards listeners a new private cards event event has occurred.
	 *
	 * Each subscribed new private cards listener is updated
	 * by calling their onNewPrivateCards() method.
	 *
	 */
	public void publishNewPocketCardsEvent(PlayerId id, NewPocketCardsEvent event) {
		List<NewPrivateCardsListener> listeners = newPrivateCardsListeners.get(id);
		if(listeners!=null){
			for(NewPrivateCardsListener listener:listeners){
				listener.onNewPrivateCardsEvent(event);
			}
		}
		publishPersonalGameEvent(id, event);
	}


	/**
	 * Subscribe the given new private cards listener for new private cards events.
	 *
	 * @param	id
	 * 			The id of the player to get the new private cards events from.
	 * @param 	listener
	 * 			The listener to subscribe.
	 *
	 * @note This method is both non-blocking and thread-safe.
	 */
	public void subscribeNewPocketCardsListener(PlayerId id, NewPrivateCardsListener listener) {
		List<NewPrivateCardsListener> currentListeners;
		List<NewPrivateCardsListener> newListeners;

		boolean notAdded;

		do{
			notAdded = false;
			currentListeners = newPrivateCardsListeners.get(id);
			if(currentListeners==null){
				newListeners = new ArrayList<NewPrivateCardsListener>();
			}else{
				newListeners = new ArrayList<NewPrivateCardsListener>(currentListeners);
			}
			newListeners.add(listener);
			if(currentListeners==null){
				notAdded = (newPrivateCardsListeners.putIfAbsent(id, Collections.unmodifiableList(newListeners))!=null);
			}else{
				notAdded = !newPrivateCardsListeners.replace(id, currentListeners, Collections.unmodifiableList(newListeners));
			}
		}while(notAdded);
	}


	/**
	 * Unsubscribe the given new private cards listener for new private cards events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeNewPocketCardsListener(PlayerId id, NewPrivateCardsListener listener) {
		List<NewPrivateCardsListener> currentListeners;
		List<NewPrivateCardsListener> newListeners;

		boolean removed;

		do{
			currentListeners = newPrivateCardsListeners.get(id);
			if(currentListeners==null)
				return;
			newListeners = new ArrayList<NewPrivateCardsListener>(currentListeners);
			newListeners.remove(listener);
			if(newListeners.size()==0){
				removed = newPrivateCardsListeners.remove(id, currentListeners);
			}else{
				removed = newPrivateCardsListeners.replace(id, currentListeners, Collections.unmodifiableList(newListeners));
			}
		}while(!removed);
	}

	/**
	 * This list contains all new private cards listeners that
	 * should be alerted on a new private cards.
	 */
	private final ConcurrentMap<PlayerId,List<NewPrivateCardsListener>> newPrivateCardsListeners = new ConcurrentHashMap<PlayerId, List<NewPrivateCardsListener>>();



	/**********************************************************
	 * All game events listener
	 **********************************************************/

	/**
	 * Inform all subscribed game event listeners a game event has occurred.
	 *
	 * Each subscribed game event listener is updated
	 * by calling their onGameEvent() method.
	 *
	 */
	private void publishGameEvent(Event event){
		for(EventListener listener:gameEventListeners){
			listener.onEvent(event);
		}
	}

	/**
	 * Subscribe the given game event listener for game events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeGameEventListener(EventListener listener) {
		gameEventListeners.add(listener);
	}

	/**
	 * Unsubscribe the given game event listener for game events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeGameEventListener(EventListener listener) {
		gameEventListeners.remove(listener);
	}

	/**
	 * This list contains all game event listeners that
	 * should be alerted on a game event.
	 */
	private final List<EventListener> gameEventListeners = new CopyOnWriteArrayList<EventListener>();

	/**********************************************************
	 * Personal game events listener
	 *
	 * All personal events for one player can easily be collected.
	 **********************************************************/

	/**
	 * Inform all subscribed personal event listeners a personal event event has occurred.
	 *
	 * Each subscribed personal event listener is updated
	 * by calling their onGameEvent() method.
	 *
	 */
	public void publishPersonalGameEvent(PlayerId id, GameEvent event) {
		List<EventListener> listeners = personalEventsListeners.get(id);
		if(listeners!=null){
			for(EventListener listener:listeners){
				listener.onEvent(event);
			}
		}
		publishAllPersonalEvents(event);
	}

	/**
	 * Subscribe the given personal event listener for personal event events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribePersonalGameEventListener(PlayerId id, EventListener listener) {
		List<EventListener> currentListeners;
		List<EventListener> newListeners;

		boolean notAdded;

		do{
			notAdded = false;
			currentListeners = personalEventsListeners.get(id);
			if(currentListeners==null){
				newListeners = new ArrayList<EventListener>();
			}else{
				newListeners = new ArrayList<EventListener>(currentListeners);
			}
			newListeners.add(listener);
			if(currentListeners==null){
				notAdded = (personalEventsListeners.putIfAbsent(id, Collections.unmodifiableList(newListeners))!=null);
			}else{
				notAdded = !personalEventsListeners.replace(id, currentListeners, Collections.unmodifiableList(newListeners));
			}
		}while(notAdded);
	}

	/**
	 * Unsubscribe the given personal event listener for personal event events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribePersonalGameEventListener(PlayerId id, EventListener listener) {
		List<EventListener> currentListeners;
		List<EventListener> newListeners;

		boolean removed;

		do{
			currentListeners = personalEventsListeners.get(id);
			if(currentListeners==null)
				return;
			newListeners = new ArrayList<EventListener>(currentListeners);
			newListeners.remove(listener);
			if(newListeners.size()==0){
				removed = personalEventsListeners.remove(id, currentListeners);
			}else{
				removed = personalEventsListeners.replace(id, currentListeners, Collections.unmodifiableList(newListeners));
			}
		}while(!removed);
	}

	/**
	 * This hash map contains all personal event listeners that
	 * should be alerted on a personal event for a given id.
	 */
	private final ConcurrentMap<PlayerId,List<EventListener>> personalEventsListeners = new ConcurrentHashMap<PlayerId, List<EventListener>>();

	/**********************************************************
	 * All personal game events listener
	 *
	 * Game loggers can also obtain private events
	 * all other players can only receive personally.
	 **********************************************************/


	/**
	 * Inform all subscribed personal listeners a personal event has occurred.
	 *
	 * Each subscribed personal listener is updated
	 * by calling their onGameEvent() method.
	 *
	 */
	public void publishAllPersonalEvents(GameEvent event) {
		for (GameEventListener listener : allPersonalEventsListeners) {
			listener.onGameEvent(event);
		}
	}

	/**
	 * Subscribe the given personal listener for personal events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeAllPersonalEventsListener(GameEventListener listener) {
		allPersonalEventsListeners.add(listener);
	}

	/**
	 * Unsubscribe the given personal listener for personal events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeAllPersonalEventsListener(GameEventListener listener) {
		allPersonalEventsListeners.remove(listener);
	}

	/**
	 * This list contains all personal listeners that
	 * should be alerted on a personal.
	 */
	private final List<GameEventListener> allPersonalEventsListeners = new CopyOnWriteArrayList<GameEventListener>();
}
