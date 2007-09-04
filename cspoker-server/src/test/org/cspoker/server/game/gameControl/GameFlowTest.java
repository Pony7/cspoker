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


package org.cspoker.server.game.gameControl;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.server.game.GameMediator;
import org.cspoker.server.game.TableId;
import org.cspoker.server.game.elements.chips.IllegalValueException;
import org.cspoker.server.game.elements.table.PlayerListFullException;
import org.cspoker.server.game.elements.table.Table;
import org.cspoker.server.game.player.Player;
import org.cspoker.server.game.player.PlayerFactory;

public class GameFlowTest extends TestCase {
	private static Logger logger = Logger.getLogger(GameFlowTest.class);

	private Player kenzo;

	private Player cedric;

	private  Player guy;

	private Table table;

	private GameControl gameControl;

	private GameMediator gameMediator;

	private PlayerFactory playerFactory;

	@Override
	protected void setUp(){
		playerFactory = new PlayerFactory();
		GameFlowTest.logger.info("**********************************************************");
		GameFlowTest.logger.info("* New Game                                               *");
		GameFlowTest.logger.info("**********************************************************");
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo",100);
			cedric = playerFactory.createNewPlayer("Cedric", 100);
			guy = playerFactory.createNewPlayer("Guy", 100);

			gameMediator = new GameMediator();

			table = new Table(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
			gameControl = new GameControl(gameMediator, table);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
	}

	public void testCase1(){
		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		GameFlowTest.logger.info("Betting Rules: " + gameControl.getGame().getGameProperty().getBettingRules().toString());
		Game game = gameControl.getGame();

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());


		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());

			//Big Blind Checks.
			gameControl.check(game.getCurrentPlayer());

		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		//New game

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

//		try {
//			gameControl.deal(dealer);
//		} catch (IllegalActionException e) {
//			fail(e.getMessage());
//		}
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

		//Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Final Round
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

	public void testCase2(){
		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());


		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.raise(game.getCurrentPlayer(),10);
			gameControl.call(game.getCurrentPlayer());
			gameControl.raise(game.getCurrentPlayer(),20);
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testBigBlindRaisesCase(){
		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		GameFlowTest.logger.info("Betting Rules: " + gameControl.getGame().getGameProperty().getBettingRules().toString());
		Game game = gameControl.getGame();

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());


		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());

			//Big Blind Raises.
			gameControl.raise(game.getCurrentPlayer(),20);

			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());

		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testAllInSmallBlindCase(){
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo",100);
			cedric = playerFactory.createNewPlayer("Cedric", 4);
			guy = playerFactory.createNewPlayer("Guy", 100);

			table = new Table(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
			gameControl = new GameControl(gameMediator, table);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		game.changeDealer(kenzo);

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
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
		//Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Next game...");

		dealer  = game.getDealer();
		GameFlowTest.logger.info("Current Player:" + game.getCurrentPlayer());
		GameFlowTest.logger.info("Next Dealer:" + game.getNextDealer());
		GameFlowTest.logger.info(game.getFirstToActPlayer());
		GameFlowTest.logger.info("Dealer: " + dealer.getName());
//		try {
//			gameControl.deal(dealer);
//		} catch (IllegalActionException e) {
//			fail(e.getMessage());
//		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
	}

	public void testAllInBigBlindCase(){
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo",100);
			cedric = playerFactory.createNewPlayer("Cedric", 100);
			guy = playerFactory.createNewPlayer("Guy", 9);

			table = new Table(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
			gameControl = new GameControl(gameMediator, table);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		game.changeDealer(kenzo);

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			GameFlowTest.logger.info(game.getCurrentPlayer());
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
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
		//Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + dealer.getName());
//		try {
//			gameControl.deal(dealer);
//		} catch (IllegalActionException e) {
//			fail(e.getMessage());
//		}
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
	}

	public void testBothBlindsAllInCase(){
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo",100);
			cedric = playerFactory.createNewPlayer("Cedric", 4);
			guy = playerFactory.createNewPlayer("Guy", 9);

			table = new Table(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
			gameControl = new GameControl(gameMediator, table);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		game.changeDealer(kenzo);

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			GameFlowTest.logger.info(game.getCurrentPlayer());
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
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

	public void test2AllInOneActivePlayerCase(){
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo",100);
			cedric = playerFactory.createNewPlayer("Cedric", 100);
			guy = playerFactory.createNewPlayer("Guy", 200);

			table = new Table(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
			gameControl = new GameControl(gameMediator, table);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		game.changeDealer(kenzo);

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
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
		//Game continues automatically
	}

	public void testAllAllInCase(){
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo",200);
			cedric = playerFactory.createNewPlayer("Cedric", 100);
			guy = playerFactory.createNewPlayer("Guy", 150);

			table = new Table(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
			gameControl = new GameControl(gameMediator, table);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		GameFlowTest.logger.info("Betting Rules: " + gameControl.getGame().getGameProperty().getBettingRules().toString());
		Game game = gameControl.getGame();

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
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

		//Flop Round
		try {
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		//New game

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());
	}

	public void testOnlyOneActivePlayer(){
		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
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
	}

	public void testOnlyOneAllInPlayer(){
		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
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

	public void testOnlyOneAllInPlayer2(){
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo",100);
			cedric = playerFactory.createNewPlayer("Cedric", 200);
			guy = playerFactory.createNewPlayer("Guy", 200);

			table = new Table(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
			gameControl = new GameControl(gameMediator, table);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		game.changeDealer(kenzo);

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
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

	public void test2PlayersOneFold(){
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo",500);
			cedric = playerFactory.createNewPlayer("Cedric", 500);

			table = new Table(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			gameControl = new GameControl(gameMediator, table);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: " + table.getGameProperty().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: " + table.getGameProperty().getBigBlind());
		Game game = gameControl.getGame();

		game.changeDealer(kenzo);

		Player dealer  = game.getDealer();
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());

		try {
			GameFlowTest.logger.info(game.getCurrentPlayer());
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		try {
			gameControl.call(kenzo);
			gameControl.raise(cedric,20);
			gameControl.call(kenzo);

			gameControl.bet(cedric,20);
			gameControl.call(kenzo);

			gameControl.bet(cedric,20);
			gameControl.call(kenzo);

			gameControl.bet(cedric,20);
			gameControl.raise(kenzo,50);
			gameControl.fold(cedric);


		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

}
