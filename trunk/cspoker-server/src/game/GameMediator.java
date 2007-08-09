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

package game;

import game.elements.table.PlayerListFullException;
import game.events.GameEvent;
import game.events.GameEventListener;
import game.events.NewCommonCardsEvent;
import game.events.NewCommonCardsListener;
import game.events.NewDealEvent;
import game.events.NewDealListener;
import game.events.NewRoundEvent;
import game.events.NewRoundListener;
import game.events.playerActionEvents.AllInEvent;
import game.events.playerActionEvents.AllInListener;
import game.events.playerActionEvents.BetEvent;
import game.events.playerActionEvents.BetListener;
import game.events.playerActionEvents.CallEvent;
import game.events.playerActionEvents.CallListener;
import game.events.playerActionEvents.CheckEvent;
import game.events.playerActionEvents.CheckListener;
import game.events.playerActionEvents.DealEvent;
import game.events.playerActionEvents.DealListener;
import game.events.playerActionEvents.FoldEvent;
import game.events.playerActionEvents.FoldListener;
import game.events.playerActionEvents.RaiseEvent;
import game.events.playerActionEvents.RaiseListener;
import game.events.privateEvents.NewPocketCardsEvent;
import game.events.privateEvents.NewPrivateCardsListener;
import game.gameControl.GameControl;
import game.gameControl.PlayerAction;
import game.gameControl.actions.IllegalActionException;
import game.player.Player;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
	public void publishNewCommonCardsEvent(NewCommonCardsEvent event){
		for(NewCommonCardsListener listener:newCommonCardsListeners){
			listener.onNewCommonCardsEvent(event);
		}
		publishGameEvent(event);
	}
	
	/**
	 * Subscribe the given new common cards listener for new common cards events.
	 * 
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeNewCommonCardsListener(NewCommonCardsListener listener) {
		newCommonCardsListeners.add(listener);
	}

	/**
	 * Unsubscribe the given new common cards listener for new common cards events.
	 * 
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeNewCommonCardsListener(NewCommonCardsListener listener) {
		newCommonCardsListeners.remove(listener);
	}

	/**
	 * This list contains all new common cards listeners that
	 * should be alerted on new common cards.
	 */
	private final List<NewCommonCardsListener> newCommonCardsListeners = new CopyOnWriteArrayList<NewCommonCardsListener>();
	
	
	
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
		publishPrivateEvent(event);
	}
	
	/**
	 * Subscribe the given new private cards listener for new private cards events.
	 * 
	 * @param	id
	 * 			The id of the player to get the new private cards events from.
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeNewPocketCardsListener(PlayerId id, NewPrivateCardsListener listener) {
		
		//TODO problems with removing an id mapping...
		
		List<NewPrivateCardsListener> listeners = newPrivateCardsListeners.get(id);
		if(listeners!=null){
			listeners.add(listener);
		}else{
			listeners = new CopyOnWriteArrayList<NewPrivateCardsListener>();
			listeners.add(listener);
			List<NewPrivateCardsListener> changedListener = newPrivateCardsListeners.putIfAbsent(id, listeners);
			if(changedListener!=null){
				changedListener.add(listener);
			}
		}
	}

	/**
	 * Unsubscribe the given new private cards listener for new private cards events.
	 * 
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeNewPocketCardsListener(NewPrivateCardsListener listener) {
		newPrivateCardsListeners.remove(listener);
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
	public void publishGameEvent(GameEvent event){
		for(GameEventListener listener:gameEventListeners){
			listener.onGameEvent(event);
		}
	}
	
	/**
	 * Subscribe the given game event listener for game events.
	 * 
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeGameEventListener(GameEventListener listener) {
		gameEventListeners.add(listener);
	}
	
	/**
	 * Unsubscribe the given game event listener for game events.
	 * 
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeGameEventListener(GameEventListener listener) {
		gameEventListeners.remove(listener);
	}
	
	/**
	 * This list contains all game event listeners that
	 * should be alerted on a game event.
	 */
	private final List<GameEventListener> gameEventListeners = new CopyOnWriteArrayList<GameEventListener>();
	
	/**********************************************************
	 * Private game events listener
	 * 
	 * Game loggers can also obtain private events
	 * all other players can only receive personally.
	 **********************************************************/
	
	/**
	 * Inform all subscribed game listeners a private event has occurred.
	 * 
	 * Each subscribed game listener is updated
	 * by calling their onGameEvent() method.
	 * 
	 */

	public void publishPrivateEvent(GameEvent event){
		for(GameEventListener listener:privateEventsListeners){
			listener.onGameEvent(event);
		}
	}
	
	/**
	 * Subscribe the given game events listener for private events events.
	 * 
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribePrivateEventsListener(GameEventListener listener) {
		privateEventsListeners.add(listener);
	}

	/**
	 * Unsubscribe the given game events listener for private events events.
	 * 
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribePrivateEventsListener(GameEventListener listener) {
		privateEventsListeners.remove(listener);
	}

	/**
	 * This list contains all game events listeners that
	 * should be alerted on a private events.
	 */
	private final List<GameEventListener> privateEventsListeners = new CopyOnWriteArrayList<GameEventListener>();
}
