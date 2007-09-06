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

import org.cspoker.server.game.TableId;
import org.cspoker.server.game.gameControl.IllegalActionException;

/**
 * An abstract class to represent player communication states.
 *
 * This class is only package-accessible.
 *
 * @author Kenzo
 *
 */
abstract class PlayerCommunicationState implements PlayerCommunication{

	/**
	 * The variable containing the player communication.
	 */
	protected final PlayerCommunicationImpl playerCommunication;

	/**********************************************************
	 * Constructor
	 **********************************************************/

	public PlayerCommunicationState(PlayerCommunicationImpl playerCommunication){
		this.playerCommunication = playerCommunication;
	}

	/**********************************************************
	 * Player Actions
	 **********************************************************/

	public void bet(int amount) throws IllegalActionException {
		throw new IllegalActionException("Bet is not a valid action. "+getStdErrorMessage());
	}

	public void raise(int amount) throws IllegalActionException {
		throw new IllegalActionException("Raise is not a valid action. "+getStdErrorMessage());
	}

	public void call() throws IllegalActionException {
		throw new IllegalActionException("Call is not a valid action. "+getStdErrorMessage());
	}
	public void check() throws IllegalActionException {
		throw new IllegalActionException("Check is not a valid action. "+getStdErrorMessage());
	}
	public void deal() throws IllegalActionException {
		throw new IllegalActionException("Deal is not a valid action. "+getStdErrorMessage());

	}
	public void fold() throws IllegalActionException {
		throw new IllegalActionException("Fold is not a valid action. "+getStdErrorMessage());
	}
	public void allIn() throws IllegalActionException {
		throw new IllegalActionException("Going all-in is not a valid action. "+getStdErrorMessage());

	}

	public void say(String message){

	}

	/**********************************************************
	 * Leave/Join Game
	 **********************************************************/

	/**
	 * Join the table with given table id.
	 *
	 * @pre 	The given id should be effective.
	 *			|id!=null
	 * @throws  IllegalActionException [must]
	 *          This actions is not a valid action in the current state.
	 */
	public void join(TableId id) throws IllegalActionException {
		throw new IllegalActionException("Joining "+id+"is not a valid action. " +getStdErrorMessage());
	}
	public void leaveTable() throws IllegalActionException {
		throw new IllegalActionException("Leaving the table is not a valid action. " +getStdErrorMessage());
	}


	/**********************************************************
	 * Create/Start Game
	 **********************************************************/
	public TableId createTable() throws IllegalActionException {
		throw new IllegalActionException("Creating a table is not a valid action. " +getStdErrorMessage());
	}
	public void startGame() throws IllegalActionException {
		throw new IllegalActionException("Starting a game is not a valid action. "+getStdErrorMessage());
	}

	/**********************************************************
	 * Actions list
	 **********************************************************/
	public Events getLatestEvents() throws IllegalActionException {
		throw new IllegalActionException("Requesting the latest game events is not a valid action. "+getStdErrorMessage());
	}
	public Events getLatestEventsAndAck(int ack) throws IllegalActionException {
		throw new IllegalActionException("Requesting the latest game events is not a valid action. "+getStdErrorMessage());
	}

	protected abstract String getStdErrorMessage();

}
