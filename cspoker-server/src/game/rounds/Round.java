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
import game.actions.IllegalActionException;
import game.chips.IllegalValueException;
import game.player.AllInPlayer;
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
	 * Bet
	 **********************************************************/
	
	/**
	 * Set the bet of this round to the given value.
	 * 
	 */
	private void setBet(int value){
		bet = value; 
	}
	
	/**
	 * Returns the current bet value of this round.
	 * 
	 * @return The current bet value of this round.
	 */
	public int getBet(){
		return bet;
	}
	
	/**
	 * Raise the bet with given amount.
	 * 
	 * @param 	amount
	 * 			The amount to raise the bet with.
	 * @pre 	The amount should be positive.
	 *			|amount>0
	 * @effect	Set the bet of this round to the current bet
	 * 			raised with given amount.
	 *		   	|setBet(getBet()+amount)
	 */
	private void raiseBetWith(int amount){
		if(amount<=0)
			throw new IllegalArgumentException();
		setBet(getBet()+amount);
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
		if(!Action.CHECK.canDoAction(this, player))
			throw new IllegalActionException(player, Action.CHECK);
		game.nextPlayer();
	}
	
	@Override
	public void bet(Player player, int amount) throws IllegalActionException{
		if(!Action.BET.canDoAction(this, player))
			throw new IllegalActionException(player, Action.BET);
		
		if(amount==0)
			throw new IllegalActionException(player, Action.RAISE, "Can not bet with 0 chips. Did you mean check?");
		
		try {
			player.transferAmountToBettedPile(amountToIncreaseBettedPileWith(player)+amount);
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player, Action.RAISE, e.getMessage());
		}
		raiseBetWith(amount);
		playerMadeEvent(player);
		game.nextPlayer();
	}
	
	@Override
	public void call(Player player) throws IllegalActionException{
		if(!Action.CALL.canDoAction(this, player))
			throw new IllegalActionException(player, Action.CALL);
		
		try {
			player.transferAmountToBettedPile(amountToIncreaseBettedPileWith(player));
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player, Action.CALL, e.getMessage());
		}
		
		game.nextPlayer();
	}
	
	@Override
	public void raise(Player player, int amount) throws IllegalActionException{
		if(!Action.RAISE.canDoAction(this, player))
			throw new IllegalActionException(player, Action.RAISE);

		if(amount==0)
			throw new IllegalActionException(player, Action.RAISE, "Can not raise with 0 chips. Did you mean call?");
		
		try {
			player.transferAmountToBettedPile(amountToIncreaseBettedPileWith(player)+amount);
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player, Action.RAISE, e.getMessage());
		}
		raiseBetWith(amount);
		playerMadeEvent(player);
		game.nextPlayer();
	}
	
	@Override
	public void fold(Player player) throws IllegalActionException{
		if(!Action.FOLD.canDoAction(this, player))
			throw new IllegalActionException(player, Action.FOLD);
		
		game.removePlayerFromCurrentDeal(player);
		
		//removing from game, automatically switches
		//to next player.
	}
	
	@Override
	public void deal(Player player) throws IllegalActionException{
		if(!Action.DEAL.canDoAction(this, player))
			throw new IllegalActionException(player, Action.DEAL);
		
	}
	
	@Override
	public void allIn(Player player) throws IllegalActionException {
		// TODO Auto-generated method stub
		
	}
	
	/**********************************************************
	 * Round Logic
	 **********************************************************/
	
	
	/**
	 * Check whether the round is ended or not.
	 * 
	 * @return 	True if the round is ended,
	 * 			false otherwise.
	 */
	public boolean roundEnded(){
		return lastEventPlayer.equals(game.getCurrentPlayer());
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
	 * Set the last event player
	 * to the given player.
	 * 
	 * @param 	player
	 * 			The player who did the last event.
	 */
	private void playerMadeEvent(Player player){
		lastEventPlayer = player;
	}
	
	public boolean someOneHasBet(){
		return bet>0;
	}
	
	public boolean onTurn(Player player){
		return game.getCurrentPlayer().equals(player);
	}
	
	public boolean isBettingRound(){
		return true;
	}
	
	protected void makeSidePots(){
		for(AllInPlayer player:game.getAllInPlayers()){
			int betToSidePot = player.getBetValue();			
		}
	}
	
	/**
	 * Returns how many chips a player
	 * must transfer to the betted pile
	 * to equal the current bet.
	 * 
	 * @param player
	 * 			The player who wants to know
	 * 			how many chips to transfer.
	 * @return	The number of chips the player
	 * 			must transfer to the betted pile
	 * 			to equal the current bet.
	 */
	protected int amountToIncreaseBettedPileWith(Player player){
		return getBet()-player.getBettedChips().getValue();
	}

}
