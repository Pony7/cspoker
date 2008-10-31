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

package org.cspoker.server.common.player;

import java.util.HashSet;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.ShowdownPlayer;
import org.cspoker.server.common.elements.cards.hand.Hand;

/**
 * A class to represent a showdown player.
 * 
 * This class implements the comparable interface to easily get the player(s)
 * with the best hand on top of the list.
 * 
 * @author Kenzo
 * 
 */
public class MutableShowdownPlayer implements Comparable<MutableShowdownPlayer> {

	/**
	 * This variable contains the showdown player.
	 */
	private final MutableSeatedPlayer player;

	/**
	 * This variable contains the showdown player's best hand.
	 */
	private final Hand bestHand;

	/**
	 * Construct a new showdown player with given player and given best hand.
	 * 
	 * @param player
	 *            The player for this showdown player.
	 * @param bestHand
	 *            The player's best hand.
	 */
	public MutableShowdownPlayer(MutableSeatedPlayer player, Hand bestHand) {
		this.player = player;
		this.bestHand = bestHand;

	}

	/**
	 * Returns the effective showdown player, as a player.
	 * 
	 * @return The effective showdown player, as a player.
	 */
	public MutableSeatedPlayer getPlayer() {
		return player;
	}

	/**
	 * Return the best hand.
	 * 
	 * @return The best hand.
	 */
	public Hand getBestHand() {
		return bestHand;
	}

	public ShowdownPlayer getSavedShowdownPlayer() {
		return new ShowdownPlayer(player.getMemento(), new HashSet<Card>(
				getBestHand().getCards()), new HashSet<Card>(player
				.getPocketCards()), getBestHand().getDescription());
	}

	/**
	 * Returns a textual representation of this showdown player.
	 */

	public String toString() {
		return player.getName() + " has a " + getBestHand();
	}

	/**
	 * After sorting, the first player is the player with the best hand.
	 * 
	 * -1 = first hand is best, 1 = second hand is best, 0 = tie
	 */
	public int compareTo(MutableShowdownPlayer o) {
		return o.getBestHand().compareTo(getBestHand());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getBestHand() == null) ? 0 : getBestHand().hashCode());
		return result;
	}

	/**
	 * 2 Players are equal when they have the same best hand.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableShowdownPlayer other = (MutableShowdownPlayer) obj;
		if (bestHand == null) {
			if (other.bestHand != null)
				return false;
		} else if (!getBestHand().equals(other.getBestHand()))
			return false;
		return true;
	}
	
	

}