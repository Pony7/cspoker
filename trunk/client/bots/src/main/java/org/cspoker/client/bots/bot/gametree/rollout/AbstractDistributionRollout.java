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
package org.cspoker.client.bots.bot.gametree.rollout;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.handeval.spears2p2.StateTableEvaluator;
import org.cspoker.common.util.MutableDouble;

public abstract class AbstractDistributionRollout {

	private final static Logger logger = Logger.getLogger(AbstractDistributionRollout.class);

	//cards
	private static final int[] offsets = new int[] { 0, 1277, 4137, 4995, 5853, 5863, 7140, 7296, 7452 };
	private static final Random random = new Random();
	private static final Card[] cards = Card.values();
	private final static int[] handRanks;
	static {
		handRanks = StateTableEvaluator.getInstance().handRanks;
	}

	public final GameState gameState;
	public final PlayerState botState;
	public final PlayerId botId;

	public final Set<PlayerState> allPlayers;
	public final Set<PlayerState> activeOpponents;

	public final int gamePotSize;
	public final int relPotSize;

	public final Card botCard1;
	public final Card botCard2;
	private final EnumSet<Card> usedFixedCommunityAndBotCards;

	public final int fixedRank;
	public final int nbMissingCommunityCards;

	AbstractDistributionRollout(GameState gameState,PlayerId botId) {
		this.botId = botId;
		this.gameState = gameState;
		this.botState = gameState.getPlayer(botId);

		this.allPlayers = Collections.unmodifiableSet(gameState.getAllSeatedPlayers());
		this.activeOpponents = Collections.unmodifiableSet(getActiveOpponents(allPlayers));

		this.gamePotSize = gameState.getGamePotSize();
		this.relPotSize = gamePotSize
				/ (allPlayers.size() * gameState.getTableConfiguration()
						.getBigBlind());

		Iterator<Card> botCardIterator = botState.getCards().iterator();
		this.botCard1 = botCardIterator.next();
		this.botCard2 = botCardIterator.next();

		EnumSet<Card> usedFixedCommunityCards = gameState.getCommunityCards();
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
	
	public RolloutResult doRollOut(int nbCommunitySamples, int nbOpponentSamples) {
		boolean traceEnabled = logger.isTraceEnabled();

		double totalProb = 0;
		TreeSet<PlayerState> drawers = createDrawersMap();
		TreeMap<Integer, MutableDouble> values = new TreeMap<Integer, MutableDouble>();

		for (int i = 0; i < nbCommunitySamples; i++) {
			int communitySampleRank = fixedRank;
			EnumSet<Card> usedCommunityAndBotCards = EnumSet
					.copyOf(usedFixedCommunityAndBotCards);
			for (int j = 0; j < nbMissingCommunityCards; j++) {
				Card communityCard = drawNewCard(usedCommunityAndBotCards);
				if (traceEnabled) {
					logger.trace("Evaluating sampled community card "
							+ communityCard);
				}
				communitySampleRank = updateIntermediateRank(
						communitySampleRank, communityCard);
			}
			if (traceEnabled) {
				logger.trace("Evaluating bot cards " + botCard1 + " "
						+ botCard2);
			}
			int botRank = extractFinalRank(updateIntermediateRank(
					updateIntermediateRank(communitySampleRank, botCard1),
					botCard2));
			for (int j = 0; j < nbOpponentSamples; j++) {
				EnumSet<Card> usedOpponentCards = EnumSet
						.copyOf(usedCommunityAndBotCards);
				double logProb = 0;
				int maxOpponentWin = 0;
				drawers.clear();
				for (PlayerState opponentThatCanWin : activeOpponents) {
					Card opponentCard1 = drawNewCard(usedOpponentCards);
					Card opponentCard2 = drawNewCard(usedOpponentCards);
					int opponentRank = extractFinalRank(updateIntermediateRank(
							updateIntermediateRank(communitySampleRank,
									opponentCard1), opponentCard2));
					if (traceEnabled) {
						logger.trace("Evaluating sampled opponent cards "
								+ opponentCard1 + " " + opponentCard2);
					}
					if (opponentRank > botRank) {
						maxOpponentWin = Math.max(maxOpponentWin,
								opponentThatCanWin.getTotalInvestment());
					} else if (opponentRank == botRank) {
						drawers.add(opponentThatCanWin);
					}
					float opponentRankProb = getRelativeProbability(
							opponentRank, relPotSize);
					logProb += Math.log(opponentRankProb);
				}
				double prob = Math.exp(logProb);
				int won = calcAmountWon(botState, maxOpponentWin, drawers,
						allPlayers);
				MutableDouble value = values.get(won);
				if (value == null) {
					values.put(won, new MutableDouble(prob));
				} else {
					value.add(prob);
				}
				totalProb += prob;
			}
		}
		return new RolloutResult(values, totalProb);
	}
	
	private int calcAmountWon(PlayerState botState, int maxOpponentWin,
			Set<PlayerState> drawers, Set<PlayerState> players) {
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

	private TreeSet<PlayerState> createDrawersMap() {
		TreeSet<PlayerState> drawers = new TreeSet<PlayerState>(
				new Comparator<PlayerState>() {

					@Override
					public int compare(PlayerState o1, PlayerState o2) {
						int o1i = o1.getTotalInvestment();
						int o2i = o2.getTotalInvestment();
						if (o1i == o2i) {
							return o1.hashCode() - o2.hashCode();
						}
						return o1i - o2i;
					}

				});
		return drawers;
	}

	private Card drawNewCard(EnumSet<Card> usedCards) {
		Card communityCard;
		do {
			communityCard = getRandomCard();
		} while (usedCards.contains(communityCard));
		usedCards.add(communityCard);
		return communityCard;
	}

	private int extractFinalRank(int rank) {
		int type = (rank >>> 12) - 1;
		rank = rank & 0xFFF;
		return offsets[type] + rank - 1;
	}

	private Set<PlayerState> getActiveOpponents(Set<PlayerState> allPlayers) {
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

	protected abstract float getRelativeProbability(int rank,
			int relativePotSize);

	private EnumSet<Card> getSetOf(Card botCard1, Card botCard2,
			EnumSet<Card> usedFixedCommunityCards) {
		EnumSet<Card> usedFixedCommunityAndBotCards = EnumSet
				.copyOf(usedFixedCommunityCards);
		usedFixedCommunityAndBotCards.add(botCard1);
		usedFixedCommunityAndBotCards.add(botCard2);
		return usedFixedCommunityAndBotCards;
	}

	private int updateIntermediateRank(int rank, Card card) {
		return handRanks[card.ordinal() + 1 + rank];
	}
	
	public double getUpperWinBound() {
		PlayerState botState = gameState.getPlayer(botId);
		return gameState.getGamePotSize()+botState.getStack();
	}

	@Override
	public String toString() {
		return "Abstract Distribution Rollout";
	}
	
	public static interface Factory{
		
		AbstractDistributionRollout create(GameState gameState, PlayerId botId);
		
	}

}
