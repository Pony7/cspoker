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

import java.rmi.RemoteException;
import java.rmi.server.Unreferenced;

import net.jcip.annotations.Immutable;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.SeatId;

@Immutable
public class ForwardingRemoteHoldemTableContext implements RemoteHoldemTableContext, Unreferenced{

	private final static Logger logger = Logger.getLogger(ForwardingRemoteHoldemTableContext.class);

	private final RemoteHoldemTableContext holdemTableContext;

	public ForwardingRemoteHoldemTableContext(RemoteHoldemTableContext holdemTableContext) {
		this.holdemTableContext  = holdemTableContext;
	}

	public void leaveTable() throws RemoteException, IllegalActionException {
		holdemTableContext.leaveTable();
	}

	public RemoteHoldemPlayerContext sitIn(SeatId seatId, int amount, HoldemPlayerListener holdemPlayerListener)
	throws RemoteException, IllegalActionException {
		return holdemTableContext.sitIn(seatId, amount, holdemPlayerListener);
	}

	public RemoteHoldemPlayerContext sitIn(int amount, HoldemPlayerListener holdemPlayerListener)
	throws RemoteException, IllegalActionException {
		return holdemTableContext.sitIn(amount, holdemPlayerListener);
	}


	@Override
	protected void finalize() throws Throwable {
		try {
			logger.debug("Garbage collecting old context: "+this);
		} finally{
			super.finalize();
		}
	}

	public void unreferenced() {
		logger.debug("No more clients referencing: "+this);
	}

}
