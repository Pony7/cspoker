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
package org.cspoker.client.bots.bot.search.node.visitor;

import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.node.Distribution;
import org.cspoker.client.bots.bot.search.node.GameTreeNode;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;

public class StatisticsVisitor implements NodeVisitor {

	private int nbNodes = 0;
	private int nbPrunedSubtrees = 0;
	private int nbPrunedTokens = 0;
	private int nbOpponentModelCalls = 0;
	
	@Override
	public void enterNode(Pair<ActionWrapper, GameTreeNode> node, double lowerBound) {
		nbNodes++;
	}

	@Override
	public void leaveNode(Pair<ActionWrapper, GameTreeNode> node,
			Distribution distribution) {
		
	}

	@Override
	public void visitLeafNode(int winnings, double probability,
			int minWinnable, int maxWinnable) {
	}
	
	@Override
	public void pruneSubTree(Pair<ActionWrapper, GameTreeNode> node,
			Distribution distribution, double lowerBound) {
		nbPrunedSubtrees++;
		nbPrunedTokens += node.getRight().getNbTokens();
	}

	@Override
	public void callOpponentModel() {
		nbOpponentModelCalls++;
	}
	
	public int getNbNodes() {
		return nbNodes;
	}
	
	public int getNbPrunedSubtrees() {
		return nbPrunedSubtrees;
	}
	
	public int getNbPrunedTokens() {
		return nbPrunedTokens;
	}
	
	public int getNbOpponentModelCalls() {
		return nbOpponentModelCalls;
	}
	
	public static class Factory implements NodeVisitor.Factory{
		
		private StatisticsVisitor statistics;
		
		@Override
		public NodeVisitor create(GameState gameState, PlayerId actor) {
			statistics = new StatisticsVisitor();
			return statistics;
		}
		
		public StatisticsVisitor getStatistics() {
			return statistics;
		}
	}

}
