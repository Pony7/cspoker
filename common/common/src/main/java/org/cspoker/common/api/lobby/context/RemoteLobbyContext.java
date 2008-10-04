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

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableList;

public interface RemoteLobbyContext extends Remote {

	//Actions
	
	RemoteHoldemTableContext joinHoldemTable(long tableId, HoldemTableListener holdemTableListener) throws RemoteException;

	DetailedHoldemTable createHoldemTable(String name, TableConfiguration configuration) throws RemoteException;

	DetailedHoldemTable getHoldemTableInformation(long tableId) throws RemoteException;

	TableList getTableList() throws RemoteException;	
}
