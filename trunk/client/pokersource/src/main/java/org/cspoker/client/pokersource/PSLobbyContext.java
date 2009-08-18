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
package org.cspoker.client.pokersource;

import java.rmi.RemoteException;

import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.elements.table.TableList;
import org.cspoker.external.pokersource.PokersourceConnection;

public class PSLobbyContext implements RemoteLobbyContext {

	private final PokersourceConnection conn;
	private final int serial;
	private final LobbyListener lobbyListener;

	public PSLobbyContext(PokersourceConnection conn, int serial, LobbyListener lobbyListener) {
		this.conn = conn;
		this.serial = serial;
		this.lobbyListener = lobbyListener;
	}

	@Override
	public DetailedHoldemTable createHoldemTable(String name,
			TableConfiguration configuration) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DetailedHoldemTable getHoldemTableInformation(TableId tableId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TableList getTableList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public RemoteHoldemTableContext joinHoldemTable(TableId tableId,
			HoldemTableListener holdemTableListener)
			throws IllegalActionException, RemoteException {
		return new PSTableContext(conn, serial, holdemTableListener);
	}

}
