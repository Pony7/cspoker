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

package org.cspoker.server.game.playerCommunication;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.common.game.IllegalActionException;
import org.cspoker.common.game.PlayerCommunication;
import org.cspoker.common.game.elements.table.TableId;
import org.cspoker.common.game.eventlisteners.AllEventsListener;
import org.cspoker.common.game.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.game.eventlisteners.game.RemoteGameMessageListener;
import org.cspoker.common.game.eventlisteners.game.RemoteNewCommunityCardsListener;
import org.cspoker.common.game.eventlisteners.game.RemoteNewDealListener;
import org.cspoker.common.game.eventlisteners.game.RemoteNewRoundListener;
import org.cspoker.common.game.eventlisteners.game.RemoteNextPlayerListener;
import org.cspoker.common.game.eventlisteners.game.RemotePlayerJoinedGameListener;
import org.cspoker.common.game.eventlisteners.game.RemotePlayerLeftTableListener;
import org.cspoker.common.game.eventlisteners.game.RemoteShowHandListener;
import org.cspoker.common.game.eventlisteners.game.RemoteWinnerListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteAllInListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteBetListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteBigBlindListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteCallListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteCheckListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteFoldListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteRaiseListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteSmallBlindListener;
import org.cspoker.common.game.eventlisteners.game.privatelistener.RemoteNewPocketCardsListener;
import org.cspoker.common.game.eventlisteners.server.RemotePlayerJoinedListener;
import org.cspoker.common.game.eventlisteners.server.RemotePlayerLeftListener;
import org.cspoker.common.game.eventlisteners.server.RemoteServerMessageListener;
import org.cspoker.common.game.eventlisteners.server.RemoteTableCreatedListener;
import org.cspoker.common.game.events.gameEvents.GameMessageEvent;
import org.cspoker.common.game.events.gameEvents.NewCommunityCardsEvent;
import org.cspoker.common.game.events.gameEvents.NewDealEvent;
import org.cspoker.common.game.events.gameEvents.NewRoundEvent;
import org.cspoker.common.game.events.gameEvents.NextPlayerEvent;
import org.cspoker.common.game.events.gameEvents.PlayerJoinedGameEvent;
import org.cspoker.common.game.events.gameEvents.PlayerLeftTableEvent;
import org.cspoker.common.game.events.gameEvents.ShowHandEvent;
import org.cspoker.common.game.events.gameEvents.WinnerEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.AllInEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.BetEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.BigBlindEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.CallEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.CheckEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.FoldEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.RaiseEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.SmallBlindEvent;
import org.cspoker.common.game.events.gameEvents.privateEvents.NewPocketCardsEvent;
import org.cspoker.common.game.events.serverEvents.PlayerJoinedEvent;
import org.cspoker.common.game.events.serverEvents.PlayerLeftEvent;
import org.cspoker.common.game.events.serverEvents.ServerMessageEvent;
import org.cspoker.common.game.events.serverEvents.TableCreatedEvent;
import org.cspoker.common.game.player.PlayerId;
import org.cspoker.server.game.GameManager;
import org.cspoker.server.game.player.GamePlayer;

/**
 * A class of player communications.
 *
 * It's the interface to all game control actions.
 *
 * @author Kenzo
 *
 */
public class PlayerCommunicationImpl implements PlayerCommunication {

    /***************************************************************************
     * Variables
     **************************************************************************/

    /**
     * The variable containing the player.
     */
    private final GamePlayer player;

    /**
     * This variable contains the player communication state.
     */
    private PlayerCommunicationState state;

    private final EventsCollector eventsCollector = new EventsCollector();

    /***************************************************************************
     * Constructor
     **************************************************************************/

    /**
     * Construct a new player communication with given player.
     *
     * @param player
     *                The given player
     */
    public PlayerCommunicationImpl(GamePlayer player) {
	this.player = player;
	state = new InitialState(this);
	GameManager.getServerMediator().subscribeAllServerEventsListener(
		player.getId(), getAllEventsListener());
	subscribeAllEventsListener(getEventsCollector());
    }

    /**
     * Returns the player contained in this player communication.
     *
     * @return The player contained in this player communication.
     */
    public GamePlayer getPlayer() {
	return player;
    }

    /**
     * Returns the player id from the player contained in this player
     * communication.
     *
     * @return The player id from the player contained in this player
     *         communication.
     */
    public PlayerId getId() {
	return player.getId();
    }
    
