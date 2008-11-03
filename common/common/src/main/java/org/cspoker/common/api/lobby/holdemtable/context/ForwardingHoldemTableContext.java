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
package org.cspoker.common.api.lobby.holdemtable.context;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.SeatId;

public class ForwardingHoldemTableContext implements HoldemTableContext{

	private HoldemTableContext holdemTableContext;

	public ForwardingHoldemTableContext(HoldemTableContext holdemTableContext) {
		this.holdemTableContext  = holdemTableContext;
	}

	public void leaveTable() {
		holdemTableContext.leaveTable();
	}

	public HoldemPlayerContext sitIn(SeatId seatId, int buyIn,
			HoldemPlayerListener holdemPlayerListener)
			throws IllegalActionException {
		return holdemTableContext.sitIn(seatId, buyIn, holdemPlayerListener);
	}

	public HoldemPlayerContext sitIn(int buyIn,
			HoldemPlayerListener holdemPlayerListener)
			throws IllegalActionException {
		return holdemTableContext.sitIn(buyIn, holdemPlayerListener);
	}
	
}
