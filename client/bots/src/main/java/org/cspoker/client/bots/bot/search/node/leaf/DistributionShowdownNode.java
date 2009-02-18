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
package org.cspoker.client.bots.bot.search.node.leaf;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.SearchConfiguration;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.handeval.spears2p2.StateTableEvaluator;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class DistributionShowdownNode extends AbstractShowdownNode{

	private final static Logger logger = Logger.getLogger(DistributionShowdownNode.class);
	
	private final static int[] handRanks;
	static{
		StateTableEvaluator.initialize();
		handRanks = StateTableEvaluator.handRanks;
	}
	
	public static final int MaxNbSamples = 400;
	
	private int tokens;
	
	DistributionShowdownNode(PlayerId botId, GameState gameState, int tokens) {
		super(botId, gameState);
		this.tokens = tokens;
	}

	public double getExpectedPotPercentage() {
		PlayerState botState = gameState.getPlayer(botId);
		Set<PlayerState> opponents = Sets.filter(gameState.getAllSeatedPlayers(),new Predicate<PlayerState>(){
			@Override
			public boolean apply(PlayerState state) {
				return state.isInForPot() && !state.getPlayerId().equals(botId);
			}
		});
		EnumSet<Card> botCards = botState.getCards();
		EnumSet<Card> fixedCommunityCards = gameState.getCommunityCards();
		EnumSet<Card> usedFixedCards = EnumSet.copyOf(fixedCommunityCards);
		usedFixedCards.addAll(botCards);
		
		int fixedRank = 53;
		for(Card fixedCommunityCard:fixedCommunityCards){
			fixedRank = handRanks[fixedCommunityCard.ordinal()+1+fixedRank];
		}
		
		int nbFixedCards = fixedCommunityCards.size();
		int nbMissingCommunityCards = 5-nbFixedCards;
		int nbSamples = Math.min(MaxNbSamples,Math.max(25,tokens*5));
		int nbCommunitySamples, nbOpponentSamples;
		if(nbMissingCommunityCards==0){
			nbCommunitySamples=1;
			nbOpponentSamples=nbSamples;
		}else{
			double root = Math.sqrt(nbSamples);
			nbCommunitySamples = (int) (root*nbMissingCommunityCards/2);
			nbOpponentSamples = (int) (root*2/nbMissingCommunityCards);
		}

		double totalProb = 0;
		double totalProfit = 0;
		for(int i=0;i<nbCommunitySamples;i++){
			int communitySampleRank = fixedRank;
			EnumSet<Card> usedCommunityCards = EnumSet.copyOf(usedFixedCards);
			for(int j=0;j<nbMissingCommunityCards;j++){
				Card communityCard;
				do{
					communityCard = getRandomCard();
				}while(usedCommunityCards.contains(communityCard));
				usedCommunityCards.add(communityCard);
				communitySampleRank = handRanks[communityCard.ordinal()+1+communitySampleRank];
			}
			
			for(int j=0;j<nbOpponentSamples;j++){
				EnumSet<Card> usedOpponentCards = EnumSet.copyOf(usedCommunityCards);
				int opponentRank = communitySampleRank;
				int botRank = communitySampleRank;
				for(Card botCard:botCards){
					botRank = handRanks[botCard.ordinal()+1+botRank];
				}
				botRank = extractFinalRank(botRank);
				
				boolean botWins = true;
				double logProb = 0;
				for(PlayerState opponent:opponents){
					Card opponentFirst;
					do{
						opponentFirst = getRandomCard();
					}while(usedOpponentCards.contains(opponentFirst));
					usedOpponentCards.add(opponentFirst);
					opponentRank = handRanks[opponentFirst.ordinal()+1+opponentRank];
					
					Card opponentSecond;
					do{
						opponentSecond = getRandomCard();
					}while(usedOpponentCards.contains(opponentSecond));
					usedOpponentCards.add(opponentSecond);
					opponentRank = extractFinalRank(handRanks[opponentSecond.ordinal()+1+opponentRank]);
					
					if(opponentRank>=botRank){
						botWins = false;
					}//TODO fix for split pot
					float opponentRankProb = getRelativeProbability(opponentRank);
					logProb += Math.log(opponentRankProb);
				}
				double prob = Math.exp(logProb);
				if(botWins){
					totalProfit += prob;
				}
				totalProb += prob;
			}
		}
		return totalProfit/totalProb;
	}

	private float getRelativeProbability(int rank) {
		return ShowdownRankPredictor.getRelativeProbability(rank);
	}

	@Override
	public String toString() {
		return "Distribution Showdown Node";
	}

	public static class Factory implements AbstractShowdownNode.Factory{
		
		@Override
		public DistributionShowdownNode create(PlayerId botId, GameState gameState, int tokens
				, SearchConfiguration config, int searchId) {
			return new DistributionShowdownNode(botId, gameState, tokens);
		}
		
		@Override
		public String toString() {
			return "Distribution Showdown Node factory";
		}
	}
	
	private static final int[] offsets = new int[] {0, 1277, 4137, 4995, 5853, 5863, 7140, 7296, 7452};

	private int extractFinalRank(int rank){
		int type = (rank >>> 12) - 1;
		rank = rank & 0xFFF;
		return offsets[type] + rank - 1;
	}

	private static Random random = new Random();
	private static Card[] cards = Card.values();

	protected Card getRandomCard(){
		return cards[random.nextInt(cards.length)];
	}

}
