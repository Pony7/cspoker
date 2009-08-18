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

package org.cspoker.server.gamecontrol.rounds;

import java.util.EnumSet;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.chips.IllegalValueException;
import org.cspoker.common.elements.player.MutableAllInPlayer;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.table.Round;
import org.cspoker.server.gamecontrol.Game;
import org.cspoker.server.gamecontrol.PokerTable;

public class PreFlopRound
		extends BettingRound {
	
	private static Logger logger = Logger.getLogger(PreFlopRound.class);
	
	private boolean bigBlindChecked = false;
	
	private MutableSeatedPlayer bigBlindPlayer;
	
	private boolean bigBlindAllIn = false;
	
	public PreFlopRound(PokerTable gameMediator, Game game) {
		super(gameMediator, game);
		
		gameMediator.publishNewRoundEvent(new NewRoundEvent(getRound(), game.getPots().getSnapshot()));
		
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
		
		//Start dealing after dealer seat, to enable card stratification
		for (MutableSeatedPlayer player : getGame().getCurrentDealPlayers()) {
			if(player.getSeatId().getId()>game.getDealer().getSeatId().getId()){
			dealPlayerCards(gameMediator, player);
			}
		}
		for (MutableSeatedPlayer player : getGame().getCurrentDealPlayers()) {
			if(player.getSeatId().getId()<=game.getDealer().getSeatId().getId()){
				dealPlayerCards(gameMediator, player);
			}
		}
		
		for (MutableAllInPlayer allInPlayer : allInPlayers) {
			MutableSeatedPlayer player = allInPlayer.getPlayer();
			player.dealPocketCard(drawCard());
			player.dealPocketCard(drawCard());
			
			PreFlopRound.logger.info("Dealt to " + player.getName() + " " + player.getPocketCards());
			
			gameMediator.publishNewPocketCardsEvent(player.getId(), new NewPocketCardsEvent(EnumSet.copyOf(player
					.getPocketCards())));
		}
		
		if (game.getCurrentPlayer() != null && getGame().getNbCurrentDealPlayers() > 1
				|| !onlyOnePlayerLeftBesidesAllInPlayersAndCalled()) {
			gameMediator.publishNextPlayerEvent(new NextPlayerEvent(game.getCurrentPlayer().getId(),
					amountToIncreaseBetPileWith(game.getCurrentPlayer())));
		}
	}

	private void dealPlayerCards(PokerTable gameMediator,
			MutableSeatedPlayer player) {
		player.dealPocketCard(drawCard());
		player.dealPocketCard(drawCard());
		
		PreFlopRound.logger.info("Dealt to " + player.getName() + " " + player.getPocketCards());
		
		EnumSet<Card> cards = EnumSet.noneOf(Card.class);
		cards.addAll(player.getPocketCards());
		gameMediator.publishNewPocketCardsEvent(player.getId(), new NewPocketCardsEvent(cards));
	}
	
	@Override
	public void check(MutableSeatedPlayer player)
			throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName() + " it is not your turn to act.");
		}
		
		if (game.getLastActionPlayer().getBetChips().getValue() > player.getBetChips().getValue()) {
			throw new IllegalActionException(player.getName() + " can not check in this round. "
					+ game.getLastActionPlayer() + " has bet " + game.getLastActionPlayer().getBetChips().getValue()
					+ " and you have only bet " + player.getBetChips().getValue());
			
		} else {
			bigBlindChecked = true;
		}
		game.nextPlayer();
		
	}
	
	@Override
	public boolean isRoundEnded() {
		return ((super.isRoundEnded() && (someoneHasRaised() || bigBlindAllIn() || someoneBigAllIn() || onlyOneActivePlayer()))
				|| bigBlindChecked() || onlyOneShowdownPlayerLeft());
	}
	
	private boolean bigBlindAllIn() {
		return bigBlindAllIn;
	}
	
	private boolean bigBlindChecked() {
		return bigBlindChecked;
	}
	
	@Override
	public AbstractRound getNextRound() {
		if (potsDividedToWinner()) {
			return getNewDealRound();
		}
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
	public String toString() {
		return "pre-flop round";
	}
	
	@Override
	public Round getRound() {
		return Round.PREFLOP;
	}
}
