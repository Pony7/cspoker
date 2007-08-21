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

import org.cspoker.server.game.GameMediator;
import org.cspoker.server.game.elements.table.PlayerListFullException;
import org.cspoker.server.game.elements.table.Table;
import org.cspoker.server.game.events.NextPlayerEvent;
import org.cspoker.server.game.events.StackChangedEvent;
import org.cspoker.server.game.events.playerActionEvents.BetEvent;
import org.cspoker.server.game.events.playerActionEvents.CallEvent;
import org.cspoker.server.game.events.playerActionEvents.CheckEvent;
import org.cspoker.server.game.events.playerActionEvents.DealEvent;
import org.cspoker.server.game.events.playerActionEvents.FoldEvent;
import org.cspoker.server.game.events.playerActionEvents.RaiseEvent;
import org.cspoker.server.game.gameControl.rounds.BettingRound;
import org.cspoker.server.game.gameControl.rounds.Round;
import org.cspoker.server.game.gameControl.rounds.WaitingRound;
import org.cspoker.server.game.player.Player;

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
	 * The variable containing the round
	 * in which the current game is.
	 *
	 */
	private Round round;


	private final GameMediator gameMediator;

	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Construct a new game control with given table.
	 *
	 */
	public GameControl(GameMediator gameMediator, Table table){
		this.gameMediator = gameMediator;
		gameMediator.setGameControl(this);
		game = new Game(table);
		round = new WaitingRound(gameMediator, game);
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
	 * @see 	PlayerAction
	 */
	public void bet(Player player, int amount) throws IllegalActionException{
		round.bet(player, amount);
		gameMediator.publishBetEvent(new BetEvent(player.getSavedPlayer(), amount));
		gameMediator.publishStackChanged(new StackChangedEvent(player.getSavedPlayer()));
		System.out.println(player.getName()+" bets "+amount+".");
		checkIfEndedAndChangeRound();
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
	 * @see 	PlayerAction
	 */
	public void call(Player player) throws IllegalActionException{
		round.call(player);
		gameMediator.publishCallEvent(new CallEvent(player.getSavedPlayer()));
		gameMediator.publishStackChanged(new StackChangedEvent(player.getSavedPlayer()));
		System.out.println(player.getName()+" calls.");
		checkIfEndedAndChangeRound();
	}

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
	 * @see 	PlayerAction
	 */
	public void check(Player player) throws IllegalActionException{
		round.check(player);
		gameMediator.publishCheckEvent(new CheckEvent(player.getSavedPlayer()));
		System.out.println(player.getName()+" checks.");
		checkIfEndedAndChangeRound();
	}

	/**
	 * Raise the bet with given amount.
	 *
	 * @param	player
	 * 			The player who raises the current bet.
	 * @param 	amount
	 * 			The amount with which to raise the bet.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 * @see 	PlayerAction
	 */
	public void raise(Player player, int amount) throws IllegalActionException{
		round.raise(player, amount);
		gameMediator.publishRaiseEvent(new RaiseEvent(player.getSavedPlayer(), amount));
		gameMediator.publishStackChanged(new StackChangedEvent(player.getSavedPlayer()));
		System.out.println(player.getName()+" raises with "+amount+".");
		checkIfEndedAndChangeRound();
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
	 * @see 	PlayerAction
	 */
	public void fold(Player player) throws IllegalActionException{
		round.fold(player);
		gameMediator.publishFoldEvent(new FoldEvent(player.getSavedPlayer()));
		System.out.println(player.getName()+" folds.");
		checkIfEndedAndChangeRound();
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
	 * @see 	PlayerAction
	 */
	public void deal(Player player) throws IllegalActionException{
		round.deal(player);
		gameMediator.publishDealEvent(new DealEvent(player.getSavedPlayer()));
		System.out.println(player.getName()+" deals.");
		checkIfEndedAndChangeRound();
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
	 * @see 	PlayerAction
	 */
	public void allIn(Player player) throws IllegalActionException {
		round.allIn(player);
		checkIfEndedAndChangeRound();
	}

	public void joinGame(Player player) throws IllegalActionException, PlayerListFullException{
		game.joinGame(player);
	}

	public void leaveGame(Player player) throws IllegalActionException{
		game.leaveGame(player);
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
		}else{
			gameMediator.publishNextPlayerEvent(new NextPlayerEvent(game.getCurrentPlayer().getSavedPlayer()));
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
		round = round.getNextRound();
		if((round instanceof BettingRound) && ((BettingRound)round).onlyAllInPlayers())
			changeToNextRound();
	}
}
