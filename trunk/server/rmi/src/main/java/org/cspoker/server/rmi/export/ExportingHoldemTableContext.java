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

import org.cspoker.common.api.lobby.holdemtable.DelegatingHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.event.RemoteHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.HoldemPlayerContext;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.server.rmi.asynchronous.listener.AsynchronousHoldemTableListener;

public class ExportingHoldemTableContext extends DelegatingHoldemTableContext {

	protected ConcurrentHashMap<RemoteHoldemTableListener, AsynchronousHoldemTableListener> wrappers = 
		new ConcurrentHashMap<RemoteHoldemTableListener, AsynchronousHoldemTableListener>();
	protected Killable connection;
	protected HoldemPlayerContext holdemPlayerContext;
	
	public ExportingHoldemTableContext(Killable connection, HoldemTableContext holdemTableContext) {
		super(holdemTableContext);
		this.connection = connection;
		try {
			this.holdemPlayerContext = (HoldemPlayerContext)UnicastRemoteObject.exportObject(super.getHoldemPlayerContext(),0);
		} catch (RemoteException exception) {
			connection.die();
		}
	}
	
	@Override
	public HoldemPlayerContext getHoldemPlayerContext() {
		return holdemPlayerContext;
	}
	
}
