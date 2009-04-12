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

import java.util.ArrayDeque;
import java.util.Deque;

import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.node.Distribution;
import org.cspoker.client.bots.bot.search.node.GameTreeNode;
import org.cspoker.common.util.Pair;

public abstract class TextOutputVisitor implements NodeVisitor {

	Deque<String> stack = new ArrayDeque<String>();

	private final int maxDepth;
	private int depth = 0;

	public TextOutputVisitor() {
		this(Integer.MAX_VALUE);
	}

	public TextOutputVisitor(int maxDepth) {
		stack.push("");
		stack.push(getPrefixElement());
		this.maxDepth = maxDepth;
	}

	@Override
	public void enterNode(Pair<ActionWrapper,GameTreeNode> pair) {
		depth++;
		if (depth <= maxDepth) {
			String prefix = stack.peek();
			output(prefix + getNewNodePrefix()
					+ getNodeDescription(pair, pair.getRight().getNbTokens()));
			stack.push(prefix + getPrefixElement());
		}
	}

	@Override
	public void leaveNode(Pair<ActionWrapper,GameTreeNode> node, Distribution distr) {
		if (depth <= maxDepth) {
			stack.pop();
			String prefix = stack.peek();
			output(prefix + getNodeEndPrefix()
					+ getEndNodeDescription(node, distr));
		}
		depth--;
	}

	@Override
	public void pruneSubTree(Pair<ActionWrapper, GameTreeNode> node,
			Distribution distribution) {
		enterNode(node);
		leaveNode(node, distribution);
	}
	
	@Override
	public void visitLeafNode(int winnings, double probability,
			int minWinnable, int maxWinnable) {
		if (depth+1 <= maxDepth) {
			String prefix = stack.peek();
			output(prefix + getNewNodePrefix()
					+ getNodeDescription(winnings, probability, minWinnable, maxWinnable));
		}
	}

	protected String getNodeDescription(int winnings, double probability,
			int minWinnable, int maxWinnable) {
		if(winnings==maxWinnable){
			return "Win, "+winnings+" ("+Math.round(100 * probability) + "%)";
		}else if(winnings==minWinnable){
			return "Lose, "+winnings+" ("+Math.round(100 * probability) + "%)";
		}else return "Draw, "+winnings+" ("+Math.round(100 * probability) + "%)";
	}

	protected String getNodeDescription(Pair<ActionWrapper,GameTreeNode> node, int tokens) {
		return node.getLeft() + " in " + node.getRight() + " with " + tokens + " token"
				+ (tokens > 1 ? "s" : "") + " in "
				+ node.getRight().getGameState().getRound();
	}

	protected String getEndNodeDescription(Pair<ActionWrapper,GameTreeNode> node, Distribution value) {
		String bound = value.isUpperBound()?"<":"";
		return "EV is "+bound + Math.round(value.getMean()) + " for " + node.getRight().toString() + " (σ="
        + Math.round(Math.sqrt(value.getVariance())) + ")";

	}

	protected String getPrefixElement() {
		return "   |";
	}

	protected String getNewNodePrefix() {
		return "---o ";
	}

	protected String getNodeEndPrefix() {
		return "   `";
	}

	protected abstract void output(String line);

}
