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
package org.cspoker.common.api.lobby.context;

import java.rmi.RemoteException;

import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.listener.ForwardingLobbyListener;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.elements.table.DetailedTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableList;

public class ForwardingRemoteLobbyContext implements RemoteLobbyContext{

	protected final RemoteLobbyContext lobbyContext;
	private ForwardingLobbyListener forwardingLobbyListener;

	public ForwardingRemoteLobbyContext(RemoteLobbyContext lobbyContext) throws RemoteException {
		this.lobbyContext  = lobbyContext;
		this.forwardingLobbyListener = new ForwardingLobbyListener();
		lobbyContext.subscribe(wrapListener(forwardingLobbyListener));
	}
	
	public LobbyListener wrapListener(LobbyListener listener) throws RemoteException{
		return listener;
	}

	public DetailedTable createTable(String name,
			TableConfiguration configuration) throws RemoteException {
		return lobbyContext.createTable(name, configuration);
	}

	public RemoteHoldemTableContext getHoldemTableContext(long tableId) throws RemoteException {
		return lobbyContext.getHoldemTableContext(tableId);
	}

	public DetailedTable getTableInformation(long tableId) throws RemoteException {
		return lobbyContext.getTableInformation(tableId);
	}

	public TableList getTableList() throws RemoteException {
		return lobbyContext.getTableList();
	}

	public DetailedTable joinTable(long tableId) throws RemoteException {
		return lobbyContext.joinTable(tableId);
	}

	public void removeTable(long tableId) throws RemoteException {
		lobbyContext.removeTable(tableId);
	}

	public void subscribe(LobbyListener lobbyListener) {
		forwardingLobbyListener.subscribe(lobbyListener);
	}

	public void unSubscribe(LobbyListener lobbyListener) {
		forwardingLobbyListener.unSubscribe(lobbyListener);
	}
	
}
