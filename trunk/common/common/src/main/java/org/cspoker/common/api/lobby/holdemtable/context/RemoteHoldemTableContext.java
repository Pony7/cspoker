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

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableId;

public interface RemoteHoldemTableContext extends Remote {

	//Actions
	
	void leaveTable() throws RemoteException, IllegalActionException;

	/**
	 * Sit in at the current table at a given seat with a given buy in amount.
	 * 
	 * @param seatId
	 * @param amount
	 * @param holdemPlayerListener
	 * @return
	 * @throws RemoteException
	 * 		   A remote exception occurred in another virtual machine.
	 * @throws IllegalActionException
	 * 		   The player is already seated at the table.
	 */
	RemoteHoldemPlayerContext sitIn(SeatId seatId, int amount, HoldemPlayerListener holdemPlayerListener) throws RemoteException, IllegalActionException;
	
	/**
	 * Sit in at the current table at any free seat with a given buy in amount.
	 * 
	 * @param buyIn
	 * @param holdemPlayerListener
	 * @return 
	 * @throws IllegalActionException
	 */
	RemoteHoldemPlayerContext sitIn(int amount, HoldemPlayerListener holdemPlayerListener) throws RemoteException, IllegalActionException;
	
}