    /***************************************************************************
     * Maintenance Actions
     **************************************************************************/
    public void destruct(){
	//TODO ?
    }
    
    /***************************************************************************
     * Player Actions
     **************************************************************************/

    public void call() throws IllegalActionException {
	state.call();
    }

    public void bet(int amount) throws IllegalActionException {
	state.bet(amount);
    }

    public void fold() throws IllegalActionException {
	state.fold();
    }

    public void check() throws IllegalActionException {
	state.check();
    }

    public void raise(int amount) throws IllegalActionException {
	state.raise(amount);
    }

    public void deal() throws IllegalActionException {
	state.deal();
    }

    public void allIn() throws IllegalActionException {
	state.allIn();
    }

    public void say(String message) {
	state.say(message);
    }

    /***************************************************************************
     * Leave/Join Game
     **************************************************************************/

    /**
     * Join the table with given table id.
     *
     * @pre The given id should be effective. |id!=null
     * @throws IllegalActionException
     *                 [can] This actions is not a valid action in the current
     *                 state.
     */
    public void joinTable(TableId id) throws IllegalActionException {
	if (id == null)
	    throw new IllegalArgumentException(
		    "The given table id is not effective.");
	state.join(id);
    }

    public void leaveTable() throws IllegalActionException {
	state.leaveTable();
    }

    public TableId createTable() throws IllegalActionException {
	return state.createTable();
    }

    public void startGame() throws IllegalActionException {
	state.startGame();
    }

    public Events getLatestEvents() throws IllegalActionException {
	return eventsCollector.getLatestEvents();
    }

    public Events getLatestEventsAndAck(int ack) throws IllegalActionException {
	return eventsCollector.getLatestEventsAndAck(ack);
    }

    EventsCollector getEventsCollector() {
	return eventsCollector;
    }

    void setPlayerCommunicationState(PlayerCommunicationState state) {
	this.state = state;
    }

    @Override
    public String toString() {
	return "player communication of " + player.getName();
    }

    /***************************************************************************
     * Publisher
     **************************************************************************/

    /**
     * Subscribe the given all-in listener for all-in events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeAllInListener(RemoteAllInListener listener) {
	allInListeners.add(listener);
    }

    /**
     * Unsubscribe the given all-in listener for all-in events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeAllInListener(RemoteAllInListener listener) {
	allInListeners.remove(listener);
    }

    /**
     * This list contains all all-in listeners that should be alerted on a
     * all-in.
     */
    private final List<RemoteAllInListener> allInListeners = new CopyOnWriteArrayList<RemoteAllInListener>();

    /**
     * Subscribe the given bet listener for bet events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeBetListener(RemoteBetListener listener) {
	betListeners.add(listener);
    }

    /**
     * Unsubscribe the given bet listener for bet events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeBetListener(RemoteBetListener listener) {
	betListeners.remove(listener);
    }

    /**
     * This list contains all bet listeners that should be alerted on a bet.
     */
    private final List<RemoteBetListener> betListeners = new CopyOnWriteArrayList<RemoteBetListener>();

    /**
     * Subscribe the given big blind listener for big blind events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeBigBlindListener(RemoteBigBlindListener listener) {
	bigBlindListeners.add(listener);
    }

    /**
     * Unsubscribe the given big blind listener for big blind events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeBigBlindListener(RemoteBigBlindListener listener) {
	bigBlindListeners.remove(listener);
    }

    /**
     * This list contains all big blind listeners that should be alerted on a
     * big blind.
     */
    private final List<RemoteBigBlindListener> bigBlindListeners = new CopyOnWriteArrayList<RemoteBigBlindListener>();

    /**
     * Subscribe the given call listener for call events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeCallListener(RemoteCallListener listener) {
	callListeners.add(listener);
    }

    /**
     * Unsubscribe the given call listener for call events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeCallListener(RemoteCallListener listener) {
	callListeners.remove(listener);
    }

    /**
     * This list contains all call listeners that should be alerted on a call.
     */
    private final List<RemoteCallListener> callListeners = new CopyOnWriteArrayList<RemoteCallListener>();

