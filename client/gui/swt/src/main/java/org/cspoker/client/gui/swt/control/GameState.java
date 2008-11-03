/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.gui.swt.control;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

import org.cspoker.common.elements.chips.Pots;
import org.cspoker.common.elements.table.DetailedHoldemTable;

/**
 * @author Stephan Schmidt
 */
public class GameState {
	
	/**
	 * Table snapshot retrieved from the server upon initialization (for
	 * GameProperty Info etc.)
	 */
	private DetailedHoldemTable tableMemento;
	private Pots pots;
	private List<NavigableMap<Chip, Integer>> currentBetPile = new ArrayList<NavigableMap<Chip, Integer>>();
	private int lastBetRaiseAmount;
	private int totalBetRaiseAmount;
	
	public GameState(DetailedHoldemTable table) {
		setTableMemento(table);
		pots = new Pots(0);
		currentBetPile = new ArrayList<NavigableMap<Chip, Integer>>();
	}
	
	/**
	 * @return the tableMemento
	 */
	public DetailedHoldemTable getTableMemento() {
		return tableMemento;
	}
	
	/**
	 * @param tableMemento the tableMemento to set
	 */
	public void setTableMemento(DetailedHoldemTable tableMemento) {
		this.tableMemento = tableMemento;
	}
	
	/**
	 * @return the pots
	 */
	public Pots getPots() {
		return pots;
	}
	
	/**
	 * TODO Adjust when more game types are supported
	 * 
	 * @return number of hole cards dealt to each player in this game type
	 */
	public int getNumberOfHoleCards() {
		return 2;
	}
	
	public void betRaise(int amount, Pots newPots) {
		pots = newPots;
		if (amount == 0) {
			return;
		}
		if (amount < 0) {
			throw new IllegalArgumentException("Cannot add negative amount to bet pile");
		} else {
			totalBetRaiseAmount += amount;
			lastBetRaiseAmount = amount;
			currentBetPile.add(Chip.getDistribution(amount));
		}
		
	}
	
	public List<NavigableMap<Chip, Integer>> getCurrentBetPile() {
		return currentBetPile;
	}
	
	public int getLastBetRaiseAmount() {
		return lastBetRaiseAmount;
	}
	
	public int getLastBetChipsTotal() {
		return totalBetRaiseAmount;
	}
	
	public void newRound() {
		lastBetRaiseAmount = 0;
		totalBetRaiseAmount = 0;
		currentBetPile.clear();
	}
	
	public void setPots(Pots pots) {
		this.pots = pots;
	}
	
}
