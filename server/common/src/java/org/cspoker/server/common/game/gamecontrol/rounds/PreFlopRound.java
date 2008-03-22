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

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.events.gameevents.NewRoundEvent;
import org.cspoker.common.events.gameevents.NextPlayerEvent;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.server.common.game.GameMediator;
import org.cspoker.server.common.game.elements.chips.IllegalValueException;
import org.cspoker.server.common.game.gamecontrol.Game;
import org.cspoker.server.common.game.player.GameAllInPlayer;
import org.cspoker.server.common.game.player.GameSeatedPlayer;

/**
 * The round after the initial 2 cards are dealt.
 * 
 * @author Kenzo
 * 
 */
public class PreFlopRound extends BettingRound {

	private static Logger logger = Logger.getLogger(PreFlopRound.class);

	private boolean bigBlindChecked = false;

	private GameSeatedPlayer bigBlindPlayer;

	private boolean bigBlindAllIn = false;

	public PreFlopRound(GameMediator gameMediator, Game game) {
		super(gameMediator, game);

		GameSeatedPlayer currentPlayer = getGame().getCurrentPlayer();

		if (currentPlayer != null) {
			gameMediator.publishNewRoundEvent(new NewRoundEvent(toString(),
					currentPlayer.getMemento()));
		}
		try {
			// If there are only 2 players, blinds are inverted.
			if (game.getNbCurrentDealPlayers() == 2) {
				game.nextPlayer();
			}
			GameSeatedPlayer player = getGame().getCurrentPlayer();
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
		for (GameSeatedPlayer player : getGame().getCurrentDealPlayers()) {
			player.dealPocketCard(drawCard());
			player.dealPocketCard(drawCard());

			PreFlopRound.logger.info("Dealt to " + player.getName() + " "
					+ player.getPocketCards());

			gameMediator.publishNewPocketCardsEvent(player.getId(),
					new NewPocketCardsEvent(player.getMemento(),
							new HashSet<Card>(player.getPocketCards())));
		}

		for (GameAllInPlayer allInPlayer : allInPlayers) {
			GameSeatedPlayer player = allInPlayer.getPlayer();
			player.dealPocketCard(drawCard());
			player.dealPocketCard(drawCard());

			PreFlopRound.logger.info("Dealt to " + player.getName() + " "
					+ player.getPocketCards());

			gameMediator.publishNewPocketCardsEvent(player.getId(),
					new NewPocketCardsEvent(player.getMemento(),
							new HashSet<Card>(player.getPocketCards())));
		}

		if (getGame().getNbCurrentDealPlayers() > 1) {
			gameMediator.publishNextPlayerEvent(new NextPlayerEvent(game
					.getCurrentPlayer().getMemento()));
		}
	}

	public void check(GameSeatedPlayer player) throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName()
					+ " can not check in this round.");
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
}
