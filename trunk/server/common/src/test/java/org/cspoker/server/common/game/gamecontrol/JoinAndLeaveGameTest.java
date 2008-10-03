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
package org.cspoker.server.common.game.gamecontrol;

import junit.framework.TestCase;

import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.server.common.GameMediator;
import org.cspoker.server.common.elements.chips.IllegalValueException;
import org.cspoker.server.common.elements.table.GameTable;
import org.cspoker.server.common.elements.table.PlayerListFullException;
import org.cspoker.server.common.gamecontrol.GameControl;
import org.cspoker.server.common.gamecontrol.rounds.PreFlopRound;
import org.cspoker.server.common.gamecontrol.rounds.WaitingRound;
import org.cspoker.server.common.player.GameSeatedPlayer;
import org.cspoker.server.common.player.PlayerFactory;

public class JoinAndLeaveGameTest extends TestCase {

	private GameSeatedPlayer kenzo;

	private GameSeatedPlayer cedric;

	private GameSeatedPlayer guy;

	private WaitingTableState table;

	private PokerTable gameMediator;

	private PlayerFactory playerFactory;

	protected void setUp() {

		playerFactory = new TestPlayerFactory();
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 500);
			cedric = playerFactory.createNewPlayer("Cedric", 500);
			guy = playerFactory.createNewPlayer("Guy", 500);

			TableId id = new TableId(0);
			gameMediator = new PokerTable(id);
			table = new WaitingTableState(id, new GameProperty(10));
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test Settings: > 2 players > on-turn player leaves the table.
	 */
	public void testLeaveTable1() {
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(gameMediator, table, kenzo);

		try {
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.leaveGame(kenzo);
			assertEquals(0, kenzo.getBetChips().getValue());
			assertEquals(WaitingRound.class, gameControl.getRound().getClass());
			gameControl.joinGame(null, kenzo);
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test Settings: > 2 players > other player (big blind player) leaves the
	 * table.
	 */
	public void testLeaveTable2() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 500);
			cedric = playerFactory.createNewPlayer("Cedric", 500);

			table = new WaitingTableState(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(gameMediator, table, kenzo);

		try {
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.leaveGame(cedric);
			assertEquals(0, cedric.getBetChips().getValue());
			assertEquals(WaitingRound.class, gameControl.getRound().getClass());
			gameControl.joinGame(null, cedric);
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test Settings: > 3 players > kenzo leaves the table.
	 */
	public void testLeaveTable3() {
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(gameMediator, table, kenzo);

		try {
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			assertEquals(kenzo, gameControl.getGame().getDealer());
			assertEquals(cedric, gameControl.getGame().getNextDealer());
			gameControl.leaveGame(kenzo);
			assertEquals(cedric, gameControl.getGame().getDealer());
			assertEquals(cedric, gameControl.getGame().getNextDealer());
			assertFalse(gameControl.getGame().hasAsActivePlayer(kenzo));
			assertFalse(gameControl.getGame().getTable().hasAsPlayer(kenzo));
			assertEquals(0, kenzo.getBetChips().getValue());
			gameControl.fold(cedric);
			assertEquals(cedric, gameControl.getGame().getDealer());
			assertEquals(guy, gameControl.getGame().getNextDealer());
			gameControl.joinGame(null, kenzo);
			assertTrue(gameControl.getGame().getTable().hasAsPlayer(kenzo));
			assertFalse(gameControl.getGame().hasAsActivePlayer(kenzo));
			gameControl.fold(cedric);
			assertEquals(guy, gameControl.getGame().getDealer());
			assertEquals(kenzo, gameControl.getGame().getNextDealer());
			assertTrue(gameControl.getGame().hasAsActivePlayer(kenzo));
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test Settings: > 3 players > guy leaves the table.
	 */
	public void testLeaveTable4() {
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(gameMediator, table, kenzo);

		try {
			assertEquals(kenzo, gameControl.getGame().getDealer());
			assertEquals(cedric, gameControl.getGame().getNextDealer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.leaveGame(guy);
			assertEquals(kenzo, gameControl.getGame().getDealer());
			assertEquals(cedric, gameControl.getGame().getNextDealer());
			assertFalse(gameControl.getGame().hasAsActivePlayer(guy));
			assertFalse(gameControl.getGame().getTable().hasAsPlayer(guy));
			assertEquals(0, guy.getBetChips().getValue());
			gameControl.fold(kenzo);
			assertEquals(cedric, gameControl.getGame().getDealer());
			assertEquals(kenzo, gameControl.getGame().getNextDealer());
			gameControl.joinGame(null, guy);
			assertTrue(gameControl.getGame().getTable().hasAsPlayer(guy));
			assertFalse(gameControl.getGame().hasAsActivePlayer(guy));
			gameControl.fold(cedric);
			assertEquals(kenzo, gameControl.getGame().getDealer());
			assertEquals(cedric, gameControl.getGame().getNextDealer());
			assertTrue(gameControl.getGame().hasAsActivePlayer(guy));
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test Settings: > 3 players > cedric leaves the table.
	 */
	public void testLeaveTable5() {
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(gameMediator, table, kenzo);

		try {
			assertEquals(kenzo, gameControl.getGame().getDealer());
			assertEquals(cedric, gameControl.getGame().getNextDealer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.leaveGame(cedric);
			assertEquals(kenzo, gameControl.getGame().getDealer());
			assertEquals(guy, gameControl.getGame().getNextDealer());
			assertFalse(gameControl.getGame().hasAsActivePlayer(cedric));
			assertFalse(gameControl.getGame().getTable().hasAsPlayer(cedric));
			assertEquals(0, cedric.getBetChips().getValue());
			gameControl.fold(kenzo);
			assertEquals(guy, gameControl.getGame().getDealer());
			assertEquals(kenzo, gameControl.getGame().getNextDealer());
			gameControl.joinGame(null, cedric);
			assertTrue(gameControl.getGame().getTable().hasAsPlayer(cedric));
			assertFalse(gameControl.getGame().hasAsActivePlayer(cedric));
			gameControl.fold(guy);
			assertEquals(kenzo, gameControl.getGame().getDealer());
			assertEquals(cedric, gameControl.getGame().getNextDealer());
			assertTrue(gameControl.getGame().hasAsActivePlayer(cedric));
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

}
