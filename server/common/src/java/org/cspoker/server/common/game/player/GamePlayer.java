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

package org.cspoker.server.common.game.player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.player.Player;
import org.cspoker.common.player.PlayerId;
import org.cspoker.server.common.game.elements.chips.Chips;
import org.cspoker.server.common.game.elements.chips.IllegalValueException;

/**
 * A class to represent players: bots or humans.
 * 
 * @author Kenzo
 * 
 */
public class GamePlayer {

	/***************************************************************************
	 * Variables
	 **************************************************************************/

	/**
	 * The variable containing the id of the player.
	 */
	private final PlayerId id;

	/**
	 * The name of the player.
	 */
	private final String name;

	/**
	 * The stack of this player.
	 */
	private final Chips chips;

	/**
	 * The chips the player has bet in this round.
	 * 
	 */
	private final Chips betChips;

	/**
	 * The hidden cards.
	 */
	private final List<Card> pocketCards;

	/**
	 * The variable containing the seat id.
	 */
	private SeatId seatId;

	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	/**
	 * Construct a new player with given id, name and initial number of chips.
	 * 
	 * @throws IllegalValueException
	 *             [must] The given initial value is not valid. |
	 *             !isValidName(name)
	 * 
	 * @post The chips pile is effective and the value of chips is the same as
	 *       the given initial value. |new.getBetChips()!=null &&
	 *       new.getChips.getValue()==initialNbChips
	 * @post The bet chips pile is effective and There are no chips on this
	 *       pile. |new.getBetChips()!=null && new.getBetChips().getValue()==0
	 */
	GamePlayer(PlayerId id, String name, int initialNbChips)
			throws IllegalValueException {
		this.id = id;
		this.name = name;
		chips = new Chips(initialNbChips);
		betChips = new Chips();
		pocketCards = new CopyOnWriteArrayList<Card>();
	}

	/**
	 * Returns the name of this player.
	 * 
	 * @return The name of this player.
	 */
	public String getName() {
		return name;
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
		return id;
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
		return chips;
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

	public String toString() {
		return getId() + ": " + getName() + " ($" + getStack() + " in chips)";
	}

	/**
	 * Returns a hash code value for this player.
	 */

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 */

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final GamePlayer other = (GamePlayer) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public synchronized Player getSavedPlayer() {
		return new Player(getId(), getSeatId(), getName(), getStack()
				.getValue(), getBetChips().getValue());
	}
}
