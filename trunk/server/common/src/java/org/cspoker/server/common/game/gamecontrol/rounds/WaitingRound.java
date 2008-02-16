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

package org.cspoker.server.common.game.gamecontrol.rounds;

import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.server.common.game.GameMediator;
import org.cspoker.server.common.game.gamecontrol.Game;
import org.cspoker.server.common.game.gamecontrol.PlayerAction;
import org.cspoker.server.common.game.player.GamePlayer;

public class WaitingRound extends Round {

    public WaitingRound(GameMediator gameMediator, Game game) {
	super(gameMediator, game);
	newDealRound();
    }

    /**
     * The player who the dealer-button has been dealt to can choose to start
     * the deal. From that moment, new players can not join the on-going deal.
     * 
     * @param player
     *                The player who deals.
     * @throws IllegalActionException
     *                 [must] It's not the turn of the given player.
     * @throws IllegalActionException
     *                 [must] The action performed is not a valid action.
     * @see PlayerAction
     */
    @Override
    public void deal(GamePlayer player) throws IllegalActionException {
	// Check whether the given player can do this action.
	if (!onTurn(player))
	    throw new IllegalActionException(player.getName()
		    + " can not deal in this round.");

	if (!(game.getTable().getNbPlayers() > 1))
	    throw new IllegalActionException(
		    "There should at least be 2 players to begin a new deal.");

	playerMadeEvent(player);
	// This will force the game control to end the waiting round
	// and change to the preflop round.
    }

    @Override
    public void endRound() {
	getGame().dealNewHand();
    }

    @Override
    public Round getNextRound() {
	return new PreFlopRound(gameMediator, getGame());
    }

    @Override
    public boolean isLowBettingRound() {
	return false;
    }

    @Override
    public boolean isHighBettingRound() {
	return false;
    }

    @Override
    public String toString() {
	return "waiting round";
    }
}
