package org.cspoker.ai.bots.bot.gametree.search.expander.sampling;

import java.util.Arrays;

public class RouletteWheelSampler extends StochasticSampler {

	public RouletteWheelSampler() {
		super();
	}
	
	public RouletteWheelSampler(int nbBetSizeSamples) {
		super(nbBetSizeSamples);
	}

	@Override
	protected double[] getStochasticSamples(int n) {
		RelativeBetDistribution distr = new RelativeBetDistribution();
		double[] samples = new double[n];
		for (int i = 0; i < samples.length; i++) 
			samples[i] = distr.inverseCdf(r.nextDouble());		
		Arrays.sort(samples);
		return samples;
	}
	
	public static void main(String[] args) {
		RouletteWheelSampler s = new RouletteWheelSampler();	
		double[] list = s.getStochasticSamples(8);
		for (int i = 0; i < list.length; i++) {
			System.out.println(" - " + list[i]);
		}
	}
}
