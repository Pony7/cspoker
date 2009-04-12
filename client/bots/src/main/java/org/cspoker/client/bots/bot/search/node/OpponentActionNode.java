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
package org.cspoker.client.bots.bot.search.node;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.SearchConfiguration;
import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.node.expander.SamplingExpander;
import org.cspoker.client.bots.bot.search.node.expander.WeightedNode;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;

public class OpponentActionNode extends ActionNode {

	private final static Logger logger = Logger
	.getLogger(OpponentActionNode.class);

	private final SamplingExpander expander;
	private Distribution valueDistribution = null;

	public OpponentActionNode(PlayerId opponentId, PlayerId botId,
			GameState gameState, SearchConfiguration config, int tokens,
			int searchId, NodeVisitor... visitors) {
		super(opponentId, botId, gameState, config, searchId, visitors);
		expander = new SamplingExpander(this, tokens);
	}

	@Override
	public Distribution getValueDistribution(double lowerBound) {
		if(valueDistribution==null){
			config.getOpponentModeler().assumeTemporarily(gameState);
			List<Pair<ActionWrapper, WeightedNode>> children = getExpander().getWeightedChildren(config.isUniformBotActionTokens());
			double percentageDone = 0;
			double valueDone = 0;
			double maxToDo = 0;
			List<Distribution> valueDistributions = new ArrayList<Distribution>(children.size());
			for (Pair<ActionWrapper, WeightedNode> pair : children) {
				WeightedNode child = pair.getRight();
				double prob = child.getWeight();
				double upperWinBound = child.getNode().getUpperWinBound();
				maxToDo += prob*upperWinBound;
			}
			for (int i=0;i<children.size();i++) {
				Pair<ActionWrapper, WeightedNode> pair = children.get(i);
				WeightedNode child = pair.getRight();
				double upperWinBound = child.getNode().getUpperWinBound();
				double prob = child.getWeight();
				maxToDo -= prob*upperWinBound;
				int requiredFromSubtree = (int)((lowerBound-valueDone-maxToDo)/prob);
				if(requiredFromSubtree>upperWinBound){
					//prune
					for(int j=i;j<children.size();j++){
						for (NodeVisitor visitor : visitors) {
							Pair<ActionWrapper,WeightedNode> skipped = children.get(j);
							Pair<ActionWrapper, GameTreeNode> node = new Pair<ActionWrapper, GameTreeNode>(skipped.getLeft(), skipped.getRight().getNode());
							visitor.pruneSubTree(node, new Distribution(node.getRight().getUpperWinBound(),0,true));
						}
					}
					valueDistribution = new Distribution(valueDone+prob*upperWinBound+maxToDo,0.0, true);
					return valueDistribution;
				}
				percentageDone += prob;
				for (NodeVisitor visitor : visitors) {
					visitor.enterNode(new Pair<ActionWrapper, GameTreeNode>(pair.getLeft(), pair.getRight().getNode()));
				}
				Distribution valueDistribution = child.getNode().getValueDistribution(requiredFromSubtree);
				for (NodeVisitor visitor : visitors) {
					visitor.leaveNode(new Pair<ActionWrapper, GameTreeNode>(pair.getLeft(), pair.getRight().getNode()), valueDistribution);
				}
				valueDone += prob*valueDistribution.getMean();
				valueDistributions.add(valueDistribution);
			}
			config.getOpponentModeler().forgetLastAssumption();
			// see Variance Estimation and Ranking of Gaussian Mixture Distributions
			// in Target Tracking Applications
			// Lidija Trailovi and Lucy Y. Pao
			double varEV = 0;
			for (int i=0;i<valueDistributions.size();i++) {
				Distribution valueDistribution = valueDistributions.get(i);
				double m = valueDistribution.getMean();
				double var = valueDistribution.getVariance();
				double w = children.get(i).getRight().getWeight();
				varEV += w * (var + m * m);
			}
			valueDistribution = new Distribution(valueDone, Math.max(0, varEV - valueDone));
		}
		return valueDistribution;
	}

	public SamplingExpander getExpander() {
		return expander;
	}
	
	public int getNbTokens() {
		return expander.getTokens();
	}

	@Override
	public String toString() {
		return "Opponent " + playerId + " Action Node";
	}


	//	@Override
	//	public Set<? extends EvaluatedAction<? extends SampledAction>> expand(boolean uniformTokens, double lowerBound) {
	//		List<SampledAction> sampledActions = sampleActions();
	//		Set<EvaluatedAction<SampledAction>> evaluatedActions = new HashSet<EvaluatedAction<SampledAction>>(
	//				sampledActions.size());
	//		double percentageDone = 0;
	//		double valueDone = 0;
	//		double maxToDo = 0;
	//		for (SampledAction sampledAction : sampledActions) {
	//			double prob = sampledAction.getTimes()/(double)sampledAction.getOutof();
	//			int upperWinBound = node.getUpperWinBound(sampledAction);
	//			maxToDo += prob*upperWinBound;
	//		}
	//		for (int i=0;i<sampledActions.size();i++) {
	//			SampledAction sampledAction = sampledActions.get(i);
	//			double prob = sampledAction.getTimes()/(double)sampledAction.getOutof();
	//			int upperWinBound = node.getUpperWinBound(sampledAction);
	//			maxToDo -= prob*upperWinBound;
	//			int requiredFromSubtree = (int)((lowerBound-valueDone-maxToDo)/prob);
	//			if(requiredFromSubtree>upperWinBound){
	//				//prune
	//				evaluatedActions.add(new EvaluatedAction<SampledAction>(sampledAction,upperWinBound,0, true));
	//				for(int j=i+1;j<sampledActions.size();j++){
	//					SampledAction sampledAction2 = sampledActions.get(j);
	//					int upperWinBound2 = node.getUpperWinBound(sampledAction2);
	//					evaluatedActions.add(new EvaluatedAction<SampledAction>(sampledAction2,upperWinBound2,0, true));
	//				}
	//				return evaluatedActions;
	//			}
	//			int tokensShare = uniformTokens? tokens / sampledActions.size() : tokens* sampledAction.getTimes() / sampledAction.getOutof();
	//			EvaluatedAction<SampledAction> expanded = node.expandWith(sampledAction, tokensShare, requiredFromSubtree);
	//			percentageDone += prob;
	//			valueDone += prob*expanded.getEV();
	//			evaluatedActions.add(expanded);
	//		}
	//		return evaluatedActions;
	//	}

}
