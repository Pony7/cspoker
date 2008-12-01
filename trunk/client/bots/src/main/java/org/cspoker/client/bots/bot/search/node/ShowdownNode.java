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
package org.cspoker.client.bots.bot.search.node;

import java.util.EnumSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Deck;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.handeval.stevebrecher.HandEval;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Multiset.Entry;

public class ShowdownNode{

	private final static Logger logger = Logger.getLogger(ShowdownNode.class);

	private final PlayerId botId;
	private final GameState gameState;
	
	public ShowdownNode(PlayerId botId, GameState gameState) {
		this.botId = botId;
		this.gameState = gameState;
	}

	public double getEV() {
		int EV = 0;
		int size = 0;
		HashMultiset<Integer> EVs = simulateOutcomes();
		for(Entry<Integer> entry:EVs.entrySet()){
			EV+=entry.getCount()*entry.getElement();
			size+=entry.getCount();
		}
		return gameState.getPlayer(botId).getStack()+Double.valueOf(EV)/size;
	}

	private HashMultiset<Integer> simulateOutcomes() {
		int nbSamples = 15;
		PlayerState botState = gameState.getPlayer(botId);
		Set<PlayerState> activeOpponents = Sets.filter(gameState.getAllSeatedPlayers(),new Predicate<PlayerState>(){
			@Override
			public boolean apply(PlayerState state) {
				return state.sitsIn() && !state.getPlayerId().equals(botId);
			}
		});
		
		EnumSet<Card> botCards = botState.getCards();
		
		EnumSet<Card> usedCards = EnumSet.noneOf(Card.class);
		usedCards.addAll(botCards);
		
		EnumSet<Card> communityCards = sampleCommunityCards(usedCards,gameState.getCommunityCards());
		usedCards.addAll(communityCards);
		
		EnumSet<Card> allBotCards = EnumSet.copyOf(botCards);
		allBotCards.addAll(communityCards);
		int botRank =  HandEval.hand7Eval(HandEval.encode(allBotCards));

		int gamePotSize = gameState.getGamePotSize();
		HashMultiset<Integer> EVs = new HashMultiset<Integer>();
		for(int i=0;i<nbSamples;i++){
			EVs.add(winsSample(activeOpponents, usedCards, communityCards,
					botRank,gamePotSize));
		}
		return EVs;
	}

	private int winsSample(Set<PlayerState> activeOpponents,
			EnumSet<Card> usedCards, EnumSet<Card> communityCards, int botRank,int gamePotSize) {
		Deck deck = Deck.createWeaklyRandomDeck();
		int nbWinners = 1;
		for(PlayerState opponent:activeOpponents){
			EnumSet<Card> opponentCards = sampleOpponentCards(opponent,deck,usedCards);
			opponentCards.addAll(communityCards);
			int opponentRank = HandEval.hand7Eval(HandEval.encode(opponentCards));
			if(opponentRank>botRank){
				return 0;
			}else if(opponentRank==botRank){
				++nbWinners;
			}
		}
		return gamePotSize/nbWinners;
	}

	private EnumSet<Card> sampleCommunityCards(EnumSet<Card> usedCards, EnumSet<Card> dealtCommunityCards){
		int nbDealt = dealtCommunityCards.size();
		if(nbDealt==5){
			return dealtCommunityCards;
		}
		Deck deck = Deck.createWeaklyRandomDeck();
		Card[] cards = new Card[5-nbDealt];
		for(int i=0;i<cards.length;i++){
			do{
				cards[i] = deck.drawCard();
			}while(usedCards.contains(cards[i]));
		}
		EnumSet<Card> result = EnumSet.noneOf(Card.class);
		result.addAll(dealtCommunityCards);
		result.addAll(ImmutableSet.of(cards));
		return result;
	}

	private EnumSet<Card> sampleOpponentCards(PlayerState player, Deck deck, EnumSet<Card> usedCards){
		Card one=null, two=null;
		do{
			one = deck.drawCard();
		}while(usedCards.contains(one));
		do{
			two = deck.drawCard();
		}while(usedCards.contains(two));
		return EnumSet.of(one, two);
	}
	
	@Override
	public String toString() {
		return "Showdown Node";
	}

}
