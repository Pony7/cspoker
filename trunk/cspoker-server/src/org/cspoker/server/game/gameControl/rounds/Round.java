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

package org.cspoker.server.game.gameControl.rounds;

import java.util.ArrayList;
import java.util.List;

import org.cspoker.server.game.GameMediator;
import org.cspoker.server.game.events.NewDealEvent;
import org.cspoker.server.game.gameControl.Game;
import org.cspoker.server.game.gameControl.IllegalActionException;
import org.cspoker.server.game.gameControl.PlayerAction;
import org.cspoker.server.game.gameControl.rules.BettingRules;
import org.cspoker.server.game.player.Player;
import org.cspoker.server.game.player.SavedPlayer;


/**
 * An abstract class to represent rounds.
 * A player can do actions in a round,
 * such as checking, betting, ...
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
	protected Player lastEventPlayer;

	/**
	 * The variable containing the game in which
	 * this round takes place.
	 */
	protected final Game game;

	/**
	 * The variable containing the game mediator.
	 */
	protected final GameMediator gameMediator;

	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Initialize a new round for given game.
	 *
	 * @param 	game
	 * 			The game to create a new round for.
	 */
	public Round(GameMediator gameMediator, Game game){
		this.gameMediator = gameMediator;
		this.game = game;
		getBettingRules().setBetPlaced(false);
		getBettingRules().clearNBRaises();
		lastEventPlayer = game.getFirstToActPlayer();
		game.setCurrentPlayer(getGame().getFirstToActPlayer());
	}

	/**
	 * Returns the game this round is part of.
	 *
	 * @return The game this round is part of.
	 */
	public Game getGame(){
		return game;
	}

	/**********************************************************
	 * Betting Rules
	 **********************************************************/
	/**
	 * Returns the betting game.rounds.rules for this round
	 */
	public BettingRules getBettingRules(){
		return getGame().getGameProperty().getBettingRules();
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
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
     * @see		PlayerAction
	 */
	public void check(Player player) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not check in this round.");
	}

	/**
	 * The player puts money in the pot.
	 *
	 * @param 	player
	 * 			The player who puts a bet.
	 * @param 	amount
	 * 			The amount of the bet.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
     * @see		PlayerAction
	 */
	public void bet(Player player, int amount) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not bet "+amount+" chips in this round.");
	}

	/**
	 * To put into the pot an amount of money equal to
	 * the most recent bet or raise.
	 *
	 * @param 	player
	 * 			The player who calls.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 * @see		PlayerAction
	 */
	public void call(Player player) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not call in this round.");
	}

	/**
	 * The player puts money in the pot.
	 *
	 * @param 	player
	 * 			The player who puts a bet.
	 * @param 	amount
	 * 			The amount of the bet.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 * @see		PlayerAction
	 */
	public void raise(Player player, int amount) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not raise with "+amount+" chips in this round.");
	}

	/**
	 * The given player folds the cards.
	 *
	 * The player will not be able to take any actions
	 * in the coming rounds of the current deal.
	 *
	 * @param 	player
	 * 			The player who folds.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 */
	public void fold(Player player) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not fold in this round.");
	}

	/**
	 * The player who the dealer-button has been dealt to
	 * can choose to start the deal.
	 * From that moment, new players can not join the on-going deal.
	 *
	 * @param 	player
	 * 			The player who deals.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 * @see		PlayerAction
	 */
	public void deal(Player player) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not deal in this round.");
	}

	/**
	 * The given player goes all-in.
	 *
	 * @param 	player
	 * 			The player who goes all-in.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 * @see		PlayerAction
	 */
	public void allIn(Player player) throws IllegalActionException {
		throw new IllegalActionException(player.getName()+" can not go all-in in this round.");
	}

	/**********************************************************
	 * Round Logic
	 **********************************************************/


	/**
	 * Check whether the round is ended or not.
	 *
	 * It is the case when there are no more active players,
	 * or when the last event player is the next player.
	 *
	 * @return 	True if the round is ended,
	 * 			false otherwise.
	 */
	public boolean isRoundEnded(){
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
	protected void playerMadeEvent(Player player){
		lastEventPlayer = player;
	}

	public boolean onTurn(Player player){
		return game.getCurrentPlayer().equals(player);
	}


	protected void removeBrokePlayers(){
		for(Player player:getGame().getTable().getPlayers()){
			if(player.getStack().getValue()==0){
				try {
					getGame().leaveGame(player);
				} catch (IllegalActionException e) {
					throw new IllegalStateException();
				}
			}
		}
	}

	public abstract boolean isLowBettingRound();

	public abstract boolean isHighBettingRound();

	public int getCurrentPotValue(){
		return 0;
	}

	protected void newDealRound(){
		removeBrokePlayers();
		game.setToInitialHandPlayers();
		List<SavedPlayer> players = new ArrayList<SavedPlayer>(game.getNbCurrentDealPlayers());
		for(Player player:game.getCurrentDealPlayers()){
			players.add(player.getSavedPlayer());
		}
		gameMediator.publishNewDealEvent(new NewDealEvent(players, game.getDealer().getSavedPlayer()));
		game.setCurrentPlayer(game.getDealer());
	}

	protected Round getNewDealRound(){
		if(game.getNbCurrentDealPlayers()<=1)
			return new WaitingRound(gameMediator, game);
		else{
			newDealRound();
			game.dealNewHand();
			return new PreFlopRound(gameMediator, game);
		}
	}
}
