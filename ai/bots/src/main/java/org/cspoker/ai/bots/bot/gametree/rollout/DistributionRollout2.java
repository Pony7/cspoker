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
package org.cspoker.ai.bots.bot.gametree.rollout;

import org.cspoker.ai.bots.bot.gametree.rollout.rankdistribution.ShowdownRankPredictor1of2;
import org.cspoker.ai.bots.bot.gametree.rollout.rankdistribution.ShowdownRankPredictor2of2;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public class DistributionRollout2 extends AbstractDistributionRollout {

	DistributionRollout2(GameState gameState,PlayerId botId) {
		super(gameState,botId);
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
		return "2 Part Distribution Rollout";
	}
	
	public static class Factory implements AbstractDistributionRollout.Factory{
		
		@Override
		public DistributionRollout2 create(GameState gameState,
				PlayerId botId) {
			return new DistributionRollout2(gameState, botId);
		}
		
	}

}