    /**
     * Subscribe the given check listener for check events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeCheckListener(RemoteCheckListener listener) {
	checkListeners.add(listener);
    }

    /**
     * Unsubscribe the given check listener for check events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeCheckListener(RemoteCheckListener listener) {
	checkListeners.remove(listener);
    }

    /**
     * This list contains all check listeners that should be alerted on a check.
     */
    private final List<RemoteCheckListener> checkListeners = new CopyOnWriteArrayList<RemoteCheckListener>();

    /**
     * Subscribe the given fold listener for fold events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeFoldListener(RemoteFoldListener listener) {
	foldListeners.add(listener);
    }

    /**
     * Unsubscribe the given fold listener for fold events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeFoldListener(RemoteFoldListener listener) {
	foldListeners.remove(listener);
    }

    /**
     * This list contains all fold listeners that should be alerted on a fold.
     */
    private final List<RemoteFoldListener> foldListeners = new CopyOnWriteArrayList<RemoteFoldListener>();

    /**
     * Subscribe the given raise listener for raise events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeRaiseListener(RemoteRaiseListener listener) {
	raiseListeners.add(listener);
    }

    /**
     * Unsubscribe the given raise listener for raise events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeRaiseListener(RemoteRaiseListener listener) {
	raiseListeners.remove(listener);
    }

    /**
     * This list contains all raise listeners that should be alerted on a raise.
     */
    private final List<RemoteRaiseListener> raiseListeners = new CopyOnWriteArrayList<RemoteRaiseListener>();

    /**
     * Subscribe the given small blind listener for small blind events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeSmallBlindListener(RemoteSmallBlindListener listener) {
	smallBlindListeners.add(listener);
    }

    /**
     * Unsubscribe the given small blind listener for small blind events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeSmallBlindListener(RemoteSmallBlindListener listener) {
	smallBlindListeners.remove(listener);
    }

    /**
     * This list contains all small blind listeners that should be alerted on a
     * small blind event.
     */
    private final List<RemoteSmallBlindListener> smallBlindListeners = new CopyOnWriteArrayList<RemoteSmallBlindListener>();

    /**
     * Subscribe the given new pocket cards listener for new pocket cards
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewPocketCardsListener(RemoteNewPocketCardsListener listener) {
	newPocketCardsListeners.add(listener);
    }

    /**
     * Unsubscribe the given new pocket cards listener for new pocket cards
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeNewPocketCardsListener(
	    RemoteNewPocketCardsListener listener) {
	newPocketCardsListeners.remove(listener);
    }

    /**
     * This list contains all new private cards listeners that should be alerted
     * on a new private cards.
     */
    private final List<RemoteNewPocketCardsListener> newPocketCardsListeners = new CopyOnWriteArrayList<RemoteNewPocketCardsListener>();

    /**
     * Subscribe the given new common cards listener for new common cards
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewCommonCardsListener(
	    RemoteNewCommunityCardsListener listener) {
	newCommunityCardsListeners.add(listener);
    }

    /**
     * Unsubscribe the given new common cards listener for new common cards
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeNewCommonCardsListener(
	    RemoteNewCommunityCardsListener listener) {
	newCommunityCardsListeners.remove(listener);
    }

    /**
     * This list contains all new common cards listeners that should be alerted
     * on new common cards.
     */
    private final List<RemoteNewCommunityCardsListener> newCommunityCardsListeners = new CopyOnWriteArrayList<RemoteNewCommunityCardsListener>();

    /**
     * Subscribe the given new deal listener for new deal events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewDealListener(RemoteNewDealListener listener) {
	newDealListeners.add(listener);
    }

    /**
     * Unsubscribe the given new deal listener for new deal events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeNewDealListener(RemoteNewDealListener listener) {
	newDealListeners.remove(listener);
    }

    /**
     * This list contains all new deal listeners that should be alerted on a new
     * deal.
     */
    private final List<RemoteNewDealListener> newDealListeners = new CopyOnWriteArrayList<RemoteNewDealListener>();

    /**
     * Subscribe the given new round listener for new round events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewRoundListener(RemoteNewRoundListener listener) {
	newRoundListeners.add(listener);
    }

    /**
     * Unsubscribe the given new round listener for new round events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeNewRoundListener(RemoteNewRoundListener listener) {
	newRoundListeners.remove(listener);
    }

    /**
     * This list contains all new round listeners that should be alerted on a
     * new round.
     */
    private final List<RemoteNewRoundListener> newRoundListeners = new CopyOnWriteArrayList<RemoteNewRoundListener>();

