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
import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.events.gameevents.PlayerJoinedTableEvent;
import org.cspoker.common.events.serverevents.TableChangedEvent;
import org.cspoker.common.events.serverevents.TableCreatedEvent;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.server.common.game.GameManager;
import org.cspoker.server.common.game.GameMediator;
import org.cspoker.server.common.game.TableManager;
import org.cspoker.server.common.game.elements.table.GameTable;
import org.cspoker.server.common.game.elements.table.PlayerListFullException;
import org.cspoker.server.common.game.elements.table.SeatTakenException;
import org.cspoker.server.common.game.exception.TableDoesNotExistException;

/**
 * A class to represent the initial state of the player.
 * 
 * InitialState -------------------------------------> TableCreatedState | \
 * createTable() | \ | \ | ---------------------------------------->
 * WaitingAtTableState | join() and table is not playing | | | join() and table
 * is playing | | \/ PlayingState
 * 
 * @author Kenzo
 * 
 * 
 */
class InitialState extends PlayerCommunicationState {
	private static Logger logger = Logger.getLogger(InitialState.class);

	/**
	 * Construct a new initial state with given player communication.
	 * 
	 * @param playerCommunication
	 *            The player communication for this initial state.
	 * @pre The given playerCommunication should be effective.
	 *      |playerCommunication!=null
	 */
	public InitialState(PlayerCommunicationImpl playerCommunication) {
		super(playerCommunication);
	}

	
	public Table join(TableId tableId, SeatId seatId) throws IllegalActionException {
		if(tableId == null)
			throw new IllegalArgumentException("The given table id is not effective.");

		GameTable table;
		try {
			table = TableManager.global_table_manager.getTable(tableId);
		} catch (TableDoesNotExistException e) {
			throw new IllegalActionException("You can not join the given table. "+e.getMessage());
		}
		
		if (table.isPlaying()) {
			GameMediator mediator = GameManager.getGame(tableId);
			GameManager.getServerMediator().unsubscribeAllServerEventsListener(
					playerCommunication.getId(),
					playerCommunication.getAllEventsListener());
			mediator.subscribeAllGameEventsListener(
					playerCommunication.getId(), playerCommunication
							.getAllEventsListener());
			mediator.joinGame(seatId, playerCommunication.getPlayer());
			playerCommunication.setPlayerCommunicationState(new PlayingState(
					playerCommunication, mediator));
		} else {
			try {
				if(seatId==null){
					table.addPlayer(playerCommunication.getPlayer());
				}else{
					table.addPlayer(seatId, playerCommunication.getPlayer());
				}

			} catch (SeatTakenException e) {
				throw new IllegalActionException(e.getMessage());
			} catch (PlayerListFullException e) {
				throw new IllegalActionException(e.getMessage());
			}
			GameManager.getGame(table.getId()).publishPlayerJoinedTable(new PlayerJoinedTableEvent(playerCommunication.getPlayer().getSavedPlayer()));
			playerCommunication
					.setPlayerCommunicationState(new WaitingAtTableState(
							playerCommunication, table, GameManager.getGame(table.getId())));
		}
		GameManager.getServerMediator().publishTableChangedEvent(new TableChangedEvent(table.getSavedTable()));
		
		InitialState.logger.info(playerCommunication.getPlayer().getName()
				+ " joined " + tableId + ".");
		return table.getSavedTable();
	}

	
	public Table createTable(String name) throws IllegalActionException {
		return createTable(name, new GameProperty());
	}
	
	
	public Table createTable(String name, GameProperty property) throws IllegalActionException {
		GameTable table = TableManager.global_table_manager.createTable(playerCommunication.getPlayer()
				.getId(), name, property);
		if(name==null)
			throw new IllegalArgumentException("The given name should be effective.");
		try {
			table.addPlayer(new SeatId(0), playerCommunication.getPlayer());
		} catch (SeatTakenException e) {
			throw new IllegalStateException(
			"A newly created table should have at least a place for one player.");
		}
		playerCommunication.setPlayerCommunicationState(new TableCreatedState(
				playerCommunication, table));
		InitialState.logger.info(playerCommunication.getPlayer().getName()
				+ " created " + table.getId() + ": "+name+".");
		GameManager.getServerMediator().publishTableCreatedEvent(
				new TableCreatedEvent(playerCommunication.getPlayer()
						.getSavedPlayer(), table.getSavedTable()));
		return table.getSavedTable();
	}

	
	protected String getStdErrorMessage() {
		return "You have to be in a game to perform this action.";
	}

	
	public void kill() {
		GameManager.getServerMediator().unsubscribeAllServerEventsListener(
				playerCommunication.getId(), playerCommunication.getAllEventsListener());
	}
}
