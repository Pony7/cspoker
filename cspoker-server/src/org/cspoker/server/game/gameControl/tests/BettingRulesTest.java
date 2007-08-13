package org.cspoker.server.game.gameControl.tests;

import org.cspoker.server.game.GameMediator;
import org.cspoker.server.game.TableId;
import org.cspoker.server.game.elements.chips.IllegalValueException;
import org.cspoker.server.game.elements.table.PlayerListFullException;
import org.cspoker.server.game.elements.table.Table;
import org.cspoker.server.game.gameControl.Game;
import org.cspoker.server.game.gameControl.GameControl;
import org.cspoker.server.game.gameControl.GameProperty;
import org.cspoker.server.game.gameControl.actions.IllegalActionException;
import org.cspoker.server.game.gameControl.rules.Limit;
import org.cspoker.server.game.gameControl.rules.NoLimit;
import org.cspoker.server.game.gameControl.rules.PotLimit;
import org.cspoker.server.game.player.Player;
import org.cspoker.server.game.player.PlayerFactory;

import junit.framework.TestCase;

public class BettingRulesTest extends TestCase {

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
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo",100);
			cedric = playerFactory.createNewPlayer("Cedric", 100); 
			guy = playerFactory.createNewPlayer("Guy", 100);
			gameMediator = new GameMediator();
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		}
	}
	public void testNoLimitRules(){
		table = new Table(new TableId(0), new GameProperty(10,new NoLimit()));
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		gameControl = new GameControl(gameMediator, table);
		System.out.println("Game Properties:");
		System.out.println("Small Blind: "+table.getGameProperty().getSmallBlind());
		System.out.println("Big Blind: "+table.getGameProperty().getBigBlind());
		System.out.println("Betting Rules: "+gameControl.getGame().getGameProperty().getBettingRules().toString());
		Game game = gameControl.getGame();

		Player dealer  = game.getDealer();
		System.out.println("Dealer: "+game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println(game.getCurrentDealPlayers());
		System.out.println("Kenzo's Cards: "+kenzo.getPocketCards());
		System.out.println("Cedric's Cards: "+cedric.getPocketCards());
		System.out.println("Guy's Cards: "+guy.getPocketCards());
		try {
			gameControl.raise(game.getCurrentPlayer(), 10);
			gameControl.raise(game.getCurrentPlayer(), 5);
			fail("Exception Expected.");
		} catch (IllegalActionException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("\n\n");
	}
	public void testPotLimitRules(){
		table = new Table(new TableId(0), new GameProperty(10,new PotLimit()));
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		gameControl = new GameControl(gameMediator, table);
		System.out.println("Game Properties:");
		System.out.println("Small Blind: "+table.getGameProperty().getSmallBlind());
		System.out.println("Big Blind: "+table.getGameProperty().getBigBlind());
		System.out.println("Betting Rules: "+gameControl.getGame().getGameProperty().getBettingRules().toString());
		Game game = gameControl.getGame();

		Player dealer  = game.getDealer();
		System.out.println("Dealer: "+game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println(game.getCurrentDealPlayers());
		System.out.println("Kenzo's Cards: "+kenzo.getPocketCards());
		System.out.println("Cedric's Cards: "+cedric.getPocketCards());
		System.out.println("Guy's Cards: "+guy.getPocketCards());

		try {
			gameControl.raise(game.getCurrentPlayer(), 10);
			assert(false);
		} catch (IllegalActionException e) {
			System.out.println(e.getMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(), 5);
			assert(false);
		} catch (IllegalActionException e) {
			System.out.println(e.getMessage());
		}

		try {
			gameControl.raise(game.getCurrentPlayer(),gameControl.getRound().getCurrentPotValue()+10);
			assert(false);
		} catch (IllegalActionException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("\n\n");
	}
	public void testLimitRules2(){
		table = new Table(new TableId(0), new GameProperty(10,new Limit(10)));
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		gameControl = new GameControl(gameMediator, table);
		System.out.println("Game Properties:");
		System.out.println("Small Blind: "+table.getGameProperty().getSmallBlind());
		System.out.println("Big Blind: "+table.getGameProperty().getBigBlind());
		System.out.println("Betting Rules: "+gameControl.getGame().getGameProperty().getBettingRules().toString());
		Game game = gameControl.getGame();

		Player dealer  = game.getDealer();
		System.out.println("Dealer: "+game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println(game.getCurrentDealPlayers());
		System.out.println("Kenzo's Cards: "+kenzo.getPocketCards());
		System.out.println("Cedric's Cards: "+cedric.getPocketCards());
		System.out.println("Guy's Cards: "+guy.getPocketCards());
		try {
			gameControl.raise(game.getCurrentPlayer(), 10);
			gameControl.raise(game.getCurrentPlayer(), 10);
			gameControl.raise(game.getCurrentPlayer(), 10);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(), 10);
			assert(false);
		} catch (IllegalActionException e) {
			System.out.println(e.getMessage());
		}
	}
	public void testLimitRules1(){
		table = new Table(new TableId(0), new GameProperty(10,new Limit(10)));
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		gameControl = new GameControl(gameMediator, table);
		System.out.println("Game Properties:");
		System.out.println("Small Blind: "+table.getGameProperty().getSmallBlind());
		System.out.println("Big Blind: "+table.getGameProperty().getBigBlind());
		System.out.println("Betting Rules: "+gameControl.getGame().getGameProperty().getBettingRules().toString());
		Game game = gameControl.getGame();

		Player dealer  = game.getDealer();
		System.out.println("Dealer: "+game.getDealer().getName());

		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println(game.getCurrentDealPlayers());
		System.out.println("Kenzo's Cards: "+kenzo.getPocketCards());
		System.out.println("Cedric's Cards: "+cedric.getPocketCards());
		System.out.println("Guy's Cards: "+guy.getPocketCards());


		try {
			gameControl.raise(game.getCurrentPlayer(), 1);
			assert(false);
		} catch (IllegalActionException e) {
			System.out.println(e.getMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(),-1);
			assert(false);
		} catch (IllegalActionException e) {
			System.out.println(e.getMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(),0);
			assert(false);
		} catch (IllegalActionException e) {
			System.out.println(e.getMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(),200);
			assert(false);
		} catch (IllegalActionException e) {
			System.out.println(e.getMessage());
		}
		try {
			gameControl.raise(game.getCurrentPlayer(),80);
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println(game.getCurrentDealPlayers());

		System.out.println("Common Cards: "+game.getCommunityCards());
		System.out.println("\n\n");
	}

}
