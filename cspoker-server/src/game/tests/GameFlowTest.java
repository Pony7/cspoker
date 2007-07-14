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


package game.tests;

import game.Game;
import game.GameControl;
import game.GameProperty;
import game.PlayerListFullException;
import game.Table;
import game.actions.IllegalActionException;
import game.chips.IllegalValueException;
import game.player.Player;
import junit.framework.TestCase;

public class GameFlowTest extends TestCase {
	
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
			
			table = new Table(new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
			gameControl = new GameControl(table);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
	}
	
	public void testCase1(){
		System.out.println("Game Properties:");
		System.out.println("Small Blind: "+table.getGameProperty().getSmallBlind());
		System.out.println("Big Blind: "+table.getGameProperty().getBigBlind());
		System.out.println("");
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
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println(game.getCurrentDealPlayers());
		System.out.println("Common Cards: "+game.getOpenCards());
		
		//Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
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
		
		//New game
		
		System.out.println(game.getCurrentDealPlayers());
		dealer  = game.getDealer();
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
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println(game.getCurrentDealPlayers());
		System.out.println("Common Cards: "+game.getOpenCards());
		
		//Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
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
		System.out.println(game.getCurrentDealPlayers());
		System.out.println("Dealer: "+game.getDealer().getName());
	}
	
	public void testAllInBigBlindCase(){
		try {
			kenzo = new Player(1, "Kenzo", 9);
			cedric = new Player(2, "Cedric", 9);
			guy = new Player(3, "Guy", 9);
			
			table = new Table(new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
			gameControl = new GameControl(table);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		
		System.out.println("Game Properties:");
		System.out.println("Small Blind: "+table.getGameProperty().getSmallBlind());
		System.out.println("Big Blind: "+table.getGameProperty().getBigBlind());
		System.out.println("");
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
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println(game.getCurrentDealPlayers());
		System.out.println("Common Cards: "+game.getOpenCards());
		
		//Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println("Common Cards: "+game.getOpenCards());
		
		//Turn Round
		try {
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
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		
		dealer  = game.getDealer();
		System.out.println("Dealer: "+dealer.getName());
		try {
			gameControl.deal(dealer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println(game.getCurrentDealPlayers());
	}
	
	public void testCase2(){
		System.out.println("Game Properties:");
		System.out.println("Small Blind: "+table.getGameProperty().getSmallBlind());
		System.out.println("Big Blind: "+table.getGameProperty().getBigBlind());
		System.out.println("");
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
			gameControl.call(game.getCurrentPlayer());
			gameControl.raise(game.getCurrentPlayer(),10);
			gameControl.call(game.getCurrentPlayer());
			gameControl.raise(game.getCurrentPlayer(),20);
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		System.out.println(game.getCurrentDealPlayers());
		System.out.println("Common Cards: "+game.getOpenCards());
		
		//Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
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
		
		//New game
		
		System.out.println(game.getCurrentDealPlayers());
	}
}
