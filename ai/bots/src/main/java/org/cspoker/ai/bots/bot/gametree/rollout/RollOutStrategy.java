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
package org.cspoker.ai.bots.bot.gametree.rollout;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.handeval.spears2p2.StateTableEvaluator;

public abstract class RollOutStrategy {

	private final static Logger logger = Logger.getLogger(RollOutStrategy.class);

	//cards
	static final int[] offsets = new int[] { 0, 1277, 4137, 4995, 5853, 5863, 7140, 7296, 7452 };
	static final Random random = new Random();
	static final Card[] cards = Card.values();
	final static int[] handRanks;
	static {
		handRanks = StateTableEvaluator.getInstance().handRanks;
	}

	public final GameState gameState;
	public final PlayerState botState;
	public final PlayerId botId;
	public final Set<PlayerState> allPlayers;
	public final Set<PlayerState> activeOpponents;
	public final int gamePotSize;
	public final Card botCard1;
	public final Card botCard2;
	protected final EnumSet<Card> usedFixedCommunityAndBotCards;
	public final int fixedRank;
	public final int nbMissingCommunityCards;
	protected final EnumSet<Card> usedFixedCommunityCards;

	public RollOutStrategy(GameState gameState, PlayerId botId) {
		this.botId = botId;
		this.gameState = gameState;
		this.botState = gameState.getPlayer(botId);

		this.allPlayers = Collections.unmodifiableSet(gameState.getAllSeatedPlayers());
		this.activeOpponents = Collections.unmodifiableSet(getActiveOpponents(allPlayers));

		this.gamePotSize = gameState.getGamePotSize();

		Iterator<Card> botCardIterator = botState.getCards().iterator();
		this.botCard1 = botCardIterator.next();
		this.botCard2 = botCardIterator.next();

		this.usedFixedCommunityCards = gameState.getCommunityCards();
		this.usedFixedCommunityAndBotCards = getSetOf(botCard1,
				botCard2, usedFixedCommunityCards);

		int fixedRankBuilder = 53;
		boolean traceEnabled = logger.isTraceEnabled();
		for (Card fixedCommunityCard : usedFixedCommunityCards) {
			if (traceEnabled) {
				logger.trace("Evaluating fixed community card "
						+ fixedCommunityCard);
			}
			fixedRankBuilder = updateIntermediateRank(fixedRankBuilder, fixedCommunityCard);
		}
		this.fixedRank = fixedRankBuilder;
		this.nbMissingCommunityCards = 5 - usedFixedCommunityCards.size();
	}

	protected int getFinalRank(int communityRank, Card handCard1,
			Card handCard2) {
		return extractFinalRank(updateIntermediateRank(
				updateIntermediateRank(communityRank,
						handCard1), handCard2));
	}

	protected int calcAmountWon(PlayerState botState, int maxOpponentWin, Set<PlayerState> drawers,
			Set<PlayerState> players) {
		int botInvestment = botState.getTotalInvestment();
		if (maxOpponentWin >= botInvestment) {
			// won nothing
			return 0;
		} else if (drawers.isEmpty()) {
			// won something, no draw
			if (maxOpponentWin == 0 && !botState.isAllIn()) {
				// just win everything
				return gamePotSize;
			} else {
				// Calculate from individual contributions
				int totalToDistribute = 0;
				for (PlayerState player : players) {
					totalToDistribute += Math.max(0, Math.min(botInvestment,
							player.getTotalInvestment())
							- maxOpponentWin);
				}
				return totalToDistribute;
			}
		} else {
			// won something but must share
			int myShare = 0;
			int distributed = maxOpponentWin;
			int nbDrawers = drawers.size() + 1;
			for (PlayerState drawer : drawers) {
				int limit = Math
				.min(botInvestment, drawer.getTotalInvestment());
				if (limit > distributed) {
					int totalToDistribute = 0;
					for (PlayerState player : players) {
						totalToDistribute += Math.max(0, Math.min(limit, player
								.getTotalInvestment())
								- distributed);
					}
					myShare += totalToDistribute / nbDrawers;
					distributed = limit;
				}
				--nbDrawers;
			}
			return myShare + botInvestment - distributed;
		}
	}


	protected static Comparator<PlayerState> playerComparatorByInvestment = new Comparator<PlayerState>() {

		@Override
		public int compare(PlayerState o1, PlayerState o2) {
			int o1i = o1.getTotalInvestment();
			int o2i = o2.getTotalInvestment();
			if (o1i == o2i) {
				return o1.hashCode() - o2.hashCode();
			}
			return o1i - o2i;
		}

	};

	protected Card drawNewCard(EnumSet<Card> usedCards) {
		Card communityCard;
		do {
			communityCard = getRandomCard();
		} while (usedCards.contains(communityCard));
		usedCards.add(communityCard);
		return communityCard;
	}

	protected int extractFinalRank(int rank) {
		int type = (rank >>> 12) - 1;
		rank = rank & 0xFFF;
		return offsets[type] + rank - 1;
	}

	protected Set<PlayerState> getActiveOpponents(Set<PlayerState> allPlayers) {
		Set<PlayerState> opponentsThatCanWin = new HashSet<PlayerState>();
		for (PlayerState playerState : allPlayers) {
			if (!playerState.hasFolded()
					&& !playerState.getPlayerId().equals(botId)) {
				opponentsThatCanWin.add(playerState);
			}
		}
		return opponentsThatCanWin;
	}

	private Card getRandomCard() {
		return cards[random.nextInt(cards.length)];
	}

	protected EnumSet<Card> getSetOf(Card botCard1, Card botCard2, EnumSet<Card> usedFixedCommunityCards) {
		EnumSet<Card> usedFixedCommunityAndBotCards = EnumSet
		.copyOf(usedFixedCommunityCards);
		usedFixedCommunityAndBotCards.add(botCard1);
		usedFixedCommunityAndBotCards.add(botCard2);
		return usedFixedCommunityAndBotCards;
	}

	protected int updateIntermediateRank(int rank, Card card) {
		return handRanks[card.ordinal() + 1 + rank];
	}

	public double getUpperWinBound() {
		PlayerState botState = gameState.getPlayer(botId);
		return gameState.getGamePotSize()+botState.getStack();
	}

}