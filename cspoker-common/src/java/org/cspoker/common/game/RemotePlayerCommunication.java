package org.cspoker.common.game;

import java.rmi.Remote;
import java.rmi.RemoteException;

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

public interface RemotePlayerCommunication extends Remote{

    /**
     * The player calls.
     *
     * To put into the pot an amount of money equal to the most recent bet or
     * raise.
     *
     * @throws IllegalActionException
     *                 [must] The player can not call in the current state.
     */
    void call() throws IllegalActionException, RemoteException;

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
    void bet(int amount) throws IllegalActionException, RemoteException;

    /**
     * The player folds.
     *
     * The player will not be able to take any actions in the coming rounds of
     * the current deal.
     *
     * @throws IllegalActionException
     *                 [must] The player can not fold in the current state.
     */
    void fold() throws IllegalActionException, RemoteException;

    /**
     * The player checks.
     *
     * If there is no bet on the table and you do not wish to place a bet. You
     * may only check when there are no prior bets.
     *
     * @throws IllegalActionException
     *                 [must] The player can not check in the current state.
     */
    void check() throws IllegalActionException, RemoteException;

    /**
     * The player raises the current bet with given amount.
     *
     * @param amount
     *                The amount to raise the current bet with.
     *
     * @throws IllegalActionException
     *                 [must] The player can not raise in the current state.
     */
    void raise(int amount) throws IllegalActionException, RemoteException;

    /**
     * The player who the dealer-button has been dealt to can choose to start
     * the deal. From that moment, new players can not join the on-going deal.
     *
     * @throws IllegalActionException
     *                 [must] The player can not deal in the current state.
     */
    void deal() throws IllegalActionException, RemoteException;

    /**
     * The player goes all-in.
     *
     * @throws IllegalActionException
     *                 [must] The player can not go all-in in the current state.
     */
    void allIn() throws IllegalActionException, RemoteException;

    void say(String message) throws RemoteException;

    /**
     * Join the table with given table id.
     *
     * @pre The given id should be effective |id!=null
     * @throws IllegalActionException
     *                 [must] This actions is not a valid action in the current
     *                 state.
     */
    void joinTable(TableId id) throws IllegalActionException, RemoteException;

    /**
     * Leave the table the player is sitting at.
     *
     * @throws IllegalActionException
     *                 [must] | The player is not seated at a table.
     */
    void leaveTable() throws IllegalActionException, RemoteException;

    /**
     * This player creates a table.
     *
     * @throws IllegalActionException
     *                 [must] This actions is not a valid action in the current
     *                 state.
     */
    TableId createTable() throws IllegalActionException, RemoteException;

    /**
     * This player starts the game. Only the player who has created the table
     * can start the game.
     *
     * @throws IllegalActionException
     *                 [must] This actions is not a valid action in the current
     *                 state.
     */
    void startGame() throws IllegalActionException, RemoteException;

