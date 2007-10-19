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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.common.game.player.PlayerId;
import org.cspoker.server.game.GameManager;
import org.cspoker.server.game.TableId;
import org.cspoker.server.game.events.AllEventsListener;
import org.cspoker.server.game.events.gameEvents.GameMessageEvent;
import org.cspoker.server.game.events.gameEvents.GameMessageListener;
import org.cspoker.server.game.events.gameEvents.NewCommunityCardsEvent;
import org.cspoker.server.game.events.gameEvents.NewCommunityCardsListener;
import org.cspoker.server.game.events.gameEvents.NewDealEvent;
import org.cspoker.server.game.events.gameEvents.NewDealListener;
import org.cspoker.server.game.events.gameEvents.NewRoundEvent;
import org.cspoker.server.game.events.gameEvents.NewRoundListener;
import org.cspoker.server.game.events.gameEvents.NextPlayerEvent;
import org.cspoker.server.game.events.gameEvents.NextPlayerListener;
import org.cspoker.server.game.events.gameEvents.PlayerJoinedGameEvent;
import org.cspoker.server.game.events.gameEvents.PlayerJoinedGameListener;
import org.cspoker.server.game.events.gameEvents.PlayerLeftTableEvent;
import org.cspoker.server.game.events.gameEvents.PlayerLeftTableListener;
import org.cspoker.server.game.events.gameEvents.ShowHandEvent;
import org.cspoker.server.game.events.gameEvents.ShowHandListener;
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
import org.cspoker.server.game.events.gameEvents.playerActionEvents.FoldEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.FoldListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.RaiseEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.RaiseListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.SmallBlindEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.SmallBlindListener;
import org.cspoker.server.game.events.gameEvents.privateEvents.NewPocketCardsEvent;
import org.cspoker.server.game.events.gameEvents.privateEvents.NewPocketCardsListener;
import org.cspoker.server.game.events.serverEvents.PlayerJoinedEvent;
import org.cspoker.server.game.events.serverEvents.PlayerJoinedListener;
import org.cspoker.server.game.events.serverEvents.PlayerLeftEvent;
import org.cspoker.server.game.events.serverEvents.PlayerLeftListener;
import org.cspoker.server.game.events.serverEvents.ServerMessageEvent;
import org.cspoker.server.game.events.serverEvents.ServerMessageListener;
import org.cspoker.server.game.events.serverEvents.TableCreatedEvent;
import org.cspoker.server.game.events.serverEvents.TableCreatedListener;
import org.cspoker.server.game.gameControl.IllegalActionException;
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
	PlayerCommunicationManager.addPlayerCommunication(player.getId(), this);
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
    public void subscribeAllInListener(AllInListener listener) {
	allInListeners.add(listener);
    }

    /**
     * Unsubscribe the given all-in listener for all-in events.
     *
     * @param listener
     *                The listener to unsubscribe.
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
     * Subscribe the given bet listener for bet events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeBetListener(BetListener listener) {
	betListeners.add(listener);
    }

    /**
     * Unsubscribe the given bet listener for bet events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeBetListener(BetListener listener) {
	betListeners.remove(listener);
    }

    /**
     * This list contains all bet listeners that should be alerted on a bet.
     */
    private final List<BetListener> betListeners = new CopyOnWriteArrayList<BetListener>();

    /**
     * Subscribe the given big blind listener for big blind events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeBigBlindListener(BigBlindListener listener) {
	bigBlindListeners.add(listener);
    }

    /**
     * Unsubscribe the given big blind listener for big blind events.
     *
     * @param listener
     *                The listener to unsubscribe.
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
     * Subscribe the given call listener for call events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeCallListener(CallListener listener) {
	callListeners.add(listener);
    }

    /**
     * Unsubscribe the given call listener for call events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeCallListener(CallListener listener) {
	callListeners.remove(listener);
    }

    /**
     * This list contains all call listeners that should be alerted on a call.
     */
    private final List<CallListener> callListeners = new CopyOnWriteArrayList<CallListener>();

    /**
     * Subscribe the given check listener for check events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeCheckListener(CheckListener listener) {
	checkListeners.add(listener);
    }

    /**
     * Unsubscribe the given check listener for check events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeCheckListener(CheckListener listener) {
	checkListeners.remove(listener);
    }

    /**
     * This list contains all check listeners that should be alerted on a check.
     */
    private final List<CheckListener> checkListeners = new CopyOnWriteArrayList<CheckListener>();

    /**
     * Subscribe the given fold listener for fold events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeFoldListener(FoldListener listener) {
	foldListeners.add(listener);
    }

    /**
     * Unsubscribe the given fold listener for fold events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeFoldListener(FoldListener listener) {
	foldListeners.remove(listener);
    }

    /**
     * This list contains all fold listeners that should be alerted on a fold.
     */
    private final List<FoldListener> foldListeners = new CopyOnWriteArrayList<FoldListener>();

    /**
     * Subscribe the given raise listener for raise events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeRaiseListener(RaiseListener listener) {
	raiseListeners.add(listener);
    }

    /**
     * Unsubscribe the given raise listener for raise events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeRaiseListener(RaiseListener listener) {
	raiseListeners.remove(listener);
    }

    /**
     * This list contains all raise listeners that should be alerted on a raise.
     */
    private final List<RaiseListener> raiseListeners = new CopyOnWriteArrayList<RaiseListener>();

    /**
     * Subscribe the given small blind listener for small blind events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeSmallBlindListener(SmallBlindListener listener) {
	smallBlindListeners.add(listener);
    }

    /**
     * Unsubscribe the given small blind listener for small blind events.
     *
     * @param listener
     *                The listener to unsubscribe.
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
     * Subscribe the given new pocket cards listener for new pocket cards
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewPocketCardsListener(NewPocketCardsListener listener) {
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
	    NewPocketCardsListener listener) {
	newPocketCardsListeners.remove(listener);
    }

    /**
     * This list contains all new private cards listeners that should be alerted
     * on a new private cards.
     */
    private final List<NewPocketCardsListener> newPocketCardsListeners = new CopyOnWriteArrayList<NewPocketCardsListener>();

    /**
     * Subscribe the given new common cards listener for new common cards
     * events.
     *
     * @param listener
     *                The listener to subscribe.
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
     *                The listener to unsubscribe.
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
     * Subscribe the given new deal listener for new deal events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewDealListener(NewDealListener listener) {
	newDealListeners.add(listener);
    }

    /**
     * Unsubscribe the given new deal listener for new deal events.
     *
     * @param listener
     *                The listener to unsubscribe.
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
     * Subscribe the given new round listener for new round events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewRoundListener(NewRoundListener listener) {
	newRoundListeners.add(listener);
    }

    /**
     * Unsubscribe the given new round listener for new round events.
     *
     * @param listener
     *                The listener to unsubscribe.
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
     * Subscribe the given next player listener for next player events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNextPlayerListener(NextPlayerListener listener) {
	nextPlayerListeners.add(listener);
    }

    /**
     * Unsubscribe the given next player listener for next player events.
     *
     * @param listener
     *                The listener to unsubscribe.
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
     * Subscribe the given player joined game listener for player joined game
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerJoinedGameListener(
	    PlayerJoinedGameListener listener) {
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
	    PlayerJoinedGameListener listener) {
	playerJoinedGameListeners.remove(listener);
    }

    /**
     * This list contains all player joined game listeners that should be
     * alerted on a player joined game.
     */
    private final List<PlayerJoinedGameListener> playerJoinedGameListeners = new CopyOnWriteArrayList<PlayerJoinedGameListener>();

    /**
     * Subscribe the given show hand listener for show hand events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeShowHandListener(ShowHandListener listener) {
	showHandListeners.add(listener);
    }

    /**
     * Unsubscribe the given show hand listener for show hand events.
     *
     * @param listener
     *                The listener to unsubscribe.
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
     * Subscribe the given winner listener for winner events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeWinnerListener(WinnerListener listener) {
	winnerListeners.add(listener);
    }

    /**
     * Unsubscribe the given winner listener for winner events.
     *
     * @param listener
     *                The listener to unsubscribe.
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
     * Subscribe the given player left table listener for player left table
     * events.
     *
     * @param listener
     *                The listener to subscribe.
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
     *                The listener to unsubscribe.
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
     * Subscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeGameMessageListener(GameMessageListener listener) {
	gameMessageListeners.add(listener);
    }

    /**
     * Unsubscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to unsubscribe.
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
     * Subscribe the given player joined listener for player joined events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerJoinedListener(PlayerJoinedListener listener) {
	playerJoinedListeners.add(listener);
    }

    /**
     * Unsubscribe the given player joined listener for player joined events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribePlayerJoinedListener(PlayerJoinedListener listener) {
	playerJoinedListeners.remove(listener);
    }

    /**
     * This list contains all player joined listeners that should be alerted on
     * a player joined event.
     */
    private final List<PlayerJoinedListener> playerJoinedListeners = new CopyOnWriteArrayList<PlayerJoinedListener>();

    /**
     * Subscribe the given table created listener for table created events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeTableCreatedListener(TableCreatedListener listener) {
	tableCreatedListeners.add(listener);
    }

    /**
     * Unsubscribe the given table created listener for table created events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeTableCreatedListener(TableCreatedListener listener) {
	tableCreatedListeners.remove(listener);
    }

    /**
     * This list contains all table created listeners that should be alerted on
     * a table created.
     */
    private final List<TableCreatedListener> tableCreatedListeners = new CopyOnWriteArrayList<TableCreatedListener>();

    /**
     * Subscribe the given player left listener for player left events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerLeftListener(PlayerLeftListener listener) {
	playerLeftListeners.add(listener);
    }

    /**
     * Unsubscribe the given player left listener for player left events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribePlayerLeftListener(PlayerLeftListener listener) {
	playerLeftListeners.remove(listener);
    }

    /**
     * This list contains all player left listeners that should be alerted on a
     * player left.
     */
    private final List<PlayerLeftListener> playerLeftListeners = new CopyOnWriteArrayList<PlayerLeftListener>();

    /**
     * Subscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeServerMessageListener(ServerMessageListener listener) {
	serverMessageListeners.add(listener);
    }

    /**
     * Unsubscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeServerMessageListener(ServerMessageListener listener) {
	serverMessageListeners.remove(listener);
    }

    public void subscribeAllEventsListener(AllEventsListener listener) {
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

    public void unsubscribeAllEventsListener(AllEventsListener listener) {
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
    private final List<ServerMessageListener> serverMessageListeners = new CopyOnWriteArrayList<ServerMessageListener>();

    /***************************************************************************
     * all events listener
     **************************************************************************/

    AllEventsListener getAllEventsListener() {
	return allEventsListener;
    }

    private final AllEventsListener allEventsListener = new AllEventsListenerImpl();

    private class AllEventsListenerImpl implements AllEventsListener {

	public void onAllInEvent(AllInEvent event) {
	    for (AllInListener listener : allInListeners) {
		listener.onAllInEvent(event);
	    }
	}

	public void onBetEvent(BetEvent event) {
	    for (BetListener listener : betListeners) {
		listener.onBetEvent(event);
	    }

	}

	public void onBigBlindEvent(BigBlindEvent event) {
	    for (BigBlindListener listener : bigBlindListeners) {
		listener.onBigBlindEvent(event);
	    }

	}

	public void onCallEvent(CallEvent event) {
	    for (CallListener listener : callListeners) {
		listener.onCallEvent(event);
	    }
	}

	public void onCheckEvent(CheckEvent event) {
	    for (CheckListener listener : checkListeners) {
		listener.onCheckEvent(event);
	    }
	}

	public void onFoldEvent(FoldEvent event) {
	    for (FoldListener listener : foldListeners) {
		listener.onFoldEvent(event);
	    }

	}

	public void onRaiseEvent(RaiseEvent event) {
	    for (RaiseListener listener : raiseListeners) {
		listener.onRaiseEvent(event);
	    }
	}

	public void onSmallBlindEvent(SmallBlindEvent event) {
	    for (SmallBlindListener listener : smallBlindListeners) {
		listener.onSmallBlindEvent(event);
	    }
	}

	public void onNewPocketCardsEvent(NewPocketCardsEvent event) {
	    for (NewPocketCardsListener listener : newPocketCardsListeners) {
		listener.onNewPocketCardsEvent(event);
	    }

	}

	public void onNewCommunityCardsEvent(NewCommunityCardsEvent event) {
	    for (NewCommunityCardsListener listener : newCommunityCardsListeners) {
		listener.onNewCommunityCardsEvent(event);
	    }
	}

	public void onNewDealEvent(NewDealEvent event) {
	    for (NewDealListener listener : newDealListeners) {
		listener.onNewDealEvent(event);
	    }

	}

	public void onNewRoundEvent(NewRoundEvent event) {
	    for (NewRoundListener listener : newRoundListeners) {
		listener.onNewRoundEvent(event);
	    }

	}

	public void onNextPlayerEvent(NextPlayerEvent event) {
	    for (NextPlayerListener listener : nextPlayerListeners) {
		listener.onNextPlayerEvent(event);
	    }
	}

	public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event) {
	    for (PlayerJoinedGameListener listener : playerJoinedGameListeners) {
		listener.onPlayerJoinedGameEvent(event);
	    }
	}

	public void onShowHandEvent(ShowHandEvent event) {
	    for (ShowHandListener listener : showHandListeners) {
		listener.onShowHandEvent(event);
	    }
	}

	public void onWinnerEvent(WinnerEvent event) {
	    for (WinnerListener listener : winnerListeners) {
		listener.onWinnerEvent(event);
	    }
	}

	public void onPlayerLeftTableEvent(PlayerLeftTableEvent event) {
	    for (PlayerLeftTableListener listener : playerLeftTableListeners) {
		listener.onPlayerLeftTableEvent(event);
	    }

	}

	public void onGameMessageEvent(GameMessageEvent event) {
	    for (GameMessageListener listener : gameMessageListeners) {
		listener.onGameMessageEvent(event);
	    }
	}

	/***********************************************************************
	 * Server Events
	 **********************************************************************/

	public void onPlayerJoinedEvent(PlayerJoinedEvent event) {
	    for (PlayerJoinedListener listener : playerJoinedListeners) {
		listener.onPlayerJoinedEvent(event);
	    }

	}

	public void onTableCreatedEvent(TableCreatedEvent event) {
	    for (TableCreatedListener listener : tableCreatedListeners) {
		listener.onTableCreatedEvent(event);
	    }
	}

	public void onPlayerLeftEvent(PlayerLeftEvent event) {
	    for (PlayerLeftListener listener : playerLeftListeners) {
		listener.onPlayerLeftEvent(event);
	    }
	}

	public void onServerMessageEvent(ServerMessageEvent event) {
	    for (ServerMessageListener listener : serverMessageListeners) {
		listener.onServerMessageEvent(event);
	    }
	}
    }
}
