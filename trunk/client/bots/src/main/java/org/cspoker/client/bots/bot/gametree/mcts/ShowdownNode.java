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

import org.cspoker.client.bots.bot.gametree.rollout.DistributionRollout4;
import org.cspoker.client.bots.bot.gametree.rollout.RolloutResult;

public class ShowdownNode extends LeafNode {

	private final DistributionRollout4 rollout;

	//stats
	protected int nbSamples = 0;
	protected double totalValue = 0;
	
	public ShowdownNode(InnerNode parent) {
		super(parent);
		this.rollout = new DistributionRollout4(parent.gameState, parent.bot);
	}
	
	@Override
	public void expand() {
		inTree = true;
	}
	
	@Override
	public double simulate() {
		RolloutResult result = rollout.doRollOut(3, 3);
		double mean = result.getMean();
		totalValue+=mean;
		++nbSamples;
		return mean;
	}

}
