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

import org.cspoker.common.events.gameevents.PlayerLeftTableEvent;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.server.common.game.GameManager;
import org.cspoker.server.common.game.GameMediator;
import org.cspoker.server.common.game.elements.table.GameTable;

/**
 * A class to represent the state where the player is waiting for the game to
 * start.
 * 
 * WaitingAtTableState --------------------------------> InitialState \
 * leaveTable() \ \ -------------------------------------------> PlayingState
 * The owner of the table calls startGame()
 * 
 * @author Kenzo
 * 
 */
class WaitingAtTableState extends PlayerCommunicationState {

	protected final GameTable table;

	public WaitingAtTableState(PlayerCommunicationImpl playerCommunication,
			GameTable table, GameMediator gameMediator) {
		super(playerCommunication);
		this.table = table;
		GameManager.getServerMediator().unsubscribeAllServerEventsListener(
				playerCommunication.getId(),
				playerCommunication.getAllEventsListener());
		gameMediator.subscribeAllGameEventsListener(
				playerCommunication.getId(), playerCommunication
						.getAllEventsListener());
	}

	@Override
	public void leaveTable() throws IllegalActionException {
		table.removePlayer(playerCommunication.getPlayer());
		playerCommunication.setPlayerCommunicationState(new InitialState(
				playerCommunication));
		GameManager.getGame(table.getId()).publishPlayerLeftTable(new PlayerLeftTableEvent(playerCommunication.getPlayer().getSavedPlayer()));
		GameManager.getServerMediator().subscribeAllServerEventsListener(
				playerCommunication.getId(),
				playerCommunication.getAllEventsListener());
		GameManager.getGame(table.getId()).unsubscribeAllGameEventsListener(
				playerCommunication.getId(), playerCommunication
						.getAllEventsListener());
	}

	@Override
	protected String getStdErrorMessage() {
		return "You are waiting at a table for a game to begin.";
	}

	@Override
	public void kill() {
		// remove to killed player from the table
		table.removePlayer(playerCommunication.getPlayer());
	}

}
