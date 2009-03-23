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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.cspoker.client.common.gamestate.AbstractGameState;
import org.cspoker.client.common.gamestate.AbstractPlayerState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

public class NewDealState
extends AbstractGameState {

	private final TableConfiguration tableConfiguration;
	private final NewDealEvent event;

	//TODO make weak reference? clean up memory?
	private final GameState previousGame;

	private final ImmutableBiMap<SeatId, PlayerId> seatMap;
	private final ImmutableMap<PlayerId, PlayerState> playerStates;

	public NewDealState(TableConfiguration tableConfiguration, NewDealEvent newDealEvent, GameState previousGame) {
		this.previousGame = previousGame;
		this.event = newDealEvent;
		this.tableConfiguration = tableConfiguration;

		ImmutableMap.Builder<PlayerId, PlayerState> playerStateBuilder = ImmutableMap.builder();
		ImmutableBiMap.Builder<SeatId, PlayerId> seatMapBuilder = ImmutableBiMap.builder();

		for (final SeatedPlayer player : newDealEvent.getPlayers()) {
			if(player.isSittingIn()){
				AbstractPlayerState playerState = new AbstractPlayerState() {

					public int getBet() {
						return 0;
					}

					@Override
					public int getTotalInvestment() {
						return 0;
					}

					public EnumSet<Card> getCards() {
						return EnumSet.noneOf(Card.class);
					}

					public int getStack() {
						return player.getStackValue();
					}

					public boolean hasFolded() {
						return false;
					}

					public PlayerId getPlayerId() {
						return player.getId();
					}

					public SeatId getSeatId() {
						return player.getSeatId();
					}

					@Override
					public boolean isSmallBlind() {
						//starting from SmallBlindEvent
						return false;
					}

					@Override
					public boolean isBigBlind() {
						//starting from BigBlindEvent
						return false;
					}

					@Override
					public boolean hasChecked() {
						return false;
					}

					@Override
					public List<Integer> getBetProgression() {
						return new ArrayList<Integer>();
					}

				};
				seatMapBuilder.put(player.getSeatId(), player.getId());
				playerStateBuilder.put(player.getId(), playerState);
			}
		}
		seatMap = seatMapBuilder.build();
		playerStates = playerStateBuilder.build();
	}

	public TableConfiguration getTableConfiguration() {
		return tableConfiguration;
	}

	public Set<PlayerId> getAllSeatedPlayerIds() {
		return playerStates.keySet();
	}

	public EnumSet<Card> getCommunityCards() {
		return EnumSet.noneOf(Card.class);
	}

	public PlayerId getDealer() {
		return event.getDealer();
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

	@Override
	public ImmutableBiMap<SeatId, PlayerId> getSeatMap() {
		return seatMap;
	}

	public GameState getPreviousGameState() {
		return previousGame;
	}

	public int getPreviousRoundsPotSize() {
		return 0;
	}

	public Round getRound() {
		return Round.PREFLOP;
	}

	public int getRoundPotSize() {
		return 0;
	}

	@Override
	public int getNbRaises() {
		return 0;
	}

	@Override
	public int getNbPlayers() {
		//TODO fix with sitout?
		return seatMap.size();
	}

	@Override
	public void acceptVisitor(GameStateVisitor visitor) {
		visitor.visitNewDealState(this);
	}

	@Override
	public String toString() {
		return getLastEvent().toString();
	}
}
