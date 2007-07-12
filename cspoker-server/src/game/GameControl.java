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

package game;

import game.actions.IllegalActionException;
import game.player.Player;
import game.rounds.PreFlopRound;
import game.rounds.Round;
import game.rounds.WaitingRound;
import game.rules.Limit;

/**
 * This class is responsible to control the flow of the game.
 * This class can change the state of users and the table.
 * 
 * @author Kenzo
 *
 */
public class GameControl implements PlayerAction{
	
	/**********************************************************
	 * Variables
	 **********************************************************/
	
	/**
	 * This variable contains all game elements,
	 * such as players and table.
	 */
	private final Game game;
	
	/**
	 * The variable containing the round in which the current game is.
	 * 
	 */
	private Round round;	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	/**
	 * Construct a new game control.
	 */
	public GameControl(){
		//TODO
		game = new Game(new GameProperty());
		round = new PreFlopRound(game);
	}
	
	/**********************************************************
	 * Player methods
	 **********************************************************/
	
	/**
	 * If there is no bet on the table and you do not wish to place a bet.
	 * You may only check when there are no prior bets.
	 * 
	 * @param	player
	 * 			The player who checks.
	 * @see PlayerAction
	 */
	public void bet(Player player, int amount) throws IllegalActionException{
		round.bet(player, amount);
		checkIfEndedAndChangeRound();
	}
	
	/**
	 * To put into the pot an amount of money equal to
	 * the most recent bet or raise.
	 * 
	 * @param 	player
	 * 			The player who calls.
	 * @see PlayerAction
	 */
	public void call(Player player) throws IllegalActionException{
		round.call(player);
		checkIfEndedAndChangeRound();
	}
	
	/**
	 * If there is no bet on the table and you do not wish to place a bet.
	 * You may only check when there are no prior bets.
	 * 
	 * @param	player
	 * 			The player who checks.
	 * @see PlayerAction
	 */
	public void check(Player player) throws IllegalActionException{
		round.check(player);
		checkIfEndedAndChangeRound();
	}
	
	public void raise(Player player, int amount) throws IllegalActionException{
		round.raise(player, amount);
		checkIfEndedAndChangeRound();
	}
	
	public void fold(Player player) throws IllegalActionException{
		round.fold(player);
		checkIfEndedAndChangeRound();
	}
	
	public void deal(Player player) throws IllegalActionException{
		round.deal(player);
		checkIfEndedAndChangeRound();
	}
	
	public void allIn(Player player) throws IllegalActionException {
		round.allIn(player);
		checkIfEndedAndChangeRound();
	}
	
	/**********************************************************
	 * Round change logic
	 **********************************************************/
	
	/**
	 * Check if this round has ended and change to
	 * the next round if it's the case.
	 */
	private void checkIfEndedAndChangeRound(){
		if(round.isRoundEnded()){
			changeToNextRound();
		}
	}
	
	/**
	 * End this round and change the round to the next round.
	 * 
	 * If only one player is left, the next round should
	 * be a waiting round.
	 * 
	 */
	private void changeToNextRound(){
		round.endRound();
		if(round.onlyOnePlayerLeft()){
			round = new WaitingRound(game);
		}else{
			round = round.getNextRound();
		}
	}
}