    /**
     * Subscribe the given all-in listener for all-in events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeAllInListener(AllInListener listener) throws RemoteException;

    /**
     * Unsubscribe the given all-in listener for all-in events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeAllInListener(AllInListener listener) throws RemoteException;

    /**
     * Subscribe the given bet listener for bet events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeBetListener(BetListener listener) throws RemoteException;

    /**
     * Unsubscribe the given bet listener for bet events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeBetListener(BetListener listener) throws RemoteException;

    /**
     * Subscribe the given big blind listener for big blind events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeBigBlindListener(BigBlindListener listener) throws RemoteException;

    /**
     * Unsubscribe the given big blind listener for big blind events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeBigBlindListener(BigBlindListener listener) throws RemoteException;

    /**
     * Subscribe the given call listener for call events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeCallListener(CallListener listener) throws RemoteException;

    /**
     * Unsubscribe the given call listener for call events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeCallListener(CallListener listener) throws RemoteException;

    /**
     * Subscribe the given check listener for check events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeCheckListener(CheckListener listener) throws RemoteException;

    /**
     * Unsubscribe the given check listener for check events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeCheckListener(CheckListener listener) throws RemoteException;

    /**
     * Subscribe the given fold listener for fold events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeFoldListener(FoldListener listener) throws RemoteException;

    /**
     * Unsubscribe the given fold listener for fold events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeFoldListener(FoldListener listener) throws RemoteException;

    /**
     * Subscribe the given raise listener for raise events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeRaiseListener(RaiseListener listener) throws RemoteException;

    /**
     * Unsubscribe the given raise listener for raise events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeRaiseListener(RaiseListener listener) throws RemoteException;

    /**
     * Subscribe the given small blind listener for small blind events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeSmallBlindListener(SmallBlindListener listener) throws RemoteException;

    /**
     * Unsubscribe the given small blind listener for small blind events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeSmallBlindListener(SmallBlindListener listener) throws RemoteException;

    /**
     * Subscribe the given new pocket cards listener for new pocket cards
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeNewPocketCardsListener(NewPocketCardsListener listener) throws RemoteException;

    /**
     * Unsubscribe the given new pocket cards listener for new pocket cards
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeNewPocketCardsListener(NewPocketCardsListener listener) throws RemoteException;

    /**
     * Subscribe the given new common cards listener for new common cards
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeNewCommonCardsListener(NewCommunityCardsListener listener) throws RemoteException;

    /**
     * Unsubscribe the given new common cards listener for new common cards
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeNewCommonCardsListener(NewCommunityCardsListener listener) throws RemoteException;

    /**
     * Subscribe the given new deal listener for new deal events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeNewDealListener(NewDealListener listener) throws RemoteException;

    /**
     * Unsubscribe the given new deal listener for new deal events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeNewDealListener(NewDealListener listener) throws RemoteException;

    /**
     * Subscribe the given new round listener for new round events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeNewRoundListener(NewRoundListener listener) throws RemoteException;

    /**
     * Unsubscribe the given new round listener for new round events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeNewRoundListener(NewRoundListener listener) throws RemoteException;

    /**
     * Subscribe the given next player listener for next player events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeNextPlayerListener(NextPlayerListener listener) throws RemoteException;

    /**
     * Unsubscribe the given next player listener for next player events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeNextPlayerListener(NextPlayerListener listener) throws RemoteException;

    /**
     * Subscribe the given player joined game listener for player joined game
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribePlayerJoinedGameListener(PlayerJoinedGameListener listener) throws RemoteException;

    /**
     * Unsubscribe the given player joined game listener for player joined game
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribePlayerJoinedGameListener(PlayerJoinedGameListener listener) throws RemoteException;

    /**
     * Subscribe the given show hand listener for show hand events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeShowHandListener(ShowHandListener listener) throws RemoteException;

    /**
     * Unsubscribe the given show hand listener for show hand events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeShowHandListener(ShowHandListener listener) throws RemoteException;

    /**
     * Subscribe the given winner listener for winner events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeWinnerListener(WinnerListener listener) throws RemoteException;

    /**
     * Unsubscribe the given winner listener for winner events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeWinnerListener(WinnerListener listener) throws RemoteException;

    /**
     * Subscribe the given player left table listener for player left table
     * events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribePlayerLeftTableListener(PlayerLeftTableListener listener) throws RemoteException;

    /**
     * Unsubscribe the given player left table listener for player left table
     * events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribePlayerLeftTableListener(PlayerLeftTableListener listener) throws RemoteException;

    /**
     * Subscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeGameMessageListener(GameMessageListener listener) throws RemoteException;

    /**
     * Unsubscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeGameMessageListener(GameMessageListener listener) throws RemoteException;

    /**
     * Subscribe the given player joined listener for player joined events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribePlayerJoinedListener(PlayerJoinedListener listener) throws RemoteException;

    /**
     * Unsubscribe the given player joined listener for player joined events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribePlayerJoinedListener(PlayerJoinedListener listener) throws RemoteException;

    /**
     * Subscribe the given table created listener for table created events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeTableCreatedListener(TableCreatedListener listener) throws RemoteException;

    /**
     * Unsubscribe the given table created listener for table created events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeTableCreatedListener(TableCreatedListener listener) throws RemoteException;

    /**
     * Subscribe the given player left listener for player left events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribePlayerLeftListener(PlayerLeftListener listener) throws RemoteException;

    /**
     * Unsubscribe the given player left listener for player left events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribePlayerLeftListener(PlayerLeftListener listener) throws RemoteException;

    /**
     * Subscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeServerMessageListener(ServerMessageListener listener) throws RemoteException;

    /**
     * Unsubscribe the given message listener for message events.
     *
     * @param listener
     *                The listener to unsubscribe.
     */
    void unsubscribeServerMessageListener(ServerMessageListener listener) throws RemoteException;

    /**
     * Subscribe the given all events listener for all events a player can
     * receive.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeAllEventsListener(AllEventsListener listener) throws RemoteException;

    /**
     * Unsubscribe the given all events listener from all events a player can
     * receive.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void unsubscribeAllEventsListener(AllEventsListener listener) throws RemoteException;

}