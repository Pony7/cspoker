/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.common.playerstate;

import java.util.EnumSet;
import java.util.List;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.SeatId;

public interface PlayerState {
	
	PlayerId getPlayerId();
	
	SeatId getSeatId();
	
	String getName();
	
	EnumSet<Card> getCards();
	
	int getStack();
	
	int getBet();
	
	int getTotalInvestment();
	
	/**
	 * @return A list of integers representing all bets/calls/raises in this
	 *         round. The total amount adds up to getBetChips()
	 *         <p>
	 *         This method is supposed to be used to depict the progression of
	 *         <i>how</i> the player has put the chips in.
	 *         getBetProgression().size() should also reflect the number of
	 *         bets/raises in the current round (including the big blind)
	 */
	List<Integer> getBetProgression();
	
	boolean hasFolded();
	
	boolean hasBeenDealt();
	
	boolean isSmallBlind();
	
	boolean isBigBlind();
	
	/**
	 * A derived state property that says whether a player is all-in or not.
	 */
	boolean isAllIn();
	
	boolean isActivelyPlaying();

	/**
	 * Last action was a check
	 */
	boolean hasChecked();
	
}
