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

import org.cspoker.common.game.IllegalActionException;
import org.cspoker.common.game.elements.table.TableId;
import org.cspoker.common.game.events.AllEventsListener;
import org.cspoker.common.game.events.gameEvents.GameMessageListener;
import org.cspoker.common.game.events.gameEvents.NewCommunityCardsListener;
import org.cspoker.common.game.events.gameEvents.NewDealListener;
import org.cspoker.common.game.events.gameEvents.NewRoundListener;
import org.cspoker.common.game.events.gameEvents.NextPlayerListener;
import org.cspoker.common.game.events.gameEvents.PlayerJoinedGameListener;
import org.cspoker.common.game.events.gameEvents.PlayerLeftTableListener;
import org.cspoker.common.game.events.gameEvents.ShowHandListener;
import org.cspoker.common.game.events.gameEvents.WinnerListener;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.AllInListener;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.BetListener;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.BigBlindListener;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.CallListener;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.CheckListener;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.FoldListener;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.RaiseListener;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.SmallBlindListener;
import org.cspoker.common.game.events.gameEvents.privateEvents.NewPocketCardsListener;
import org.cspoker.common.game.events.serverEvents.PlayerJoinedListener;
import org.cspoker.common.game.events.serverEvents.PlayerLeftListener;
import org.cspoker.common.game.events.serverEvents.ServerMessageListener;
import org.cspoker.common.game.events.serverEvents.TableCreatedListener;

/**
 * An interface to define all possible invocations a player can do.
 *
 *
 * @author Kenzo
 *
 */
public interface PlayerCommunicationForGUI {

    /***************************************************************************
     * Player Actions
     **************************************************************************/

    /**
     * The player calls.
     *
     * To put into the pot an amount of money equal to the most recent bet or
     * raise.
     *
     * @throws IllegalActionException
     *                 [must] The player can not call in the current state.
     */
    public void call() throws IllegalActionException;

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
    public void bet(int amount) throws IllegalActionException;

    /**
     * The player folds.
     *
     * The player will not be able to take any actions in the coming rounds of
     * the current deal.
     *
     * @throws IllegalActionException
     *                 [must] The player can not fold in the current state.
     */
    public void fold() throws IllegalActionException;

    /**
     * The player checks.
     *
     * If there is no bet on the table and you do not wish to place a bet. You
     * may only check when there are no prior bets.
     *
     * @throws IllegalActionException
     *                 [must] The player can not check in the current state.
     */
    public void check() throws IllegalActionException;

    /**
     * The player raises the current bet with given amount.
     *
     * @param amount
     *                The amount to raise the current bet with.
     *
     * @throws IllegalActionException
     *                 [must] The player can not raise in the current state.
     */
    public void raise(int amount) throws IllegalActionException;

    /**
     * The player who the dealer-button has been dealt to can choose to start
     * the deal. From that moment, new players can not join the on-going deal.
     *
     * @throws IllegalActionException
     *                 [must] The player can not deal in the current state.
     */
    public void deal() throws IllegalActionException;

    /**
     * The player goes all-in.
     *
     * @throws IllegalActionException
     *                 [must] The player can not go all-in in the current state.
     */
    public void allIn() throws IllegalActionException;

    public void say(String message);

    /***************************************************************************
     * Leave/Join Game
     **************************************************************************/

    /**
     * Join the table with given table id.
     *
     * @pre The given id should be effective |id!=null
     * @throws IllegalActionException
     *                 [must] This actions is not a valid action in the current
     *                 state.
     */
    public void joinTable(TableId id) throws IllegalActionException;

    /**
     * Leave the table the player is sitting at.
     *
     * @throws IllegalActionException
     *                 [must] | The player is not seated at a table.
     */
    public void leaveTable() throws IllegalActionException;

    /***************************************************************************
     * Create/Start Game
     **************************************************************************/

    /**
     * This player creates a table.
     *
     * @throws IllegalActionException
     *                 [must] This actions is not a valid action in the current
     *                 state.
     */
    public TableId createTable() throws IllegalActionException;

    /**
     * This player starts the game. Only the player who has created the table
     * can start the game.
     *
     * @throws IllegalActionException
     *                 [must] This actions is not a valid action in the current
     *                 state.
     */
    public void startGame() throws IllegalActionException;


    /***************************************************************************
     * Game Events Publisher
     **************************************************************************/

    /***************************************************************************
     * Player Action Events Publisher
     **************************************************************************/

    /**
     * Subscribe the given all-in listener for all-in events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeAllInListener(AllInListener listener);

    /**
     * Unsubscribe the given all-in listener for all-in events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeAllInListener(AllInListener listener);

    /**
     * Subscribe the given bet listener for bet events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeBetListener(BetListener listener);

    /**
     * Unsubscribe the given bet listener for bet events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeBetListener(BetListener listener);

    /**
     * Subscribe the given big blind listener for big blind events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeBigBlindListener(BigBlindListener listener);

    /**
     * Unsubscribe the given big blind listener for big blind events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeBigBlindListener(BigBlindListener listener);

    /**
     * Subscribe the given call listener for call events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeCallListener(CallListener listener);

    /**
     * Unsubscribe the given call listener for call events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeCallListener(CallListener listener);

    /**
     * Subscribe the given check listener for check events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeCheckListener(CheckListener listener);

    /**
     * Unsubscribe the given check listener for check events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeCheckListener(CheckListener listener);


    /**
     * Subscribe the given fold listener for fold events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeFoldListener(FoldListener listener);

    /**
     * Unsubscribe the given fold listener for fold events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeFoldListener(FoldListener listener);

    /**
     * Subscribe the given raise listener for raise events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeRaiseListener(RaiseListener listener);

    /**
     * Unsubscribe the given raise listener for raise events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeRaiseListener(RaiseListener listener);

    /**
     * Subscribe the given small blind listener for small blind events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeSmallBlindListener(SmallBlindListener listener);

    /**
     * Unsubscribe the given small blind listener for small blind events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeSmallBlindListener(SmallBlindListener listener);

    /***************************************************************************
     * Private Events Publisher
     **************************************************************************/

