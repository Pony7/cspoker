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

package game.rounds;

import game.Game;
import game.PlayerAction;
import game.actions.Action;
import game.chips.IllegalValueException;
import game.player.Player;

/**
 * An abstract class to represent rounds.
 * A player can do actions in a round,
 * such as checking, betting, ...
 * 
 * A state pattern is used.
 * 
 * @author Kenzo
 *
 */
public abstract class Round implements PlayerAction{
	
	/**********************************************************
	 * Variables
	 **********************************************************/
	
	/**
	 * The last event player is the last player
	 * that has done significant change,
	 * such as a raise.
	 * 
	 * If the next player is the last event player,
	 * the round is over.
	 * 
	 * It is initialized in each game as the first better
	 * after the big blind, in every next round,
	 * it is the player on to the left side of the player
	 * with the dealer-button.
	 */
	private Player lastEventPlayer;
	
	private final Game game;
	
	private int bet;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	/**
	 * Initialize a new round for given game.
	 * 
	 * @param 	game
	 * 			The game to create a new round for.
	 */
	public Round(Game game){
		this.game = game;
	}
	
	protected Game getGame(){
		return game;
	}
	
	/**********************************************************
	 * Bidding methods
	 **********************************************************/
	
	/**
	 * If there is no bet on the table and you do not wish to place a bet.
	 * You may only check when there are no prior bets.
	 * 
	 * @param	player
	 * 			The player who checks.
	 * @see PlayerAction
	 */
	@Override
	public void check(Player player) throws IllegalActionException{
		if(Action.CHECK.canDoAction(this, player)){
			
		}
		
		//Only if player may check.
		game.nextPlayer();
	}
	
	public void bet(Player player, int amount){
		
		
		playerMadeEvent(player);
	}
	
	public void call(Player player){
		
	}
	
	public void raise(Player player, int amount) throws IllegalActionException{
		try {
			player.getChips().transferAmountTo((bet+amount)-player.getBettedChips().getValue(), player.getBettedChips());
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player, Action.RAISE, e.getMessage());
		}
		bet=bet+amount;
		playerMadeEvent(player);
	}
	
	public void fold(Player player){
		game.removePlayerFromCurrentDeal(player);
	}
	
	public void deal(Player player){
		
	}
	
	
	public boolean roundEnded(){
		return lastEventPlayer == game.getCurrentPlayer();
	}
	
	/**
	 * End the current round.
	 * 
	 */
	public abstract void endRound();
	
	/**
	 * Returns the next round.
	 * 
	 */
	public abstract Round getNextRound();
	
	/**
	 * 
	 * @param player
	 */
	private void playerMadeEvent(Player player){
		lastEventPlayer = player;
	}
	
	private boolean canCheck(Player player){
		return onTurn(player) && (bet==0);
	}
	
	public boolean someOneHasBet(){
		return bet>0;
	}
	
	public boolean onTurn(Player player){
		return game.getCurrentPlayer().equals(player);
	}

}
