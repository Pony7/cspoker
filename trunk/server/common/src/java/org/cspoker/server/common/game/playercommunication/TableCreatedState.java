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
package org.cspoker.server.common.game.playercommunication;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.events.serverevents.PlayerLeftEvent;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.player.PlayerId;
import org.cspoker.server.common.game.GameManager;
import org.cspoker.server.common.game.GameMediator;
import org.cspoker.server.common.game.TableManager;
import org.cspoker.server.common.game.elements.table.GameTable;
import org.cspoker.server.common.game.gamecontrol.GameControl;
import org.cspoker.server.common.game.session.PlayerKilledExcepion;
import org.cspoker.server.common.game.session.SessionManager;

/**
 * A player who has created the table goes to the table created state.
 * 
 * Only this player can change game settings or start the game.
 * 
 * 
 * 
 * InitialState --------------------------------> TableCreatedState /\
 * createTable() | | |__________________________________________________| |
 * startGame() leaveTable() && only player at table. | | \/ PlayingState (for
 * all players at the table)
 * 
 * @author Kenzo
 * 
 */
class TableCreatedState extends WaitingAtTableState {
	private static Logger logger = Logger.getLogger(TableCreatedState.class);

	/**
	 * Construct a new table created state with given player communication and
	 * table.
	 * 
	 * @param playerCommunication
	 *            The playerCommunication of the player.
	 * @param table
	 *            The created table.
	 */
	public TableCreatedState(PlayerCommunicationImpl playerCommunication,
			GameTable table) {
		super(playerCommunication, table, GameManager.createNewGame(table.getId()));
	}

	@Override
	public void startGame() throws IllegalActionException {

		/**
		 * The table should be locked so while constructing the new game no
		 * player can exit or enter the table.
		 * 
		 * Important for not having a dead-lock: only one player can call
		 * startGame(), as it is guaranteed.
		 */
		synchronized (table) {
			GameMediator gameMediator = GameManager.getGame(table.getId());
			if(table.getNbPlayers()<=1)
				throw new IllegalActionException("At least two players must be seated to play a game.");
			for (PlayerId id : table.getPlayerIds()) {
				PlayerCommunicationImpl comm;
				try {
					comm = SessionManager.global_session_manager.getSession(id)
							.getPlayerCommunication();
					comm.setPlayerCommunicationState(new PlayingState(comm,
							gameMediator));
				} catch (PlayerKilledExcepion e) {
					// no op
					// killed players should have been removed from the table
					// already
				}
			}
			new GameControl(gameMediator, table);
		}

		TableCreatedState.logger.info("Game Started.");
	}

	@Override
	public void leaveTable() throws IllegalActionException {
		synchronized (table) {
			if (table.getNbPlayers() == 1) {
				TableManager.removeTable(table);
				super.leaveTable();
			} else {
				throw new IllegalActionException(
						"The owner can only leave if he is the only player at the table.");
			}
		}
	}

	@Override
	public void join(TableId id) throws IllegalActionException {
		throw new IllegalActionException("You are already sitting at table "
				+ table.getId() + ".");
	}

	@Override
	protected String getStdErrorMessage() {
		return "You have not yet started the game.";
	}

}
