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

import org.apache.log4j.Logger;
import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.server.common.game.GameMediator;
import org.cspoker.server.common.game.elements.chips.IllegalValueException;
import org.cspoker.server.common.game.elements.table.GameTable;
import org.cspoker.server.common.game.elements.table.PlayerListFullException;
import org.cspoker.server.common.game.gamecontrol.rounds.FinalRound;
import org.cspoker.server.common.game.gamecontrol.rounds.FlopRound;
import org.cspoker.server.common.game.gamecontrol.rounds.PreFlopRound;
import org.cspoker.server.common.game.gamecontrol.rounds.TurnRound;
import org.cspoker.server.common.game.gamecontrol.rounds.WaitingRound;
import org.cspoker.server.common.game.player.GameSeatedPlayer;
import org.cspoker.server.common.game.player.PlayerFactory;

public class GameFlowTest extends TestCase {

	static {
		Log4JPropertiesLoader
				.load("org/cspoker/server/common/logging/log4j.properties");
	}

	private static Logger logger = Logger.getLogger(GameFlowTest.class);

	private GameSeatedPlayer kenzo;

	private GameSeatedPlayer cedric;

	private GameSeatedPlayer guy;

	private GameSeatedPlayer craig;

	private GameTable table;

	private GameMediator gameMediator;

	private PlayerFactory playerFactory;

	protected void setUp() {

		playerFactory = new TestPlayerFactory();
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 100);
			cedric = playerFactory.createNewPlayer("Cedric", 100);
			guy = playerFactory.createNewPlayer("Guy", 100);

