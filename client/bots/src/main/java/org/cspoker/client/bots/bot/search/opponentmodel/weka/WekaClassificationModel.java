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
package org.cspoker.client.bots.bot.search.opponentmodel.weka;


import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

import weka.classifiers.Classifier;

public class WekaClassificationModel extends WekaModel {

	protected final Classifier checkBetClassifier;
	protected final Classifier callRaiseClassifier;
	
	public WekaClassificationModel(Classifier checkBetClassifier, Classifier callRaiseClassifier) {
		this.checkBetClassifier = checkBetClassifier;
		this.callRaiseClassifier = callRaiseClassifier;

	}

	@Override
	public Pair<Double, Double> getCheckBetProbabilities(GameState gameState,
			PlayerId actor) {
		try {
			double[] prob = checkBetClassifier.distributionForInstance(getCheckBetInstance(actor));
			Pair<Double, Double> result = new Pair<Double, Double>(Math.max(0, prob[0]), Math.max(0, prob[1]));
			if(logger.isTraceEnabled()){
				logger.trace(getCheckBetInstance(actor)+": "+result);
			}
			return result;
		} catch (Exception e) {
			throw new IllegalStateException(getCheckBetInstance(actor).toString(), e);
		}
	}

	@Override
	public Triple<Double, Double, Double> getFoldCallRaiseProbabilities(
			GameState gameState, PlayerId actor) {
		try {
			double[] prob = callRaiseClassifier.distributionForInstance(getFoldCallRaiseInstance(actor));
			Triple<Double, Double, Double> result = new Triple<Double, Double, Double>(Math.max(0, prob[0]),Math.max(0, prob[1]),Math.max(0, prob[2]));
			if(logger.isTraceEnabled()){
				logger.trace(getFoldCallRaiseInstance(actor)+": "+result);
			}
			return result;
		} catch (Exception e) {
			throw new IllegalStateException(getFoldCallRaiseInstance(actor).toString(), e);
		}
	}

}
