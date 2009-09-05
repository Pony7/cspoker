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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.cspoker.common.elements.player.MutableAllInPlayer;
import org.cspoker.common.elements.player.MutableSeatedPlayer;

/**
 * A class to represent a group of pots.
 */
public class MutablePots {
	
	private LinkedList<MutablePot> pots;
	
	public MutablePots(Collection<MutableSeatedPlayer> players) {
		pots = new LinkedList<MutablePot>();
		pots.add(new MutablePot(players));
	}
	
	public synchronized void createSidePots(List<MutableAllInPlayer> allInPlayers,
			Collection<Chips> betsFromFoldedPlayers) {
		Collections.sort(allInPlayers);
		LinkedList<MutableAllInPlayer> allInPlayersQueue = new LinkedList<MutableAllInPlayer>(allInPlayers);
		
		while (!allInPlayersQueue.isEmpty()) {
			MutableAllInPlayer allInPlayer = allInPlayersQueue.poll();
			MutablePot currentPot = pots.element();
			
			if (allInPlayer.getBetValue() > 0) {
				pots.addFirst(currentPot.createPot());
				currentPot.collectChips(allInPlayer.getBetValue());
				
				for (Chips c : betsFromFoldedPlayers) {
					if (c.getValue() > allInPlayer.getBetValue()) {
						c.transferAmountTo(allInPlayer.getBetValue(), currentPot.getChips());
					} else {
						c.transferAllChipsTo(currentPot.getChips());
						betsFromFoldedPlayers.remove(c);
					}
				}
			}
			pots.element().removeContributor(allInPlayer.getPlayer());
		}
		if (pots.element().getNbContributors() == 0) {
			pots.removeFirst();
		}
	}
	
	public synchronized void removeContributor(MutableSeatedPlayer player) {
		for (MutablePot pot : pots) {
			pot.removeContributor(player);
		}
	}
	
	public synchronized int getTotalValue() {
		int value = 0;
		for (MutablePot pot : pots) {
			value += pot.getChips().getValue();
		}
		return value;
	}
	
	public synchronized MutablePot getMainPot() {
		return pots.element();
	}
	
	public synchronized int getNbShowdownPlayers() {
		return pots.getLast().getNbContributors();
	}
	
	public synchronized List<MutablePot> getAllPots() {
		return Collections.unmodifiableList(pots);
	}
	
	public synchronized Collection<MutableSeatedPlayer> getShowdownPlayers() {
		return pots.getLast().getContributors();
	}
	
	public synchronized Pots getSnapshot() {
		List<Pot> toReturn = new ArrayList<Pot>(pots.size());
		int value = 0;
		for (MutablePot pot : pots) {
			toReturn.add(pot.getSnapshot());
			value += pot.getChips().getValue();
		}
		return new Pots(value);
	}
	
	@Override
	public String toString() {
		return pots.toString();
	}
}
