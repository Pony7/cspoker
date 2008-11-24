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
package org.cspoker.common.elements.chips;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.SeatedPlayer;

/**
 * A class to represent mutable pots.
 */
public class MutablePot {
	
	private Set<MutableSeatedPlayer> players;
	
	private Chips chips;
	
	/**
	 * Construct a new mutable pot with given players.
	 * 
	 * @param players The players who share this pot.
	 */
	public MutablePot(Collection<MutableSeatedPlayer> players) {
		this.players = new HashSet<MutableSeatedPlayer>(players);
		this.chips = new Chips();
	}
	
	public synchronized void removeContributor(MutableSeatedPlayer player) {
		players.remove(player);
	}
	
	public synchronized int getNbContributors() {
		return players.size();
	}
	
	public synchronized Collection<MutableSeatedPlayer> getContributors() {
		return Collections.unmodifiableCollection(players);
	}
	
	public synchronized void collectAllChips() {
		for (MutableSeatedPlayer player : players) {
			player.getBetChips().transferAllChipsTo(chips);
		}
	}
	
	public synchronized void collectChips(int amount) {
		for (MutableSeatedPlayer player : players) {
			try {
				player.getBetChips().transferAmountTo(amount, chips);
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public synchronized void collectAllChips(Collection<Chips> chipsCollection) {
		for (Chips sender : chipsCollection) {
			sender.transferAllChipsTo(chips);
		}
	}
	
	public Chips getChips() {
		return chips;
	}
	
	public MutablePot createPot() {
		return new MutablePot(players);
	}
	
	/**
	 * Returns an immutable snapshot of this pot.
	 * 
	 * @return An immutable snapshot of this pot.
	 */
	public synchronized Pot getSnapshot() {
		Set<SeatedPlayer> toReturn = new HashSet<SeatedPlayer>();
		for (MutableSeatedPlayer seatedPlayer : players) {
			toReturn.add(seatedPlayer.getMemento());
		}
		return new Pot(toReturn, chips.getValue());
	}
	
	@Override
	public String toString() {
		return players.toString() + " > " + chips.toString();
	}
	
}
