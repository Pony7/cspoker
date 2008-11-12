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
import org.cspoker.client.common.SmartHoldemTableListener;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;

/**
 * @author Stephan Schmidt
 */
public class GameState
		extends SmartHoldemTableListener {
	
	private final TableId tableId;
	private final String tableName;
	/**
	 * Table snapshot retrieved from the server upon initialization (for
	 * GameProperty Info etc.)
	 */
	private final TableConfiguration tableConfiguration;
	private Map<PlayerId, List<Integer>> betPiles = new HashMap<PlayerId, List<Integer>>();
	private PlayerId lastActed;
	
	private Logger logger = Logger.getLogger(GameState.class);
	
	public GameState(GameWindow gameWindow, DetailedHoldemTable table) {
		super(gameWindow);
		tableConfiguration = table.getTableConfiguration();
		tableId = table.getId();
		tableName = table.getName();
		initialize(table);
	}
	
	/**
	 * Initializes the GameState with the {@link DetailedHoldemTable} from the
	 * server
	 * 
	 * @param table The initial table configuration
	 */
	private void initialize(DetailedHoldemTable table) {
		for (SeatedPlayer player : table.getPlayers()) {
			players.put(player.getId(), new MutableSeatedPlayer(player));
			if (table.getDealer() != null) {
				dealer = table.getDealer().getId();
			}
			getCommunityCards().addAll(table.getCommunityCards());
			pots = table.getPots();
			betPiles.put(player.getId(), new ArrayList<Integer>(player.getBetChipsValue()));
		}
	}
	
	/**
	 * @return the tableMemento
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
	
	public int getPotRaiseAmount(PlayerId player) {
		SeatedPlayer snapshot = getSnapshot(player);
		int potRaise = getTotalPot() + getToCall(player);
		potRaise = Math.min(potRaise, snapshot.getStackValue() - getToCall(player));
		potRaise = Math.max(potRaise, 0);
		
		return potRaise;
	}
	
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
	 * @return
	 */
	public List<Integer> getBetPile(PlayerId player) {
		List<NavigableMap<Chip, Integer>> chipStacks = new ArrayList<NavigableMap<Chip, Integer>>();
		if (!betPiles.containsKey(player)) {
			betPiles.put(player, new ArrayList<Integer>());
		}
		return betPiles.get(player);
	}
	
	/**
	 * Add all the bets by all players from current round (not yet in middle)
	 * and the pot in the middle
	 * 
	 * @return
	 */
	private int getTotalPot() {
		int pot = getPots().getTotalValue();
		for (List<Integer> bets : betPiles.values()) {
			for (Integer bet : bets) {
				pot += bet;
			}
		}
		return pot;
	}
	
	@Override
	protected void addToBet(PlayerId playerId, int amount) {
		int toCall = getToCall(playerId);
		super.addToBet(playerId, amount);
		
		List<Integer> currentPile = betPiles.get(playerId);
		if (lastActed == null) {
			// This may be the case when the player just joined observing ...
			currentPile.add(amount);
		} else {
			List<Integer> lastPile = betPiles.get(lastActed);
			currentPile.clear();
			currentPile.addAll(lastPile);
			currentPile.add(amount - toCall);
			
			int totalAmount = 0;
			for (Integer i : currentPile) {
				totalAmount += i;
				
			}
			// use just one stack when less or equal than big blind (may be in
			// small blind ...)
			if (totalAmount <= tableConfiguration.getBigBlind()) {
				currentPile.clear();
				currentPile.add(totalAmount);
			}
		}
		lastActed = playerId;
	}
	
	/**
	 * @param newRoundEvent
	 * @see org.cspoker.client.common.SmartHoldemTableListener#onNewRound(org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent)
	 */
	@Override
	public void onNewRound(NewRoundEvent newRoundEvent) {
		super.onNewRound(newRoundEvent);
		for (List<Integer> bets : betPiles.values()) {
			bets.clear();
		}
	}
	
	@Override
	public void onNewDeal(NewDealEvent newDealEvent) {
		super.onNewDeal(newDealEvent);
		lastActed = getDealer();
		betPiles.clear();
		List<SeatedPlayer> seatedPlayers = newDealEvent.getPlayers();
		for (SeatedPlayer seatedPlayer : seatedPlayers) {
			betPiles.put(seatedPlayer.getId(), new ArrayList<Integer>());
		}
	}
	
	public List<NavigableMap<Chip, Integer>> getBetChipStacks() {
		return null;
	}
	
	public TableId getTableId() {
		return tableId;
	}
	
	public String getTableName() {
		return tableName;
	}
}
