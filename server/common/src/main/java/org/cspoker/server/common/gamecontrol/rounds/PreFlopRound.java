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

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.table.Rounds;
import org.cspoker.server.common.elements.chips.IllegalValueException;
import org.cspoker.server.common.gamecontrol.Game;
import org.cspoker.server.common.gamecontrol.PokerTable;
import org.cspoker.server.common.player.MutableAllInPlayer;
import org.cspoker.server.common.player.MutableSeatedPlayer;

public class PreFlopRound extends BettingRound {

	private static Logger logger = Logger.getLogger(PreFlopRound.class);

	private boolean bigBlindChecked = false;

	private MutableSeatedPlayer bigBlindPlayer;

	private boolean bigBlindAllIn = false;

	public PreFlopRound(PokerTable gameMediator, Game game) {
		super(gameMediator, game);

		MutableSeatedPlayer currentPlayer = getGame().getCurrentPlayer();

		if (currentPlayer != null) {
			gameMediator.publishNewRoundEvent(new NewRoundEvent(getRound()));
		}
		try {
			// If there are only 2 players, blinds are inverted.
			if (game.getNbCurrentDealPlayers() == 2) {
				game.nextPlayer();
			}
			MutableSeatedPlayer player = getGame().getCurrentPlayer();
			collectSmallBlind(player);
			getGame().nextPlayer();
		} catch (IllegalValueException e) {
			goAllIn(getGame().getCurrentPlayer());
			someoneBigAllIn = false;
		}

		if (getGame().getNbCurrentDealPlayers() != 1) {
			try {
				bigBlindPlayer = getGame().getCurrentPlayer();
				collectBigBlind(bigBlindPlayer);
				getGame().nextPlayer();
			} catch (IllegalValueException e) {
				goAllIn(getGame().getCurrentPlayer());
				bigBlindAllIn = true;
			}
		}

		PreFlopRound.logger.info("*** HOLE CARDS ***");
		for (MutableSeatedPlayer player : getGame().getCurrentDealPlayers()) {
			player.dealPocketCard(drawCard());
			player.dealPocketCard(drawCard());

			PreFlopRound.logger.info("Dealt to " + player.getName() + " "
					+ player.getPocketCards());

			gameMediator.publishNewPocketCardsEvent(player.getId(),
					new NewPocketCardsEvent(
							new HashSet<Card>(player.getPocketCards())));
		}

		for (MutableAllInPlayer allInPlayer : allInPlayers) {
			MutableSeatedPlayer player = allInPlayer.getPlayer();
			player.dealPocketCard(drawCard());
			player.dealPocketCard(drawCard());

			PreFlopRound.logger.info("Dealt to " + player.getName() + " "
					+ player.getPocketCards());

			gameMediator.publishNewPocketCardsEvent(player.getId(),
							new NewPocketCardsEvent(new HashSet<Card>(player.getPocketCards())));
		}

		if (getGame().getNbCurrentDealPlayers() > 1) {
			gameMediator.publishNextPlayerEvent(new NextPlayerEvent(game
					.getCurrentPlayer().getMemento()));
		}
	}

	public void check(MutableSeatedPlayer player) throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName()
					+ " can not check in this round.");
		}
		if(game.getNbSeatedPlayers()==2 && game.getDealer().equals(player)){
			game.getDealer().equals(player);
			throw new IllegalActionException(player.getName()
					+ " can not check in this round. Someone has already bet.");
		}
		if (bigBlindPlayer.equals(player) && someoneHasRaised()) {
			throw new IllegalActionException(player.getName()
					+ " can not check in this round. Someone has already bet.");
		} else {
			bigBlindChecked = true;
		}
		game.nextPlayer();
	}

	public boolean isRoundEnded() {
		return ((super.isRoundEnded() && (someoneHasRaised() || bigBlindAllIn()
				|| someoneBigAllIn() || onlyOneActivePlayer()))
				|| bigBlindChecked() || onlyOnePlayerLeft());
	}

	private boolean bigBlindAllIn() {
		return bigBlindAllIn;
	}

	private boolean bigBlindChecked() {
		return bigBlindChecked;
	}

	public Round getNextRound() {
		if (potsDividedToWinner()) {
			return getNewDealRound();
		}
		return new FlopRound(gameMediator, getGame());
	}

	public boolean isLowBettingRound() {
		return true;
	}

	public boolean isHighBettingRound() {
		return !isLowBettingRound();
	}

	public String toString() {
		return "pre-flop round";
	}
	
	public Rounds getRound(){
		return Rounds.PREFLOP;
	}
}
