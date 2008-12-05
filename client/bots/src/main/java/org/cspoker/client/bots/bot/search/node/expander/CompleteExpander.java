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

import java.util.LinkedHashSet;
import java.util.Set;

import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.action.SearchBotAction;
import org.cspoker.client.bots.bot.search.node.InnerGameTreeNode;

public class CompleteExpander extends Expander{

	public CompleteExpander(InnerGameTreeNode node, int tokens) {
		super(node, tokens);
	}

	public Set<? extends EvaluatedAction<? extends SearchBotAction>> expand(){
		Set<SearchBotAction> actions = node.getAllPossibleActions();
		int subtreeTokens = Math.max(1,tokens/actions.size());
		Set<EvaluatedAction<SearchBotAction>> evaluatedActions = new LinkedHashSet<EvaluatedAction<SearchBotAction>>(actions.size());
		for(SearchBotAction action:actions){
			evaluatedActions.add(node.expandWith(action, subtreeTokens));
		}
		return evaluatedActions;
	}
	
}
