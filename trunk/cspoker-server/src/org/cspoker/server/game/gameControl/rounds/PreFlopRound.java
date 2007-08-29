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
import org.cspoker.server.game.events.NewRoundEvent;
import org.cspoker.server.game.events.NextPlayerEvent;
import org.cspoker.server.game.events.PotChangedEvent;
import org.cspoker.server.game.events.playerActionEvents.BigBlindEvent;
import org.cspoker.server.game.events.playerActionEvents.SmallBlindEvent;
import org.cspoker.server.game.events.privateEvents.NewPocketCardsEvent;
import org.cspoker.server.game.gameControl.Game;
import org.cspoker.server.game.gameControl.IllegalActionException;
import org.cspoker.server.game.player.Player;


/**
 * The round after the initial 2 cards are dealt.
 *
 * @author Kenzo
 *
 */
public class PreFlopRound extends BettingRound{

	private boolean bigBlindChecked = false;

	private Player bigBlindPlayer;

	private boolean bigBlindAllIn = false;


	public PreFlopRound(GameMediator gameMediator, Game game) {
		super(gameMediator, game);
		System.out.println("** PreFlop Round **");
		for(Player player:getGame().getCurrentDealPlayers()){
			player.dealPocketCard(drawCard());
			player.dealPocketCard(drawCard());
			gameMediator.publishNewPocketCardsEvent(
					player.getId(), new NewPocketCardsEvent(player.getSavedPlayer()));
		}

		Player currentPlayer = getGame().getCurrentPlayer();
		if(currentPlayer!=null)
			gameMediator.publishNewRoundEvent(new NewRoundEvent(toString(), currentPlayer.getSavedPlayer()));
		try {
			Player player = getGame().getCurrentPlayer();
			collectSmallBlind(player);
			gameMediator.publishSmallBlindEvent(new SmallBlindEvent(player.getSavedPlayer(), getGame().getGameProperty().getSmallBlind()));
			gameMediator.publishPotChangedEvent(new PotChangedEvent(getCurrentPotValue()));
			System.out.println(player.getName()+" has placed small blind of "
					+getGame().getGameProperty().getSmallBlind());
			getGame().nextPlayer();
		} catch (IllegalValueException e) {
			goAllIn(getGame().getCurrentPlayer());
			someoneBigAllIn = false;
		}

		if(getGame().getNbCurrentDealPlayers()!=1){
			try {
				bigBlindPlayer = getGame().getCurrentPlayer();
				collectBigBlind(bigBlindPlayer);
				gameMediator.publishBigBlindEvent(new BigBlindEvent(bigBlindPlayer.getSavedPlayer(), getGame().getGameProperty().getBigBlind()));
				gameMediator.publishPotChangedEvent(new PotChangedEvent(getCurrentPotValue()));
				System.out.println(getGame().getCurrentPlayer().getName()+" has placed big blind of "
						+getGame().getGameProperty().getBigBlind());
				getGame().nextPlayer();
			} catch (IllegalValueException e) {
				goAllIn(getGame().getCurrentPlayer());
				bigBlindAllIn = true;
			}
		}

		if(getGame().getNbCurrentDealPlayers()>1){
			gameMediator.publishNextPlayerEvent(new NextPlayerEvent(game.getCurrentPlayer().getSavedPlayer()));
		}
	}

	@Override
	public void check(Player player) throws IllegalActionException{
		if(!onTurn(player))
			throw new IllegalActionException(player.getName()+" can not check in this round.");
		if(!bigBlindPlayer.equals(player) && someoneHasBet())
			throw new IllegalActionException(player.getName()+" can not check in this round. Someone has already bet.");
		else{
			bigBlindChecked = true;
		}
		game.nextPlayer();
	}

	@Override
	public boolean isRoundEnded(){
		return (super.isRoundEnded() && (someoneHasRaised() || bigBlindAllIn() || someoneBigAllIn()))
				|| bigBlindChecked();
	}

	private boolean bigBlindAllIn(){
		return bigBlindAllIn;
	}

	private boolean bigBlindChecked(){
		return bigBlindChecked;
	}



	@Override
	public Round getNextRound() {
		if(onlyOnePlayerLeft())
			return getNewDealRound();
		return new FlopRound(gameMediator, getGame());
	}
	@Override
	public boolean isLowBettingRound() {
		return true;
	}

	@Override
	public boolean isHighBettingRound() {
		return !isLowBettingRound();
	}

	@Override
	public String toString(){
		return "pre-flop round";
	}
}
