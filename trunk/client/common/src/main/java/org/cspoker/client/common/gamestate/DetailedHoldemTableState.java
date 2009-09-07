/**
 * 
 * Copyright 2008 DAI-Labor, Deutsche Telekom Laboratories
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */
package org.cspoker.client.common.gamestate;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.client.common.playerstate.SeatedPlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.chips.Pots;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

import com.google.common.collect.ImmutableBiMap;

/**
 * @author stephans
 */
public class DetailedHoldemTableState
		extends AbstractGameState {
	
	private final DetailedHoldemTable table;
	private final ImmutableBiMap<SeatId, PlayerId> seatMap;
	
	/**
	 * @param tableConfiguration
	 */
	public DetailedHoldemTableState(DetailedHoldemTable table) {
		this.table = table;
		ImmutableBiMap.Builder<SeatId, PlayerId> seatMapBuilder = new ImmutableBiMap.Builder<SeatId, PlayerId>();
		for (SeatedPlayer player : table.getPlayers()) {
			seatMapBuilder.put(player.getSeatId(), player.getId());
		}
		this.seatMap = seatMapBuilder.build();
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getCommunityCards()
	 */
	@Override
	public EnumSet<Card> getCommunityCards() {
		List<Card> communityCards = table.getCommunityCards();
		if(communityCards.isEmpty()){
			return EnumSet.noneOf(Card.class);
		}
		return EnumSet.copyOf(communityCards);
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getDealer()
	 */
	@Override
	public PlayerId getDealer() {
		return table.getDealer().getId();
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getLargestBet()
	 */
	@Override
	public int getLargestBet() {
		int result = 0;
		for (SeatedPlayer player : table.getPlayers()) {
			result = Math.max(result, player.getBetChipsValue());
		}
		return result;
	}
	
	/**
	 * TODO Horribly complicated hack
	 * 
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getLastBettor()
	 */
	@Override
	public PlayerId getLastBettor() {
		int maxBet = getLargestBet();
		int dealerSeatId = getPlayer(getDealer()).getSeatId().getId();
		List<SeatedPlayer> candidates = new ArrayList<SeatedPlayer>();
		for (SeatedPlayer player : table.getPlayers()) {
			if (player.getBetChipsValue() == maxBet) {
				candidates.add(player);
			}
		}
		for (int i = 0; i < table.getTableConfiguration().getMaxNbPlayers(); i++) {
			for (SeatedPlayer player : candidates) {
				if (player.getSeatId().getId() == dealerSeatId) {
					return player.getId();
				}
			}
			dealerSeatId--;
			if (dealerSeatId < 0) {
				dealerSeatId += table.getTableConfiguration().getMaxNbPlayers();
			}
		}
		
		return null;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getLastEvent()
	 */
	@Override
	public HoldemTableTreeEvent getLastEvent() {
		throw new UnsupportedOperationException("Implement this");
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getMinNextRaise()
	 */
	@Override
	public int getMinNextRaise() {
		throw new UnsupportedOperationException("Implement this");
	}
	
	/**
	 * @return Unknown after initialization, return 0
	 * @see org.cspoker.client.common.gamestate.GameState#getNbRaises()
	 */
	@Override
	public int getNbRaises() {
		return 0;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getNextToAct()
	 */
	@Override
	public PlayerId getNextToAct() {
		throw new UnsupportedOperationException("Implement this");
	}
	
	/**
	 * @param playerId
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getPlayer(org.cspoker.common.elements.player.PlayerId)
	 */
	@Override
	public PlayerState getPlayer(PlayerId playerId) {
		SeatedPlayer selected = null;
		for (SeatedPlayer seated : table.getPlayers()) {
			if (seated.getId().equals(playerId)) {
				selected = seated;
			}
		}
		if (selected == null || !selected.isSittingIn()) {
			return null;
		}
		return new SeatedPlayerState(selected);
	}
	
	@Override
	public ImmutableBiMap<SeatId, PlayerId> getSeatMap() {
		return seatMap;
	}
	
	/**
	 * @return Point to yourself ...
	 * @see org.cspoker.client.common.gamestate.GameState#getPreviousGameState()
	 */
	@Override
	public GameState getPreviousGameState() {
		return this;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getPreviousRoundsPotSize()
	 */
	@Override
	public int getPreviousRoundsPotSize() {
		Pots pots = table.getPots();
		if(pots==null){
			//TODO check why pots can be null?
			return 0;
		}
		return pots.getTotalValue();
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getRound()
	 */
	@Override
	public Round getRound() {
		return table.getRound();
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getRoundPotSize()
	 */
	@Override
	public int getRoundPotSize() {
		int result = 0;
		for (SeatedPlayer player : table.getPlayers()) {
			result += player.getBetChipsValue();
		}
		return result;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getTableConfiguration()
	 */
	@Override
	public TableConfiguration getTableConfiguration() {
		return table.getTableConfiguration();
	}
	
	@Override
	public void acceptHistoryVisitor(GameStateVisitor visitor, GameState start) {
		if(start!=this){
			acceptVisitor(visitor);
		}
	}

	@Override
	public void acceptVisitor(GameStateVisitor visitor) {
		visitor.visitInitialGameState(this);
	}
	
	@Override
	public String toString() {
		return table.toString();
	}
}
