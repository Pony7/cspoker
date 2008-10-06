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

import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.listener.ForwardingLobbyListener;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableList;

public class ForwardingLobbyContext implements LobbyContext{

	protected final LobbyContext lobbyContext;

	public ForwardingLobbyContext(LobbyContext lobbyContext) {
		this.lobbyContext  = lobbyContext;
	}

	public DetailedHoldemTable createHoldemTable(String name,
			TableConfiguration configuration) {
		return lobbyContext.createHoldemTable(name, configuration);
	}

	public DetailedHoldemTable getHoldemTableInformation(long tableId) {
		return lobbyContext.getHoldemTableInformation(tableId);
	}

	public TableList getTableList() {
		return lobbyContext.getTableList();
	}

	public HoldemTableContext joinHoldemTable(long tableId,
			HoldemTableListener holdemTableListener) throws IllegalActionException {
		return lobbyContext.joinHoldemTable(tableId, holdemTableListener);
	}
}
