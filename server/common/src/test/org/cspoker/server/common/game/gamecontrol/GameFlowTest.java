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
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.server.common.game.GameMediator;
import org.cspoker.server.common.game.elements.chips.IllegalValueException;
import org.cspoker.server.common.game.elements.table.GameTable;
import org.cspoker.server.common.game.elements.table.PlayerListFullException;
import org.cspoker.server.common.game.gamecontrol.rounds.WaitingRound;
import org.cspoker.server.common.game.player.GamePlayer;
import org.cspoker.server.common.game.player.PlayerFactory;

public class GameFlowTest extends TestCase {
	
	static {
		Log4JPropertiesLoader
		.load("org/cspoker/server/common/logging/log4j.properties");
	}
	
	private static Logger logger = Logger.getLogger(GameFlowTest.class);

	private GamePlayer kenzo;

	private GamePlayer cedric;

	private GamePlayer guy;

	private GamePlayer craig;

	private GameTable table;

	private GameMediator gameMediator;

	private PlayerFactory playerFactory;

	@Override
	protected void setUp() {
		
		playerFactory = new TestPlayerFactory();
		GameFlowTest.logger
				.info("**********************************************************");
		GameFlowTest.logger
				.info("* New Game                                               *");
		GameFlowTest.logger
				.info("**********************************************************");
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 100);
			cedric = playerFactory.createNewPlayer("Cedric", 100);
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
	}

	public void testCase1() {
		GameControl gameControl = new GameControl(gameMediator, table);
		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());
		GameFlowTest.logger.info("Betting Rules: "
				+ gameControl.getGame().getBettingRules()
						.toString());
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());

			// Big Blind Checks.
			gameControl.check(game.getCurrentPlayer());

		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		// New game

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		// try {
		// gameControl.deal(dealer);
		// } catch (IllegalActionException e) {
		// fail(e.getMessage());
		// }
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());
	}

	public void testCase2() {
		GameControl gameControl = new GameControl(gameMediator, table);
		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.raise(game.getCurrentPlayer(), 10);
			gameControl.call(game.getCurrentPlayer());
			gameControl.raise(game.getCurrentPlayer(), 20);
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testBigBlindRaisesCase() {
		GameControl gameControl = new GameControl(gameMediator, table);
		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());
		GameFlowTest.logger.info("Betting Rules: "
				+ gameControl.getGame().getBettingRules()
						.toString());
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());

			// Big Blind Raises.
			gameControl.raise(game.getCurrentPlayer(), 20);

			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());

		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

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

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());
		// Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

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
		// try {
		// gameControl.deal(dealer);
		// } catch (IllegalActionException e) {
		// fail(e.getMessage());
		// }
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

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());

		GameControl gameControl = new GameControl(gameMediator, table, kenzo);
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());
		GameFlowTest.logger.info("Side pots: " + game.getPots().getSidePots());
		GameFlowTest.logger.info("Main pot: " + game.getPots().getMainPot());
		// Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
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

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());
		GameControl gameControl = new GameControl(gameMediator, table, kenzo);
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

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

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());
		GameControl gameControl = new GameControl(gameMediator, table, kenzo);
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

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

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());
		GameFlowTest.logger.info("Betting Rules: "
				+ gameControl.getGame().getBettingRules()
						.toString());
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

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
		System.out.println("");
		System.out.println(game.getCurrentDealPlayers());
		System.out.println("Nb seated players: "+ game.getNbSeatedPlayers());
		GamePlayer testPlayer = PlayerFactory.global_Player_Factory.createNewPlayer("test");
		
		try {
			gameControl.joinGame(testPlayer);
			System.out.println(game.getCurrentDealPlayers());
			System.out.println(game.getTable().getPlayers());
			System.out.println("Nb seated players: "+ game.getNbSeatedPlayers());
			System.out.println(game.getDealer());
			assertFalse(gameControl.getRound() instanceof WaitingRound);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		

		GameFlowTest.logger.info(game.getCurrentDealPlayers());

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());
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

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());

		GameControl gameControl = new GameControl(gameMediator, table, kenzo);

		GameFlowTest.logger.info("Betting Rules: "
				+ gameControl.getGame().getBettingRules()
						.toString());
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

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
				+ gameControl.getGame().getBettingRules()
						.toString());
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

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());

		GameControl gameControl = new GameControl(gameMediator, table);

		GameFlowTest.logger.info("Betting Rules: "
				+ gameControl.getGame().getBettingRules()
						.toString());
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());
		GameFlowTest.logger.info("Craig's Cards: " + craig.getPocketCards());

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
		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());

		GameControl gameControl = new GameControl(gameMediator, table);

		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.fold(game.getCurrentPlayer());
			gameControl.fold(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		
		assertFalse(gameControl.getRound() instanceof WaitingRound);

		
	}

	public void testOnlyOneAllInPlayer() {
		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());

		GameControl gameControl = new GameControl(gameMediator, table);

		Game game = gameControl.getGame();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

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

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());

		GameControl gameControl = new GameControl(gameMediator, table, kenzo);

		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

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

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());

		GameControl gameControl = new GameControl(gameMediator, table, kenzo);
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

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

}
