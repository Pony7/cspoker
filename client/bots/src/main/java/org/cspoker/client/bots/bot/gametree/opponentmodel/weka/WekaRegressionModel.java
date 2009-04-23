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
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

import weka.classifiers.Classifier;
import weka.core.Instance;

public class WekaRegressionModel extends WekaModel {

	protected final Classifier preBetModel;
	protected final Classifier preFoldModel;
	protected final Classifier preCallModel;
	protected final Classifier preRaiseModel;
	protected final Classifier postBetModel;
	protected final Classifier postFoldModel;
	protected final Classifier postCallModel;
	protected final Classifier postRaiseModel;

	public WekaRegressionModel(
			Classifier preBetModel, Classifier preFoldModel, Classifier preCallModel, Classifier preRaiseModel,
			Classifier postBetModel, Classifier postFoldModel, Classifier postCallModel, Classifier postRaiseModel) {
		this.preBetModel = preBetModel;
		this.preFoldModel = preFoldModel;
		this.preCallModel = preCallModel;
		this.preRaiseModel = preRaiseModel;
		this.postBetModel = postBetModel;
		this.postFoldModel = postFoldModel;
		this.postCallModel = postCallModel;
		this.postRaiseModel = postRaiseModel;

	}

	@Override
	public Pair<Double, Double> getCheckBetProbabilities(GameState gameState,
			PlayerId actor) {
		Instance instance;
		if(Round.PREFLOP.equals(gameState.getRound())){
			instance = getPreCheckBetInstance(actor);
		}else{
			instance = getPostCheckBetInstance(actor);
		}
		try {
			double prediction;
			if(Round.PREFLOP.equals(gameState.getRound())){
				prediction = preBetModel.classifyInstance(instance);
			}else{
				prediction = postBetModel.classifyInstance(instance);
			}
			double prob = Math.min(1,Math.max(0, prediction));

			if(Double.isNaN(prob) || Double.isInfinite(prob)){
				throw new IllegalStateException("Bad probability:"+prob);
			}
			Pair<Double, Double> result = new Pair<Double, Double>(1-prob,prob);
			if(logger.isTraceEnabled()){
				if(Round.PREFLOP.equals(gameState.getRound())){
					logger.trace(instance+": "+result);
				}else{
					logger.trace(instance+": "+result);
				}
			}
			return result;
		} catch (Exception e) {
			throw new IllegalStateException(instance.toString(), e);
		}
	}

	@Override
	public Triple<Double, Double, Double> getFoldCallRaiseProbabilities(
			GameState gameState, PlayerId actor) {
		Instance instance;
		boolean preflop = Round.PREFLOP.equals(gameState.getRound());
		if(preflop){
			instance = getPreFoldCallRaiseInstance(actor);
		}else{
			instance = getPostFoldCallRaiseInstance(actor);
		}
		try {
			
			double probFold;
			if(preflop){
				probFold = preFoldModel.classifyInstance(instance);
			}else{
				probFold = postFoldModel.classifyInstance(instance);
			}
			probFold = Math.min(1,Math.max(0, probFold));
			
			double probCall;
			if(preflop){
				probCall = preCallModel.classifyInstance(instance);
			}else{
				probCall = postCallModel.classifyInstance(instance);
			}
			probCall = Math.min(1,Math.max(0, probCall));
			
			double probRaise;
			if(preflop){
				probRaise = preRaiseModel.classifyInstance(instance);
			}else{
				probRaise = postRaiseModel.classifyInstance(instance);
			}
			probRaise = Math.min(1,Math.max(0, probRaise));
			
			double sum = probFold + probCall + probRaise;
			if(Double.isNaN(sum) || sum==0 || Double.isInfinite(sum)){
				throw new IllegalStateException("Bad probabilities:"+probFold+", "+probCall+", "+probRaise);
			}
			Triple<Double, Double, Double> result = new Triple<Double, Double, Double>(probFold/sum,probCall/sum,probRaise/sum);
			if(logger.isTraceEnabled()){
				if(preflop){
					logger.trace(instance+": "+result);
				}else{
					logger.trace(instance+": "+result);
				}
			}
			return result;
		} catch (Exception e) {
			throw new IllegalStateException(instance.toString(), e);
		}
	}

}
