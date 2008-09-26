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
import java.util.concurrent.ConcurrentHashMap;

import org.cspoker.common.api.lobby.DelegatingLobbyContext;
import org.cspoker.common.api.lobby.LobbyContext;
import org.cspoker.common.api.lobby.event.RemoteLobbyListener;
import org.cspoker.common.api.lobby.holdemtable.HoldemTableContext;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.server.rmi.asynchronous.listener.AsynchronousLobbyListener;

public class ExportingLobbyContext extends DelegatingLobbyContext {

	protected ConcurrentHashMap<RemoteLobbyListener, AsynchronousLobbyListener> wrappedListeners = new ConcurrentHashMap<RemoteLobbyListener, AsynchronousLobbyListener>();
	private Killable connection;

	protected ConcurrentHashMap<Long, HoldemTableContext> wrappedContexts = new ConcurrentHashMap<Long, HoldemTableContext>();


	public ExportingLobbyContext(Killable connection, LobbyContext lobbyContext) {
		super(lobbyContext);
		this.connection = connection;
	}
	
	@Override
	public HoldemTableContext getHoldemTableContext(long tableId) {
		HoldemTableContext context;
		if((context = wrappedContexts.get(tableId))==null){
			try {
				context = (HoldemTableContext)UnicastRemoteObject.exportObject(new ExportingHoldemTableContext(connection, super.getHoldemTableContext(tableId)),0);
				if(wrappedContexts.putIfAbsent(tableId, context)==null){
					return context;
				}else{
					return wrappedContexts.get(tableId);
				}			
			} catch (RemoteException exception) {
				connection.die();
				return null;
			}
		}else{
			return context;
		}
	}

}
