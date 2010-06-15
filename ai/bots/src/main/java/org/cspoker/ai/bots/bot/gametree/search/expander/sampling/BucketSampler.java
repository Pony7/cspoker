package org.cspoker.ai.bots.bot.gametree.search.expander.sampling;

import java.util.ArrayList;
import java.util.List;

import org.cspoker.ai.bots.bot.gametree.action.CallAction;
import org.cspoker.ai.bots.bot.gametree.action.CheckAction;
import org.cspoker.ai.bots.bot.gametree.action.FoldAction;
import org.cspoker.ai.bots.bot.gametree.action.ProbabilityAction;
import org.cspoker.ai.opponentmodels.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class BucketSampler extends Sampler {

	private double threshold;

	public BucketSampler(double threshold) {
		this.threshold = threshold;
	}
	
	class Bucket {
		private double start;
		private double end;
		private double prob;
		private double totalProb;
		private double averageProb;
		
		public Bucket(double start, double prob, double totalProb) {
			this.start = start;
			this.setProb(prob);
			this.setTotalProb(totalProb);
		}
		public double getStart() {
			return start;
		}
		public double getEnd() {
			return end;
		}
		public void setEnd(double end) {
			this.end = end;
		}
		public double getProb() {
			return prob;
		}
		private void setProb(double prob) {
			this.prob = prob;
		}
		public double getTotalProb() {
			return totalProb;
		}
		public void setTotalProb(double totalProb) {
			this.totalProb = totalProb;
		}
		public String toString() {
			return "Start: " + start + ",  \t end: " + end + ",\t prob: "  + 
			prob + ", averageProb: " + averageProb + ", totalProb: " + totalProb;
		}
		public void setAverageProb(int length) {
			if (end==start)
				this.averageProb = totalProb;
			else
				this.averageProb = totalProb/((end-start)*(length-1));
		}
		public double getAverageProb() {
			return averageProb;
		}
	}
	
	protected List<Bucket> getBuckets() {
		RelativeBetDistribution distr = new RelativeBetDistribution();
		List<Bucket> buckets = new ArrayList<Bucket>();
		// start of range bucket, prob of start, totalProb
		Bucket bucket = new Bucket(0, distr.pdf(0), 0);
		for (int i = 0; i < distr.length(); i++) {
			double relBet = (double) i / (distr.length() - 1);
			if (Math.abs(bucket.getProb() - distr.pdf(relBet)) > threshold) {
				bucket.setEnd(relBet);
				bucket.setAverageProb(distr.length());
				buckets.add(bucket);
				bucket = new Bucket(relBet, distr.pdf(relBet), 0);
			}
			bucket.setTotalProb(bucket.getTotalProb() + distr.pdf(relBet));
		}
		bucket.setEnd(1);
		bucket.setAverageProb(distr.length());
		buckets.add(bucket);
		return buckets;		
	}
	
	private static double[] samples = {};
	private static double[] samplesProb = {};
	
	public double[] getRelBetSizeSamples() {
		if (samples.length>0)
			return samples;
		
		List<Bucket> buckets = getBuckets();
		samples = new double[buckets.size()];
		samplesProb = new double[buckets.size()];
		for (int i = 0; i< samples.length; i++) {
			samples[i] = (buckets.get(i).end + buckets.get(i).start) / 2.0;
			samplesProb[i] = buckets.get(i).totalProb;
		}
		return samples;
	}
	
	public double[] getRelPBetSizeSamples() {
		if (samplesProb.length>0)
			return samplesProb;
		
		getRelBetSizeSamples();
		return samplesProb;
	}
	
	public static void main(String[] args) {
		BucketSampler s = new BucketSampler(0.01);	
		List<Bucket> list = s.getBuckets();
		double totalProb = 0;
		for (int i = 0; i < list.size(); i++) {
			totalProb += list.get(i).getTotalProb();
			System.out.println(" - " + list.get(i));
		}
		System.out.println("\n => TotalProb: " + totalProb);
	}

	@Override
	public ImmutableList<ProbabilityAction> getProbabilityActions(
			GameState gameState, OpponentModel model, PlayerId actor,
			PlayerId bot) {
		List<ProbabilityAction> actions = Lists.newArrayListWithExpectedSize(2+getRelBetSizeSamples().length);
		if (gameState.getDeficit(actor)>0) {
			// call, raise or fold
			Triple<Double, Double, Double> probabilities = 
				model.getFoldCallRaiseProbabilities(gameState, actor);

			double foldProbability = probabilities.getLeft();
			actions.add(new ProbabilityAction(new FoldAction(gameState, actor), foldProbability));

			double callProbability = probabilities.getMiddle();
			actions.add(new ProbabilityAction(new CallAction(gameState, actor), callProbability));

			if (!gameState.getPlayer(bot).isAllIn()
					&& gameState.isAllowedToRaise(actor)) {
				double raiseProbability = probabilities.getRight();
				addRaiseProbalities(gameState, actor, actions, raiseProbability,
						true, getRelBetSizeSamples(), getRelPBetSizeSamples());			
			}
		} else {
			// check or bet
			Pair<Double, Double> probabilities = model.getCheckBetProbabilities(gameState, actor);
			
			double checkProbability = probabilities.getLeft();
			actions.add(new ProbabilityAction(new CheckAction(gameState, actor), checkProbability));

			if (!gameState.getPlayer(bot).isAllIn()
					&& gameState.isAllowedToRaise(actor)) {
				double betProbability = probabilities.getRight();
				addRaiseProbalities(gameState, actor, actions, betProbability, 
						false, getRelBetSizeSamples(), getRelPBetSizeSamples());
			}
		}
		ImmutableList.Builder<ProbabilityAction> normalizedActionsBuilder = ImmutableList.builder();
		for (ProbabilityAction action : actions) {
			normalizedActionsBuilder.add(new ProbabilityAction(action
					.getActionWrapper(), action.getProbability()));
		}	
		return normalizedActionsBuilder.build();
	}
}
