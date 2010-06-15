package org.cspoker.ai.bots.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cspoker.ai.opponentmodels.OpponentModel;
import org.cspoker.ai.opponentmodels.listener.OpponentModelListener;
import org.cspoker.ai.opponentmodels.weka.WekaLearningModel;
import org.cspoker.ai.opponentmodels.weka.WekaRegressionModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

public class KullbackLeiblerListener extends DealCountingListener implements OpponentModelListener {
	
	protected static final Logger logger = Logger.getLogger(KullbackLeiblerListener.class);
	
	private class KLState {
		GameState gameState;
		PlayerId actor;
		
		public KLState(GameState gameState, PlayerId actor) {
			this.gameState = gameState;
			this.actor = actor;
		}
	}
	
	private	ArrayList<KLState> checkBetList = new ArrayList<KLState>();
	private	ArrayList<KLState> foldCallRaiseList = new ArrayList<KLState>();
	private	ArrayList<KLState> showdownList = new ArrayList<KLState>();
	
	private final int max = 5000; // max gameSate per player
	
	private final int reportInterval;
	
	
	private WekaLearningModel learningModel;
	
	public KullbackLeiblerListener() {
		this(64);
	}

	public KullbackLeiblerListener(int reportInterval) {
		this.reportInterval = reportInterval;
	}

	public int size() {
		return checkBetList.size() + foldCallRaiseList.size() + showdownList.size();
	}
	
	
	public double calculatePlayerKL(WekaRegressionModel base, WekaRegressionModel model) {
		double value = 0;
		boolean print = false;
		int index = 0;
		
		for (KLState state : checkBetList) {
			learningModel.assumeTemporarily(state.gameState);
			Pair<Double, Double> pair = model.getCheckBetProbabilities(state.actor, learningModel.getCurrentGamePropositionalizer());
			Pair<Double, Double> basePair = base.getCheckBetProbabilities(state.actor, learningModel.getCurrentGamePropositionalizer());
			if (print) {
				System.out.println(index++ + " - (" + pair.getLeft() + "," + pair.getRight() + ") => (" + basePair.getLeft() + "," + basePair.getRight() + ")");
			}
			learningModel.forgetLastAssumption();
			value += pair.getLeft() * (pair.getLeft()!=0?Math.log(pair.getLeft()/basePair.getLeft()):1);
//			value += pair.getRight() * (pair.getRight()!=0?Math.log(pair.getRight()/basePair.getRight()):1);
		}
		index = 0;
		for (KLState state : foldCallRaiseList) {
			learningModel.assumeTemporarily(state.gameState);
			Triple<Double,Double,Double> triple = model.getFoldCallRaiseProbabilities(state.actor, learningModel.getCurrentGamePropositionalizer());
			Triple<Double,Double,Double> baseTriple = base.getFoldCallRaiseProbabilities(state.actor, learningModel.getCurrentGamePropositionalizer());
			if (print) {
				System.out.println(index++ + " - (" + triple.getLeft() + "," + triple.getMiddle() + "," + triple.getRight() 
						+ ") => (" + + baseTriple.getLeft() + "," + baseTriple.getMiddle() + "," + baseTriple.getRight() + ")");
			}
			learningModel.forgetLastAssumption();
			value += triple.getLeft() * (triple.getLeft()!=0?Math.log(triple.getLeft()/baseTriple.getLeft()):1);
			value += triple.getMiddle() * (triple.getMiddle()!=0?Math.log(triple.getMiddle()/baseTriple.getMiddle()):1);
//			value += triple.getRight() * (triple.getRight()!=0?Math.log(triple.getRight()/baseTriple.getRight()):1);
		}
		index = 0;
		for (KLState state : showdownList) {
			learningModel.assumeTemporarily(state.gameState);
			double[] arr = model.getShowdownProbabilities(state.actor, learningModel.getCurrentGamePropositionalizer());
			double[] baseArr = base.getShowdownProbabilities(state.actor, learningModel.getCurrentGamePropositionalizer());
			if (print) {
				System.out.print(index++ + " - (");
				for (int j = 0; j < arr.length; j++)
					System.out.print(arr[j] + ",");
				System.out.print(") => (");
				for (int j = 0; j < arr.length; j++)
					System.out.print(baseArr[j] + ",");
				System.out.print(")");
			}
			learningModel.forgetLastAssumption();
			for (int i = 1; i < arr.length; i++)
				value += arr[i] * (arr[i]!=0?Math.log(arr[i]/baseArr[i]):1);
		}
		return value;
	}
	
	public HashMap<Object, Double> calculateAllKL() {
		if (learningModel==null) 
			return new HashMap<Object, Double>();
		
		HashMap<Object, Double> klMap = new HashMap<Object, Double>();
		Map<PlayerId, WekaRegressionModel> map = learningModel.getOpponentModels();
		for (PlayerId actor : map.keySet()) {
			klMap.put(actor, calculatePlayerKL(learningModel.getDefaultModel(), map.get(actor)));
		}
		return klMap;
	}
	
	@Override
	public void onGetCheckProbabilities(GameState state, PlayerId actor) {
		if (size() < max)
			checkBetList.add(new KLState(state, actor));
	}

	@Override
	public void onGetFoldCallRaiseProbabilities(GameState state, PlayerId actor) {
		if (size() < max)
			foldCallRaiseList.add(new KLState(state, actor));
	}

	@Override
	public void onGetShowdownProbilities(GameState state, PlayerId actor) {
		if (size() < max)
			showdownList.add(new KLState(state, actor));
	}
	
	@Override
	public void onNewDeal() {
		int deals = getDeals();
		if (deals % reportInterval == 0) {
//			logger.info("deal #" + deals);
//			logger.info("KL list size: " + size());
			if (size() == max) {
				HashMap<Object, Double> list = calculateAllKL();
				for (Object actor : list.keySet()) {
//					logger.info("KL for player " + actor + " equals " + list.get(actor));
					System.out.println(deals + "\t" + list.get(actor));
				}
			} else {
				logger.info("Not enough game states to calculate Kullback Leibler");
			}
		}
		
		super.onNewDeal();
	}

	@Override
	public void setOpponentModel(OpponentModel opponentModel) {
		try {
			this.learningModel = (WekaLearningModel) opponentModel;	
		} catch (Exception e) {
			throw new IllegalStateException("Kullback Leibler Divergence can only be used with WekaLearningModel!");
		}
	}

}
