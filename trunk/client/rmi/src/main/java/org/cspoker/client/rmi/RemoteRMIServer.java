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
package org.cspoker.client.rmi;

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.login.LoginException;

import org.cspoker.common.CSPokerServer;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.shared.context.ServerContext;

public class RemoteRMIServer implements RemoteCSPokerServer {

	private CSPokerServer server;

	public RemoteRMIServer(String server) throws AccessException,
			RemoteException, NotBoundException {
		this(server, 1099);
	}

	public RemoteRMIServer(String server, int port)
			throws AccessException, RemoteException, NotBoundException {
		System.setSecurityManager(null);
		Registry registry = LocateRegistry.getRegistry(server, port);
		this.server = (CSPokerServer) registry.lookup("CSPokerServer");
	}

	public ServerContext login(String username, String password)
			throws RemoteException, LoginException {

		final ServerContext p = server.login(username, password);

		return new DefaultS(p) {

			private Map<RemoteAllEventsListener, RemoteAllEventsListener> listeners = new ConcurrentHashMap<RemoteAllEventsListener, RemoteAllEventsListener>();

			public void subscribeAllEventsListener(
					RemoteAllEventsListener listener) throws RemoteException {
				RemoteAllEventsListener wrapped = new RemoteifyingListener(
						listener);
				try {
					UnicastRemoteObject.unexportObject(wrapped, true);
				} catch (NoSuchObjectException e) {
					// ignore
				}
				RemoteAllEventsListener listenerStub = (RemoteAllEventsListener) UnicastRemoteObject
						.exportObject(wrapped, 0);
				listeners.put(listener, listenerStub);
				p.subscribeAllEventsListener(listenerStub);
			}

			public void unsubscribeAllEventsListener(
					RemoteAllEventsListener listener) throws RemoteException {
				RemoteAllEventsListener old = listeners.remove(listener);
				if (old != null) {
					p.unsubscribeAllEventsListener(old);
					UnicastRemoteObject.unexportObject(old, true);
				}
			}
		};
	}

}
