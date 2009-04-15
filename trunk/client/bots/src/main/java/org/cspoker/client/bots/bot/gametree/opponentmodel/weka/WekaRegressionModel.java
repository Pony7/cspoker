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
package org.cspoker.client.bots.bot.gametree.opponentmodel.weka;


import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

import weka.classifiers.Classifier;

public class WekaRegressionModel extends WekaModel {

	protected final Classifier betModel;
	protected final Classifier foldModel;
	protected final Classifier callModel;
	protected final Classifier raiseModel;
	
	public WekaRegressionModel(Classifier betModel, Classifier foldModel, Classifier callModel, Classifier raiseModel) {
		this.betModel = betModel;
		this.foldModel = foldModel;
		this.callModel = callModel;
		this.raiseModel = raiseModel;

	}

	@Override
	public Pair<Double, Double> getCheckBetProbabilities(GameState gameState,
			PlayerId actor) {
		try {
			double prob = Math.max(0, betModel.classifyInstance(getCheckBetInstance(actor)));
			Pair<Double, Double> result = new Pair<Double, Double>(1-prob,prob);
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
			double probFold = Math.max(0, foldModel.classifyInstance(getFoldCallRaiseInstance(actor)));
			double probCall = Math.max(0, callModel.classifyInstance(getFoldCallRaiseInstance(actor)));
			double probRaise = Math.max(0, raiseModel.classifyInstance(getFoldCallRaiseInstance(actor)));
			double sum = probFold + probCall + probRaise;
			if(sum==0){
				probFold = probCall = probRaise = 1/3;
				sum = 1;
			}
			Triple<Double, Double, Double> result = new Triple<Double, Double, Double>(probFold/sum,probCall/sum,probRaise/sum);
			if(logger.isTraceEnabled()){
				logger.trace(getFoldCallRaiseInstance(actor)+": "+result);
			}
			return result;
		} catch (Exception e) {
			throw new IllegalStateException(getFoldCallRaiseInstance(actor).toString(), e);
		}
	}

}
