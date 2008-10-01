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
import java.util.Map;
import java.util.NavigableMap;
import java.util.Map.Entry;

import org.cspoker.common.elements.pots.Pots;
import org.cspoker.common.elements.table.DetailedTable;

/**
 * TODO Bad design, should be specific for the user The game state at a table
 * 
 * @author Stephan Schmidt
 */
public class GameState {
	
	/**
	 * Table snapshot retrieved from the server upon initialization (for
	 * GameProperty Info etc.)
	 */
	private DetailedTable tableMemento;
	private Pots pots;
	private List<NavigableMap<Chip, Integer>> currentBetPile = new ArrayList<NavigableMap<Chip, Integer>>();
	
	private int betChipsThisRound;
	
	public int getBetChipsThisRound() {
		return betChipsThisRound;
	}
	
	public void setBetChipsThisRound(int betChipsThisRound) {
		this.betChipsThisRound = betChipsThisRound;
	}
	
	private int remainingStack;
	
	public int getRemainingStack() {
		return remainingStack;
	}
	
	public void setRemainingStack(int remainingStack) {
		this.remainingStack = remainingStack;
	}
	
	public List<NavigableMap<Chip, Integer>> getCurrentBetPile() {
		return currentBetPile;
	}
	
	private int moneyInMiddle;
	
	public void setMoneyInMiddle(int moneyInMiddle) {
		this.moneyInMiddle = moneyInMiddle;
	}
	
	public GameState(DetailedTable table) {
		setTableMemento(table);
		pots = new Pots(0);
		currentBetPile = new ArrayList<NavigableMap<Chip, Integer>>();
	}
	
	/**
	 * @return the tableMemento
	 */
	public DetailedTable getTableMemento() {
		return tableMemento;
	}
	
	/**
	 * @param tableMemento the tableMemento to set
	 */
	public void setTableMemento(DetailedTable tableMemento) {
		this.tableMemento = tableMemento;
	}
	
	/**
	 * @return the pots
	 */
	public Pots getPots() {
		return pots;
	}
	
	/**
	 * @param pots the pots to set
	 */
	public void setPots(Pots pots) {
		this.pots = pots;
	}
	
	public int getToCallAmount() {
		return getValue(currentBetPile) - getBetChipsThisRound();
	}
	
	public int getMinBetRaiseAmount() {
		return Math.max(0, Math.min(getRemainingStack() - getToCallAmount(), Math.max(tableMemento.getGameProperty()
				.getBigBlind(), getToCallAmount())));
	}
	
	public int getPotRaiseAmount() {
		return Math.max(0, Math.min(getRemainingStack() - getToCallAmount(), getToCallAmount()
				+ getPots().getTotalValue()));
	}
	
	public void newRound(String roundName) {
		if (roundName.equalsIgnoreCase("pre-flop round")) {
			setPots(new Pots(0));
		}
		currentBetPile.clear();
		moneyInMiddle = getPots().getTotalValue();
		setBetChipsThisRound(0);
	}
	
	public int getMoneyInMiddle() {
		return moneyInMiddle;
	}
	
	public static int getValue(List<NavigableMap<Chip, Integer>> chipPiles) {
		int amount = 0;
		for (Map<Chip, Integer> chips : chipPiles) {
			for (Entry<Chip, Integer> chipEntry : chips.entrySet()) {
				amount += chipEntry.getKey().getValue() * chipEntry.getValue();
			}
		}
		return amount;
	}
	
	/**
	 * TODO Adjust when more game types are supported
	 * 
	 * @return number of hole cards dealt to each player in this game type
	 */
	public int getNumberOfHoleCards() {
		return 2;
	}
	
	public List<NavigableMap<Chip, Integer>> addToCurrentBetPile(int amount) {
		
		int lastBetRaiseAmount = Math.max(0, amount - getValue(currentBetPile));
		currentBetPile.add(Chip.getDistribution(lastBetRaiseAmount));
		
		return currentBetPile;
		
	}
	
	public void updateStackAndBetChips(int betRaiseAmount) {
		setRemainingStack(getRemainingStack() - getToCallAmount() - betRaiseAmount);
		setBetChipsThisRound(getBetChipsThisRound() + getToCallAmount() + betRaiseAmount);
	}
	
}
