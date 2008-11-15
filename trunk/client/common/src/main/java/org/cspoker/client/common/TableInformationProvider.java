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
package org.cspoker.client.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.chips.Pots;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.Rounds;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;

/**
 * This class contains a number of methods to retrieve continually updated table
 * information during a hand
 * <p>
 * This class should be thread safe since it does not publish any mutable
 * objects
 * 
 * @author Stephan Schmidt
 */
@ThreadSafe
public class TableInformationProvider {
	
	private static Logger logger = Logger.getLogger(TableInformationProvider.class);
	
	private final TableId tableId;
	private final String tableName;
	
	/**
	 * Table snapshot retrieved from the server upon initialization (for
	 * GameProperty Info etc.)
	 */
	private final TableConfiguration tableConfiguration;
	private final SmartHoldemTableListener listener;
	
	public TableInformationProvider(DetailedHoldemTable table, SmartHoldemTableListener listener) {
		tableId = table.getId();
		tableName = table.getName();
		tableConfiguration = table.getTableConfiguration();
		this.listener = listener;
	}
	
	public int getAllStakes(PlayerId playerID) {
		synchronized (listener.playersLock) {
			MutableSeatedPlayer player = listener.players.get(playerID);
			if (player == null) {
				return 0;
			} else {
				if (getPots() != null && getPots().getTotalValue() > 0) {
					throw new IllegalStateException("Pots are not empty, can't calculate all stakes");
				}
				return player.getStack().getValue() + player.getBetChips().getValue();
			}
		}
	}
	
	public Set<Card> getCommunityCards() {
		return listener.communityCards;
	}
	
	public Rounds getCurrentRound() {
		return listener.round;
	}
	
	/**
	 * @return Current dealer
	 */
	public PlayerId getDealer() {
		return listener.dealer;
	}
	
	/**
	 * Allows easier computing of a valid next raise (needs to be at least the
	 * amount of the last raise (or the remaining stack))
	 * 
	 * @return
	 */
	public int getLastBetRaiseAmount() {
		return listener.lastBetRaiseAmount;
	}
	
	public int getPotRaiseAmount(PlayerId player) {
		SeatedPlayer snapshot = getSnapshot(player);
		int potRaise = getTotalPot() + getToCall(player);
		potRaise = Math.min(potRaise, snapshot.getStackValue() - getToCall(player));
		potRaise = Math.max(potRaise, 0);
		
		return potRaise;
	}
	
	/**
	 * Returns the minimum amount the player has to raise to perform a valid
	 * raise
	 * 
	 * @param player The {@link PlayerId}
	 * @return The minimum raise amount (at least the amount of the last raise,
	 *         or the remaining stack)
	 */
	public int getMinBetRaiseAmount(PlayerId player) {
		SeatedPlayer snapshot = getSnapshot(player);
		int bigBlind = tableConfiguration.getBigBlind();
		int lastRaise = getLastBetRaiseAmount();
		int minBetRaise = Math.max(bigBlind, lastRaise);
		int moneyRemaining = snapshot.getStackValue() - getToCall(player);
		minBetRaise = Math.min(minBetRaise, moneyRemaining);
		return minBetRaise;
	}
	
	/**
	 * @return The player who has bet the most chips in this round
	 */
	public int getMaxBet() {
		int max = 0;
		for (SeatedPlayer player : listener.getPlayers().values()) {
			if (player.getBetChipsValue() > max) {
				max = player.getBetChipsValue();
			}
		}
		
		return max;
	}
	
	/**
	 * Add all the bets by all players from current round (not yet in middle)
	 * and the pot in the middle
	 * 
	 * @return
	 */
	private int getTotalPot() {
		int pot = getPots().getTotalValue();
		for (SeatedPlayer player : listener.getPlayers().values()) {
			pot += player.getBetChipsValue();
		}
		return pot;
	}
	
	public Pots getPots() {
		return listener.pots;
	}
	
	/**
	 * Get an immutable snapshot of the player with the given id
	 * 
	 * @param id The {@link PlayerId}
	 * @return Snapshot of the player
	 */
	public SeatedPlayer getSnapshot(PlayerId id) {
		return listener.getPlayers().get(id);
	}
	
	/**
	 * @param playerId The {@link PlayerId}
	 * @return The amount the player has to call if it were/is his turn to act
	 *         and he wants to stay in the hand
	 */
	public int getToCall(PlayerId playerId) {
		if (getMaxBet() == tableConfiguration.getSmallBlind()) {
			return 0;
		}
		return Math.max(0, getMaxBet() - getSnapshot(playerId).getBetChipsValue());
	}
	
	public boolean isPlaying(PlayerId playerID) {
		
		return listener.getPlayers().containsKey(playerID);
	}
	
	/**
	 * @return the table configuration
	 */
	public TableConfiguration getTableConfiguration() {
		return tableConfiguration;
	}
	
	/**
	 * TODO Adjust when more game types are supported
	 * 
	 * @return number of hole cards dealt to each player in this game type
	 */
	public int getNumberOfHoleCards() {
		return 2;
	}
	
	/**
	 * @return The table id
	 */
	public TableId getTableId() {
		return tableId;
	}
	
	/**
	 * @return The name of the table
	 */
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * For graphical representation of chip stacks. This may be moved to
	 * somewhere else if nobody wants it, maybe other graphical clients can use
	 * it.
	 * <p>
	 * Calling this basically returns a list of integers whose sum is the total
	 * bet chips amount for the player, and the progression indicates the number
	 * of bets/raises/calls in the round
	 * 
	 * @return
	 */
	public List<Integer> getBetPile(PlayerId playerID) {
		List<Integer> result = new ArrayList<Integer>(listener.betsInCurrentRound.subList(0,
				listener.inPotUntilBettingRound.get(playerID)));
		// Merge 2 small blinds into 1 big blind if necessary
		if (result.size() > 1) {
			if (result.get(0) == tableConfiguration.getSmallBlind()) {
				assert (result.get(1) == tableConfiguration.getSmallBlind()) : "I want to merge the two small blinds, something is wrong here, if the first is the SB the second should also be the SB";
				
				result.subList(0, 2).clear();
				result.add(0, tableConfiguration.getBigBlind());
			}
		}
		return result;
	}
}
