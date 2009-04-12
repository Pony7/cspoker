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
package org.cspoker.client.bots.bot.rule;

import java.rmi.RemoteException;
import java.util.EnumSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.AbstractBot;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.handeval.stevebrecher.HandEval;

public class RuleBasedBot2 extends AbstractBot {

	private final static Logger logger = Logger.getLogger(RuleBasedBot2.class);

	public RuleBasedBot2(PlayerId playerId, TableId tableId,
			SmartLobbyContext lobby, int buyIn, ExecutorService executor,
			BotListener... botListeners) {
		super(playerId, tableId, lobby, buyIn, executor, botListeners);
	}

	@Override
	public void doNextAction() {
		GameState gameState = playerContext.getGameState();
		int deficit = gameState.getDeficit(RuleBasedBot2.this.playerId);

		double winPercentage = getWinPercentage(gameState
				.getCommunityCards(), playerContext.getPocketCards(),
				100, gameState.getNbPlayers() - 1);
		if (logger.isDebugEnabled()) {
			logger.debug("Win percentage is " + winPercentage
					+ " with " + playerContext.getPocketCards()
					+ " and " + gameState.getCommunityCards() + " and "
					+ (gameState.getNbPlayers() - 1) + " opponents.");
		}
		double EV = winPercentage
		* (gameState.getGamePotSize() + deficit);
		if (gameState.getRound().equals(Round.PREFLOP)) {
			// be more aggressive on the flop
			EV += EV;
		}
		try {
			if (gameState
					.getNextActivePlayerAfter(RuleBasedBot2.this.playerId) == null) {
				// can only call or fold
				if (deficit <= Math.round(EV)) {
					playerContext.checkOrCall();
				} else {
					playerContext.fold();
				}
			} else {
				playerContext.raiseMaxBetWith((int) Math
						.round(EV * 0.5), (int) Math.round(EV));
			}
		} catch (IllegalActionException e) {
			logger.warn("Raise bounds: "
					+ tableContext.getGameState().getLowerRaiseBound(
							RuleBasedBot2.this.playerId)
							+ " to "
							+ tableContext.getGameState().getUpperRaiseBound(
									RuleBasedBot2.this.playerId));
			logger.error(e);
			throw new IllegalStateException("Action was not allowed.",
					e);
		} catch (RemoteException e) {
			logger.error(e);
			throw new IllegalStateException("Action failed.", e);
		}
	}

	// Card roll out code

	public double getWinPercentage(EnumSet<Card> fixedCommunityCards,
			EnumSet<Card> botCards, int nbSamples, int nbOpponents) {
		EnumSet<Card> usedFixedCards = EnumSet.copyOf(fixedCommunityCards);
		usedFixedCards.addAll(botCards);

		int nbFixedCards = fixedCommunityCards.size();
		int nbMissingCommunityCards = 5 - nbFixedCards;
		int nbCommunitySamples, nbOpponentSamples;
		if (nbMissingCommunityCards == 0) {
			nbCommunitySamples = 1;
			nbOpponentSamples = nbSamples;
		} else {
			double root = Math.sqrt(nbSamples);
			nbCommunitySamples = (int) (root * nbMissingCommunityCards / 2);
			nbOpponentSamples = (int) (root * 2 / nbMissingCommunityCards);
		}

		int nbWins = 0;
		for (int i = 0; i < nbCommunitySamples; i++) {
			EnumSet<Card> fixedAndCommunityCards = EnumSet
			.copyOf(usedFixedCards);
			EnumSet<Card> communityCards = EnumSet.copyOf(fixedCommunityCards);
			for (int j = 0; j < nbMissingCommunityCards; j++) {
				Card communityCard;
				do {
					communityCard = getRandomCard();
				} while (fixedAndCommunityCards.contains(communityCard));
				fixedAndCommunityCards.add(communityCard);
				communityCards.add(communityCard);
			}
			for (int j = 0; j < nbOpponentSamples; j++) {
				EnumSet<Card> fixedAndCommunityAndOpponentCards = EnumSet
				.copyOf(fixedAndCommunityCards);
				// fixedAndCommunityCards coincidentally contains the bot's
				// hand!
				int botRank = getRank(fixedAndCommunityCards);
				boolean botWins = true;
				for (int k = 0; k < nbOpponents; k++) {
					Card opponentFirst;
					do {
						opponentFirst = getRandomCard();
					} while (fixedAndCommunityAndOpponentCards
							.contains(opponentFirst));
					fixedAndCommunityAndOpponentCards.add(opponentFirst);
					Card opponentSecond;
					do {
						opponentSecond = getRandomCard();
					} while (fixedAndCommunityAndOpponentCards
							.contains(opponentSecond));
					fixedAndCommunityAndOpponentCards.add(opponentSecond);
					EnumSet<Card> particularOpponentHand = EnumSet.of(
							opponentFirst, opponentSecond);
					particularOpponentHand.addAll(communityCards);
					int opponentRank = getRank(particularOpponentHand);
					if (opponentRank >= botRank) {
						botWins = false;
					}// TODO fix for split pot
				}
				if (botWins) {
					++nbWins;
				}
			}
		}
		return (double) nbWins / (nbOpponentSamples * nbCommunitySamples);
	}

	private int getRank(EnumSet<Card> cards) {
		return HandEval.getRank(cards);
	}

	private static Random random = new Random();
	private static Card[] cards = Card.values();

	protected static Card getRandomCard() {
		return cards[random.nextInt(cards.length)];
	}

}
