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
package org.cspoker.client.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

import org.cspoker.common.api.lobby.context.ExternalRemoteLobbyContext;
import org.cspoker.common.api.lobby.context.ForwardingExternalRemoteLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.ExternalRemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.listener.ForwardingRemoteHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.RemoteHoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.util.lazy.IWrapper1;

public class LobbyContextStub extends ForwardingExternalRemoteLobbyContext{

	protected ConcurrentHashMap<Long, IWrapper1<RemoteHoldemTableContext,RemoteException>> wrappedContexts = new ConcurrentHashMap<Long, IWrapper1<RemoteHoldemTableContext,RemoteException>>();

	public LobbyContextStub(ExternalRemoteLobbyContext context)
	throws RemoteException {
		super(context);
	}

	@Override
	public ExternalRemoteHoldemTableContext joinHoldemTable(long tableId,
			HoldemTableListener holdemTableListener) throws RemoteException, IllegalActionException {
		RemoteHoldemTableListener stub = (RemoteHoldemTableListener) UnicastRemoteObject.exportObject(
				new ForwardingRemoteHoldemTableListener(holdemTableListener), 0);
		ExternalRemoteHoldemTableContext tableContext = super.joinHoldemTable(tableId,stub);
		return new HoldemTableContextStub(tableContext);
	}

	@Override
	public ExternalRemoteHoldemTableContext joinHoldemTable(long tableId,
			RemoteHoldemTableListener holdemTableListener)
	throws IllegalActionException, RemoteException {
		RemoteHoldemTableListener stub = (RemoteHoldemTableListener) UnicastRemoteObject.exportObject(
				new ForwardingRemoteHoldemTableListener(holdemTableListener), 0);
		ExternalRemoteHoldemTableContext tableContext = super.joinHoldemTable(tableId,stub);
		return new HoldemTableContextStub(tableContext);
	}
}
