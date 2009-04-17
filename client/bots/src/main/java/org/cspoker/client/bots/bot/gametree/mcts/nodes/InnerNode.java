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
package org.cspoker.client.bots.bot.gametree.mcts.nodes;

import java.util.List;
import java.util.Random;

import org.cspoker.client.bots.bot.gametree.action.DefaultWinnerException;
import org.cspoker.client.bots.bot.gametree.action.GameEndedException;
import org.cspoker.client.bots.bot.gametree.action.ProbabilityAction;
import org.cspoker.client.bots.bot.gametree.action.SearchBotAction;
import org.cspoker.client.bots.bot.gametree.mcts.listeners.MCTSListener;
import org.cspoker.client.bots.bot.gametree.mcts.strategies.SelectionStrategy;
import org.cspoker.client.bots.bot.gametree.opponentmodel.OpponentModel;
import org.cspoker.client.bots.bot.gametree.search.expander.Expander;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

import com.google.common.collect.ImmutableList;

public abstract class InnerNode extends AbstractNode {

	private final static Random random = new Random();

	//config
	private final OpponentModel model;

	//parent
	public final GameState gameState;
	public final PlayerId bot;

	//children
	private double[] cumulativeActionProbability = null;
	private ImmutableList<INode> children = null;

	//stats
	protected double totalValue = 0;

	protected boolean inTree = false;

	public InnerNode(InnerNode parent, ProbabilityAction probAction, GameState gameState, PlayerId bot, OpponentModel model) {
		super(parent,probAction);
		this.bot = bot;
		this.gameState = gameState;
		this.model = model;
	}

	public INode selectRecursively(SelectionStrategy strategy){
		if(!inTree) return this;
		return selectChild(strategy).selectRecursively(strategy);
	}

	public abstract INode selectChild(SelectionStrategy strategy);

	@Override
	public void expand() {
		inTree = true;
	}

	public double simulate(){
		if(children==null){
			model.assumeTemporarily(gameState);
			expandChildren();
		}
		double result = getRandomChild().simulate();
		if(children==null){
			model.forgetLastAssumption();
		}
		return result;
	}

	public INode getRandomChild() {
		double randomNumber = random.nextDouble();
		ImmutableList<INode> children = getChildren();
		for(int i=0;i<cumulativeActionProbability.length-1;i++){
			if(randomNumber<cumulativeActionProbability[i]){
				return children.get(i);
			}
		}
		return children.get(cumulativeActionProbability.length-1);
	}

	public void backPropagate(double value){
		addSample(value);
		parent.backPropagate(value);
	}

	protected void addSample(double value) {
		++nbSamples;
		totalValue+=value;
	}

	@Override
	public double getAverage() {
		return totalValue/nbSamples;
	}

	public ImmutableList<INode> getChildren(){
		return children;
	}
	
	@Override
	public GameState getGameState() {
		return gameState;
	}

	protected void expandChildren(){
		if(children == null){
			Expander expander = new Expander(gameState, model, gameState.getNextToAct(), bot);
			List<ProbabilityAction> actions = expander.getProbabilityActions();
			ImmutableList.Builder<INode> childrenBuilder = ImmutableList.builder();
			cumulativeActionProbability = new double[actions.size()];
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
	}
	
	public INode getChildAfter(ProbabilityAction probAction) {
		SearchBotAction action = probAction.getAction();
		if (action.endsInvolvementOf(bot)) {
			// bot folded
			return new ConstantLeafNode(this,probAction,gameState.getPlayer(bot).getStack());
		} else {
			try {
				GameState nextState = action.getStateAfterAction();
				// expand further
				if(nextState.getNextToAct().equals(bot)){
					return new DecisionNode(this, probAction, nextState, bot, model);
				}else{
					return new OpponentNode(this, probAction, nextState, bot, model);
				}
			} catch (GameEndedException e) {
				// no active players left
				// go to showdown
				return new ShowdownNode(e.lastState,this, probAction);
			} catch (DefaultWinnerException e) {
				assert e.winner.getPlayerId().equals(bot) : "Bot should have folded earlier, winner can't be " + e.winner;
				// bot wins
				return new ConstantLeafNode(this, probAction, e.winner.getStack() + e.foldState.getGamePotSize());
			}
		}
	}

}
