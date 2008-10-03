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
package org.cspoker.server.common.playercommunication;

import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.server.common.GameManager;
import org.cspoker.server.common.PokerTable;

/**
 * A class to represent the playing state of the player.
 * 
 * PlayingState --------------------------------> InitialState leaveTable()
 * 
 * @author Kenzo
 * 
 */
class PlayingState extends PlayerCommunicationState {

	/**
	 * The mediator the player is in.
	 */
	private final PokerTable gameMediator;

	/**
	 * Create a new playing state with given player communication and game
	 * mediator
	 * 
	 * @param playerCommunication
	 *            The player communication for this playing state.
	 * @param gameMediator
	 *            The game mediator for this playing state.
	 */
	public PlayingState(PlayerCommunicationImpl playerCommunication,
			PokerTable gameMediator) {
		super(playerCommunication);
		this.gameMediator = gameMediator;
	}

	public void call() throws IllegalActionException {
		gameMediator.call(playerCommunication.getPlayer());
	}

	public void bet(int amount) throws IllegalActionException {
		gameMediator.bet(playerCommunication.getPlayer(), amount);
	}

	public void fold() throws IllegalActionException {
		gameMediator.fold(playerCommunication.getPlayer());
	}

	public void check() throws IllegalActionException {
		gameMediator.check(playerCommunication.getPlayer());
	}

	public void raise(int amount) throws IllegalActionException {
		gameMediator.raise(playerCommunication.getPlayer(), amount);
	}

	public void allIn() throws IllegalActionException {
		gameMediator.allIn(playerCommunication.getPlayer());
		;
	}

	public void say(String message) {
		gameMediator.publishGameMessageEvent(new GameMessageEvent(
				playerCommunication.getPlayer().getMemento(), message));
	}

	public void leaveTable() throws IllegalActionException {
		gameMediator.unsubscribeAllGameEventsListener(playerCommunication
				.getId(), playerCommunication.getAllEventsListener());
		gameMediator.leaveGame(playerCommunication.getPlayer());
		GameManager.getServerMediator().subscribeAllServerEventsListener(
				playerCommunication.getId(),
				playerCommunication.getAllEventsListener());
		playerCommunication.setPlayerCommunicationState(new InitialState(
				playerCommunication));

	}

	protected String getStdErrorMessage() {
		return "You can not perform this action while playing.";
	}

	public void kill() {
		try {
			leaveTable();
		} catch (IllegalActionException e) {
			// kill the hard way?
			// TODO
		}
		GameManager.getServerMediator().unsubscribeAllServerEventsListener(
				playerCommunication.getId(),
				playerCommunication.getAllEventsListener());

	}

}
