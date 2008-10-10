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
package org.cspoker.server.common.gamecontrol;

import java.util.List;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.server.common.elements.id.SeatId;
import org.cspoker.server.common.player.GameSeatedPlayer;

public abstract class TableState {
	
	protected final PokerTable mediatingTable;

	public TableState(PokerTable table){
		this.mediatingTable = table;
	}
	
	public abstract void bet(GameSeatedPlayer player, int amount) throws IllegalActionException;

	public abstract void call(GameSeatedPlayer player)
			throws IllegalActionException;

	public abstract void check(GameSeatedPlayer player)
			throws IllegalActionException;

	public abstract void raise(GameSeatedPlayer player, int amount)
			throws IllegalActionException;

	public abstract void fold(GameSeatedPlayer player)
			throws IllegalActionException;

	public abstract void deal(GameSeatedPlayer player)
			throws IllegalActionException;

	public abstract void allIn(GameSeatedPlayer player)
			throws IllegalActionException;

	public abstract HoldemPlayerContext sitIn(SeatId seatId, GameSeatedPlayer player)
			throws IllegalActionException;

	public abstract void sitOut(GameSeatedPlayer player)
			throws IllegalActionException;

	
	public abstract List<SeatedPlayer> getSeatedPlayers();
	
	public abstract List<GameSeatedPlayer> getSeatedServerPlayers();

	/**
	 * Check whether players are playing or not at this table.
	 * 
	 * @return True if players are playing at this table, False otherwise.
	 */
	public abstract boolean isPlaying();

	public Game getGame() {
		// TODO Auto-generated method stub
		return null;
	}
}
