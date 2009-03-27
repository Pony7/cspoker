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
package org.cspoker.client.bots.bot.search.node.expander;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.action.BetAction;
import org.cspoker.client.bots.bot.search.action.CallAction;
import org.cspoker.client.bots.bot.search.action.CheckAction;
import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.action.FoldAction;
import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.action.RaiseAction;
import org.cspoker.client.bots.bot.search.node.InnerGameTreeNode;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

public abstract class Expander {
	
	public static final int nbBetSizeSamples = 3;
	private final static Random r = new Random();
	
	protected final InnerGameTreeNode node;
	protected final int tokens;

	public Expander(InnerGameTreeNode node, int tokens) {
		this.node = node;
		this.tokens = tokens;
	}
	
	public abstract Set<? extends EvaluatedAction<? extends ActionWrapper>> expand();

	public int getTokens() {
		return tokens;
	}
	
	public InnerGameTreeNode getNode() {
		return node;
	}
	
	protected Set<ProbabilityAction> getProbabilityActions() {
		GameState gameState = node.getGameState();
		HashSet<ProbabilityAction> actions = new LinkedHashSet<ProbabilityAction>();
		double totalProbability = 0;
		if(gameState.hasBet()){
			//call, raise or fold
			Triple<Double,Double,Double> probabilities = node.getFoldCallRaiseProbabilities();

			double foldProbability = probabilities.getLeft();
			totalProbability+= foldProbability;
			actions.add(new ProbabilityAction(new FoldAction(gameState, node.getPlayerId()),foldProbability));
			
			double callProbability = probabilities.getMiddle();
			totalProbability+=callProbability;
			actions.add(new ProbabilityAction(new CallAction(gameState, node.getPlayerId()),callProbability));

			if(!gameState.getPlayer(node.getBotId()).isAllIn() && gameState.isAllowedToRaise(node.getPlayerId())){
				double raiseProbability = probabilities.getRight();
				int lowerRaiseBound = gameState.getLowerRaiseBound(node.getPlayerId());
				int upperRaiseBound = gameState.getUpperRaiseBound(node.getPlayerId());
				int n = upperRaiseBound>lowerRaiseBound? nbBetSizeSamples:1;
				actions.add(new ProbabilityAction(new RaiseAction(gameState, node.getPlayerId(), lowerRaiseBound),raiseProbability/n));
				totalProbability+=raiseProbability/n;
				if(n>1){
					double[] betSizeSamples = getLogarithmicSamples(n-1);
					for (int i = 0; i < betSizeSamples.length; i++) {
						RaiseAction raiseAction = new RaiseAction(gameState, node.getPlayerId(), (int)Math.round(lowerRaiseBound+betSizeSamples[i]*(upperRaiseBound-lowerRaiseBound)));
						actions.add(new ProbabilityAction(raiseAction,raiseProbability/n));
						totalProbability+=raiseProbability/n;
					}
				}
			}
		}else{
			//check or bet
			Pair<Double,Double> probabilities = node.getCheckBetProbabilities();
			double checkProbability = probabilities.getLeft();
			totalProbability+=checkProbability;
			actions.add(new ProbabilityAction(new CheckAction(gameState, node.getPlayerId()),checkProbability));

			if(!gameState.getPlayer(node.getBotId()).isAllIn() && gameState.isAllowedToRaise(node.getPlayerId())){
				double betProbability = probabilities.getRight();
				int lowerRaiseBound = gameState.getLowerRaiseBound(node.getPlayerId());
				int upperRaiseBound = gameState.getUpperRaiseBound(node.getPlayerId());
				int n = upperRaiseBound>lowerRaiseBound? 3:1;
				actions.add(new ProbabilityAction(new BetAction(gameState, node.getPlayerId(), lowerRaiseBound),betProbability/n));
				totalProbability+=betProbability/n;
				if(n>1){
					double[] betSizeSamples = getLogarithmicSamples(n-1);
					for (int i = 0; i < betSizeSamples.length; i++) {
						BetAction betAction = new BetAction(gameState, node.getPlayerId(), (int)Math.round(lowerRaiseBound+betSizeSamples[i]*(upperRaiseBound-lowerRaiseBound)));
						actions.add(new ProbabilityAction(betAction,betProbability/n));
						totalProbability+=betProbability/n;
					}
				}
			}
		}
		HashSet<ProbabilityAction> normalizedActions = new HashSet<ProbabilityAction>();
		for(ProbabilityAction action:actions){
			normalizedActions.add(new ProbabilityAction(action.getActionWrapper(), action.getProbability()/totalProbability));
		}	
		return normalizedActions;
	}

	public static double[] getLogarithmicSamples(int n){
		double[] samples = new double[n];
		samples[0] = r.nextDouble()/Math.pow(2, n-1);
		for(int i=1;i<samples.length;i++){
			double p = 1/Math.pow(2, n-i);
			samples[i] = p+r.nextDouble()*p;
		}
		return samples;
	}
	
	public static interface Factory{
		Expander create(InnerGameTreeNode node, int tokens);
	}
	
}
