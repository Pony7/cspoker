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

import org.cspoker.common.api.lobby.context.ForwardingRemoteLobbyContext;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.util.lazy.IWrapper;

public class LobbyContextStub extends ForwardingRemoteLobbyContext{

	protected ConcurrentHashMap<Long, IWrapper<RemoteHoldemTableContext,RemoteException>> wrappedContexts = new ConcurrentHashMap<Long, IWrapper<RemoteHoldemTableContext,RemoteException>>();

	public LobbyContextStub(RemoteLobbyContext context)
			throws RemoteException {
		super(context);
	}
	
	@Override
	public LobbyListener wrapListener(LobbyListener listener) throws RemoteException {
		return (LobbyListener) UnicastRemoteObject.exportObject(listener, 0);
	}

	@Override
	public RemoteHoldemTableContext getHoldemTableContext(final long tableId) throws RemoteException {
		wrappedContexts.putIfAbsent(tableId, new IWrapper<RemoteHoldemTableContext,RemoteException>(){

			private RemoteHoldemTableContext content = null;
			
			public synchronized RemoteHoldemTableContext getContent() throws RemoteException {
				if(content == null){
					content = new HoldemTableContextStub(lobbyContext.getHoldemTableContext(tableId));
				}
				return content;
			}
		});
		return wrappedContexts.get(tableId).getContent();
	}
}
