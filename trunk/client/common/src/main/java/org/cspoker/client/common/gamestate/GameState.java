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
package org.cspoker.client.common.gamestate;

import java.util.EnumSet;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

@Immutable
/**
 * States of the game.
 * 
 */
public interface GameState {
	
	public TableConfiguration getTableConfiguration();
	
	/**
	 * Returns the PlayerState of the player sitting in the given seat or null
	 * if the seat is empty;
	 */
	public PlayerId getPlayerId(SeatId seatId);
	
	public PlayerState getPlayer(PlayerId playerId);

	public Set<PlayerId> getAllSeatedPlayerIds();
	
	public Set<PlayerState> getAllSeatedPlayers();
	
	public PlayerId getDealer();
	
	public PlayerId getLastBettor();
	
	/**
	 * Returns the ID of the player that is next to act or null if nobody should act.
	 */
	public PlayerId getNextToAct();

	public int getPreviousRoundsPotSize();
	public int getRoundPotSize();	
	
	/**
	 * A derived state property that is the sum of the pot this round and previous rounds.
	 */
	public int getGamePotSize();
	
	public int getLargestBet();
	public int getMinNextRaise();
	
	public Round getRound();
	
	public EnumSet<Card> getCommunityCards();
	
	public GameState getPreviousGameState();
	public HoldemTableTreeEvent getLastEvent();
	
	/**
	 * A derived state property that is the difference between the largest bet and the
	 * current bet of a given player.
	 */
	public int getDeficit(PlayerId playerId);	

	/**
	 * A derived state property that is the minimum of the player deficit and stack.
	 */
	public int getCallValue(PlayerId playerId);

	/**
	 * A derived state property that is the minimum of the minimal raise and stack.
	 */
	public int getLowerRaiseBound(PlayerId playerId);
	
	/**
	 * A derived state property that is the minimum of the minimal raise and stack.
	 */
	public int getUpperRaiseBound(PlayerId playerId);
	
	/**
	 * A derived state property whether the given player has enough money to raise.
	 */
	public boolean isAllowedToRaise(PlayerId playerId);

	public PlayerState previewNextToAct();

	public boolean hasBet();
	
}
