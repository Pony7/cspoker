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
import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.node.ActionNode;

public abstract class TextOutputVisitor implements NodeVisitor{

	Deque<String> stack = new ArrayDeque<String>();

	private final int maxDepth;
	private int depth=0;


	public TextOutputVisitor() {
		this(Integer.MAX_VALUE);
	}

	public TextOutputVisitor(int maxDepth) {
		stack.push("");
		stack.push(getPrefixElement());
		this.maxDepth = maxDepth;
	}

	@Override
	public void enterNode(ActionNode node, ActionWrapper action, int tokens) {
		depth++;
		if(depth<=maxDepth){
			String prefix = stack.peek();
			output(prefix+getNewNodePrefix()+getNodeDescription(node,action,tokens));
			stack.push(prefix+getPrefixElement());
		}
	}

	@Override
	public void leaveNode(EvaluatedAction<? extends ActionWrapper> evaluation) {
		if(depth<=maxDepth){
			stack.pop();
			String prefix = stack.peek();
			output(prefix+getNodeEndPrefix()+getEndNodeDescription(evaluation));
		}
		depth--;
	}

	protected String getNodeDescription(ActionNode node, ActionWrapper action,
			int tokens) {
		return action+" in "+node + " with "+tokens+" token"+(tokens>1? "s":"")+" in " +node.getGameState().getRound();
	}

	protected String getEndNodeDescription(
			EvaluatedAction<? extends ActionWrapper> evaluation) {
		return evaluation.toString();
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
