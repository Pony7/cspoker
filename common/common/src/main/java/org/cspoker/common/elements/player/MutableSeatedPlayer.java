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

package org.cspoker.common.elements.player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.chips.Chips;
import org.cspoker.common.elements.table.SeatId;

/**
 * A class to represent players.
 * 
 * @author Kenzo
 */
public class MutableSeatedPlayer {
	
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	
	private final MutablePlayer player;
	
	/**
	 * The hand cards.
	 */
	private final List<Card> pocketCards;
	
	/**
	 * The variable containing the seat id.
	 */
	private SeatId seatId;
	
	private boolean sittingIn;
	
	private Chips betChips;
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	
	/**
	 * @param player The mutable player object
	 * @param buyIn the buyin amount
	 * @throws IllegalArgumentException If the given buyin is illegal
	 */
	public MutableSeatedPlayer(MutablePlayer player, int buyIn) {
		this.player = player;
		player.getStack().discard();
		
		new Chips(buyIn).transferAllChipsTo(player.getStack());
		betChips = new Chips(0);
		
		pocketCards = new CopyOnWriteArrayList<Card>();
		// TODO Kenzo: Why default false??
		sittingIn = false;
	}
	
	/**
	 * @param player The {@link SeatedPlayer} object
	 * @throws IllegalArgumentException If the player is in an illegal state
	 */
	public MutableSeatedPlayer(SeatedPlayer seatedPlayer) {
		this.player = new MutablePlayer(seatedPlayer);
		betChips = new Chips(seatedPlayer.getBetChipsValue());
		pocketCards = new CopyOnWriteArrayList<Card>();
		this.sittingIn = seatedPlayer.isSittingIn();
	}
	
	/**
	 * @param player The mutable player object
	 * @param buyIn the buyin amount
	 * @param sittingIn Whether the player is initially sitting in
	 * @throws IllegalArgumentException If the given buyin is illegal
	 */
	public MutableSeatedPlayer(MutablePlayer player, int buyIn, boolean sittingIn) {
		this(player, buyIn);
		this.sittingIn = sittingIn;
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
		return player.getStack();
	}
	
	public Chips getBetChips() {
		return betChips;
	}
	
	public synchronized void transferAmountToBetPile(int amount) {
		getStack().transferAmountTo(amount, getBetChips());
	}
	
	public synchronized void transferAllChipsToBetPile() {
		getStack().transferAllChipsTo(getBetChips());
	}
	
	public synchronized boolean isBroke(){
		return player.getStack().getValue() <= 0;
	}
	
	/***************************************************************************
	 * Cards
	 **************************************************************************/
	
	/**
	 * Deal a pocket card to this player.
	 */
	public void dealPocketCard(Card... cards) {
		for (Card card : cards) {
			pocketCards.add(card);
		}
	}
	
	/**
	 * Returns the pocket cards of this player. A change in the returned list,
	 * does not change the internal representation.
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
		return new SeatedPlayer(getId(), getSeatId(), getName(), getStack().getValue(), getBetChips().getValue(),
				sittingIn, pocketCards.size() > 0);
	}
	
	@Override
	public String toString() {
		return getId() + ": " + getName() + " (" + getStack() + " in chips)";
	}
	
	/**
	 * Returns a hash code value for this player.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((player == null) ? 0 : player.getId().getId()));
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
		final MutableSeatedPlayer other = (MutableSeatedPlayer) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}
	
	/**
	 * @return <code>true</code> if the player is sitting in and ready to play,
	 *         <code>false</code> otherwise (the player will skip the next hands
	 *         until he sits back in)
	 */
	public boolean isSittingIn() {
		return sittingIn;
	}
	
	public void setSittingIn(boolean sittingIn) {
		this.sittingIn = sittingIn;
	}
}
