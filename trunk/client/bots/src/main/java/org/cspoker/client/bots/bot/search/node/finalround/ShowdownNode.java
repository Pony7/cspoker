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
package org.cspoker.client.bots.bot.search.node.finalround;

import java.util.EnumSet;
import java.util.Set;

import org.cspoker.client.bots.bot.search.node.GameTreeNode;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Deck;
import org.cspoker.common.elements.hand.Hand;
import org.cspoker.common.elements.player.PlayerId;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.common.collect.Multiset.Entry;

public class ShowdownNode implements GameTreeNode{

	private final PlayerId botId;
	private final GameState gameState;
	private final Multiset<Integer> EVs = new HashMultiset<Integer>();
	private final int depth;
	
	public ShowdownNode(PlayerId botId, GameState gameState, int depth) {
		this.botId = botId;
		this.gameState = gameState;
		this.depth = depth;
	}

	@Override
	public void expand() {
		int nbSamples = 20;
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

		int loseEV = botState.getStack();
		int winEV = loseEV+gameState.getGamePotSize();
		
		EnumSet<Card> allBotCards = EnumSet.copyOf(botCards);
		allBotCards.addAll(communityCards);
		int botRank =  new Hand(allBotCards).getBestFiveRank();

		for(int i=0;i<nbSamples;i++){
			if(winsSample(activeOpponents, usedCards, communityCards,
					botRank)){
				EVs.add(winEV);
			}else{
				EVs.add(0);
			}
		}
	}

	private boolean winsSample(Set<PlayerState> activeOpponents,
			EnumSet<Card> usedCards, EnumSet<Card> communityCards, int botRank) {
		Deck deck = Deck.createWeaklyRandomDeck();
		for(PlayerState opponent:activeOpponents){
			EnumSet<Card> opponentCards = sampleOpponentCards(opponent,deck,usedCards);
			int opponentRank = new Hand(Sets.union(communityCards, opponentCards)).getBestFiveRank();
			//TODO handle split pot
			if(opponentRank<botRank){
				return false;
			}
		}
		return true;
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
	public double getEV() {
		int EV = 0;
		int size = 0;
		for(Entry<Integer> entry:EVs.entrySet()){
			EV+=entry.getCount()*entry.getElement();
			size+=entry.getCount();
		}
		return ((double)EV)/size;
	}

}