    /**
     * Subscribe the given next player listener for next player events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNextPlayerListener(RemoteNextPlayerListener listener) {
	nextPlayerListeners.add(listener);
    }

    /**
     * Unsubscribe the given next player listener for next player events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeNextPlayerListener(RemoteNextPlayerListener listener) {
	nextPlayerListeners.remove(listener);
    }

    /**
     * This list contains all next player listeners that should be alerted on a
     * next player.
     */
    private final List<RemoteNextPlayerListener> nextPlayerListeners = new CopyOnWriteArrayList<RemoteNextPlayerListener>();

    /**
     * Subscribe the given player joined game listener for player joined game
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerJoinedGameListener(
	    RemotePlayerJoinedGameListener listener) {
	playerJoinedGameListeners.add(listener);
    }

    /**
     * Unsubscribe the given player joined game listener for player joined game
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribePlayerJoinedGameListener(
	    RemotePlayerJoinedGameListener listener) {
	playerJoinedGameListeners.remove(listener);
    }

    /**
     * This list contains all player joined game listeners that should be
     * alerted on a player joined game.
     */
    private final List<RemotePlayerJoinedGameListener> playerJoinedGameListeners = new CopyOnWriteArrayList<RemotePlayerJoinedGameListener>();

    /**
     * Subscribe the given show hand listener for show hand events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeShowHandListener(RemoteShowHandListener listener) {
	showHandListeners.add(listener);
    }

    /**
     * Unsubscribe the given show hand listener for show hand events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeShowHandListener(RemoteShowHandListener listener) {
	showHandListeners.remove(listener);
    }

    /**
     * This list contains all show hand listeners that should be alerted on a
     * show hand.
     */
    private final List<RemoteShowHandListener> showHandListeners = new CopyOnWriteArrayList<RemoteShowHandListener>();

    /**
     * Subscribe the given winner listener for winner events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeWinnerListener(RemoteWinnerListener listener) {
	winnerListeners.add(listener);
    }

    /**
     * Unsubscribe the given winner listener for winner events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeWinnerListener(RemoteWinnerListener listener) {
	winnerListeners.remove(listener);
    }

    /**
     * This list contains all winner listeners that should be alerted on a
     * winner.
     */
    private final List<RemoteWinnerListener> winnerListeners = new CopyOnWriteArrayList<RemoteWinnerListener>();

    /**
     * Subscribe the given player left table listener for player left table
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerLeftTableListener(
	    RemotePlayerLeftTableListener listener) {
	playerLeftTableListeners.add(listener);
    }

    /**
     * Unsubscribe the given player left table listener for player left table
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribePlayerLeftTableListener(
	    RemotePlayerLeftTableListener listener) {
	playerLeftTableListeners.remove(listener);
    }

    /**
     * This list contains all player left table listeners that should be alerted
     * on a player left table.
     */
    private final List<RemotePlayerLeftTableListener> playerLeftTableListeners = new CopyOnWriteArrayList<RemotePlayerLeftTableListener>();

    /**
     * Subscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeGameMessageListener(RemoteGameMessageListener listener) {
	gameMessageListeners.add(listener);
    }

    /**
     * Unsubscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeGameMessageListener(RemoteGameMessageListener listener) {
	gameMessageListeners.remove(listener);
    }

    /**
     * This list contains all message listeners that should be alerted on a
     * message.
     */
    private final List<RemoteGameMessageListener> gameMessageListeners = new CopyOnWriteArrayList<RemoteGameMessageListener>();

    /**
     * Subscribe the given player joined listener for player joined events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerJoinedListener(RemotePlayerJoinedListener listener) {
	playerJoinedListeners.add(listener);
    }

    /**
     * Unsubscribe the given player joined listener for player joined events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribePlayerJoinedListener(RemotePlayerJoinedListener listener) {
	playerJoinedListeners.remove(listener);
    }

    /**
     * This list contains all player joined listeners that should be alerted on
     * a player joined event.
     */
    private final List<RemotePlayerJoinedListener> playerJoinedListeners = new CopyOnWriteArrayList<RemotePlayerJoinedListener>();

