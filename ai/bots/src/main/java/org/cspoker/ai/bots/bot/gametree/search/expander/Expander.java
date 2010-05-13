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
package org.cspoker.ai.bots.bot.gametree.search.expander;

import org.cspoker.ai.bots.bot.gametree.action.ProbabilityAction;
import org.cspoker.ai.bots.bot.gametree.search.InnerGameTreeNode;
import org.cspoker.ai.bots.bot.gametree.search.expander.sampling.Sampler;
import org.cspoker.ai.opponentmodels.OpponentModel;
import org.cspoker.ai.opponentmodels.OpponentModelPool;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import com.google.common.collect.ImmutableList;

public class Expander {

	public static final int nbBetSizeSamples = 5;

	private final GameState gameState;
	private final PlayerId actor;
	private final PlayerId bot;
	private final Sampler sampler;

	public Expander(GameState gameState, 
			PlayerId actor, PlayerId bot, Sampler sampler) {
		this.gameState = gameState;
		this.actor = actor;
		this.bot = bot;
		this.sampler = sampler;
	}

	public ImmutableList<ProbabilityAction> getProbabilityActions() {
		OpponentModel actorModel = OpponentModelPool.getInstance().getModel(actor);
		if (actorModel == null) actorModel = OpponentModelPool.getInstance().getModel(bot);
		return sampler.getProbabilityActions(gameState, actorModel, actor, bot);		
	}

	public static interface Factory {
		Expander create(InnerGameTreeNode node, int tokens, Sampler sampler);
	}

}
