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

import java.util.*;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.chips.IllegalValueException;
import org.cspoker.common.elements.chips.Pots;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
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
	
	private Map<MutableSeatedPlayer, List<NavigableMap<Chip, Integer>>> betPiles = new Hashtable<MutableSeatedPlayer, List<NavigableMap<Chip, Integer>>>();
	
	private MutableSeatedPlayer dealer;
	private Logger logger = Logger.getLogger(GameState.class);
	
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
	
	public void betRaise(int amount) {
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
		betPiles.clear();
	}
	
	public void setPots(Pots pots) {
		this.pots = pots;
	}
	
	public int getToCallAmount(MutableSeatedPlayer mutableSeatedPlayer) {
		return Chip.getValue(getCurrentBetPile()) - mutableSeatedPlayer.getBetChips().getValue();
	}
	
	public int getPotRaiseAmount(MutableSeatedPlayer mutableSeatedPlayer) {
		return Math.max(0, Math.min(mutableSeatedPlayer.getStack().getValue() - getToCallAmount(mutableSeatedPlayer),
				getToCallAmount(mutableSeatedPlayer) + getPots().getTotalValue()));
	}
	
	public int getMinBetRaiseAmount(MutableSeatedPlayer mutableSeatedPlayer) {
		return Math.max(0, Math.min(mutableSeatedPlayer.getStack().getValue() - getToCallAmount(mutableSeatedPlayer),
				Math.max(getLastBetRaiseAmount(), getToCallAmount(mutableSeatedPlayer))));
	}
	
	public void updateStackAndBetChips(MutableSeatedPlayer mutableSeatedPlayer, int betRaiseAmount) {
		int totalAmount = getToCallAmount(mutableSeatedPlayer) + betRaiseAmount;
		try {
			mutableSeatedPlayer.getStack().transferAmountTo(totalAmount, mutableSeatedPlayer.getBetChips());
		} catch (IllegalValueException e) {
			logger.error(e);
		}
	}
	
	public void setBetChipsValue(MutableSeatedPlayer mutableSeatedPlayer, int newBetChipsValue) {
		List<NavigableMap<Chip, Integer>> playerBetPile = getBetPile(mutableSeatedPlayer);
		playerBetPile.clear();
		playerBetPile.addAll(currentBetPile);
		playerBetPile.add(Chip.getDistribution(newBetChipsValue));
		betPiles.put(mutableSeatedPlayer, playerBetPile);
		
	}
	
	public List<NavigableMap<Chip, Integer>> getBetPile(MutableSeatedPlayer player) {
		List<NavigableMap<Chip, Integer>> betPile = betPiles.get(player);
		if (betPile == null) {
			betPile = new ArrayList<NavigableMap<Chip, Integer>>(Arrays.asList(Chip.getDistribution(0)));
			betPiles.put(player, betPile);
		}
		return betPile;
	}
	
	/**
	 * @return
	 */
	public MutableSeatedPlayer getDealer() {
		return dealer;
	}
	
	/**
	 * @param player
	 */
	public void setDealer(MutableSeatedPlayer player) {
		this.dealer = player;
		
	}
	
}
