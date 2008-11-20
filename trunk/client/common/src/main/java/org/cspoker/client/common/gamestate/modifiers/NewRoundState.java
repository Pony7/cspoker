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
package org.cspoker.client.common.gamestate.modifiers;

import java.util.EnumSet;
import java.util.Set;

import org.cspoker.client.common.gamestate.AbstractGameState;
import org.cspoker.client.common.gamestate.AbstractPlayerState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class NewRoundState extends AbstractGameState {

	private NewRoundEvent event;

	private final ImmutableMap<SeatId, PlayerId> seatPlayer;
	private final ImmutableMap<PlayerId,PlayerState> playerStates;
	
	private final PlayerId dealer;
	private final Set<Card> communityCards;

	private final TableConfiguration tableConfiguration;
	
	public NewRoundState(GameState gameState, NewRoundEvent event) {
		this.event = event;

		this.tableConfiguration = gameState.getTableConfiguration();
		this.dealer = gameState.getDealer();
		this.communityCards = gameState.getCommunityCards();

		Builder<PlayerId, PlayerState> playerStateBuilder = ImmutableMap.builder();
		Builder<SeatId, PlayerId> seatPlayerBuilder = ImmutableMap.builder();
		
		Set<PlayerId> playersIds = gameState.getAllSeatedPlayerIds();
		
		for(final PlayerId playerId:playersIds){
			PlayerState oldPlayerState = gameState.getPlayer(playerId);
			
			final EnumSet<Card> cards = oldPlayerState.getCards();
			final int stack = oldPlayerState.getStack();
			final boolean sitsIn = oldPlayerState.sitsIn();
			final boolean hasFolded = oldPlayerState.hasFolded();
			final SeatId seat = oldPlayerState.getSeatId();
			
			PlayerState playerState = new AbstractPlayerState(){

				public int getBet() {
					return 0;
				}

				public EnumSet<Card> getCards() {
					return EnumSet.copyOf(cards);
				}

				public int getStack() {
					return stack;
				}

				public boolean hasFolded() {
					return hasFolded;
				}

				public boolean sitsIn() {
					return sitsIn;
				}

				public PlayerId getPlayerId() {
					return playerId;
				}

				public SeatId getSeatId() {
					return seat;
				}
				
			};
			playerStateBuilder.put(playerId, playerState);
			seatPlayerBuilder.put(oldPlayerState.getSeatId(), playerId);
		}
		seatPlayer = seatPlayerBuilder.build();
		playerStates = playerStateBuilder.build();
	}
	
	public TableConfiguration getTableConfiguration() {
		return tableConfiguration;
	}

	public Set<PlayerId> getAllSeatedPlayerIds() {
		return playerStates.keySet();
	}
	
	public EnumSet<Card> getCommunityCards() {
		return EnumSet.copyOf(communityCards);
	}

	public PlayerId getDealer() {
		return dealer;
	}

	public int getLargestBet() {
		return 0;
	}

	public PlayerId getLastBettor() {
		return null;
	}

	public HoldemTableTreeEvent getLastEvent() {
		return event;
	}

	public int getMinNextRaise() {
		return tableConfiguration.getSmallBet();
	}

	public PlayerId getNextToAct() {
		return null;
	}

	public PlayerState getPlayer(PlayerId playerId) {
		return playerStates.get(playerId);
	}

	public PlayerId getPlayerId(SeatId seatId) {
		return seatPlayer.get(seatId);
	}

	public GameState getPreviousGameState() {
		return null;
	}

	public int getPreviousRoundsPotSize() {
		return event.getPots().getTotalValue();
	}

	public Round getRound() {
		return event.getRound();
	}

	public int getRoundPotSize() {
		return 0;
	}
	
	public int getNbSeats() {
		return tableConfiguration.getMaxNbPlayers();
	}
}
