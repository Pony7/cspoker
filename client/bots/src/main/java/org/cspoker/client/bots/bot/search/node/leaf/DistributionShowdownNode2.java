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
package org.cspoker.client.bots.bot.search.node.leaf;

import org.cspoker.client.bots.bot.search.SearchConfiguration;
import org.cspoker.client.bots.bot.search.node.leaf.rankdistribution.ShowdownRankPredictor1of2;
import org.cspoker.client.bots.bot.search.node.leaf.rankdistribution.ShowdownRankPredictor2of2;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public class DistributionShowdownNode2 extends AbstractDistributionShowdownNode {

	DistributionShowdownNode2(PlayerId botId, GameState gameState, int tokens,
			NodeVisitor... nodeVisitors) {
		super(botId, gameState, tokens, nodeVisitors);
	}

	@Override
	protected float getRelativeProbability(int rank, int relativePotSize) {
		if (relativePotSize <= 15) {
			return ShowdownRankPredictor1of2.getRelativeProbability(rank);
		} else {
			return ShowdownRankPredictor2of2.getRelativeProbability(rank);
		}

	}

	@Override
	public String toString() {
		return "2 Part Distribution Showdown Node";
	}

	public static class Factory implements
			AbstractDistributionShowdownNode.Factory {

		@Override
		public DistributionShowdownNode2 create(PlayerId botId,
				GameState gameState, int tokens, SearchConfiguration config,
				int searchId, NodeVisitor... nodeVisitors) {
			return new DistributionShowdownNode2(botId, gameState, tokens,
					nodeVisitors);
		}

		@Override
		public String toString() {
			return "2 Part Distribution Showdown Node factory";
		}
	}
}
