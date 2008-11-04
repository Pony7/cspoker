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

package org.cspoker.server.common.gamecontrol;

import java.util.List;

import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.server.common.HoldemPlayerContextImpl;
import org.cspoker.server.common.elements.table.PlayerListFullException;
import org.cspoker.server.common.elements.table.SeatTakenException;
import org.cspoker.server.common.elements.table.ServerTable;

/**
 * A class to represent players at the table.
 * 
 * 
 * @invar A table must have a valid game property. |
 *        canHaveAsGameProperty(getGameProperty())
 * @invar Each player at the table is unique.
 * 
 */
public class WaitingTableState
		extends TableState {
	
	private ServerTable serverTable;
	
	public WaitingTableState(PokerTable table) {
		super(table);
		serverTable = new ServerTable(table.getTableConfiguration().getMaxNbPlayers());
	}
	
	/**
	 * Check whether players are playing or not at this table.
	 * 
	 * @return True if players are playing at this table, False otherwise.
	 */
	@Override
	public boolean isPlaying() {
		return false;
	}
	
	/**
	 * Returns the list with all the players at this table.
	 * 
	 * The returned list is unmodifiable.
	 * 
	 * @return The list with all the players at this table.
	 */
	@Override
	public List<MutableSeatedPlayer> getSeatedServerPlayers() {
		return serverTable.getSeatedServerPlayers();
	}
	
	@Override
	public List<SeatedPlayer> getSeatedPlayers() {
		return serverTable.getSeatedPlayers();
	}
	
	@Override
	public void allIn(MutableSeatedPlayer player)
			throws IllegalActionException {
		throw new IllegalActionException("Going all-in is not a valid action.");
	}
	
	@Override
	public void bet(MutableSeatedPlayer player, int amount)
			throws IllegalActionException {
		throw new IllegalActionException("Bet is not a valid action.");
	}
	
	@Override
	public void call(MutableSeatedPlayer player)
			throws IllegalActionException {
		throw new IllegalActionException("Call is not a valid action.");
	}
	
	@Override
	public void check(MutableSeatedPlayer player)
			throws IllegalActionException {
		throw new IllegalActionException("Check is not a valid action.");
	}
	
	@Override
	public void deal(MutableSeatedPlayer player)
			throws IllegalActionException {
		throw new IllegalActionException("Deal is not a valid action.");
	}
	
	@Override
	public void fold(MutableSeatedPlayer player)
			throws IllegalActionException {
		throw new IllegalActionException("Fold is not a valid action.");
		
	}
	
	@Override
	public void raise(MutableSeatedPlayer player, int amount)
			throws IllegalActionException {
		throw new IllegalActionException("Raise is not a valid action.");
	}
	
	@Override
	public HoldemPlayerContext sitIn(SeatId seatId, MutableSeatedPlayer player)
			throws IllegalActionException {
		
		try {
			serverTable.addPlayer(seatId, player);
		} catch (SeatTakenException e) {
			throw new IllegalActionException("Joining table " + mediatingTable.getTableId().toString()
					+ " is not a valid action." + e.getMessage());
		} catch (IllegalActionException e) {
			throw new IllegalActionException("Joining table " + mediatingTable.getTableId().toString()
					+ " is not a valid action." + e.getMessage());
		}
		
		mediatingTable.publishSitInEvent(new SitInEvent(player.getMemento()));
		return new HoldemPlayerContextImpl(player, mediatingTable);
		
	}

	@Override
	public HoldemPlayerContext sitIn(MutableSeatedPlayer player)
			throws IllegalActionException {
		try {
			serverTable.addPlayer(player);
		} catch (PlayerListFullException e) {
			throw new IllegalActionException("Joining table " + mediatingTable.getTableId().toString()
					+ " failed: " + e.getMessage());
		}
		
		mediatingTable.publishSitInEvent(new SitInEvent(player.getMemento()));
		return new HoldemPlayerContextImpl(player, mediatingTable);
	}
	
	@Override
	public void sitOut(MutableSeatedPlayer player) {
		serverTable.removePlayer(player);
		mediatingTable.publishSitOutEvent(new SitOutEvent(player.getId(), false));
	}
	
	@Override
	public PlayingTableState getNextState() {
		return new PlayingTableState(mediatingTable, serverTable);
	}
}
