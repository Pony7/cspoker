package org.cspoker.server.game.gameControl;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.game.elements.table.TableId;
import org.cspoker.server.game.GameMediator;
import org.cspoker.server.game.elements.chips.IllegalValueException;
import org.cspoker.server.game.elements.table.PlayerListFullException;
import org.cspoker.server.game.elements.table.Table;
import org.cspoker.server.game.gameControl.rules.Limit;
import org.cspoker.server.game.gameControl.rules.NoLimit;
import org.cspoker.server.game.gameControl.rules.PotLimit;
import org.cspoker.server.game.player.GamePlayer;
import org.cspoker.server.game.player.IllegalNameException;
import org.cspoker.server.game.player.PlayerFactory;

public class BettingRulesTest extends TestCase {
    private static Logger logger = Logger.getLogger(BettingRulesTest.class);

    private GamePlayer kenzo;

    private GamePlayer cedric;

    private GamePlayer guy;

    private Table table;

    private GameControl gameControl;

    private GameMediator gameMediator;

    private PlayerFactory playerFactory;

    @Override
    protected void setUp() {
	playerFactory = new TestPlayerFactory();
	try {
	    kenzo = playerFactory.getUniquePlayer("Kenzo", 100);
	    cedric = playerFactory.getUniquePlayer("Cedric", 100);
	    guy = playerFactory.getUniquePlayer("Guy", 100);
	    gameMediator = new GameMediator();
	} catch (IllegalValueException e) {
	    fail(e.getMessage());
	} catch (IllegalNameException e) {
	    fail(e.getMessage());
	}
    }

    public void testNoLimitRules() {
	table = new Table(new TableId(0), new GameProperty(10, new NoLimit()));
	try {
	    table.addPlayer(kenzo);
	    table.addPlayer(cedric);
	    table.addPlayer(guy);
	} catch (PlayerListFullException e) {
	    fail(e.getMessage());
	}
	gameControl = new GameControl(gameMediator, table);
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
	table = new Table(new TableId(0), new GameProperty(10, new PotLimit()));
	try {
	    table.addPlayer(kenzo);
	    table.addPlayer(cedric);
	    table.addPlayer(guy);
	} catch (PlayerListFullException e) {
	    fail(e.getMessage());
	}
	gameControl = new GameControl(gameMediator, table);
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
	table = new Table(new TableId(0), new GameProperty(10, new Limit(10)));
	try {
	    table.addPlayer(kenzo);
	    table.addPlayer(cedric);
	    table.addPlayer(guy);
	} catch (PlayerListFullException e) {
	    fail(e.getMessage());
	}
	gameControl = new GameControl(gameMediator, table);
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
	table = new Table(new TableId(0), new GameProperty(10, new Limit(10)));
	try {
	    table.addPlayer(kenzo);
	    table.addPlayer(cedric);
	    table.addPlayer(guy);
	} catch (PlayerListFullException e) {
	    fail(e.getMessage());
	}
	gameControl = new GameControl(gameMediator, table);
	BettingRulesTest.logger.info("Game Properties:");
	BettingRulesTest.logger.info("Small Blind: "
		+ table.getGameProperty().getSmallBlind());
	BettingRulesTest.logger.info("Big Blind: "
		+ table.getGameProperty().getBigBlind());
	BettingRulesTest.logger.info("Betting Rules: "
		+ gameControl.getGame().getGameProperty().getBettingRules()
			.toString());
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
