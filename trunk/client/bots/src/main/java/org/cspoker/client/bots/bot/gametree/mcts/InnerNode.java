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
package org.cspoker.client.bots.bot.gametree.mcts;

import java.util.List;
import java.util.Random;

import org.cspoker.client.bots.bot.gametree.action.DefaultWinnerException;
import org.cspoker.client.bots.bot.gametree.action.GameEndedException;
import org.cspoker.client.bots.bot.gametree.action.ProbabilityAction;
import org.cspoker.client.bots.bot.gametree.action.SearchBotAction;
import org.cspoker.client.bots.bot.gametree.opponentmodel.OpponentModel;
import org.cspoker.client.bots.bot.gametree.search.expander.Expander;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

import com.google.common.collect.ImmutableList;

public class InnerNode extends AbstractNode {

	private final static Random random = new Random();
	
	//config
	private final OpponentModel model;

	//parent
	public final GameState gameState;
	public final SearchBotAction lastAction;
	public final PlayerId bot;

	//children
	private double[] cumulativeActionProbability = null;
	private ImmutableList<INode> children = null;
	private boolean inTree = false;

	//stats
	protected int nbSamples = 0;
	protected double totalValue = 0;

	public InnerNode(InnerNode parent, GameState gameState, SearchBotAction lastAction, PlayerId bot, OpponentModel model) {
		super(parent);
		this.gameState = gameState;
		this.lastAction = lastAction;
		this.bot = bot;
		this.model = model;
	}

	public INode select(SelectionStrategy strategy){
		if(!inTree) return this;
		return strategy.select(children).select(strategy);
	}

	public void expand(){
		inTree = true;
	}

	public double simulate(){
		return getRandomChild().simulate();
	}

	public INode getRandomChild() {
		double randomNumber = random.nextDouble();
		for(int i=0;i<cumulativeActionProbability.length-1;i++){
			if(randomNumber<cumulativeActionProbability[i]){
				return children.get(i);
			}
		}
		return children.get(cumulativeActionProbability.length);
	}

	public void backPropagate(int value){
		++nbSamples;
		totalValue+=value;
		parent.backPropagate(value);
	}

	public ImmutableList<INode> getChildren(){
		if(children == null){
			Expander expander = new Expander(gameState, model, lastAction.actor, bot);
			List<ProbabilityAction> actions = expander.getProbabilityActions();
			ImmutableList.Builder<INode> childrenBuilder = ImmutableList.builder();
			double cumul = 0;
			for (int i=0;i<actions.size();i++) {
				ProbabilityAction action = actions.get(i);
				double probability = action.getProbability();
				childrenBuilder.add(getChildAfter(action));
				cumul += probability;
				cumulativeActionProbability[i] = cumul;
			}
			children = childrenBuilder.build();
		}
		return children;
	}

	public INode getChildAfter(ProbabilityAction action) {
		if (action.getAction().endsInvolvementOf(bot)) {
			// bot folded
			return new ConstantLeafNode(this,gameState.getPlayer(bot).getStack());
		} else {
			try {
				GameState nextState = action.getAction().getStateAfterAction();
				// expand further
				return new InnerNode(this, nextState, action.getAction(), bot, model);
			} catch (GameEndedException e) {
				// no active players left
				// go to showdown
				return new ShowdownNode(this);
			} catch (DefaultWinnerException e) {
				assert e.winner.getPlayerId().equals(bot) : "Bot should have folded earlier, winner can't be " + e.winner;
				// bot wins
				return new ConstantLeafNode(this, e.winner.getStack() + e.foldState.getGamePotSize());
			}
		}
	}
}
