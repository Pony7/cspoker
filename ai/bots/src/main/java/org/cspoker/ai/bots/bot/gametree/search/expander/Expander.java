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
package org.cspoker.ai.bots.bot.gametree.search.expander;

import java.util.List;
import java.util.Random;

import org.cspoker.ai.bots.bot.gametree.action.BetAction;
import org.cspoker.ai.bots.bot.gametree.action.CallAction;
import org.cspoker.ai.bots.bot.gametree.action.CheckAction;
import org.cspoker.ai.bots.bot.gametree.action.FoldAction;
import org.cspoker.ai.bots.bot.gametree.action.ProbabilityAction;
import org.cspoker.ai.bots.bot.gametree.action.RaiseAction;
import org.cspoker.ai.bots.bot.gametree.opponentmodel.OpponentModel;
import org.cspoker.ai.bots.bot.gametree.search.InnerGameTreeNode;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Expander {

	public static final int nbBetSizeSamples = 3;
	private final static Random r = new Random();

	private final GameState gameState;
	private final OpponentModel model;
	private final PlayerId actor;
	private final PlayerId bot;

	public Expander(GameState gameState, OpponentModel model, PlayerId actor, PlayerId bot) {
		this.gameState = gameState;
		this.model = model;
		this.actor = actor;
		this.bot = bot;
	}

	public ImmutableList<ProbabilityAction> getProbabilityActions() {
		List<ProbabilityAction> actions = Lists.newArrayListWithExpectedSize(2+nbBetSizeSamples);
		double totalProbability = 0;
		if (gameState.hasBet() 
				//case if big blind can check/bet in stead of fold/call/raise
				&& !(Round.PREFLOP.equals(gameState.getRound()) 
						&& gameState.getPlayer(actor).isBigBlind() 
						&& gameState.getDeficit(actor)<=0)) {
			// call, raise or fold
			Triple<Double, Double, Double> probabilities;
			probabilities = model.getFoldCallRaiseProbabilities(gameState, actor);

			double foldProbability = probabilities.getLeft();
			totalProbability += foldProbability;
			actions.add(new ProbabilityAction(new FoldAction(gameState, actor), foldProbability));

			double callProbability = probabilities.getMiddle();
			totalProbability += callProbability;
			actions.add(new ProbabilityAction(new CallAction(gameState, actor), callProbability));

			if (!gameState.getPlayer(bot).isAllIn()
					&& gameState.isAllowedToRaise(actor)) {
				double raiseProbability = probabilities.getRight();
				int lowerRaiseBound = gameState.getLowerRaiseBound(actor);
				int upperRaiseBound = gameState.getUpperRaiseBound(actor);
				int n = upperRaiseBound > lowerRaiseBound ? nbBetSizeSamples
						: 1;
				actions.add(new ProbabilityAction(new RaiseAction(gameState,
						actor, lowerRaiseBound), raiseProbability
						/ n));
				totalProbability += raiseProbability / n;
				if (n > 1) {
					actions.add(new ProbabilityAction(new RaiseAction(gameState,
							actor, upperRaiseBound), raiseProbability
							/ n));
					totalProbability += raiseProbability / n;
					double[] betSizeSamples = getLinearSamples(n - 2);
					for (double betSizeSample : betSizeSamples) {
						RaiseAction raiseAction = new RaiseAction(gameState,
								actor,
								(int) Math.round(lowerRaiseBound
										+ betSizeSample
										* (upperRaiseBound - lowerRaiseBound)));
						actions.add(new ProbabilityAction(raiseAction,
								raiseProbability / n));
						totalProbability += raiseProbability / n;
					}
				}
			}
		} else {
			// check or bet
			Pair<Double, Double> probabilities = model.getCheckBetProbabilities(gameState, actor);
			double checkProbability = probabilities.getLeft();
			totalProbability += checkProbability;
			actions.add(new ProbabilityAction(new CheckAction(gameState, actor), checkProbability));

			if (!gameState.getPlayer(bot).isAllIn()
					&& gameState.isAllowedToRaise(actor)) {
				double betProbability = probabilities.getRight();
				int lowerRaiseBound = gameState.getLowerRaiseBound(actor);
				int upperRaiseBound = gameState.getUpperRaiseBound(actor);
				int n = upperRaiseBound > lowerRaiseBound ? nbBetSizeSamples : 1;
				actions.add(new ProbabilityAction(new BetAction(gameState, actor, lowerRaiseBound), betProbability / n));
				totalProbability += betProbability / n;
				if (n > 1) {
					actions.add(new ProbabilityAction(new BetAction(gameState, actor, upperRaiseBound), betProbability / n));
					totalProbability += betProbability / n;
					double[] betSizeSamples = getLinearSamples(n - 2);
					for (double betSizeSample : betSizeSamples) {
						BetAction betAction = new BetAction(gameState, actor, (int) Math.round(lowerRaiseBound + betSizeSample
								* (upperRaiseBound - lowerRaiseBound)));
						actions.add(new ProbabilityAction(betAction,
								betProbability / n));
						totalProbability += betProbability / n;
					}
				}
			}
		}
		ImmutableList.Builder<ProbabilityAction> normalizedActionsBuilder = ImmutableList.builder();
		for (ProbabilityAction action : actions) {
			normalizedActionsBuilder.add(new ProbabilityAction(action
					.getActionWrapper(), action.getProbability()
					/ totalProbability));
		}
		return normalizedActionsBuilder.build();
	}

	public static double[] getLinearSamples(int n) {
		double[] samples = new double[n];
		samples[0] = r.nextDouble() / n;
		for (int i = 1; i < samples.length; i++) {
			samples[i] = samples[i-1]+1.0/n;
		}
		return samples;
	}

	public static double[] getLogarithmicSamples(int n) {
		double[] samples = new double[n];
		samples[0] = r.nextDouble() / Math.pow(2, n - 1);
		for (int i = 1; i < samples.length; i++) {
			double p = 1 / Math.pow(2, n - i);
			samples[i] = p + r.nextDouble() * p;
		}
		return samples;
	}

	public static interface Factory {
		Expander create(InnerGameTreeNode node, int tokens);
	}

}