    /**
     * Subscribe the given table created listener for table created events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeTableCreatedListener(RemoteTableCreatedListener listener) {
	tableCreatedListeners.add(listener);
    }

    /**
     * Unsubscribe the given table created listener for table created events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeTableCreatedListener(RemoteTableCreatedListener listener) {
	tableCreatedListeners.remove(listener);
    }

    /**
     * This list contains all table created listeners that should be alerted on
     * a table created.
     */
    private final List<RemoteTableCreatedListener> tableCreatedListeners = new CopyOnWriteArrayList<RemoteTableCreatedListener>();

    /**
     * Subscribe the given player left listener for player left events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerLeftListener(RemotePlayerLeftListener listener) {
	playerLeftListeners.add(listener);
    }

    /**
     * Unsubscribe the given player left listener for player left events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribePlayerLeftListener(RemotePlayerLeftListener listener) {
	playerLeftListeners.remove(listener);
    }

    /**
     * This list contains all player left listeners that should be alerted on a
     * player left.
     */
    private final List<RemotePlayerLeftListener> playerLeftListeners = new CopyOnWriteArrayList<RemotePlayerLeftListener>();

    /**
     * Subscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeServerMessageListener(RemoteServerMessageListener listener) {
	serverMessageListeners.add(listener);
    }

    /**
     * Unsubscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeServerMessageListener(RemoteServerMessageListener listener) {
	serverMessageListeners.remove(listener);
    }

    public void subscribeAllEventsListener(RemoteAllEventsListener listener) {
	subscribeAllInListener(listener);
	subscribeBetListener(listener);
	subscribeBigBlindListener(listener);
	subscribeCallListener(listener);
	subscribeCheckListener(listener);
	subscribeFoldListener(listener);
	subscribeGameMessageListener(listener);
	subscribeNewCommonCardsListener(listener);
	subscribeNewDealListener(listener);
	subscribeNewPocketCardsListener(listener);
	subscribeNewRoundListener(listener);
	subscribeNextPlayerListener(listener);
	subscribePlayerJoinedGameListener(listener);
	subscribePlayerJoinedListener(listener);
	subscribePlayerLeftListener(listener);
	subscribePlayerLeftTableListener(listener);
	subscribeRaiseListener(listener);
	subscribeServerMessageListener(listener);
	subscribeShowHandListener(listener);
	subscribeSmallBlindListener(listener);
	subscribeTableCreatedListener(listener);
	subscribeWinnerListener(listener);
    }

    public void unsubscribeAllEventsListener(RemoteAllEventsListener listener) {
	unsubscribeAllInListener(listener);
	unsubscribeBetListener(listener);
	unsubscribeBigBlindListener(listener);
	unsubscribeCallListener(listener);
	unsubscribeCheckListener(listener);
	unsubscribeFoldListener(listener);
	unsubscribeGameMessageListener(listener);
	unsubscribeNewCommonCardsListener(listener);
	unsubscribeNewDealListener(listener);
	unsubscribeNewPocketCardsListener(listener);
	unsubscribeNewRoundListener(listener);
	unsubscribeNextPlayerListener(listener);
	unsubscribePlayerJoinedGameListener(listener);
	unsubscribePlayerJoinedListener(listener);
	unsubscribePlayerLeftListener(listener);
	unsubscribePlayerLeftTableListener(listener);
	unsubscribeRaiseListener(listener);
	unsubscribeServerMessageListener(listener);
	unsubscribeShowHandListener(listener);
	unsubscribeSmallBlindListener(listener);
	unsubscribeTableCreatedListener(listener);
	unsubscribeWinnerListener(listener);
    }

    /**
     * This list contains all message listeners that should be alerted on a
     * message.
     */
    private final List<RemoteServerMessageListener> serverMessageListeners = new CopyOnWriteArrayList<RemoteServerMessageListener>();

    /***************************************************************************
     * all events listener
     **************************************************************************/

    RemoteAllEventsListener getAllEventsListener() {
	return allEventsListener;
    }

    private final RemoteAllEventsListener allEventsListener = new AllEventsListenerImpl();

    private class AllEventsListenerImpl implements RemoteAllEventsListener {

	public void onAllInEvent(AllInEvent event) throws RemoteException {
	    for (RemoteAllInListener listener : allInListeners) {
		listener.onAllInEvent(event);
	    }
	}

	public void onBetEvent(BetEvent event) throws RemoteException {
	    for (RemoteBetListener listener : betListeners) {
		listener.onBetEvent(event);
	    }

	}

