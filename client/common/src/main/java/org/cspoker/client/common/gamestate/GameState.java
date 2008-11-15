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

import java.util.Set;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

@Immutable
public interface GameState {

	public int getStack(PlayerId playerId);
	public int getBetSize(PlayerId playerId);
	public SeatId getSeatId(PlayerId playerId);
	public boolean isPlaying(PlayerId playerId);
	
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
	 * Returns the PlayerId of the player sitting in the given seat or null
	 * if the seat is empty;
	 */
	public PlayerId getPlayerId(SeatId seatId);
	
	public PlayerId getDealer();
	
	/**
	 * Returns the ID of the player that is next to act or null if nobody should act.
	 */
	public PlayerId getNextToAct();
	
	public int getPotSize();
	public int getLargestBet();
	public int getMinNextRaise();
	
	public TableConfiguration getTableConfiguration();
	
	public Round getRound();

	public Set<Card> getCards(PlayerId playerId);
	public Set<Card> getCommunityCards();
	
	public GameState getPreviousGameState();
	public HoldemTableEvent getLastEvent();
	
}
