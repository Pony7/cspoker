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

import org.cspoker.common.game.elements.table.TableId;
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


/**
 * An interface to define all possible invocations a player can do.
 *
 *
 * @author Kenzo
 *
 */
public interface PlayerCommunication extends RemotePlayerCommunication{

    
    void destruct();
    
    /**
     * The player calls.
     *
     * To put into the pot an amount of money equal to the most recent bet or
     * raise.
     *
     * @throws IllegalActionException
     *                 [must] The player can not call in the current state.
     */
    void call() throws IllegalActionException;

    /**
     * The player bets.
     *
     * The player puts money in the pot.
     *
     * @param amount
     *                The amount of the bet.
     * @throws IllegalActionException
     *                 [must] The player can not bet in the current state.
     */
    void bet(int amount) throws IllegalActionException;

    /**
     * The player folds.
     *
     * The player will not be able to take any actions in the coming rounds of
     * the current deal.
     *
     * @throws IllegalActionException
     *                 [must] The player can not fold in the current state.
     */
    void fold() throws IllegalActionException;

    /**
     * The player checks.
     *
     * If there is no bet on the table and you do not wish to place a bet. You
     * may only check when there are no prior bets.
     *
     * @throws IllegalActionException
     *                 [must] The player can not check in the current state.
     */
    void check() throws IllegalActionException;

    /**
     * The player raises the current bet with given amount.
     *
     * @param amount
     *                The amount to raise the current bet with.
     *
     * @throws IllegalActionException
     *                 [must] The player can not raise in the current state.
     */
    void raise(int amount) throws IllegalActionException;

    /**
     * The player who the dealer-button has been dealt to can choose to start
     * the deal. From that moment, new players can not join the on-going deal.
     *
     * @throws IllegalActionException
     *                 [must] The player can not deal in the current state.
     */
    void deal() throws IllegalActionException;

    /**
     * The player goes all-in.
     *
     * @throws IllegalActionException
     *                 [must] The player can not go all-in in the current state.
     */
    void allIn() throws IllegalActionException;

    void say(String message);

    /**
     * Join the table with given table id.
     *
     * @pre The given id should be effective |id!=null
     * @throws IllegalActionException
     *                 [must] This actions is not a valid action in the current
     *                 state.
     */
    void joinTable(TableId id) throws IllegalActionException;

    /**
     * Leave the table the player is sitting at.
     *
     * @throws IllegalActionException
     *                 [must] | The player is not seated at a table.
     */
    void leaveTable() throws IllegalActionException;

    /**
     * This player creates a table.
     *
     * @throws IllegalActionException
     *                 [must] This actions is not a valid action in the current
     *                 state.
     */
    TableId createTable() throws IllegalActionException;

    /**
     * This player starts the game. Only the player who has created the table
     * can start the game.
     *
     * @throws IllegalActionException
     *                 [must] This actions is not a valid action in the current
     *                 state.
     */
    void startGame() throws IllegalActionException;

