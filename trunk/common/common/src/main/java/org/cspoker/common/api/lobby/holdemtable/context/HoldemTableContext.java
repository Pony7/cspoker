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

public interface HoldemTableContext extends RemoteHoldemTableContext{

	//Actions
	
	/**
	 * The player leaves the table.
	 * The player will no longer receive table events.
	 */
	void leaveTable();
	
	/**
	 * The player chooses to sit-in at the table. 
	 * A player who is sit-in can act at the table. He will also receive pocket cards.
	 */
	HoldemPlayerContext sitIn(SeatId seatId, int buyIn, HoldemPlayerListener holdemPlayerListener) throws IllegalActionException;
	

	HoldemPlayerContext sitIn(int buyIn, HoldemPlayerListener holdemPlayerListener) throws IllegalActionException;
	
}
