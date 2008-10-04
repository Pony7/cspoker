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

import org.cspoker.common.api.lobby.context.ForwardingRemoteLobbyContext;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.common.util.lazy.IFactory;
import org.cspoker.common.util.lazy.LazyMap;

public class ExportingLobbyContext extends ForwardingRemoteLobbyContext {

	private final LazyMap<Long, RemoteHoldemTableContext, RemoteException> wrappers = new LazyMap<Long, RemoteHoldemTableContext, RemoteException>();
	
	public ExportingLobbyContext(RemoteLobbyContext lobbyContext) throws RemoteException {
		super(lobbyContext);
	}
	
	@Override
	public RemoteHoldemTableContext joinHoldemTable(final long tableId,
			final HoldemTableListener holdemTableListener) throws RemoteException {
		return wrappers.getOrCreate(tableId, new IFactory<RemoteHoldemTableContext, RemoteException>(){
			public RemoteHoldemTableContext create() throws RemoteException {
				ExportingHoldemTableContext remoteObject = new ExportingHoldemTableContext(ExportingLobbyContext.super.joinHoldemTable(tableId, holdemTableListener),new Killable(){
					public void kill() {
						wrappers.remove(tableId);
					}
				});
				return (HoldemTableContext)UnicastRemoteObject.exportObject(remoteObject,0);
			}
		});
	}

}
