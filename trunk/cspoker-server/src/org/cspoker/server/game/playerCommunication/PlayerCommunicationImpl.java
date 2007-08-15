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

import org.cspoker.server.game.TableId;
import org.cspoker.server.game.events.GameEvent;
import org.cspoker.server.game.gameControl.actions.IllegalActionException;
import org.cspoker.server.game.player.Player;

/**
 * A class of player communications.
 *
 * It's the interface to all game control actions.
 *
 * @author Kenzo
 *
 */
public class PlayerCommunicationImpl implements PlayerCommunication {

	/**********************************************************
	 * Variables
	 **********************************************************/

	/**
	 * The variable containing the player.
	 */
	private final Player player;

	/**
	 * This variable contains the player communication state.
	 */
	private PlayerCommunicationState state;


	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Construct a new player communication with given player.
	 *
	 * @param 	player
	 * 			The given player
	 */
	public PlayerCommunicationImpl(Player player){
		this.player = player;
		state = new InitialState(this);

		//TODO Temporary...
		PlayerCommunicationManager.addPlayerCommunication(player.getId(), this);
	}

	/**
	 * Returns the player contained in this player communication.
	 *
	 * @return The player contained in this player communication.
	 */
	public Player getPlayer(){
		return player;
	}

	/**********************************************************
	 * Player Actions
	 **********************************************************/

	public void call() throws IllegalActionException{
		state.call();
	}

	public void bet(int amount) throws IllegalActionException{
		state.bet(amount);
	}

	public void fold() throws IllegalActionException{
		state.fold();
	}

	public void check() throws IllegalActionException{
		state.check();
	}

	public void raise(int amount) throws IllegalActionException{
		state.raise(amount);
	}

	public void deal() throws IllegalActionException{
		state.deal();
	}

	public void allIn() throws IllegalActionException {
		state.allIn();
	}

	/**********************************************************
	 * Leave/Join Game
	 **********************************************************/

	/**
	 * Join the table with given table id.
	 *
	 * @pre 	The given id should be effective.
	 *			|id!=null
	 * @throws  IllegalActionException [can]
	 *          This actions is not a valid action in the current state.
	 */
	public void join(TableId id) throws IllegalActionException{
		if(id==null)
			throw new IllegalArgumentException("The given table id is not effective.");
		state.join(id);
	}


	public void leaveTable() throws IllegalActionException{
		state.leaveTable();
	}

	public TableId createTable() throws IllegalActionException {
		return state.createTable();
	}

	public void startGame() throws IllegalActionException{
		state.startGame();
	}


	public List<GameEvent> getLatestGameEvents() throws IllegalActionException{
		return state.getLatestGameEvents();
	}


	public List<GameEvent> getLatestGameEventsAndAck(int ack) throws IllegalActionException{
		return state.getLatestGameEventsAndAck(ack);
	}

	void setPlayerCommunicationState(PlayerCommunicationState state){
		this.state = state;
	}
	
	@Override
	public String toString(){
		return "player communication of "+player.getName();
	}

}
