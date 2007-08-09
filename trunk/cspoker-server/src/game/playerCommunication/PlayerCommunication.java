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
package game.playerCommunication;

import game.TableId;
import game.events.GameEvent;
import game.gameControl.actions.IllegalActionException;
import game.player.Player;

import java.util.List;

/**
 * An interface to define all possible invocations a player can do.
 *
 *
 * @author Kenzo
 *
 */
public interface PlayerCommunication {

	/**********************************************************
	 * Player Actions
	 **********************************************************/

	/**
	 * The player calls.
	 *
	 * To put into the pot an amount of money equal to
	 * the most recent bet or raise.
	 *
	 * @throws	IllegalActionException [must]
	 * 			The player can not call in the current state.
	 */
	public void call() throws IllegalActionException;

	/**
	 * The player bets.
	 *
	 * The player puts money in the pot.
	 *
	 * @param 	amount
	 * 			The amount of the bet.
	 * @throws 	IllegalActionException [must]
	 * 			The player can not bet in the current state.
	 */
	public void bet(int amount) throws IllegalActionException;

	/**
	 * The player folds.
	 *
	 * The player will not be able to take any actions
	 * in the coming rounds of the current deal.
	 *
	 * @throws 	IllegalActionException [must]
	 * 			The player can not fold in the current state.
	 */
	public void fold() throws IllegalActionException;

	/**
	 * The player checks.
	 *
	 * If there is no bet on the table and you do not wish to place a bet.
	 * You may only check when there are no prior bets.
	 *
	 * @throws 	IllegalActionException [must]
	 * 			The player can not check in the current state.
	 */
	public void check() throws IllegalActionException;

	/**
	 * The player raises the current bet with given amount.
	 *
	 * @param 	amount
	 * 			The amount to raise the current bet with.
	 *
	 * @throws 	IllegalActionException [must]
	 * 			The player can not raise in the current state.
	 */
	public void raise(int amount) throws IllegalActionException;

	/**
	 * The player who the dealer-button has been dealt to
	 * can choose to start the deal.
	 * From that moment, new players can not join the on-going deal.
	 *
	 * @throws 	IllegalActionException [must]
	 * 			The player can not deal in the current state.
	 */
	public void deal() throws IllegalActionException;

	/**
	 * The player goes all-in.
	 *
	 * @throws 	IllegalActionException [must]
	 * 			The player can not go all-in in the current state.
	 */
	public void allIn() throws IllegalActionException;

	/**********************************************************
	 * Leave/Join Game
	 **********************************************************/

	/**
	 * Join the table with given table id.
	 *
	 * @pre 	The given id should be effective
	 *			|id!=null
	 * @throws  IllegalActionException [can]
	 *          This actions is not a valid action in the current state.
	 */
	public void join(TableId id) throws IllegalActionException;

	/**
	 * Leave the table the player is sitting at.
	 *
	 * @throws	IllegalActionException [must]
	 * 			| The player is not seated at a table.
	 */
	public void leaveTable() throws IllegalActionException;

	/**********************************************************
	 * Create/Start Game
	 **********************************************************/

	/**
	 * This player creates a table.
	 *
	 * @throws  IllegalActionException [can]
	 *          This actions is not a valid action in the current state.
	 */
	public TableId createTable() throws IllegalActionException;

	/**
	 * This player starts the game.
	 * Only the player who has created the table can start the game.
	 *
	 * @throws  IllegalActionException [can]
	 *          This actions is not a valid action in the current state.
	 */
	public void startGame() throws IllegalActionException;

	/**********************************************************
	 * Actions list
	 **********************************************************/

	/**
	 * Returns the latest game events.
	 *
	 * All method calls are idempotent, which means that calling the method,
	 * does not change any internal state and can be called multiple
	 * times without changing the result. (i.e. retry after network error)
	 *
	 */
	public List<GameEvent> getLatestGameEvents() throws IllegalActionException;

	/**
	 * Returns the latest game events,
	 * while acknowledging until the given ack.
	 *
	 * @param ack
	 * @return
	 * @throws IllegalActionException
	 */
	public List<GameEvent> getLatestGameEventsAndAck(long ack) throws IllegalActionException;

}