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
import org.cspoker.common.api.lobby.context.ExternalRemoteLobbyContext;
import org.cspoker.common.api.lobby.context.ForwardingExternalRemoteLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.ExternalHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.ExternalRemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.RemoteHoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;

public class ExportingLobbyContext extends ForwardingExternalRemoteLobbyContext {

	private final static Logger logger = Logger.getLogger(ExportingLobbyContext.class);

	public ExportingLobbyContext(ExternalRemoteLobbyContext lobbyContext) throws RemoteException {
		super(lobbyContext);
	}

	@Override
	public ExternalRemoteHoldemTableContext joinHoldemTable(long tableId,
			HoldemTableListener holdemTableListener) throws RemoteException, IllegalActionException {
		try {
			ExportingHoldemTableContext remoteObject = new ExportingHoldemTableContext(super.joinHoldemTable(tableId, holdemTableListener));
			return (ExternalHoldemTableContext)UnicastRemoteObject.exportObject(remoteObject,0);
		} catch (RemoteException exception) {
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
	}

	@Override
	public ExternalRemoteHoldemTableContext joinHoldemTable(long tableId,
			RemoteHoldemTableListener holdemTableListener)
	throws IllegalActionException, RemoteException {		
		try {
			ExportingHoldemTableContext remoteObject = new ExportingHoldemTableContext(super.joinHoldemTable(tableId, holdemTableListener));
			return (ExternalRemoteHoldemTableContext)UnicastRemoteObject.exportObject(remoteObject,0);
		} catch (RemoteException exception) {
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
	}

}
