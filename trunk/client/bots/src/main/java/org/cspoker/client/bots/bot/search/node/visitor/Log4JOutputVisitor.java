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

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.action.SampledAction;
import org.cspoker.client.bots.bot.search.node.ActionNode;

public class Log4JOutputVisitor extends TextOutputVisitor {

	private final static Logger logger = Logger.getLogger(Log4JOutputVisitor.class);

	public Log4JOutputVisitor() {
		super();
	}
	
	public Log4JOutputVisitor(int maxDepth) {
		super(maxDepth);
	}
	
	@Override
	public void enterNode(ActionNode node, ActionWrapper action, int tokens) {
		if(logger.isDebugEnabled()){
			super.enterNode(node, action, tokens);
		}
	}

	@Override
	public void leaveNode(EvaluatedAction<? extends ActionWrapper> evaluation) {
		if(logger.isDebugEnabled()){
			super.leaveNode(evaluation);
		}
	}

	@Override
	protected void output(String line) {
		logger.debug(line);
	}
	
	public static class Factory implements NodeVisitor.Factory{

		private final int maxDepth;

		public Factory(int maxDepth) {
			this.maxDepth = maxDepth;
		}
		
		@Override
		public Log4JOutputVisitor create() {
			return new Log4JOutputVisitor(maxDepth);
		}
		
	}

	@Override
	public void visitLoseNode(int ev, double p) {
		
	}

	@Override
	public void visitWinNode(int ev, double p) {
		
	}

}
