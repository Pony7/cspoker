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

package org.cspoker.server.embedded.gamecontrol.rounds;

import java.util.EnumSet;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.table.Round;
import org.cspoker.server.embedded.gamecontrol.Game;
import org.cspoker.server.embedded.gamecontrol.PokerTable;

public class FlopRound
		extends BettingRound {
	
	private static Logger logger = Logger.getLogger(FlopRound.class);
	
	public FlopRound(PokerTable gameMediator, Game game) {
		super(gameMediator, game);
		gameMediator.publishNewRoundEvent(new NewRoundEvent(getRound(), game.getPots().getSnapshot()));
		drawMuckCard();
		drawOpenCard();
		drawOpenCard();
		drawOpenCard();
		EnumSet<Card> cards = EnumSet.noneOf(Card.class);
		cards.addAll(getGame().getCommunityCards());
		gameMediator.publishNewCommonCardsEvent(new NewCommunityCardsEvent(cards));
		FlopRound.logger.info("*** FLOP *** " + game.getCommunityCards());
		if (getGame().getCurrentPlayer() != null && getGame().getNbCurrentDealPlayers() > 1) {
			gameMediator.publishNextPlayerEvent(new NextPlayerEvent(game.getCurrentPlayer().getId()));
		}
		
	}
	
	@Override
	public AbstractRound getNextRound() {
		if (potsDividedToWinner()) {
			return getNewDealRound();
		}
		return new TurnRound(gameMediator, getGame());
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
	public String toString() {
		return "flop round";
	}
	
	@Override
	public Round getRound() {
		return Round.FLOP;
	}
}
