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
package org.cspoker.client.bots.bot.search.node.leaf;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.SearchConfiguration;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.handeval.spears2p2.StateTableEvaluator;
import org.cspoker.common.util.MutableDouble;
import org.cspoker.common.util.Pair;

public abstract class AbstractDistributionShowdownNode extends
		AbstractShowdownNode {

	public static interface Factory extends AbstractShowdownNode.Factory {

		AbstractDistributionShowdownNode create(PlayerId botId,
				GameState gameState, int tokens, SearchConfiguration config,
				int searchId, NodeVisitor... nodeVisitors);
	}

	private final static Logger logger = Logger
			.getLogger(AbstractDistributionShowdownNode.class);
	private final static int[] handRanks;

	public static final int MaxNbSamples = 400;

	private static final int[] offsets = new int[] { 0, 1277, 4137, 4995, 5853,
			5863, 7140, 7296, 7452 };

	private static Random random = new Random();

	private static Card[] cards = Card.values();

	static {
		handRanks = StateTableEvaluator.getInstance().handRanks;
	}

	private final int tokens;

	AbstractDistributionShowdownNode(PlayerId botId, GameState gameState,
			int tokens, NodeVisitor... nodeVisitors) {
		super(botId, gameState, nodeVisitors);
		this.tokens = tokens;
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
				return gameState.getGamePotSize();
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

	private double calcMean(double totalProb, Map<Integer, MutableDouble> values) {
		double mean = 0;
		for (Entry<Integer, MutableDouble> entry : values.entrySet()) {
			mean += entry.getKey() * entry.getValue().getValue();
		}
		mean /= totalProb;
		return mean;
	}

	private double calcVariance(double mean, double totalProb, int nbSamples,
			Map<Integer, MutableDouble> values) {
		double var = 0;
		for (Entry<Integer, MutableDouble> entry : values.entrySet()) {
			double diff = mean - entry.getKey();
			var += diff * diff * entry.getValue().getValue();
		}
		var /= totalProb;
		// use sample variance because variance of samples is smaller than real
		// variance
		var *= (double) nbSamples / (nbSamples - 1);
		return var;
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

	public Pair<Double, Double> getExpectedValue() {
		PlayerState botState = gameState.getPlayer(botId);

		Set<PlayerState> allPlayers = gameState.getAllSeatedPlayers();
		Set<PlayerState> activeOpponents = getActiveOpponents(allPlayers);

		int gamePotSize = gameState.getGamePotSize();
		int relPotSize = gamePotSize
				/ (allPlayers.size() * gameState.getTableConfiguration()
						.getBigBlind());

		Iterator<Card> botCardIterator = botState.getCards().iterator();
		Card botCard1 = botCardIterator.next();
		Card botCard2 = botCardIterator.next();

		EnumSet<Card> usedFixedCommunityCards = gameState.getCommunityCards();
		EnumSet<Card> usedFixedCommunityAndBotCards = getSetOf(botCard1,
				botCard2, usedFixedCommunityCards);

		int fixedRank = 53;
		boolean traceEnabled = logger.isTraceEnabled();

		for (Card fixedCommunityCard : usedFixedCommunityCards) {
			if (traceEnabled) {
				logger.trace("Evaluating fixed community card "
						+ fixedCommunityCard);
			}
			fixedRank = updateIntermediateRank(fixedRank, fixedCommunityCard);
		}

		int nbMissingCommunityCards = 5 - usedFixedCommunityCards.size();
		int nbSamplesEst = Math.min(MaxNbSamples, Math.max(25, tokens * 5));
		int nbCommunitySamples, nbOpponentSamples;
		if (nbMissingCommunityCards == 0) {
			nbCommunitySamples = 1;
			nbOpponentSamples = nbSamplesEst;
		} else {
			double root = Math.sqrt(nbSamplesEst);
			nbCommunitySamples = (int) (root * nbMissingCommunityCards / 2);
			nbOpponentSamples = (int) (root * 2 / nbMissingCommunityCards);
		}
		int nbSamples = nbOpponentSamples * nbCommunitySamples;

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
		int stackSize = botState.getStack();
		informListeners(values, totalProb, gamePotSize, stackSize);
		double mean = stackSize + calcMean(totalProb, values);
		double var = calcVariance(mean, totalProb, nbSamples, values);
		return new Pair<Double, Double>(mean, var);
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
	private void informListeners(TreeMap<Integer, MutableDouble> values,
			double totalProb, int gamePotSize, int stackSize) {
		for (Entry<Integer, MutableDouble> value : values.entrySet()) {
			for (NodeVisitor nodeVisitor : nodeVisitors) {
				nodeVisitor.visitLeafNode(stackSize + value.getKey(), value
						.getValue().getValue()
						/ totalProb, stackSize, stackSize + gamePotSize);
			}
		}
	}

	@Override
	public String toString() {
		return "Abstract Distribution Showdown Node";
	}

	private int updateIntermediateRank(int rank, Card card) {
		return handRanks[card.ordinal() + 1 + rank];
	}

}
