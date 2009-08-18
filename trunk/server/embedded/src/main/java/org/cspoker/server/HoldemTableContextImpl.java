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
package org.cspoker.server;

import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.MutablePlayer;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.server.gamecontrol.PokerTable;

public class HoldemTableContextImpl
		implements HoldemTableContext {
	
	private MutablePlayer player;
	
	private PokerTable table;
	
	public HoldemTableContextImpl(MutablePlayer player, PokerTable table) {
		this.player = player;
		this.table = table;
	}
	
	public void leaveTable() {
		table.leaveTable(player);
	}

	public HoldemPlayerContext sitIn(SeatId seatId, int buyIn, 
			HoldemPlayerListener holdemPlayerListener) throws IllegalActionException {
			return table.sitIn(seatId, buyIn, player, holdemPlayerListener);
	}
	
	public HoldemPlayerContext sitIn(int buyIn, 
			HoldemPlayerListener holdemPlayerListener) throws IllegalActionException {
			return table.sitIn(buyIn, player, holdemPlayerListener);
	}
}
