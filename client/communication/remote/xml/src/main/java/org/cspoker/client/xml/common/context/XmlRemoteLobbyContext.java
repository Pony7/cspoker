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
package org.cspoker.client.xml.common.context;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;

import org.cspoker.client.xml.common.IDGenerator;
import org.cspoker.client.xml.common.listener.XmlServerListenerTree;
import org.cspoker.common.api.lobby.action.CreateHoldemTableAction;
import org.cspoker.common.api.lobby.action.GetHoldemTableInformationAction;
import org.cspoker.common.api.lobby.action.GetTableListAction;
import org.cspoker.common.api.lobby.action.JoinHoldemTableAction;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.action.ActionPerformer;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.elements.table.TableList;

@ThreadSafe
public class XmlRemoteLobbyContext implements RemoteLobbyContext{

	private ActionPerformer performer;
	private IDGenerator generator;
	private ConcurrentHashMap<TableId, RemoteHoldemTableContext> contexts = new ConcurrentHashMap<TableId, RemoteHoldemTableContext>();
	private XmlServerListenerTree serverListenerTree;

	public XmlRemoteLobbyContext(ActionPerformer performer, IDGenerator generator, XmlServerListenerTree serverListenerTree) {
		this.performer = performer;
		this.generator = generator;
		this.serverListenerTree = serverListenerTree;
	}
	
	public DetailedHoldemTable createHoldemTable(String name,
			TableConfiguration configuration) throws RemoteException, IllegalActionException {
		return performer.perform(new CreateHoldemTableAction(generator.getNextID(),name,configuration));
	}

	public DetailedHoldemTable getHoldemTableInformation(TableId tableId)
			throws RemoteException, IllegalActionException {
		return performer.perform(new GetHoldemTableInformationAction(generator.getNextID(), tableId));
	}

	public TableList getTableList() throws RemoteException, IllegalActionException {
		return performer.perform(new GetTableListAction(generator.getNextID()));
	}

	public RemoteHoldemTableContext joinHoldemTable(TableId tableID,
			HoldemTableListener holdemTableListener) throws RemoteException,
			IllegalActionException {
		XmlRemoteHoldemTableContext context = new XmlRemoteHoldemTableContext(performer,generator,tableID, serverListenerTree);
		if(contexts.putIfAbsent(tableID, context)==null){
			serverListenerTree.getLobbyListenerTree().getHoldemTableListenerTree(tableID).setHoldemTableListener(holdemTableListener);
			performer.perform(new JoinHoldemTableAction(generator.getNextID(),tableID));
			return context;
		}else{
			throw new IllegalActionException("Already joined table #"+tableID+".");
		}
	}

}
