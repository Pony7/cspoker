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

package org.cspoker.server.common.player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.server.common.elements.chips.Chips;
import org.cspoker.server.common.elements.chips.IllegalValueException;
import org.cspoker.server.common.elements.id.PlayerId;
import org.cspoker.server.common.elements.id.SeatId;

/**
 * A class to represent players: bots or humans.
 * 
 * @author Kenzo
 * 
 */
public class GameSeatedPlayer {
	
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	
	private final ServerPlayer player;
	
	private final Chips stack;
	
	/**
	 * The chips the player has bet in this round.
	 * 
	 */
	private final Chips betChips;
	
	/**
	 * The hand cards.
	 */
	private final List<Card> pocketCards;
	
	/**
	 * The variable containing the seat id.
	 */
	private SeatId seatId;
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	
	public GameSeatedPlayer(ServerPlayer player, int buyIn)
			throws IllegalValueException {
		this.player = player;
		this.stack = new Chips(buyIn);
		betChips = new Chips();
		pocketCards = new CopyOnWriteArrayList<Card>();
	}
	
	/**
	 * Returns the name of this player.
	 * 
	 * @return The name of this player.
	 */
	public String getName() {
		return player.getName();
	}
	
	/***************************************************************************
	 * Id
	 **************************************************************************/
	
	/**
	 * Returns the id of this player.
	 * 
	 * @return The id of this player.
	 */
	public PlayerId getId() {
		return player.getId();
	}
	
	/***************************************************************************
	 * Seat Id
	 **************************************************************************/
	
	/**
	 * Returns the id of this player.
	 * 
	 * @return The id of this player.
	 */
	public SeatId getSeatId() {
		return seatId;
	}
	
	public void setSeatId(SeatId seatId) {
		this.seatId = seatId;
	}
	
	/***************************************************************************
	 * Chips
	 **************************************************************************/
	
	public Chips getStack() {
		return stack;
	}
	
	public Chips getBetChips() {
		return betChips;
	}
	
	public synchronized void transferAmountToBetPile(int amount)
			throws IllegalValueException {
		getStack().transferAmountTo(amount, getBetChips());
	}
	
	public synchronized void transferAllChipsToBetPile()
			throws IllegalValueException {
		getStack().transferAllChipsTo(getBetChips());
	}
	
	/***************************************************************************
	 * Cards
	 **************************************************************************/
	
	/**
	 * Deal a pocket card to this player.
	 * 
	 */
	public void dealPocketCard(Card... cards) {
		for (Card card : cards) {
			pocketCards.add(card);
		}
	}
	
	/**
	 * Returns the pocket cards of this player.
	 * 
	 * A change in the returned list, does not change the internal
	 * representation.
	 * 
	 * @return The pocket cards of this player.
	 */
	public List<Card> getPocketCards() {
		return new ArrayList<Card>(pocketCards);
	}
	
	public void clearPocketCards() {
		pocketCards.clear();
	}
	
	public synchronized SeatedPlayer getMemento() {
		return new SeatedPlayer(getId().getId(), getSeatId().getId(), getName(), getStack().getValue(), getBetChips()
				.getValue());
	}
	
	@Override
	public String toString() {
		return getId() + ": " + getName() + " ($" + getStack() + " in chips)";
	}
	
	/**
	 * Returns a hash code value for this player.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		return result;
	}
	
	/**
	 * Indicates whether some other object is "equal to" this one.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final GameSeatedPlayer other = (GameSeatedPlayer) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}
}
