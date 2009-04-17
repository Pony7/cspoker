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

import org.cspoker.client.bots.bot.gametree.action.ProbabilityAction;
import org.cspoker.client.bots.bot.gametree.rollout.DistributionRollout4;
import org.cspoker.client.common.gamestate.GameState;

public class ShowdownNode extends LeafNode {

	public final DistributionRollout4 rollout;

	//stats
	protected double totalValue = 0;

	public final int stackSize;
	
	public ShowdownNode(GameState gameState, InnerNode parent, ProbabilityAction probAction) {
		super(parent, probAction);
		this.rollout = new DistributionRollout4(gameState,parent.bot);
		this.stackSize = rollout.botState.getStack();
	}
	
	@Override
	public double getAverage() {
		return totalValue/nbSamples;
	}
	
	@Override
	public double simulate() {
		return stackSize + rollout.doRollOut(4, 4).getMean();
	}
	
	@Override
	public void backPropagate(double value) {
		totalValue+=value;
		++nbSamples;
		parent.backPropagate(value);
	}

	@Override
	public GameState getGameState() {
		return rollout.gameState;
	}

}
