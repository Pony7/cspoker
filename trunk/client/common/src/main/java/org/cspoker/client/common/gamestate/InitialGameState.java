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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

/**
 * @author stephans
 */
public class InitialGameState
		implements GameState {
	
	private final TableConfiguration tableConfiguration;
	
	/**
	 * @param tableConfiguration
	 */
	public InitialGameState(TableConfiguration tableConfiguration) {
		this.tableConfiguration = tableConfiguration;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getAllSeatedPlayerIds()
	 */
	@Override
	public Set<PlayerId> getAllSeatedPlayerIds() {
		return Collections.emptySet();
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getAllSeatedPlayers()
	 */
	@Override
	public Set<PlayerState> getAllSeatedPlayers() {
		return Collections.emptySet();
	}
	
	/**
	 * @param playerId
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getCallValue(org.cspoker.common.elements.player.PlayerId)
	 */
	@Override
	public int getCallValue(PlayerId playerId) {
		return 0;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getCommunityCards()
	 */
	@Override
	public EnumSet<Card> getCommunityCards() {
		return EnumSet.noneOf(Card.class);
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getDealer()
	 */
	@Override
	public PlayerId getDealer() {
		return null;
	}
	
	/**
	 * @param playerId
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getDeficit(org.cspoker.common.elements.player.PlayerId)
	 */
	@Override
	public int getDeficit(PlayerId playerId) {
		return 0;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getGamePotSize()
	 */
	@Override
	public int getGamePotSize() {
		return 0;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getLargestBet()
	 */
	@Override
	public int getLargestBet() {
		return 0;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getLastBettor()
	 */
	@Override
	public PlayerId getLastBettor() {
		return null;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getLastEvent()
	 */
	@Override
	public HoldemTableTreeEvent getLastEvent() {
		return null;
	}
	
	/**
	 * @param playerId
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getLowerRaiseBound(org.cspoker.common.elements.player.PlayerId)
	 */
	@Override
	public int getLowerRaiseBound(PlayerId playerId) {
		return 0;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getMinNextRaise()
	 */
	@Override
	public int getMinNextRaise() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * @return
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
		return null;
	}
	
	/**
	 * @param playerId
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getPlayer(org.cspoker.common.elements.player.PlayerId)
	 */
	@Override
	public PlayerState getPlayer(PlayerId playerId) {
		return null;
	}
	
	/**
	 * @param seatId
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getPlayerId(org.cspoker.common.elements.table.SeatId)
	 */
	@Override
	public PlayerId getPlayerId(SeatId seatId) {
		return null;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getPreviousGameState()
	 */
	@Override
	public GameState getPreviousGameState() {
		return null;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getPreviousRoundsPotSize()
	 */
	@Override
	public int getPreviousRoundsPotSize() {
		return 0;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getRound()
	 */
	@Override
	public Round getRound() {
		return Round.WAITING;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getRoundPotSize()
	 */
	@Override
	public int getRoundPotSize() {
		return 0;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getTableConfiguration()
	 */
	@Override
	public TableConfiguration getTableConfiguration() {
		return tableConfiguration;
	}
	
	/**
	 * @param playerId
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getUpperRaiseBound(org.cspoker.common.elements.player.PlayerId)
	 */
	@Override
	public int getUpperRaiseBound(PlayerId playerId) {
		return 0;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#hasBet()
	 */
	@Override
	public boolean hasBet() {
		return false;
	}
	
	/**
	 * @param playerId
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#isAllowedToRaise(org.cspoker.common.elements.player.PlayerId)
	 */
	@Override
	public boolean isAllowedToRaise(PlayerId playerId) {
		return false;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#previewNextToAct()
	 */
	@Override
	public PlayerState previewNextToAct() {
		return null;
	}
	
}
