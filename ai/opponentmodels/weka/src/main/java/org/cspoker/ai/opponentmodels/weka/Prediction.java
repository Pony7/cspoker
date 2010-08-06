package org.cspoker.ai.opponentmodels.weka;

import org.cspoker.ai.bots.bot.gametree.action.SearchBotAction;

public class Prediction {
	
	private SearchBotAction action;
	private double probActual;
	private double probHypothesis;

	public Prediction(SearchBotAction action, double probActual, double probHypothesis) {
		if (!checkProbability(probActual))
			throw new IllegalArgumentException("Incorrect probability of actual action => " + probActual);
		if (!checkProbability(probHypothesis))
			throw new IllegalArgumentException("Incorrect probability of hypothesis action => " + probHypothesis);
		
		this.action = action;
		this.probActual = probActual;
		this.probHypothesis = probHypothesis;
	}
	
	private boolean checkProbability(double prob) {
		return (prob >= 0.0 && prob <= 1.0);
	}
	
	public SearchBotAction getAction() {
		return action;
	}
	
	public double getTruePositive() {
		return Math.min(probActual, probHypothesis);
	}
	
	public double getTrueNegative() {
		return Math.min(1-probActual, 1-probHypothesis);
	}
	
	public double getFalsePositive() {
		return Math.max(0, (1-probActual) - getTrueNegative());
	}
	
	public double getFalseNegative() {
		return Math.max(0, probActual - getTruePositive());
	}
	
	@Override
	public String toString() {
		return action + " with probability " + probHypothesis;
	}
}
