package game.tests;

import game.Game;
import game.GameControl;
import game.GameProperty;
import game.PlayerListFullException;
import game.Table;
import game.actions.IllegalActionException;
import game.chips.IllegalValueException;
import game.player.Player;
import game.rules.Limit;
import junit.framework.TestCase;

public class BettingRulesTest extends TestCase {

private Player kenzo;
	
	private Player cedric;
	
	private  Player guy;
	
	private Table table;
	
	private GameControl gameControl;
	
	@Override
	protected void setUp(){
		try {
			kenzo = new Player(1, "Kenzo", 100);
			cedric = new Player(2, "Cedric", 100);
			guy = new Player(3, "Guy", 100);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		}
	}
	
	public void testLimitRules(){
		table = new Table(new GameProperty(10,new Limit(10)));
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		gameControl = new GameControl(table);
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
		
		//Flop Round
		try {
			gameControl.call(game.getCurrentPlayer());
			
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println("Common Cards: "+game.getOpenCards());
		
		//Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println("Common Cards: "+game.getOpenCards());
		
		//Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println("\n\n");
	}

}
