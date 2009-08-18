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
package org.cspoker.server.rmi.export;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.context.ExternalRemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.ForwardingExternalRemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.ForwardingRemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.RemoteHoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.SeatId;

public class ExportingHoldemTableContext extends ForwardingExternalRemoteHoldemTableContext {

	private final static Logger logger = Logger.getLogger(ExportingHoldemTableContext.class);

	public ExportingHoldemTableContext(ExternalRemoteHoldemTableContext holdemTableContext) {
		super(holdemTableContext);
	}

	@Override
	public RemoteHoldemPlayerContext sitIn(SeatId seatId, int buyIn, HoldemPlayerListener holdemPlayerListener)
	throws RemoteException, IllegalActionException {
		try {
			RemoteHoldemPlayerContext wrappedObject = new ForwardingRemoteHoldemPlayerContext(super.sitIn(seatId, buyIn, holdemPlayerListener));
			return (RemoteHoldemPlayerContext) UnicastRemoteObject.exportObject(wrappedObject, 0);
		} catch (RemoteException exception) {
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
	}
	
	@Override
	public RemoteHoldemPlayerContext sitIn(SeatId seatId, int buyIn,
			RemoteHoldemPlayerListener holdemPlayerListener)
			throws IllegalActionException, RemoteException {
		try {
			RemoteHoldemPlayerContext wrappedObject = new ForwardingRemoteHoldemPlayerContext(super.sitIn(seatId, buyIn, holdemPlayerListener));
			return (RemoteHoldemPlayerContext) UnicastRemoteObject.exportObject(wrappedObject, 0);
		} catch (RemoteException exception) {
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
	}

	
	@Override
	public RemoteHoldemPlayerContext sitIn(int buyIn,
			RemoteHoldemPlayerListener holdemPlayerListener)
			throws IllegalActionException, RemoteException {
		try {
			RemoteHoldemPlayerContext wrappedObject = new ForwardingRemoteHoldemPlayerContext(super.sitIn(buyIn, holdemPlayerListener));
			return (RemoteHoldemPlayerContext) UnicastRemoteObject.exportObject(wrappedObject, 0);
		} catch (RemoteException exception) {
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
	}

}
