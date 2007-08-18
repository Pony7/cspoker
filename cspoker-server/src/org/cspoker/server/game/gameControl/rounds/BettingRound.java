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

import org.cspoker.server.game.GameMediator;
import org.cspoker.server.game.elements.chips.IllegalValueException;
import org.cspoker.server.game.gameControl.Game;
import org.cspoker.server.game.gameControl.actions.Action;
import org.cspoker.server.game.gameControl.actions.IllegalActionException;
import org.cspoker.server.game.player.Player;

public abstract class BettingRound extends Round {
	
	public BettingRound(GameMediator gameMediator, Game game){
		super(gameMediator, game);
	}
	
	@Override
	public void check(Player player) throws IllegalActionException{
		if(!onTurn(player) || someoneHasBet())
			throw new IllegalActionException(player.getName()+" can not check in this round.");
		game.nextPlayer();
	}
	
	@Override
	public void bet(Player player, int amount) throws IllegalActionException{
		if(!onTurn(player) || someoneHasBet() || onlyOnePlayerLeft())
			throw new IllegalActionException(player.getName()+" can not bet "+amount+" chips in this round.");
		
		//Check whether the bet is valid, according to the betting rules.
		if(!getBettingRules().isValidBet(amount, this))
			throw new IllegalActionException(player, Action.BET,getBettingRules().getLastBetErrorMessage());

		//Can not bet with zero, it is equal to check. Please use check.
		if(amount==0)
			throw new IllegalActionException(player, Action.BET, "Can not bet with 0 chips. Did you mean check?");
		if(amount>=player.getStack().getValue())
			throw new IllegalActionException(player,Action.BET,"Can not bet an amount higher than your current amount of chips;" +
					" did you mean all-in??");

		try {
			player.transferAmountToBettedPile(amountToIncreaseBettedPileWith(player)+amount);
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player, Action.BET, e.getMessage());
		}
		raiseBetWith(amount);
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(amount);
		playerMadeEvent(player);
		game.nextPlayer();
	}
	
	@Override
	public void call(Player player) throws IllegalActionException{
		if(!onTurn(player) || !someoneHasBet())
			throw new IllegalActionException(player.getName()+" can not call in this round.");
		
		//Check whether the amount with which the betted chips pile
		//is increased exceeds the player's stack.
		if(amountToIncreaseBettedPileWith(player)>=player.getStack().getValue())
			throw new IllegalActionException(player,Action.CALL,"Can not call a bet higher than your current amount of chips;" +
					" did you mean all-in??");

		//Try to transfer the amount to the betted pile.
		try {
			player.transferAmountToBettedPile(amountToIncreaseBettedPileWith(player));
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player, Action.CALL, e.getMessage());
		}

		/**
		 * If the last event player is an all-in player,
		 * change the last event player to the calling player.
		 */
		if(!game.hasAsActivePlayer(lastEventPlayer))
			playerMadeEvent(player);

		//Change to next player
		game.nextPlayer();
	}
	
	@Override
	public void raise(Player player, int amount) throws IllegalActionException{
		if(!onTurn(player) || !someoneHasBet() || onlyOnePlayerLeft())
			throw new IllegalActionException(player.getName()+" can not raise with "+amount+" chips in this round.");
		
		//Check whether the raise is valid.
		if(!getBettingRules().isValidRaise(amount, this))
			throw new IllegalActionException(player, Action.RAISE,getBettingRules().getLastRaiseErrorMessage());

		//Can not raise with zero, it is equal to call. Please use call.
		if(amount==0)
			throw new IllegalActionException(player, Action.RAISE, "Can not raise with 0 chips. Did you mean call?");

		//If the total number of chips needed for this raise,
		//exceeds or equals the stack of the player, the player should
		//go all-in explicitly.
		if((amount+amountToIncreaseBettedPileWith(player))>=player.getStack().getValue())
			throw new IllegalActionException(player,Action.RAISE,"Can not raise with an amount higher than your current amount of chips;" +
					" did you mean all-in??");

		//Try to transfer the amount to the betted pile.
		try {
			player.transferAmountToBettedPile(amountToIncreaseBettedPileWith(player)+amount);
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player, Action.RAISE, e.getMessage());
		}
		raiseBetWith(amount);
		getBettingRules().incrementNBRaises();
		getBettingRules().setLastBetAmount(amount);
		playerMadeEvent(player);
		game.nextPlayer();
	}
	
	@Override
	public void fold(Player player) throws IllegalActionException{
		if(!onTurn(player))
			throw new IllegalActionException(player.getName()+" can not fold. It should be his turn to do an action.");
		
		player.clearPocketCards();

		/**
		 * If the folding player has done a bet in this round,
		 * he should be remembered until the end of the round.
		 * It had to be implemented this way and not directly
		 * collected to the main pot, because all-in logic
		 * would be to complicated.
		 *
		 * By doing the all-in logic at the end of a round,
		 * the code is easier to write.
		 */
		if(player.getBettedChips().getValue()>0){
			foldedPlayersWithBet.add(player);
		}
		game.removePlayerFromCurrentDeal(player);

		//removing from game, automatically switches
		//to next player.
	}
	
	@Override
	public void allIn(Player player) throws IllegalActionException {
		if(!onTurn(player))
			throw new IllegalActionException(player.getName()+" can not go all-in. It should be his turn to do an action.");
		goAllIn(player);
	}
	
	
	
}
