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

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaClassifierModel implements OpponentModel {

	private final static Logger logger = Logger.getLogger(WekaClassifierModel.class);

	private final PlayerTrackingVisitor visitor;

	private final Classifier checkBetClassifier;
	private final Classifier callRaiseClassifier;

	private final Instances callRaiseDataSet;
	private final Instances checkBetDataSet;


	public WekaClassifierModel(Classifier checkBetClassifier, Classifier callRaiseClassifier) {
		this.visitor = new PlayerTrackingVisitor();
		this.checkBetClassifier = checkBetClassifier;
		this.callRaiseClassifier = callRaiseClassifier;
		try {
			this.callRaiseDataSet = new DataSource(
					getClass().
					getClassLoader().
					getResourceAsStream("org/cspoker/client/bots/bot/search/opponentmodel/weka/CallRaiseActionHeader.arff")
			).getStructure();
			this.checkBetDataSet = new DataSource(
					getClass().
					getClassLoader().
					getResourceAsStream("org/cspoker/client/bots/bot/search/opponentmodel/weka/CheckBetActionHeader.arff")
			).getStructure();
		} catch (Exception e) {
			//stupid Weka
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Pair<Double, Double> getCheckBetProbabilities(GameState gameState,
			PlayerId actor) {
		Propositionalizer prop = getTopVisitor().getPropz();
		PlayerData p = prop.getPlayers().get(actor);
		Instance instance = new Instance(23);
		instance.setDataset(checkBetDataSet);
		// Timing
		instance.setValue(0,prop.getRoundCompletion());
		instance.setValue(1,prop.getPlayersActed());
		instance.setValue(2,prop.getPlayersToAct());
		instance.setValue(3,prop.getRound());
		instance.setValue(4,p.getGameCount());
		instance.setValue(5,prop.isSomebodyActedThisRound()+"");
		// Amounts
		instance.setValue(6,prop.getPotSize());
		instance.setValue(7,p.getStackSize(prop));
		// Player count
		instance.setValue(8,prop.getNbSeatedPlayers());
		instance.setValue(9,prop.getNbActivePlayers() );
		instance.setValue(10,prop.getActivePlayerRatio());
		// Global player frequencies
		instance.setValue(11,p.getBetFrequency());
		// Per-round player frequencies
		instance.setValue(12,p.getRoundBetFrequency(prop));
		// Game betting behaviour
		instance.setValue(13,p.isComitted()+"");
		instance.setValue(14,prop.getNbGameRaises());
		instance.setValue(15,p.getNbPlayerRaises());
		instance.setValue(16,p.getGameRaisePercentage(prop));
		instance.setValue(17,p.isLastActionWasRaise()+"");
		// PT Stats
		instance.setValue(18,p.getVPIP());
		instance.setValue(19,p.getPFR());
		instance.setValue(20,p.getAF());
		instance.setValue(21,p.getWtSD());
		try {
			double[] prob = checkBetClassifier.distributionForInstance(instance);
			Pair<Double, Double> result = new Pair<Double, Double>(Math.max(0, prob[0]), Math.max(0, prob[1]));
			if(logger.isTraceEnabled()){
				logger.trace(instance+": "+result);
			}
			return result;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Triple<Double, Double, Double> getFoldCallRaiseProbabilities(
			GameState gameState, PlayerId actor) {
		Propositionalizer prop = getTopVisitor().getPropz();
		PlayerData p = prop.getPlayers().get(actor);
		Instance instance = new Instance(29);
		instance.setDataset(callRaiseDataSet);
		// Timing
		instance.setValue(0,prop.getRoundCompletion());
		instance.setValue(1,prop.getPlayersActed());
		instance.setValue(2,prop.getPlayersToAct());
		//		if(prop.getPlayersToAct()<0){
		//			throw new IllegalStateException();
		//		}
		instance.setValue(3,prop.getRound());
		instance.setValue(4,p.getGameCount());
		instance.setValue(5,prop.isSomebodyActedThisRound()+"");
		// Amounts
		instance.setValue(6,prop.getPotSize());
		instance.setValue(7,p.getDeficit(prop));
		instance.setValue(8,p.getPotOdds(prop));
		instance.setValue(9,p.getStackSize(prop));
		// Player count
		instance.setValue(10,prop.getNbSeatedPlayers());
		instance.setValue(11,prop.getNbActivePlayers() );
		instance.setValue(12,prop.getActivePlayerRatio());
		// Global player frequencies
		instance.setValue(13,p.getFoldFrequency());
		instance.setValue(14,p.getCallFrequency());
		instance.setValue(15,p.getRaiseFrequency());
		// Per-round player frequencies
		instance.setValue(16,p.getRoundFoldFrequency(prop));
		instance.setValue(17,p.getRoundCallFrequency(prop));
		instance.setValue(18,p.getRoundRaiseFrequency(prop));
		// Game betting behaviour
		instance.setValue(19,p.isComitted()+"");
		instance.setValue(20,prop.getNbGameRaises());
		instance.setValue(21,p.getNbPlayerRaises());
		instance.setValue(22,p.getGameRaisePercentage(prop));
		instance.setValue(23,p.isLastActionWasRaise()+"");
		// PT Stats
		instance.setValue(24,p.getVPIP());
		instance.setValue(25,p.getPFR());
		instance.setValue(26,p.getAF());
		instance.setValue(27,p.getWtSD());
		try {
			double[] prob = callRaiseClassifier.distributionForInstance(instance);
			Triple<Double, Double, Double> result = new Triple<Double, Double, Double>(Math.max(0, prob[0]),Math.max(0, prob[1]),Math.max(0, prob[2]));
			if(logger.isTraceEnabled()){
				logger.trace(instance+": "+result);
			}
			return result;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void assumePermanently(GameState gameState) {
		visitor.readHistory(gameState);
	}

	private final Deque<PlayerTrackingVisitor> visitors = new ArrayDeque<PlayerTrackingVisitor>();


	@Override
	public void assumeTemporarily(GameState gameState) {
		PlayerTrackingVisitor root = visitors.isEmpty() ? visitor : getTopVisitor();
		PlayerTrackingVisitor clonedTopVisitor = root.clone();
		clonedTopVisitor.readHistory(gameState);
		visitors.push(clonedTopVisitor);
	}

	@Override
	public void forgetLastAssumption() {
		visitors.pop();
	}

	protected PlayerTrackingVisitor getTopVisitor() {
		return visitors.peek();
	}

}
