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
package org.cspoker.common.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.common.game.eventlisteners.AllEventsListener;
import org.cspoker.common.game.eventlisteners.game.GameMessageListener;
import org.cspoker.common.game.eventlisteners.game.NewCommunityCardsListener;
import org.cspoker.common.game.eventlisteners.game.NewDealListener;
import org.cspoker.common.game.eventlisteners.game.NewRoundListener;
import org.cspoker.common.game.eventlisteners.game.NextPlayerListener;
import org.cspoker.common.game.eventlisteners.game.PlayerJoinedGameListener;
import org.cspoker.common.game.eventlisteners.game.PlayerLeftTableListener;
import org.cspoker.common.game.eventlisteners.game.ShowHandListener;
import org.cspoker.common.game.eventlisteners.game.WinnerListener;
import org.cspoker.common.game.eventlisteners.game.actions.AllInListener;
import org.cspoker.common.game.eventlisteners.game.actions.BetListener;
import org.cspoker.common.game.eventlisteners.game.actions.BigBlindListener;
import org.cspoker.common.game.eventlisteners.game.actions.CallListener;
import org.cspoker.common.game.eventlisteners.game.actions.CheckListener;
import org.cspoker.common.game.eventlisteners.game.actions.FoldListener;
import org.cspoker.common.game.eventlisteners.game.actions.RaiseListener;
import org.cspoker.common.game.eventlisteners.game.actions.SmallBlindListener;
import org.cspoker.common.game.eventlisteners.game.privatelistener.NewPocketCardsListener;
import org.cspoker.common.game.eventlisteners.server.PlayerJoinedListener;
import org.cspoker.common.game.eventlisteners.server.PlayerLeftListener;
import org.cspoker.common.game.eventlisteners.server.ServerMessageListener;
import org.cspoker.common.game.eventlisteners.server.TableCreatedListener;

public abstract class AbstractPlayerCommunication implements PlayerCommunication{

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
    protected final List<AllInListener> allInListeners = new CopyOnWriteArrayList<AllInListener>();

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
    protected final List<BetListener> betListeners = new CopyOnWriteArrayList<BetListener>();

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
    protected final List<BigBlindListener> bigBlindListeners = new CopyOnWriteArrayList<BigBlindListener>();

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
    protected final List<CallListener> callListeners = new CopyOnWriteArrayList<CallListener>();

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
    protected final List<CheckListener> checkListeners = new CopyOnWriteArrayList<CheckListener>();

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
    protected final List<FoldListener> foldListeners = new CopyOnWriteArrayList<FoldListener>();

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
    protected final List<RaiseListener> raiseListeners = new CopyOnWriteArrayList<RaiseListener>();

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
    protected final List<SmallBlindListener> smallBlindListeners = new CopyOnWriteArrayList<SmallBlindListener>();

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
    protected final List<NewPocketCardsListener> newPocketCardsListeners = new CopyOnWriteArrayList<NewPocketCardsListener>();

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
    protected final List<NewCommunityCardsListener> newCommunityCardsListeners = new CopyOnWriteArrayList<NewCommunityCardsListener>();

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
    protected final List<NewDealListener> newDealListeners = new CopyOnWriteArrayList<NewDealListener>();

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
    protected final List<NewRoundListener> newRoundListeners = new CopyOnWriteArrayList<NewRoundListener>();

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
    protected final List<NextPlayerListener> nextPlayerListeners = new CopyOnWriteArrayList<NextPlayerListener>();

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
    protected final List<PlayerJoinedGameListener> playerJoinedGameListeners = new CopyOnWriteArrayList<PlayerJoinedGameListener>();

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
    protected final List<ShowHandListener> showHandListeners = new CopyOnWriteArrayList<ShowHandListener>();

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
    protected final List<WinnerListener> winnerListeners = new CopyOnWriteArrayList<WinnerListener>();

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
    protected final List<PlayerLeftTableListener> playerLeftTableListeners = new CopyOnWriteArrayList<PlayerLeftTableListener>();

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
    protected final List<GameMessageListener> gameMessageListeners = new CopyOnWriteArrayList<GameMessageListener>();

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
    protected final List<PlayerJoinedListener> playerJoinedListeners = new CopyOnWriteArrayList<PlayerJoinedListener>();

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
    protected final List<TableCreatedListener> tableCreatedListeners = new CopyOnWriteArrayList<TableCreatedListener>();

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
    protected final List<PlayerLeftListener> playerLeftListeners = new CopyOnWriteArrayList<PlayerLeftListener>();

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
    
    /**
     * This list contains all message listeners that should be alerted on a
     * message.
     */
    protected final List<ServerMessageListener> serverMessageListeners = new CopyOnWriteArrayList<ServerMessageListener>();

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

}