	public void onBigBlindEvent(BigBlindEvent event) throws RemoteException {
	    for (RemoteBigBlindListener listener : bigBlindListeners) {
		listener.onBigBlindEvent(event);
	    }

	}

	public void onCallEvent(CallEvent event) throws RemoteException  {
	    for (RemoteCallListener listener : callListeners) {
		listener.onCallEvent(event);
	    }
	}

	public void onCheckEvent(CheckEvent event) throws RemoteException {
	    for (RemoteCheckListener listener : checkListeners) {
		listener.onCheckEvent(event);
	    }
	}

	public void onFoldEvent(FoldEvent event) throws RemoteException {
	    for (RemoteFoldListener listener : foldListeners) {
		listener.onFoldEvent(event);
	    }

	}

	public void onRaiseEvent(RaiseEvent event) throws RemoteException {
	    for (RemoteRaiseListener listener : raiseListeners) {
		listener.onRaiseEvent(event);
	    }
	}

	public void onSmallBlindEvent(SmallBlindEvent event) throws RemoteException {
	    for (RemoteSmallBlindListener listener : smallBlindListeners) {
		listener.onSmallBlindEvent(event);
	    }
	}

	public void onNewPocketCardsEvent(NewPocketCardsEvent event) throws RemoteException {
	    for (RemoteNewPocketCardsListener listener : newPocketCardsListeners) {
		listener.onNewPocketCardsEvent(event);
	    }

	}

	public void onNewCommunityCardsEvent(NewCommunityCardsEvent event) throws RemoteException  {
	    for (RemoteNewCommunityCardsListener listener : newCommunityCardsListeners) {
		listener.onNewCommunityCardsEvent(event);
	    }
	}

	public void onNewDealEvent(NewDealEvent event) throws RemoteException {
	    for (RemoteNewDealListener listener : newDealListeners) {
		listener.onNewDealEvent(event);
	    }

	}

	public void onNewRoundEvent(NewRoundEvent event) throws RemoteException {
	    for (RemoteNewRoundListener listener : newRoundListeners) {
		listener.onNewRoundEvent(event);
	    }

	}

	public void onNextPlayerEvent(NextPlayerEvent event) throws RemoteException {
	    for (RemoteNextPlayerListener listener : nextPlayerListeners) {
		listener.onNextPlayerEvent(event);
	    }
	}

	public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event) throws RemoteException {
	    for (RemotePlayerJoinedGameListener listener : playerJoinedGameListeners) {
		listener.onPlayerJoinedGameEvent(event);
	    }
	}

	public void onShowHandEvent(ShowHandEvent event) throws RemoteException {
	    for (RemoteShowHandListener listener : showHandListeners) {
		listener.onShowHandEvent(event);
	    }
	}

	public void onWinnerEvent(WinnerEvent event) throws RemoteException {
	    for (RemoteWinnerListener listener : winnerListeners) {
		listener.onWinnerEvent(event);
	    }
	}

	public void onPlayerLeftTableEvent(PlayerLeftTableEvent event) throws RemoteException {
	    for (RemotePlayerLeftTableListener listener : playerLeftTableListeners) {
		listener.onPlayerLeftTableEvent(event);
	    }

	}

	public void onGameMessageEvent(GameMessageEvent event)throws RemoteException  {
	    for (RemoteGameMessageListener listener : gameMessageListeners) {
		listener.onGameMessageEvent(event);
	    }
	}

	/***********************************************************************
	 * Server Events
	 **********************************************************************/

	public void onPlayerJoinedEvent(PlayerJoinedEvent event) throws RemoteException {
	    for (RemotePlayerJoinedListener listener : playerJoinedListeners) {
		listener.onPlayerJoinedEvent(event);
	    }

	}

	public void onTableCreatedEvent(TableCreatedEvent event) throws RemoteException {
	    for (RemoteTableCreatedListener listener : tableCreatedListeners) {
		listener.onTableCreatedEvent(event);
	    }
	}

	public void onPlayerLeftEvent(PlayerLeftEvent event) throws RemoteException {
	    for (RemotePlayerLeftListener listener : playerLeftListeners) {
		listener.onPlayerLeftEvent(event);
	    }
	}

	public void onServerMessageEvent(ServerMessageEvent event) throws RemoteException {
	    for (RemoteServerMessageListener listener : serverMessageListeners) {
		listener.onServerMessageEvent(event);
	    }
	}
    }
}
