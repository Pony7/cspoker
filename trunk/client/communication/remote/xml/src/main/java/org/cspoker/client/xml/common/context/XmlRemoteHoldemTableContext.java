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
import java.util.concurrent.atomic.AtomicReference;

import net.jcip.annotations.ThreadSafe;

import org.cspoker.client.xml.common.IDGenerator;
import org.cspoker.client.xml.common.listener.XmlServerListenerTree;
import org.cspoker.common.api.lobby.holdemtable.action.LeaveTableAction;
import org.cspoker.common.api.lobby.holdemtable.action.SitInAction;
import org.cspoker.common.api.lobby.holdemtable.action.SitInAnywhereAction;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.action.ActionPerformer;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableId;

@ThreadSafe
public class XmlRemoteHoldemTableContext implements RemoteHoldemTableContext {

	private ActionPerformer performer;
	private IDGenerator generator;
	private TableId tableID;
	
	private final AtomicReference<RemoteHoldemPlayerContext> playerContext = new AtomicReference<RemoteHoldemPlayerContext>();
	private XmlServerListenerTree serverListenerTree;

	public XmlRemoteHoldemTableContext(ActionPerformer performer, IDGenerator generator, TableId tableID, XmlServerListenerTree serverListenerTree) {
		this.performer = performer;
		this.generator = generator;
		this.tableID = tableID;
		this.serverListenerTree = serverListenerTree;
	}
	
	public void leaveTable() throws RemoteException, IllegalActionException {
		performer.perform(new LeaveTableAction(generator.getNextID(),tableID));
	}

	public RemoteHoldemPlayerContext sitIn(SeatId seatId, int amount,
			HoldemPlayerListener holdemPlayerListener) throws RemoteException,
			IllegalActionException {
		if(playerContext.compareAndSet(null, new XmlRemoteHoldemPlayerContext(performer,generator,tableID))){
			serverListenerTree.getLobbyListenerTree().getHoldemTableListenerTree(tableID).setHoldemPlayerListener(holdemPlayerListener);
			performer.perform(new SitInAction(generator.getNextID(),tableID,seatId,amount));
			return playerContext.get();
		}else{
			throw new IllegalActionException("Already seated at table #"+tableID+".");
		}
	}

	public RemoteHoldemPlayerContext sitIn(int amount,
			HoldemPlayerListener holdemPlayerListener) throws RemoteException,
			IllegalActionException {
		if(playerContext.compareAndSet(null, new XmlRemoteHoldemPlayerContext(performer,generator,tableID))){
			serverListenerTree.getLobbyListenerTree().getHoldemTableListenerTree(tableID).setHoldemPlayerListener(holdemPlayerListener);
			performer.perform(new SitInAnywhereAction(generator.getNextID(),tableID,amount));
			return playerContext.get();
		}else{
			throw new IllegalActionException("Already seated at table #"+tableID+".");
		}
	}

}
