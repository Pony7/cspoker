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
import org.cspoker.common.handeval.stevebrecher.HandEval;
import org.cspoker.common.util.Pair;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class UniformShowdownNode extends AbstractShowdownNode{

	//TODO put in config
	private static final int MaxNbSamples = 400;

	private final static Logger logger = Logger.getLogger(UniformShowdownNode.class);

	private int tokens;

	UniformShowdownNode(PlayerId botId, GameState gameState, int tokens) {
		super(botId, gameState);
		this.tokens = tokens;
	}

	public Pair<Double,Double> getExpectedPotPercentage() {
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

		int totalNbWins = 0;
		for(int i=0;i<nbCommunitySamples;i++){
			EnumSet<Card> fixedAndCommunityCards = EnumSet.copyOf(usedFixedCards);
			EnumSet<Card> communityCards = EnumSet.copyOf(fixedCommunityCards);
			for(int j=0;j<nbMissingCommunityCards;j++){
				Card communityCard;
				do{
					communityCard = getRandomCard();
				}while(fixedAndCommunityCards.contains(communityCard));
				fixedAndCommunityCards.add(communityCard);
				communityCards.add(communityCard);
			}
			for(int j=0;j<nbOpponentSamples;j++){
				EnumSet<Card> fixedAndCommunityAndOpponentCards = EnumSet.copyOf(fixedAndCommunityCards);
				//fixedAndCommunityCards coincidentally contain the bot's hand!
				int botRank = getRank(fixedAndCommunityCards);
				boolean botWins = true;
				for(PlayerState opponent:opponents){
					Card opponentFirst;
					do{
						opponentFirst = getRandomCard();
					}while(fixedAndCommunityAndOpponentCards.contains(opponentFirst));
					fixedAndCommunityAndOpponentCards.add(opponentFirst);
					Card opponentSecond;
					do{
						opponentSecond = getRandomCard();
					}while(fixedAndCommunityAndOpponentCards.contains(opponentSecond));
					fixedAndCommunityAndOpponentCards.add(opponentSecond);
					EnumSet<Card> particularOpponentHand = EnumSet.of(opponentFirst, opponentSecond);
					particularOpponentHand.addAll(communityCards);
					int opponentRank = getRank(particularOpponentHand);
					if(opponentRank>=botRank){
						botWins = false;
					}//TODO fix for split pot
				}
				if(botWins){
					++totalNbWins;
				}
			}
		}
		double p = ((double)totalNbWins)/(nbOpponentSamples*nbCommunitySamples);
		return new Pair<Double, Double>(p,p*(1-p));
	}

	private int getRank(EnumSet<Card> cards) {
		return HandEval.hand7Eval(HandEval.encode(cards));
	}

	@Override
	public String toString() {
		return "Uniform Showdown Node";
	}

	public static class Factory implements AbstractShowdownNode.Factory{
		@Override
		public UniformShowdownNode create(PlayerId botId, GameState gameState, int tokens, SearchConfiguration config, int searchId) {
			return new UniformShowdownNode(botId, gameState, tokens);
		}

		@Override
		public String toString() {
			return "Uniform Showdown Node factory";
		}
	}
	
	private static Random random = new Random();
	private static Card[] cards = Card.values();

	protected Card getRandomCard(){
		return cards[random.nextInt(cards.length)];
	}
}
