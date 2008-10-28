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
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.table.Rounds;
import org.cspoker.server.common.gamecontrol.Game;
import org.cspoker.server.common.gamecontrol.PokerTable;
import org.cspoker.server.common.player.GameSeatedPlayer;

public class FlopRound extends BettingRound {
	private static Logger logger = Logger.getLogger(FlopRound.class);

	public FlopRound(PokerTable gameMediator, Game game) {
		super(gameMediator, game);
		GameSeatedPlayer currentPlayer = getGame().getCurrentPlayer();
		if (currentPlayer != null) {
			gameMediator.publishNewRoundEvent(new NewRoundEvent(getRound()));
			gameMediator.publishNextPlayerEvent(new NextPlayerEvent(currentPlayer.getMemento()));
		}
		drawMuckCard();
		drawOpenCard();
		drawOpenCard();
		drawOpenCard();
		gameMediator.publishNewCommonCardsEvent(new NewCommunityCardsEvent(
				new HashSet<Card>(getGame().getCommunityCards())));
		FlopRound.logger.info("*** FLOP *** " + game.getCommunityCards());
	}

	public Round getNextRound() {
		if (potsDividedToWinner()) {
			return getNewDealRound();
		}
		return new TurnRound(gameMediator, getGame());
	}

	public boolean isLowBettingRound() {
		return true;
	}

	public boolean isHighBettingRound() {
		return !isLowBettingRound();
	}

	public String toString() {
		return "flop round";
	}
	
	public Rounds getRound(){
		return Rounds.FLOP;
	}
}
