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

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.cspoker.common.game.elements.table.TableId;
import org.cspoker.common.game.eventlisteners.RemoteAllEventsListener;

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
     * Subscribe the given all events listener for all events a player can
     * receive.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void subscribeAllEventsListener(RemoteAllEventsListener listener) throws RemoteException;

    /**
     * Unsubscribe the given all events listener from all events a player can
     * receive.
     *
     * @param listener
     *                The listener to subscribe.
     */
    void unsubscribeAllEventsListener(RemoteAllEventsListener listener) throws RemoteException;

}