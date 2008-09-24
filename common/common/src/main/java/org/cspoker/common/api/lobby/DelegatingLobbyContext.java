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
package org.cspoker.common.api.lobby;

import org.cspoker.common.api.lobby.event.RemoteLobbyListener;
import org.cspoker.common.api.lobby.holdemtable.HoldemTableContext;
import org.cspoker.common.elements.table.DetailedTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableList;


public class DelegatingLobbyContext implements LobbyContext{

	private final LobbyContext lobbyContext;

	public DelegatingLobbyContext(LobbyContext accountContext) {
		this.lobbyContext  = accountContext;
	}

	public DetailedTable createTable(String name,
			TableConfiguration configuration) {
		return lobbyContext.createTable(name, configuration);
	}

	public HoldemTableContext getHoldemTableContext(long tableId) {
		return lobbyContext.getHoldemTableContext(tableId);
	}

	public DetailedTable getTableInformation(long tableId) {
		return lobbyContext.getTableInformation(tableId);
	}

	public TableList getTableList() {
		return lobbyContext.getTableList();
	}

	public DetailedTable joinTable(long tableId) {
		return lobbyContext.joinTable(tableId);
	}

	public void removeTable(long tableId) {
		lobbyContext.removeTable(tableId);
	}

	public void subscribe(RemoteLobbyListener lobbyListener) {
		lobbyContext.subscribe(lobbyListener);
	}

	public void unSubscribe(RemoteLobbyListener lobbyListener) {
		lobbyContext.unSubscribe(lobbyListener);
	}
	
}
