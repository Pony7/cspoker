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
package org.cspoker.ai.bots.bot.gametree.rollout;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.cspoker.ai.opponentmodels.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public class BucketRollOut extends RollOutStrategy {

	private final static Logger logger = Logger.getLogger(BucketRollOut.class);

	private final OpponentModel model;

	public BucketRollOut(GameState gameState, PlayerId botId, OpponentModel model) {
		super(gameState, botId);
		this.model = model;
	}

	public double doRollOut(int nbCommunitySamples) {
		boolean traceEnabled = logger.isTraceEnabled();
		double totalEV = 0;
		model.assumeTemporarily(gameState);
		for (int i = 0; i < nbCommunitySamples; i++) {
			int communitySampleRank = fixedRank;
			EnumSet<Card> usedCommunityAndBotCards = EnumSet.copyOf(usedFixedCommunityAndBotCards);
			EnumSet<Card> usedCommunityCards = EnumSet.copyOf(usedFixedCommunityCards);
			for (int j = 0; j < nbMissingCommunityCards; j++) {
				Card communityCard = drawNewCard(usedCommunityAndBotCards);
				if (traceEnabled) {
					logger.trace("Evaluating sampled community card "
							+ communityCard);
				}
				usedCommunityCards.add(communityCard);
				communitySampleRank = updateIntermediateRank(
						communitySampleRank, communityCard);
			}
			if (traceEnabled) {
				logger.trace("Evaluating bot cards " + botCard1 + " "
						+ botCard2);
			}
			int botRank = getFinalRank(communitySampleRank, botCard1, botCard2);

			int minSampleRank = Integer.MAX_VALUE;
			int maxSampleRank = Integer.MIN_VALUE;
			int sum = 0;
			Multiset<Integer> ranks = new TreeMultiset<Integer>();
			Multiset<Integer> deadRanks = new TreeMultiset<Integer>();
			int n = 100;
			for (int j = 0; j < n; j++) {
				EnumSet<Card> handCards = EnumSet.copyOf(usedCommunityCards);
				Card sampleCard1 = drawNewCard(handCards);
				Card sampleCard2 = drawNewCard(handCards);
				int sampleRank = getFinalRank(communitySampleRank, sampleCard1, sampleCard2);
				ranks.add(sampleRank);
				if(botCard1.equals(sampleCard1) || botCard1.equals(sampleCard2) 
						|| botCard2.equals(sampleCard1) || botCard2.equals(sampleCard2)){
					deadRanks.add(sampleRank);
				}	
				if(sampleRank<minSampleRank){
					minSampleRank = sampleRank;
				}
				if(sampleRank>maxSampleRank){
					maxSampleRank = sampleRank;
				}
				sum += sampleRank;
			}
			double mean = ((double)sum)/n;
			double var = calcVariance(ranks, mean);
			int averageSampleRank = (int) Math.round(mean);
			int sigmaSampleRank = (int) Math.round(Math.sqrt(var));

			WinDistribution[] winProbs = calcWinDistributions(botRank, ranks, deadRanks);
			double[] deadCardWeights = calcDeadCardWeights(ranks, deadRanks);

			TreeMap<PlayerState, WinDistribution> winDistributions = calcOpponentWinDistributionMap(
					winProbs, deadCardWeights, minSampleRank, maxSampleRank,
					averageSampleRank, sigmaSampleRank);
			
			int maxDistributed = 0;
			int botInvestment = botState.getTotalInvestment();
			double sampleEV = 0;
			for (Iterator<PlayerState> iter = winDistributions.keySet().iterator(); iter.hasNext();) {
				PlayerState opponent = iter.next();
				int toDistribute = Math.min(botInvestment, opponent.getTotalInvestment())-maxDistributed;
				if(toDistribute>0){
					double pWin = 1;
					double pNotLose = 1;
					for (WinDistribution distribution : winDistributions.values()) {
						//you win when you win from every opponent
						pWin *= distribution.pWin;
						//you don't lose when you don't lose from every opponent
						pNotLose *= distribution.pWin+distribution.pDraw;
					}
					sampleEV += toDistribute*pWin;
					//you draw when you don't lose but don't win everything either;
					double pDraw = pNotLose - pWin;
					// assume worst case, with winDistributions.size()+1 drawers
					//TODO do this better, use rollout or statistics!
					sampleEV += pDraw * toDistribute / (winDistributions.size()+1.0);
					maxDistributed += toDistribute;
				}
				iter.remove();
			}
			//get back uncalled investment
			sampleEV += botInvestment-maxDistributed;
			totalEV += sampleEV;
		}
		model.forgetLastAssumption();
		return totalEV/nbCommunitySamples;
	}

	private TreeMap<PlayerState, WinDistribution> calcOpponentWinDistributionMap(
			WinDistribution[] winProbs, double[] deadCardWeights,
			int minSampleRank, int maxSampleRank, int averageSampleRank,
			int sigmaSampleRank) {
		TreeMap<PlayerState,WinDistribution> winDistributions= new TreeMap<PlayerState,WinDistribution>(playerComparatorByInvestment);
		for (PlayerState opponentThatCanWin : activeOpponents) {
			double[] bucketProbs = model.getShowdownProbabilities(gameState, opponentThatCanWin.getPlayerId(), minSampleRank, maxSampleRank, averageSampleRank, sigmaSampleRank, deadCardWeights);
			winDistributions.put(opponentThatCanWin, calcOpponentWinDistr(winProbs, bucketProbs));
		}
		return winDistributions;
	}

	private WinDistribution calcOpponentWinDistr(WinDistribution[] winProbs,
			double[] bucketProbs) {
		WinDistribution winDistr;
		double pWin=0, pDraw=0, pLose=0;
		for (int j = 0; j < bucketProbs.length; j++) {
			pWin += winProbs[j].pWin*bucketProbs[j];
			pDraw += winProbs[j].pDraw*bucketProbs[j];
			pLose += winProbs[j].pLose*bucketProbs[j];
		}
		winDistr = new WinDistribution(pWin,pDraw,pLose);
		return winDistr;
	}

	private WinDistribution[] calcWinDistributions(int botRank,
			Multiset<Integer> ranks, Multiset<Integer> deadRanks) {
		Iterator<Integer> iter = ranks.iterator();
		WinDistribution[] winProbs = new WinDistribution[10];
		for(int bucket=0;bucket<10;bucket++){
			double winWeight = 0;
			double drawWeight = 0;
			double loseWeight = 0;
			for (int j = 0; j < 10; j++) {
				int rank = iter.next();
				double weight = 1-deadRanks.count(rank)/ranks.count(rank);
				if(rank<botRank){
						winWeight+=weight;
					}else if(rank>botRank){
						loseWeight+=weight;
					}else{
						drawWeight+=weight;
					}
			}
			double nbSamples = winWeight+drawWeight+loseWeight;
			if(nbSamples==0) nbSamples = 1;
			winProbs[bucket] = new WinDistribution(winWeight/nbSamples, drawWeight/nbSamples, loseWeight/nbSamples);
		}
		return winProbs;
	}
	
	public static class WinDistribution{

		//from the perspective of the bot
		public final double pWin, pDraw, pLose;
		
		public WinDistribution(double pWin, double pDraw, double pLose) {
			this.pWin = pWin;
			this.pDraw = pDraw;
			this.pLose = pLose;
		}
		
		@Override
		public String toString() {
			return pWin+"/"+pDraw+"/"+pLose;
		}
		
	}

	private double[] calcDeadCardWeights(Multiset<Integer> ranks,
			Multiset<Integer> deadRanks) {
		Iterator<Integer> iter = ranks.iterator();
		double[] deadCardWeights = new double[10];
		for(int bucket=0;bucket<10;bucket++){
			double nbDead = 0;
			for (int j = 0; j < 10; j++) {
				int rank = iter.next();
				double count = ranks.count(rank);
				double deadCount = deadRanks.count(rank);
				nbDead += deadCount/count;
			}
			deadCardWeights[bucket] = ((10.0-nbDead)/10.0);
		}
		return deadCardWeights;
	}

	private double calcVariance(Multiset<Integer> ranks, double mean) {
		double var = 0;
		for (Multiset.Entry<Integer> entry : ranks.entrySet()) {
			double diff = mean - entry.getElement();
			var += diff * diff * entry.getCount();
		}
		var /= (ranks.size()-1);
		return var;
	}

}
