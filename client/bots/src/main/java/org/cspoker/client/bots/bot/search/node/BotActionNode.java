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

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.SearchConfiguration;
import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.action.BetAction;
import org.cspoker.client.bots.bot.search.action.CallAction;
import org.cspoker.client.bots.bot.search.action.CheckAction;
import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.action.FoldAction;
import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.action.RaiseAction;
import org.cspoker.client.bots.bot.search.action.SearchBotAction;
import org.cspoker.client.bots.bot.search.node.expander.Expander;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor;
import org.cspoker.client.common.gamestate.CachingNode;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;

public class BotActionNode extends ActionNode{

	private final static Logger logger = Logger.getLogger(BotActionNode.class);

	private final Expander expander;

	public BotActionNode(PlayerId botId, GameState gameState,
			SearchConfiguration config, int tokens, int searchId, NodeVisitor... visitors) {
		super(botId, botId, new CachingNode(gameState), config, searchId, visitors);
		this.expander = config.getBotNodeExpanderFactory().create(this,tokens);
	}

	@Override
	public Pair<Double, Double> getEV() {
		EvaluatedAction<? extends ActionWrapper> bestEvaluatedAction = getBestEvaluatedAction();
		return new Pair<Double, Double>(bestEvaluatedAction.getEV(), bestEvaluatedAction.getVarEV());
	}


	public void performbestAction(RemoteHoldemPlayerContext context) throws RemoteException,
	IllegalActionException {
		EvaluatedAction<? extends ActionWrapper> bestEvaluatedAction = getBestEvaluatedAction();
		for(NodeVisitor visitor:visitors){
			visitor.leaveNode(bestEvaluatedAction);
		}
		bestEvaluatedAction.getAction().perform(context);
	}

	public EvaluatedAction<? extends ActionWrapper> getBestEvaluatedAction(){
		double maxEv=Double.NEGATIVE_INFINITY;
		EvaluatedAction<? extends ActionWrapper> action = null;
		Set<? extends EvaluatedAction<? extends ActionWrapper>> actions = getExpander().expand();
		
		for(EvaluatedAction<? extends ActionWrapper> eval : actions){
			double ev = eval.getDiscountedEV(config.getEVDiscount());
			if(ev>maxEv){
				maxEv = ev;
				action = eval;
			}
		}
		if(action == null){
			System.out.println();
		}
		return action;
	}

	protected Expander getExpander() {
		return expander;
	}

	@Override
	public String toString() {
		return "Bot Action Node";
	}

}
