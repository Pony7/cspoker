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
import game.rounds.Round;
import game.rounds.WaitingRound;

/**
 * This class is responsible to control the flow of the game.
 * This class changes the state (round) in which the players are.
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
	 * Construct a new game control with given table.
	 */
	public GameControl(Table table){
		game = new Game(table);
		round = new WaitingRound(game);
		table.getGameProperty().setGameControl(this);
	}
	
	/**
	 * Returns the game controlled by this game controller.
	 * 
	 * @return The game controlled by this game controller.
	 */
	public Game getGame(){
		return game;
	}
	
	public Round getRound(){
		return round;
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
		if(amount==player.getStack().getValue()){
			allIn(player);
		}else{
		round.bet(player, amount);
		System.out.println(player.getName()+" bets "+amount+".");
		checkIfEndedAndChangeRound();
		}
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
		if(round.getBet()==player.getStack().getValue()){
			allIn(player);
		}else{
			round.call(player);
			System.out.println(player.getName()+" calls.");
			checkIfEndedAndChangeRound();
		}
//		if(round.getBet()>player.getStack().getValue()){
//			//TODO: you can't split the pot here,
//			//it should be done at the end of the round.
//			//it is taken care of by goAllIn() method.
//			//and makeSidePots()
//		}
//		else{
//		}
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
		System.out.println(player.getName()+" checks.");
		checkIfEndedAndChangeRound();
	}
	
	public void raise(Player player, int amount) throws IllegalActionException{
		if(amount==player.getStack().getValue()){
			//TODO This does not work. The call value should
			//also be considered.
			//see amountToIncreaseBettedPileWith method.
			allIn(player);
		}else{
			round.raise(player, amount);
			System.out.println(player.getName()+" raises with "+amount+".");
			checkIfEndedAndChangeRound();
		}
	}
	
	public void fold(Player player) throws IllegalActionException{
		round.fold(player);
		System.out.println(player.getName()+" folds.");
		checkIfEndedAndChangeRound();
	}
	
	public void deal(Player player) throws IllegalActionException{
		round.deal(player);
		System.out.println(player.getName()+" deals.");
		checkIfEndedAndChangeRound();
	}
	
	public void allIn(Player player) throws IllegalActionException {
		round.allIn(player);
//		System.out.println(player.getName()+" goes all in.");
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
		if(round.onlyOnePlayerLeft()){
			round.endRound();
			round = new WaitingRound(game);
		}else{
			do{
				round.endRound();
				round = round.getNextRound();
			}while(!(round instanceof WaitingRound) && (getGame().getNbCurrentDealPlayers()==0));
		}
	}
}