    /**
     * Subscribe the given all-in listener for all-in events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeAllInListener(RemoteAllInListener listener);

    /**
     * Unsubscribe the given all-in listener for all-in events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeAllInListener(RemoteAllInListener listener);

    /**
     * Subscribe the given bet listener for bet events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeBetListener(RemoteBetListener listener);

    /**
     * Unsubscribe the given bet listener for bet events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeBetListener(RemoteBetListener listener);

    /**
     * Subscribe the given big blind listener for big blind events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeBigBlindListener(RemoteBigBlindListener listener);

    /**
     * Unsubscribe the given big blind listener for big blind events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeBigBlindListener(RemoteBigBlindListener listener);

    /**
     * Subscribe the given call listener for call events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeCallListener(RemoteCallListener listener);

    /**
     * Unsubscribe the given call listener for call events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeCallListener(RemoteCallListener listener);

    /**
     * Subscribe the given check listener for check events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeCheckListener(RemoteCheckListener listener);

    /**
     * Unsubscribe the given check listener for check events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeCheckListener(RemoteCheckListener listener);

    /**
     * Subscribe the given fold listener for fold events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeFoldListener(RemoteFoldListener listener);

    /**
     * Unsubscribe the given fold listener for fold events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeFoldListener(RemoteFoldListener listener);

    /**
     * Subscribe the given raise listener for raise events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeRaiseListener(RemoteRaiseListener listener);

    /**
     * Unsubscribe the given raise listener for raise events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeRaiseListener(RemoteRaiseListener listener);

    /**
     * Subscribe the given small blind listener for small blind events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeSmallBlindListener(RemoteSmallBlindListener listener);

    /**
     * Unsubscribe the given small blind listener for small blind events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeSmallBlindListener(RemoteSmallBlindListener listener);

    /**
     * Subscribe the given new pocket cards listener for new pocket cards
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeNewPocketCardsListener(RemoteNewPocketCardsListener listener);

    /**
     * Unsubscribe the given new pocket cards listener for new pocket cards
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeNewPocketCardsListener(RemoteNewPocketCardsListener listener);

    /**
     * Subscribe the given new common cards listener for new common cards
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeNewCommonCardsListener(RemoteNewCommunityCardsListener listener);

    /**
     * Unsubscribe the given new common cards listener for new common cards
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeNewCommonCardsListener(RemoteNewCommunityCardsListener listener);

    /**
     * Subscribe the given new deal listener for new deal events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeNewDealListener(RemoteNewDealListener listener);

    /**
     * Unsubscribe the given new deal listener for new deal events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeNewDealListener(RemoteNewDealListener listener);

    /**
     * Subscribe the given new round listener for new round events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeNewRoundListener(RemoteNewRoundListener listener);

    /**
     * Unsubscribe the given new round listener for new round events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeNewRoundListener(RemoteNewRoundListener listener);

    /**
     * Subscribe the given next player listener for next player events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeNextPlayerListener(RemoteNextPlayerListener listener);

    /**
     * Unsubscribe the given next player listener for next player events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeNextPlayerListener(RemoteNextPlayerListener listener);

    /**
     * Subscribe the given player joined game listener for player joined game
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribePlayerJoinedGameListener(RemotePlayerJoinedGameListener listener);

    /**
     * Unsubscribe the given player joined game listener for player joined game
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribePlayerJoinedGameListener(RemotePlayerJoinedGameListener listener);

    /**
     * Subscribe the given show hand listener for show hand events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeShowHandListener(RemoteShowHandListener listener);

    /**
     * Unsubscribe the given show hand listener for show hand events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeShowHandListener(RemoteShowHandListener listener);

    /**
     * Subscribe the given winner listener for winner events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeWinnerListener(RemoteWinnerListener listener);

    /**
     * Unsubscribe the given winner listener for winner events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeWinnerListener(RemoteWinnerListener listener);

    /**
     * Subscribe the given player left table listener for player left table
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribePlayerLeftTableListener(RemotePlayerLeftTableListener listener);

    /**
     * Unsubscribe the given player left table listener for player left table
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribePlayerLeftTableListener(RemotePlayerLeftTableListener listener);

    /**
     * Subscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeGameMessageListener(RemoteGameMessageListener listener);

    /**
     * Unsubscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeGameMessageListener(RemoteGameMessageListener listener);

    /**
     * Subscribe the given player joined listener for player joined events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribePlayerJoinedListener(RemotePlayerJoinedListener listener);

    /**
     * Unsubscribe the given player joined listener for player joined events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribePlayerJoinedListener(RemotePlayerJoinedListener listener);

    /**
     * Subscribe the given table created listener for table created events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeTableCreatedListener(RemoteTableCreatedListener listener);

    /**
     * Unsubscribe the given table created listener for table created events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeTableCreatedListener(RemoteTableCreatedListener listener);

    /**
     * Subscribe the given player left listener for player left events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribePlayerLeftListener(RemotePlayerLeftListener listener);

    /**
     * Unsubscribe the given player left listener for player left events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribePlayerLeftListener(RemotePlayerLeftListener listener);

    /**
     * Subscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeServerMessageListener(RemoteServerMessageListener listener);

    /**
     * Unsubscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeServerMessageListener(RemoteServerMessageListener listener);

    /**
     * Subscribe the given all events listener for all events a player can
     * receive.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeAllEventsListener(RemoteAllEventsListener listener);

    /**
     * Unsubscribe the given all events listener from all events a player can
     * receive.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void unsubscribeAllEventsListener(RemoteAllEventsListener listener);
}