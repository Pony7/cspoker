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
package org.cspoker.client.bots.bot.search.opponentmodel.prolog.redundant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

@Deprecated
public class RedundantModel implements AllPlayersModel{

	private final Collection<? extends AllPlayersModel> models;

	public RedundantModel(Collection<? extends AllPlayersModel> models) {
		this.models = models;
	}

	@Override
	public void assume(GameState gameState) {
		for(AllPlayersModel model:models)
			model.assume(gameState);
	}

	@Override
	public void forgetAssumption() {
		for(AllPlayersModel model:models)
			model.forgetAssumption();
	}

	@Override
	public OpponentModel getModelFor(PlayerId opponentId, GameState gameState) {
		final List<OpponentModel> opponentModels = new ArrayList<OpponentModel>(models.size());
		for(AllPlayersModel model:models){
			opponentModels.add(model.getModelFor(opponentId, gameState));
		}
		return new OpponentModel(){
			
			@Override
			public Set<ProbabilityAction> getProbabilityActions(
					GameState gameState) {
				Set<ProbabilityAction> actions = null;
				Set<ProbabilityAction> previousActions = null;
				for(OpponentModel model:opponentModels){
					actions  = model.getProbabilityActions(gameState);
					if(previousActions!= null && !previousActions.equals(actions)){
						throw new IllegalStateException("Inconsistency: "+previousActions+" "+actions);
					}
					previousActions = actions;
				}
				return actions;
			}
			
		};
	}

	@Override
	public void signalNextAction(GameState gameState) {
		for(AllPlayersModel model:models)
			model.signalNextAction(gameState);
	}

}
