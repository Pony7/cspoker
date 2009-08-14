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
package org.cspoker.ai.bots.bot.gametree.opponentmodel.prolog.redundant;

import java.util.Collection;

import org.cspoker.ai.bots.bot.gametree.opponentmodel.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

@Deprecated
public class RedundantModel implements OpponentModel {

	private final Collection<? extends OpponentModel> models;

	public RedundantModel(Collection<? extends OpponentModel> models) {
		this.models = models;
	}

	@Override
	public void assumeTemporarily(GameState gameState) {
		for (OpponentModel model : models) {
			model.assumeTemporarily(gameState);
		}
	}

	@Override
	public void forgetLastAssumption() {
		for (OpponentModel model : models) {
			model.forgetLastAssumption();
		}
	}

	@Override
	public void assumePermanently(GameState gameState) {
		for (OpponentModel model : models) {
			model.assumePermanently(gameState);
		}
	}

	@Override
	public Pair<Double, Double> getCheckBetProbabilities(GameState gameState,
			PlayerId actor) {
		Pair<Double, Double> previous = null;
		for (OpponentModel opponentModel : models) {
			Pair<Double, Double> current = opponentModel
					.getCheckBetProbabilities(gameState, actor);
			if (previous != null && !previous.equals(current)) {
				throw new IllegalStateException("Inconsistentcy in models: "
						+ previous + " vs " + current);
			}
			previous = current;
		}
		return previous;

	}

	@Override
	public Triple<Double, Double, Double> getFoldCallRaiseProbabilities(
			GameState gameState, PlayerId actor) {
		Triple<Double, Double, Double> previous = null;
		for (OpponentModel opponentModel : models) {
			Triple<Double, Double, Double> current = opponentModel
					.getFoldCallRaiseProbabilities(gameState, actor);
			if (previous != null && !previous.equals(current)) {
				throw new IllegalStateException("Inconsistentcy in models: "
						+ previous + " vs " + current);
			}
			previous = current;
		}
		return previous;
	}

	@Override
	public double[] getShowdownProbabilities(GameState gameState,
			PlayerId actor, int minrank, int maxrank, int avgrank, int sigmarank, double[] weights)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}