    /**
     * Subscribe the given new pocket cards listener for new pocket cards
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewPocketCardsListener(NewPocketCardsListener listener);

    /**
     * Unsubscribe the given new pocket cards listener for new pocket cards
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeNewPocketCardsListener(
	    NewPocketCardsListener listener);

    /***************************************************************************
     * Other Game Events Publisher
     **************************************************************************/

    /**
     * Subscribe the given new common cards listener for new common cards
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewCommonCardsListener(
	    NewCommunityCardsListener listener);

    /**
     * Unsubscribe the given new common cards listener for new common cards
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeNewCommonCardsListener(
	    NewCommunityCardsListener listener);

    /**
     * Subscribe the given new deal listener for new deal events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewDealListener(NewDealListener listener);

    /**
     * Unsubscribe the given new deal listener for new deal events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeNewDealListener(NewDealListener listener);

    /**
     * Subscribe the given new round listener for new round events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNewRoundListener(NewRoundListener listener);

    /**
     * Unsubscribe the given new round listener for new round events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeNewRoundListener(NewRoundListener listener);

    /**
     * Subscribe the given next player listener for next player events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeNextPlayerListener(NextPlayerListener listener);

    /**
     * Unsubscribe the given next player listener for next player events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeNextPlayerListener(NextPlayerListener listener);

    /**
     * Subscribe the given player joined game listener for player joined game
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerJoinedGameListener(
	    PlayerJoinedGameListener listener);

    /**
     * Unsubscribe the given player joined game listener for player joined game
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribePlayerJoinedGameListener(
	    PlayerJoinedGameListener listener);

//    /**
//     * Subscribe the given pot changed listener for pot changed events.
//     *
//     * @param listener
//     *                The listener to subscribe.
//     */
//    public void subscribePotChangedListener(PotChangedListener listener);
//
//    /**
//     * Unsubscribe the given pot changed listener for pot changed events.
//     *
//     * @param listener
//     *                The listener to unsubscribe.
//     */
//    public void unsubscribePotChangedListener(PotChangedListener listener);

    /**
     * Subscribe the given show hand listener for show hand events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeShowHandListener(ShowHandListener listener);

    /**
     * Unsubscribe the given show hand listener for show hand events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeShowHandListener(ShowHandListener listener);

//    /**
//     * Subscribe the given stack changed listener for stack changed events.
//     *
//     * @param listener
//     *                The listener to subscribe.
//     */
//    public void subscribeStackChangedListener(StackChangedListener listener);
//
//    /**
//     * Unsubscribe the given stack changed listener for stack changed events.
//     *
//     * @param listener
//     *                The listener to unsubscribe.
//     */
//    public void unsubscribeStackChangedListener(StackChangedListener listener);

    /**
     * Subscribe the given winner listener for winner events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeWinnerListener(WinnerListener listener);

    /**
     * Unsubscribe the given winner listener for winner events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeWinnerListener(WinnerListener listener);

    /**
     * Subscribe the given player left table listener for player left table
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerLeftTableListener(
	    PlayerLeftTableListener listener);

    /**
     * Unsubscribe the given player left table listener for player left table
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribePlayerLeftTableListener(
	    PlayerLeftTableListener listener);

    /**
     * Subscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeGameMessageListener(GameMessageListener listener);

    /**
     * Unsubscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeGameMessageListener(GameMessageListener listener);

    /***************************************************************************
     * Server Events Publisher
     **************************************************************************/

    /**
     * Subscribe the given player joined listener for player joined events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerJoinedListener(PlayerJoinedListener listener);

    /**
     * Unsubscribe the given player joined listener for player joined events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribePlayerJoinedListener(PlayerJoinedListener listener);

    /**
     * Subscribe the given table created listener for table created events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeTableCreatedListener(TableCreatedListener listener);

    /**
     * Unsubscribe the given table created listener for table created events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeTableCreatedListener(TableCreatedListener listener);

    /**
     * Subscribe the given player left listener for player left events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribePlayerLeftListener(PlayerLeftListener listener);

    /**
     * Unsubscribe the given player left listener for player left events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribePlayerLeftListener(PlayerLeftListener listener);

    /**
     * Subscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeServerMessageListener(ServerMessageListener listener);

    /**
     * Unsubscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    public void unsubscribeServerMessageListener(ServerMessageListener listener);

    /**
     * Subscribe the given all events listener for all events a player can
     * receive.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void subscribeAllEventsListener(AllEventsListener listener);

    /**
     * Unsubscribe the given all events listener from all events a player can
     * receive.
     *
     * @param listener
     *                The listener to subscribe.
     */
    public void unsubscribeAllEventsListener(AllEventsListener listener);
}