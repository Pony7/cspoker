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

import org.cspoker.common.api.lobby.holdemtable.context.ExternalRemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.RemoteHoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.elements.table.TableList;

/**
 * A wrapper class that converts a LobbyContextConverter in to a ExternalRemoteLobbyContext.
 * These 2 classes could inherit if it wasn't for a bug in Sun JDK 6 that prohibits covariant inheritance.
 * This class is a workaround for the problem.
 * 
 * @see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6294779
 * 
 * @author guy
 *
 */
public class LobbyContextConverter implements ExternalRemoteLobbyContext {

	private ExternalLobbyContext lobbyContext;

	public LobbyContextConverter(ExternalLobbyContext lobbyContext) {
		this.lobbyContext=lobbyContext;
	}

	public ExternalRemoteHoldemTableContext joinHoldemTable(TableId tableId,
			HoldemTableListener holdemTableListener) throws RemoteException,
			IllegalActionException {
		return lobbyContext.joinHoldemTable(tableId, holdemTableListener);
	}

	public ExternalRemoteHoldemTableContext joinHoldemTable(TableId tableId,
			RemoteHoldemTableListener holdemTableListener)
			throws IllegalActionException, RemoteException {
		return lobbyContext.joinHoldemTable(tableId, holdemTableListener);
	}

	public DetailedHoldemTable createHoldemTable(String name,
			TableConfiguration configuration) throws RemoteException,
			IllegalActionException {
		return lobbyContext.createHoldemTable(name, configuration);
	}

	public DetailedHoldemTable getHoldemTableInformation(TableId tableId)
			throws RemoteException, IllegalActionException {
		return lobbyContext.getHoldemTableInformation(tableId);
	}

	public TableList getTableList() throws RemoteException,
			IllegalActionException {
		return lobbyContext.getTableList();
	}

}
