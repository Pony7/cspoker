package org.cspoker.server.common.game.gamecontrol;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.server.common.game.GameMediator;
import org.cspoker.server.common.game.elements.chips.IllegalValueException;
import org.cspoker.server.common.game.elements.table.GameTable;
import org.cspoker.server.common.game.elements.table.PlayerListFullException;
import org.cspoker.server.common.game.gamecontrol.rules.Limit;
import org.cspoker.server.common.game.gamecontrol.rules.NoLimit;
import org.cspoker.server.common.game.gamecontrol.rules.PotLimit;
import org.cspoker.server.common.game.player.GamePlayer;
import org.cspoker.server.common.game.player.PlayerFactory;

public class BettingRulesTest extends TestCase {
	private static Logger logger = Logger.getLogger(BettingRulesTest.class);

	private GamePlayer kenzo;

	private GamePlayer cedric;

	private GamePlayer guy;

	private GameTable table;

	private GameControl gameControl;

	private GameMediator gameMediator;

	private PlayerFactory playerFactory;

	protected void setUp() {
		playerFactory = new TestPlayerFactory();
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 100);
			cedric = playerFactory.createNewPlayer("Cedric", 100);
			guy = playerFactory.createNewPlayer("Guy", 100);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		}
	}

	public void testNoLimitRules() {
		TableId id = new TableId(0);
		table = new GameTable(id, new GameProperty(10));
		gameMediator = new GameMediator(id);
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		gameControl = new GameControl(gameMediator, table, new NoLimit());
		Game game = gameControl.getGame();

		try {
			gameControl.raise(game.getCurrentPlayer(), 10);
			gameControl.raise(game.getCurrentPlayer(), 5);
			fail("Exception Expected.");
		} catch (IllegalActionException e) {
			BettingRulesTest.logger.error(e.getLocalizedMessage());
		}
	}

	public void testPotLimitRules() {
		TableId id = new TableId(0);
		table = new GameTable(id, new GameProperty(10));
		gameMediator = new GameMediator(id);
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		gameControl = new GameControl(gameMediator, table, new PotLimit());
		Game game = gameControl.getGame();

		try {
			gameControl.raise(game.getCurrentPlayer(), 10);
			assert (false);
		} catch (IllegalActionException e) {
			BettingRulesTest.logger.error(e.getLocalizedMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(), 5);
			assert (false);
		} catch (IllegalActionException e) {
			BettingRulesTest.logger.error(e.getLocalizedMessage());
		}

		try {
			gameControl.raise(game.getCurrentPlayer(), gameControl.getRound()
					.getCurrentPotValue() + 10);
			assert (false);
		} catch (IllegalActionException e) {
			BettingRulesTest.logger.error(e.getLocalizedMessage());
		}
	}

	public void testLimitRules2() {
		TableId id = new TableId(0);
		table = new GameTable(id, new GameProperty(10));
		gameMediator = new GameMediator(id);
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		gameControl = new GameControl(gameMediator, table, new Limit(10));
		Game game = gameControl.getGame();

		try {
			gameControl.raise(game.getCurrentPlayer(), 10);
			gameControl.raise(game.getCurrentPlayer(), 10);
			gameControl.raise(game.getCurrentPlayer(), 10);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(), 10);
			assert (false);
		} catch (IllegalActionException e) {
			BettingRulesTest.logger.error(e.getLocalizedMessage());
		}
	}

	public void testLimitRules1() {
		TableId id = new TableId(0);
		table = new GameTable(id, new GameProperty(10));
		gameMediator = new GameMediator(id);
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		gameControl = new GameControl(gameMediator, table, new Limit(10));
		BettingRulesTest.logger.info("Game Properties:");
		BettingRulesTest.logger.info("Small Blind: "
				+ table.getGameProperty().getSmallBlind());
		BettingRulesTest.logger.info("Big Blind: "
				+ table.getGameProperty().getBigBlind());
		BettingRulesTest.logger.info("Betting Rules: "
				+ gameControl.getGame().getBettingRules().toString());
		Game game = gameControl.getGame();

		BettingRulesTest.logger.info("Dealer: " + game.getDealer().getName());

		BettingRulesTest.logger.info(game.getCurrentDealPlayers());
		BettingRulesTest.logger
				.info("Kenzo's Cards: " + kenzo.getPocketCards());
		BettingRulesTest.logger.info("Cedric's Cards: "
				+ cedric.getPocketCards());
		BettingRulesTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.raise(game.getCurrentPlayer(), 1);
			assert (false);
		} catch (IllegalActionException e) {
			BettingRulesTest.logger.error(e.getLocalizedMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(), -1);
			assert (false);
		} catch (IllegalActionException e) {
			BettingRulesTest.logger.error(e.getLocalizedMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(), 0);
			assert (false);
		} catch (IllegalActionException e) {
			BettingRulesTest.logger.error(e.getLocalizedMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(), 200);
			assert (false);
		} catch (IllegalActionException e) {
			BettingRulesTest.logger.error(e.getLocalizedMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(), 80);
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BettingRulesTest.logger.info(game.getCurrentDealPlayers());

		BettingRulesTest.logger.info("Common Cards: "
				+ game.getCommunityCards());
	}

}
