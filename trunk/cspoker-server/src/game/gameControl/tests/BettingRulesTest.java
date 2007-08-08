package game.gameControl.tests;

import game.GameMediator;
import game.PlayerId;
import game.TableId;
import game.elements.chips.IllegalValueException;
import game.elements.player.Player;
import game.elements.table.PlayerListFullException;
import game.elements.table.Table;
import game.gameControl.Game;
import game.gameControl.GameControl;
import game.gameControl.GameProperty;
import game.gameControl.actions.IllegalActionException;
import game.gameControl.rules.Limit;
import game.gameControl.rules.NoLimit;
import game.gameControl.rules.PotLimit;
import junit.framework.TestCase;

public class BettingRulesTest extends TestCase {

private Player kenzo;

	private Player cedric;

	private  Player guy;

	private Table table;

	private GameControl gameControl;

	private GameMediator gameMediator;

	@Override
	protected void setUp(){
		try {
			kenzo = new Player(new PlayerId(1), "Kenzo", 100);
			cedric = new Player(new PlayerId(2), "Cedric", 100);
			guy = new Player(new PlayerId(3), "Guy", 100);
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

		System.out.println("Common Cards: "+game.getOpenCards());
		System.out.println("\n\n");
	}

}
