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

import game.GameManager;
import game.GameMediator;
import game.PlayerId;
import game.elements.table.Table;
import game.gameControl.GameControl;
import game.gameControl.actions.IllegalActionException;

/**
 * A player who has created the table goes to the table created state.
 *
 * Only this player can change game settings or start the game.
 *
 *
 *
 * InitialState  --------------------------------> 	TableCreatedState
 *                    createTable()							|
 *                    										| startGame()
 *                    										|
 *                    										|
 *                    										\/
 *                    								PlayingState
 *                    						(for all players at the table)
 *
 * @author Kenzo
 *
 */
class TableCreatedState extends WaitingAtTableState {

	/**
	 * Construct a new table created state
	 * @param playerCommunication
	 * @param table
	 */
	public TableCreatedState(PlayerCommunicationImpl playerCommunication, Table table) {
		super(playerCommunication, table);
	}

	@Override
	public void startGame() throws IllegalActionException {

		/**
		 * The table should be locked so while constructing the new game
		 * no player can exit or enter the table.
		 *
		 * Important for not having a dead-lock:
		 * only one player can call startGame(), as it is guaranteed.
		 */
		synchronized (table) {
			GameMediator gameMediator = new GameMediator();
			new GameControl(gameMediator, table);
			for(PlayerId id:table.getPlayerIds()){
				PlayerCommunicationImpl comm = PlayerCommunicationManager.getPlayerCommunication(id);
				comm.setPlayerCommunicationState(new PlayingState(comm, gameMediator));
			}
			GameManager.addGame(table.getId(), gameMediator);
		}

		System.out.println("Game Started.");

		/**
		 * TODO Concurrency: table set to isPlaying, but game does not exist
		 * in the GameManager
		 */
	}

	@Override
	protected String getStdErrorMessage() {
		// TODO Auto-generated method stub
		return "";
	}

}
