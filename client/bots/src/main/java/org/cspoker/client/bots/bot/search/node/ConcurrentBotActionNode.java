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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.action.SearchBotAction;
import org.cspoker.client.bots.bot.search.node.expander.CompleteExpander;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public class ConcurrentBotActionNode extends BotActionNode {

	private final static Logger logger = Logger.getLogger(ConcurrentBotActionNode.class);
	private final ExecutorService executor;

	public ConcurrentBotActionNode(ExecutorService executor, PlayerId botId, GameState gameState,
			AllPlayersModel opponentModeler, String prefix) {
		super(botId, gameState, opponentModeler, prefix);
		this.executor = executor;
	}

	@Override
	protected CompleteExpander getExpander() {
		return new ConcurrentCompleteExpander(this,executor);
	}

	private static class ConcurrentCompleteExpander extends CompleteExpander{

		private final ExecutorService executor;

		public ConcurrentCompleteExpander(ConcurrentBotActionNode node, ExecutorService executor) {
			super(node);
			this.executor = executor;
		}

		@Override
		public Set<? extends EvaluatedAction<? extends SearchBotAction>> expand() {
			Set<SearchBotAction> actions = node.getAllPossibleActions();

			List<Callable<EvaluatedAction<? extends SearchBotAction>>> tasks = new ArrayList<Callable<EvaluatedAction<? extends SearchBotAction>>>(actions.size());
			for(final SearchBotAction action:actions){
				Callable<EvaluatedAction<? extends SearchBotAction>> task = new Callable<EvaluatedAction<? extends SearchBotAction>>(){
					public EvaluatedAction<? extends SearchBotAction> call(){
						return ConcurrentCompleteExpander.this.node.expandWith(action);
					}
				};
				tasks.add(task);
			}

			try {
				List<Future<EvaluatedAction<? extends SearchBotAction>>> futures = executor.invokeAll(tasks);
				Set<EvaluatedAction<? extends SearchBotAction>> evaluatedActions = new HashSet<EvaluatedAction<? extends SearchBotAction>>(actions.size());
				for(Future<EvaluatedAction<? extends SearchBotAction>> future:futures){
					evaluatedActions.add(future.get());
				}
				return evaluatedActions;
			} catch (InterruptedException e) {
				logger.error(e);
				throw new IllegalStateException(e);
			} catch (ExecutionException e) {
				logger.error(e);
				throw new IllegalStateException(e);
			}
		}

	}

}