			TableId id = new TableId(0);
			gameMediator = new GameMediator(id);
			table = new GameTable(id, new GameProperty(10));
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);

		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
	}

	public void testCase1() {
		GameControl gameControl = new GameControl(gameMediator, table, kenzo);

		Game game = gameControl.getGame();

		// Pre-flop Round
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		assertTrue(game.getCurrentPlayer().equals(kenzo));

		try {
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());

			// Big Blind Checks.
			gameControl.check(game.getCurrentPlayer());

		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		assertEquals(FlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FlopRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FlopRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		assertEquals(TurnRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(TurnRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(TurnRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		assertEquals(FinalRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FinalRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FinalRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// New Deal
		assertEquals(cedric, game.getDealer());
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		assertEquals(FlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FlopRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FlopRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		assertEquals(TurnRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(TurnRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(TurnRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		assertEquals(FinalRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FinalRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FinalRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// New Deal
		assertEquals(guy, game.getDealer());
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
	}

	public void testCase2() {
		GameControl gameControl = new GameControl(gameMediator, table, guy);
		Game game = gameControl.getGame();

		// New Deal
		assertEquals(guy, game.getDealer());
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());

		try {
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.raise(game.getCurrentPlayer(), 10);
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.raise(game.getCurrentPlayer(), 20);
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		assertEquals(FlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		assertEquals(TurnRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		assertEquals(FinalRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// New Deal
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
	}

	/**
	 * Test Settings: > 3 players > Big blind raises.
	 */
	public void testBigBlindRaisesCase() {
		GameControl gameControl = new GameControl(gameMediator, table);
		Game game = gameControl.getGame();

		// Pre-flop Round
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());

			// Big Blind Raises.
			gameControl.raise(game.getCurrentPlayer(), 20);

			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		assertEquals(FlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testAllInSmallBlindCase() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 100);
			cedric = playerFactory.createNewPlayer("Cedric", 4);
			guy = playerFactory.createNewPlayer("Guy", 100);

			table = new GameTable(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameControl gameControl = new GameControl(gameMediator, table, kenzo);

		Game game = gameControl.getGame();

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Next game...");

		GameFlowTest.logger.info("Current Player:" + game.getCurrentPlayer());
		GameFlowTest.logger.info("Next Dealer:" + game.getNextDealer());
		GameFlowTest.logger.info(game.getFirstToActPlayer());
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
	}

	public void testAllInBigBlindCase() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 100);
			cedric = playerFactory.createNewPlayer("Cedric", 100);
			guy = playerFactory.createNewPlayer("Guy", 9);

			table = new GameTable(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameControl gameControl = new GameControl(gameMediator, table, kenzo);
		Game game = gameControl.getGame();

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		// Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testBothBlindsAllInCase() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 100);
			cedric = playerFactory.createNewPlayer("Cedric", 4);
			guy = playerFactory.createNewPlayer("Guy", 9);

			table = new GameTable(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		GameControl gameControl = new GameControl(gameMediator, table, kenzo);
		Game game = gameControl.getGame();

		try {
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void test2AllInOneActivePlayerCase() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 100);
			cedric = playerFactory.createNewPlayer("Cedric", 100);
			guy = playerFactory.createNewPlayer("Guy", 200);

			table = new GameTable(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		GameControl gameControl = new GameControl(gameMediator, table, kenzo);
		Game game = gameControl.getGame();

		try {
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		// Game continues automatically
	}

	public void testAllAllInCase() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 200);
			cedric = playerFactory.createNewPlayer("Cedric", 100);
			guy = playerFactory.createNewPlayer("Guy", 150);

			table = new GameTable(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameControl gameControl = new GameControl(gameMediator, table);

		Game game = gameControl.getGame();

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		try {
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// New game
		GameSeatedPlayer testPlayer = PlayerFactory.global_Player_Factory
				.createNewPlayer("test");

		try {
			gameControl.joinGame(new SeatId(4), testPlayer);
			assertFalse(gameControl.getRound() instanceof WaitingRound);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

	}

	/*
	 * An all-in raise that is smaller than half the previous raise does not
	 * open up the round for more betting. Only calls are allowed.
	 */
	public void testAllInCallCase() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 200);
			cedric = playerFactory.createNewPlayer("Cedric", 220);
			guy = playerFactory.createNewPlayer("Guy", 100);

			TableId id = new TableId(0);
			gameMediator = new GameMediator(id);
			table = new GameTable(id, new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameControl gameControl = new GameControl(gameMediator, table, kenzo);

		try {
			gameControl.raise(kenzo, 75);
			gameControl.call(cedric);
			gameControl.allIn(guy);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		try {
			gameControl.raise(kenzo, 75);
			fail("Previous all in was not big enough to open up betting!");
		} catch (IllegalActionException e1) {
			// Should not reach here
		}
	}

	/*
	 * An all-in raise that is greater than half the previous raise opens up the
	 * round for more betting.
	 */
	public void testAllInRaiseCase() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 200);
			cedric = playerFactory.createNewPlayer("Cedric", 220);
			guy = playerFactory.createNewPlayer("Guy", 100);

			TableId id = new TableId(0);
			gameMediator = new GameMediator(id);
			table = new GameTable(id, new GameProperty());

			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());

		GameControl gameControl = new GameControl(gameMediator, table, kenzo);

		GameFlowTest.logger.info("Betting Rules: "
				+ gameControl.getGame().getBettingRules().toString());
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.raise(kenzo, 20);
			gameControl.call(cedric);
			gameControl.allIn(guy);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		try {
			gameControl.raise(kenzo, 75);
		} catch (IllegalActionException e1) {
			fail("Previous all in big enough to open up betting!");
		}
	}

	/*
	 * Test that the dealer gets to act first on the first round in a heads up
	 * game
	 */
	public void testHeadsUpDealerSwitch() {
		try {
			guy = playerFactory.createNewPlayer("Guy", 100);
			craig = playerFactory.createNewPlayer("Craig", 100);

			TableId id = new TableId(0);
			gameMediator = new GameMediator(id);
			table = new GameTable(id, new GameProperty());
			table.addPlayer(guy);
			table.addPlayer(craig);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameControl gameControl = new GameControl(gameMediator, table);

		Game game = gameControl.getGame();

		try {
			gameControl.call(game.getDealer());
			gameControl.check(game.getCurrentPlayer());

			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getDealer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testOnlyOneActivePlayer() {

		GameControl gameControl = new GameControl(gameMediator, table);

		Game game = gameControl.getGame();

		try {
			gameControl.fold(game.getCurrentPlayer());
			gameControl.fold(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		assertFalse(gameControl.getRound() instanceof WaitingRound);

	}

	public void testOnlyOneAllInPlayer() {

		GameControl gameControl = new GameControl(gameMediator, table);

		Game game = gameControl.getGame();

		try {
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.fold(game.getCurrentPlayer());
			gameControl.fold(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testOnlyOneAllInPlayer2() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 100);
			cedric = playerFactory.createNewPlayer("Cedric", 200);
			guy = playerFactory.createNewPlayer("Guy", 200);

			table = new GameTable(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameControl gameControl = new GameControl(gameMediator, table, kenzo);

		Game game = gameControl.getGame();

		try {
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());

			gameControl.fold(game.getCurrentPlayer());
			gameControl.fold(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void test2PlayersOneFold() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 500);
			cedric = playerFactory.createNewPlayer("Cedric", 500);

			table = new GameTable(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		GameControl gameControl = new GameControl(gameMediator, table, kenzo);

		try {
			gameControl.call(kenzo);
			gameControl.raise(cedric, 20);
			gameControl.call(kenzo);

			gameControl.bet(cedric, 20);
			gameControl.call(kenzo);

			gameControl.bet(cedric, 20);
			gameControl.call(kenzo);

			gameControl.bet(cedric, 20);
			gameControl.raise(kenzo, 50);
			gameControl.fold(cedric);

		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void test2PlayersSmallBlindRaises() {
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 500);
			cedric = playerFactory.createNewPlayer("Cedric", 500);

			table = new GameTable(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		GameControl gameControl = new GameControl(gameMediator, table, kenzo);

		try {
			gameControl.raise(kenzo, 20);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		try {
			gameControl.check(cedric);
			fail("Exception expected: can not check after raise");
		} catch (IllegalActionException e) {
		}

		try {
			gameControl.call(cedric);

			assertEquals(FlopRound.class, gameControl.getRound().getClass());
			gameControl.check(cedric);
			gameControl.check(kenzo);

			assertEquals(TurnRound.class, gameControl.getRound().getClass());
			gameControl.check(cedric);
			gameControl.check(kenzo);
			assertEquals(FinalRound.class, gameControl.getRound().getClass());
			gameControl.check(cedric);
			gameControl.check(kenzo);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
	}
}
