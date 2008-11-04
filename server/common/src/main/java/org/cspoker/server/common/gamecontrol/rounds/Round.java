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

package org.cspoker.server.common.gamecontrol.rounds;

import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.server.common.gamecontrol.Game;
import org.cspoker.server.common.gamecontrol.PokerTable;
import org.cspoker.server.common.gamecontrol.rules.BettingRules;

/**
 * An abstract class to represent rounds. A player can do actions in a round,
 * such as checking, betting, ...
 * 
 */
public abstract class Round {

	/***************************************************************************
	 * Variables
	 **************************************************************************/

	/**
	 * The variable containing the game in which this round takes place.
	 */
	protected final Game game;

	/**
	 * The variable containing the game mediator.
	 */
	protected final PokerTable gameMediator;

	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	/**
	 * Initialize a new round for given game.
	 * 
	 * @param game
	 *            The game to create a new round for.
	 */
	public Round(PokerTable gameMediator, Game game) {
		this.gameMediator = gameMediator;
		this.game = game;
		getBettingRules().setBetPlaced(false);
		getBettingRules().clearNBRaises();

		game.changeCurrentPlayerToInitial();
		game.setLastActionPlayer(game.getFirstToActPlayer());
	}

	/**
	 * Returns the game this round is part of.
	 * 
	 * @return The game this round is part of.
	 */
	public Game getGame() {
		return game;
	}

	/***************************************************************************
	 * Betting Rules
	 **************************************************************************/
	/**
	 * Returns the betting game.rounds.rules for this round
	 */
	public BettingRules getBettingRules() {
		return getGame().getBettingRules();
	}

	/***************************************************************************
	 * Bidding methods
	 **************************************************************************/

	/**
	 * If there is no bet on the table and you do not wish to place a bet. You
	 * may only check when there are no prior bets.
	 * 
	 * @param player
	 *            The player who checks.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void check(MutableSeatedPlayer player) throws IllegalActionException {
		throw new IllegalActionException(player.getName()
				+ " can not check in this round.");
	}

	/**
	 * The player puts money in the pot.
	 * 
	 * @param player
	 *            The player who puts a bet.
	 * @param amount
	 *            The amount of the bet.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void bet(MutableSeatedPlayer player, int amount)
			throws IllegalActionException {
		throw new IllegalActionException(player.getName() + " can not bet "
				+ amount + " chips in this round.");
	}

	/**
	 * To put into the pot an amount of money equal to the most recent bet or
	 * raise.
	 * 
	 * @param player
	 *            The player who calls.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void call(MutableSeatedPlayer player) throws IllegalActionException {
		throw new IllegalActionException(player.getName()
				+ " can not call in this round.");
	}

	/**
	 * The player puts money in the pot.
	 * 
	 * @param player
	 *            The player who puts a bet.
	 * @param amount
	 *            The amount of the bet.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void raise(MutableSeatedPlayer player, int amount)
			throws IllegalActionException {
		throw new IllegalActionException(player.getName()
				+ " can not raise with " + amount + " chips in this round.");
	}

	/**
	 * The given player folds the cards.
	 * 
	 * The player will not be able to take any actions in the coming rounds of
	 * the current deal.
	 * 
	 * @param player
	 *            The player who folds.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void fold(MutableSeatedPlayer player) throws IllegalActionException {
		throw new IllegalActionException(player.getName()
				+ " can not fold in this round.");
	}

	public void foldAction(MutableSeatedPlayer player) throws IllegalActionException {
	}

	/**
	 * The player who the dealer-button has been dealt to can choose to start
	 * the deal. From that moment, new players can not join the on-going deal.
	 * 
	 * @param player
	 *            The player who deals.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void deal(MutableSeatedPlayer player) throws IllegalActionException {
		throw new IllegalActionException(player.getName()
				+ " can not deal in this round.");
	}

	/**
	 * The given player goes all-in.
	 * 
	 * @param player
	 *            The player who goes all-in.
	 * @throws IllegalActionException
	 *             [must] It's not the turn of the given player.
	 * @throws IllegalActionException
	 *             [must] The action performed is not a valid action.
	 */
	public void allIn(MutableSeatedPlayer player) throws IllegalActionException {
		throw new IllegalActionException(player.getName()
				+ " can not go all-in in this round.");
	}

	/***************************************************************************
	 * Round Logic
	 **************************************************************************/

	/**
	 * Check whether the round is ended or not.
	 * 
	 * It is the case when there are no more active players, or when the last
	 * event player is the next player.
	 * 
	 * @return True if the round is ended, false otherwise.
	 */
	public boolean isRoundEnded() {
		return game.getLastActionPlayer().equals(game.getCurrentPlayer());
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
	 * Set the last event player to the given player.
	 * 
	 * @param player
	 *            The player who did the last event.
	 */
	protected void playerMadeEvent(MutableSeatedPlayer player) {
		game.setLastActionPlayer(player);
	}

	public boolean onTurn(MutableSeatedPlayer player) {
		return game.getCurrentPlayer().equals(player);
	}

	protected void removeBrokePlayers() {
		for (MutableSeatedPlayer player : getGame().getTable().getSeatedServerPlayers()) {
			if (player.getStack().getValue() == 0) {
				try {
					getGame().sitOut(player);
					gameMediator
							.publishSitOutEvent(new SitOutEvent(player.getId(), true));
				} catch (IllegalActionException e) {
					throw new IllegalStateException();
				}
			}
		}
	}

	public abstract boolean isLowBettingRound();

	public abstract boolean isHighBettingRound();

	public int getCurrentPotValue() {
		return 0;
	}

	protected Round getNewDealRound() {
		return new WaitingRound(gameMediator, game);
	}

	private boolean potsDividedToWinner;

	public boolean potsDividedToWinner() {
		return potsDividedToWinner;
	}

	protected void setPotsDividedToWinner(boolean flag) {
		potsDividedToWinner = flag;
	}
}
