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

import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModel;
import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModels;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

@Deprecated
public class RedundantModel implements OpponentModels{

	private final Collection<? extends OpponentModels> models;

	public RedundantModel(Collection<? extends OpponentModels> models) {
		this.models = models;
	}

	@Override
	public void assume(GameState gameState) {
		for(OpponentModels model:models)
			model.assume(gameState);
	}

	@Override
	public void forgetAssumption() {
		for(OpponentModels model:models)
			model.forgetAssumption();
	}

	@Override
	public OpponentModel getModelFor(GameState gameState, PlayerId opponentId) {
		final List<OpponentModel> opponentModels = new ArrayList<OpponentModel>(models.size());
		for(OpponentModels model:models){
			opponentModels.add(model.getModelFor(gameState, opponentId));
		}
		return new OpponentModel(){
			
			@Override
			public Pair<Double, Double> getCheckBetProbabilities(
					GameState gameState, PlayerId actor) {
				Pair<Double,Double> previous = null;
				for (OpponentModel opponentModel : opponentModels) {
					Pair<Double,Double> current = opponentModel.getCheckBetProbabilities(gameState, actor);
					if(previous!=null && !previous.equals(current)){
						throw new IllegalStateException("Inconsistentcy in models: "+previous+" vs "+current);
					}
					previous = current;
				}
				return previous;
			}
			
			@Override
			public Triple<Double, Double, Double> getFoldCallRaiseProbabilities(
					GameState gameState, PlayerId actor) {
				Triple<Double, Double, Double> previous = null;
				for (OpponentModel opponentModel : opponentModels) {
					Triple<Double, Double, Double> current = opponentModel.getFoldCallRaiseProbabilities(gameState, actor);
					if(previous!=null && !previous.equals(current)){
						throw new IllegalStateException("Inconsistentcy in models: "+previous+" vs "+current);
					}
					previous = current;
				}
				return previous;
			}
			
		};
	}

	@Override
	public void signalNextAction(GameState gameState) {
		for(OpponentModels model:models)
			model.signalNextAction(gameState);
	}

}
