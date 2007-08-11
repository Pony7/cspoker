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

import org.cspoker.server.game.GameManager;
import org.cspoker.server.game.GameMediator;
import org.cspoker.server.game.TableId;
import org.cspoker.server.game.TableManager;
import org.cspoker.server.game.elements.table.PlayerListFullException;
import org.cspoker.server.game.elements.table.Table;
import org.cspoker.server.game.gameControl.actions.IllegalActionException;

/**
 * A class to represent the initial state of the player.
 *
 * InitialState  ------------------------------------->	TableCreatedState
 *    |    \            createTable()
 *    |     \
 *    |      \
 *    |       ---------------------------------------->	WaitingAtTableState
 *    |				join() and table is not playing
 *    |
 *    |
 *    | join() and table is playing
 *    |
 *    |
 *	  \/
 *	PlayingState
 *
 * @author Kenzo
 *
 *
 */
class InitialState extends PlayerCommunicationState {

	/**
	 * Construct a new initial state with given player communication.
	 *
	 * @param 	playerCommunication
	 * 			The player communication for this initial state.
	 * @pre 	The given playerCommunication should be effective.
	 *			|playerCommunication!=null
	 */
	public InitialState(PlayerCommunicationImpl playerCommunication) {
		super(playerCommunication);
	}

	@Override
	public void join(TableId id) throws IllegalActionException {
		if(id==null)
			throw new IllegalArgumentException("The given table id should be effective.");

		Table table = TableManager.getTable(id);

		if(table==null)
			throw new IllegalArgumentException("The given table id can not be found in the tables.");

		if(table.isPlaying()){
			GameMediator mediator = GameManager.getGame(id);
			mediator.joinGame(playerCommunication.getPlayer());
			playerCommunication.setPlayerCommunicationState(
					new PlayingState(playerCommunication, mediator));
		}else{
			try {
				table.addPlayer(playerCommunication.getPlayer());
			} catch (PlayerListFullException e) {
				throw new IllegalActionException("You can not join. The table is full.");
			}
			playerCommunication.setPlayerCommunicationState(
					new WaitingAtTableState(playerCommunication, table));
		}
		System.out.println(playerCommunication.getPlayer().getName()+" joined "+id+".");
	}

	@Override
	public TableId createTable() throws IllegalActionException{
		Table table = TableManager.createTable(playerCommunication.getPlayer().getId());
		try {
			table.addPlayer(playerCommunication.getPlayer());
		} catch (PlayerListFullException e) {
			throw new IllegalStateException("A newly created table should have at least a place for one player.");
		}
		playerCommunication.setPlayerCommunicationState(new TableCreatedState(playerCommunication, table));
		//TODO low priority Enhancement: Give a name to the table?
		System.out.println(playerCommunication.getPlayer().getName()+" created "+table.getId()+".");
		return table.getId();
	}

	@Override
	protected String getStdErrorMessage() {
		return "You have to be in a game to perform this action.";
	}
}
