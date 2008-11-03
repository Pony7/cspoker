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

import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.SeatId;

/**
 * Will comment later due to maybe pending changes in Server design
 * <p>
 * Basically intended to keep track of a player sitting at a table and his
 * current status. The {@link GameState} is needed so the player can be asked
 * questions such as <b>What is the amount I have to call at this given
 * moment</b> etc.
 * 
 * @author Stephan Schmidt
 */
public class MutableSeatedPlayer {
	
	private SeatedPlayer wrappedPlayer;
	
	private int stackValue;
	private int betChipsValue;
	private boolean dealer;
	private boolean sittingOut;
	private List<NavigableMap<Chip, Integer>> currentBetPile = new ArrayList<NavigableMap<Chip, Integer>>();
	
	protected GameState gameState;
	public final static MutableSeatedPlayer UNOCCUPIED = new MutableSeatedPlayer(new SeatedPlayer(new PlayerId(
			Long.MAX_VALUE), new SeatId(Long.MAX_VALUE), "Empty Seat", 0, 0), null);
	
	public MutableSeatedPlayer(SeatedPlayer player, GameState gameState) {
		setPlayer(player);
		this.gameState = gameState;
		
	}
	
	public void setPlayer(SeatedPlayer player) {
		wrappedPlayer = player;
		setBetChipsValue(player.getBetChipsValue());
		setStackValue(player.getStackValue());
	}
	
	public int getStackValue() {
		return stackValue;
	}
	
	public int getBetChipsValue() {
		return betChipsValue;
	}
	
	public String getName() {
		return wrappedPlayer.getName();
	}
	
	public SeatId getSeatId() {
		return wrappedPlayer.getSeatId();
	}
	
	public List<NavigableMap<Chip, Integer>> getCurrentBetPile() {
		return currentBetPile;
	}
	
	public void setDealer(boolean b) {
		dealer = b;
	}
	
	public PlayerId getId() {
		return wrappedPlayer.getId();
	}
	
	public int getToCallAmount() {
		return Chip.getValue(gameState.getCurrentBetPile()) - getBetChipsValue();
	}
	
	public int getMinBetRaiseAmount() {
		return Math.max(0, Math.min(getStackValue() - getToCallAmount(), Math.max(gameState.getLastBetRaiseAmount(),
				getToCallAmount())));
	}
	
	public int getPotRaiseAmount() {
		return Math.max(0, Math.min(getStackValue() - getToCallAmount(), getToCallAmount()
				+ gameState.getPots().getTotalValue()));
	}
	
	public void updateStackAndBetChips(int betRaiseAmount) {
		int totalAmount = getToCallAmount() + betRaiseAmount;
		stackValue = getStackValue() - totalAmount;
		betChipsValue = getBetChipsValue() + totalAmount;
	}
	
	public void setBetChipsValue(int newBetChipsValue) {
		betChipsValue = newBetChipsValue;
		getCurrentBetPile().clear();
		getCurrentBetPile().add(Chip.getDistribution(newBetChipsValue));
		
	}
	
	public void setStackValue(int newStackValue) {
		stackValue = newStackValue;
	}
	
	public boolean isDealer() {
		return dealer;
	}
	
	public SeatedPlayer getMemento() {
		return wrappedPlayer;
	}
	
	public boolean isSittingOut() {
		return sittingOut;
	}
	
	public void setSittingOut(boolean sittingOut) {
		this.sittingOut = sittingOut;
	}
}
