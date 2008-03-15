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
package org.cspoker.server.rmi;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.util.DefaultRemotePlayerCommunication;

public class AsynchonousPlayerCommunication extends
		DefaultRemotePlayerCommunication {

	private String name;
	private Map<RemoteAllEventsListener, AsynchronousListener> listeners = new ConcurrentHashMap<RemoteAllEventsListener, AsynchronousListener>();

	public AsynchonousPlayerCommunication(RemotePlayerCommunication pc,
			String name) {
		super(pc);
		this.name = name;
	}

	public void subscribeAllEventsListener(RemoteAllEventsListener listener)
			throws RemoteException {
		AsynchronousListener wrapped = new AsynchronousListener(listener, name);
		listeners.put(listener, wrapped);
		super.subscribeAllEventsListener(wrapped);
	}

	public void unsubscribeAllEventsListener(RemoteAllEventsListener listener)
			throws RemoteException {
		AsynchronousListener old = listeners.remove(listener);
		if (old != null) {
			super.unsubscribeAllEventsListener(old);
		}
	}

}
