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

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.security.auth.login.LoginException;

import org.cspoker.common.ExternalRemoteCSPokerServer;
import org.cspoker.common.api.shared.context.ExternalRemoteServerContext;

public class RemoteRMIServer implements ExternalRemoteCSPokerServer {

	private String server;
	private int port;

	public RemoteRMIServer(String server) throws RemoteException, NotBoundException {
		this(server, 1099);
	}

	public RemoteRMIServer(String server, int port) {
		this.server = server;
		this.port = port;
	}

	public ExternalRemoteServerContext login(String username, String password)
			throws RemoteException, LoginException {
		System.setSecurityManager(null);
		Registry registry = LocateRegistry.getRegistry(server, port);
		ExternalRemoteCSPokerServer cspokerServer;
		try {
			cspokerServer = (ExternalRemoteCSPokerServer) registry.lookup("CSPokerServer");
		} catch (NotBoundException exception) {
			throw new RemoteException("CSPokerServer not found in registry.",exception);
		}
		ExternalRemoteServerContext context = cspokerServer.login(username, password);
		return new ServerContextStub(context);
	}
	
	@Override
	public String toString() {
		return "rmi://"+server+":"+port;
	}

}
