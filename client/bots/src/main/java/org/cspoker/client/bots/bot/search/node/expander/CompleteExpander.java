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

import java.util.ArrayList;
import java.util.List;

import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.node.GameTreeNode;
import org.cspoker.client.bots.bot.search.node.InnerGameTreeNode;
import org.cspoker.common.util.Pair;

public class CompleteExpander extends Expander {

	public CompleteExpander(InnerGameTreeNode node, int tokens) {
		super(node, tokens);
	}

	public List<Pair<ActionWrapper,GameTreeNode>> getChildren(boolean uniformTokens) {
		if(!uniformTokens){
			throw new IllegalArgumentException("Only uniform tokens are allowed.");
		}
		List<ProbabilityAction> actions = getProbabilityActions();
		int subtreeTokens = Math.max(1, tokens / actions.size());
		List<Pair<ActionWrapper,GameTreeNode>> children = new ArrayList<Pair<ActionWrapper,GameTreeNode>>(actions.size());
		for (ProbabilityAction action : actions) {
			children.add(new Pair<ActionWrapper, GameTreeNode>(action,node.getChildAfter(action, subtreeTokens)));
		}
		return children;
	}
	
	public static class Factory implements Expander.Factory {
		public CompleteExpander create(InnerGameTreeNode node, int tokens) {
			return new CompleteExpander(node, tokens);
		}
	}

}
