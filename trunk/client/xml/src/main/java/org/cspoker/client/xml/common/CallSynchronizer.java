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
package org.cspoker.client.xml.common;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import org.cspoker.common.api.shared.action.ActionPerformer;
import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.event.ActionEvent;
import org.cspoker.common.api.shared.event.ServerEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.api.shared.listener.ActionAndServerEventListener;
import org.cspoker.common.api.shared.listener.ServerListenerTree;

public class CallSynchronizer implements ActionPerformer, ActionAndServerEventListener {

	private ConcurrentHashMap<Long, Semaphore> semaphores = new ConcurrentHashMap<Long, Semaphore>();
	private ConcurrentHashMap<Long, ActionEvent<?>> actionEvents = new ConcurrentHashMap<Long, ActionEvent<?>>();
	private XmlActionSerializer serializer;
	private ServerListenerTree listenerTree;
	
	
	public CallSynchronizer(ServerListenerTree listenerTree, XmlActionSerializer serializer) {
		this.serializer = serializer;
		this.listenerTree = listenerTree;
		this.serializer.setEventListener(this);
	}
	
	public <T> T perform(DispatchableAction<T> action) throws IllegalActionException, RemoteException{
		semaphores.putIfAbsent(action.getID(), new Semaphore(0));
		Semaphore semaphore = semaphores.get(action.getID());
		serializer.perform(action);
		try {
			semaphore.acquire();
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
		}
		ActionEvent<T> actionEvent = cast(action);
		return actionEvent.getResult();
	}

	@SuppressWarnings("unchecked")
	private <T> ActionEvent<T> cast(DispatchableAction<T> action) {
		return (ActionEvent<T>) actionEvents.remove(action.getID());
	}

	public void onActionPerformed(ActionEvent<?> event) {
		actionEvents.putIfAbsent(event.getID(), event);
		Semaphore semaphore = semaphores.remove(event.getID());
		semaphore.release();
	}

	public void onServerEvent(ServerEvent event) {
		event.dispatch(listenerTree);
	}
	
}
