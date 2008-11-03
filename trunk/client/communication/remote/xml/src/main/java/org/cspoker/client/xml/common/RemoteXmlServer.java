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

import javax.security.auth.login.LoginException;

import org.cspoker.client.xml.common.context.XmlRemoteServerContext;
import org.cspoker.client.xml.common.listener.XmlServerListenerTree;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.event.EventId;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.api.shared.socket.LoginAction;

public abstract class RemoteXmlServer implements RemoteCSPokerServer {

	protected final String server;
	protected final int port;

	public RemoteXmlServer(String server, int port) {
		this.server = server;
		this.port = port;
	}

	public RemoteServerContext login(String username, String password)
			throws RemoteException, LoginException {
		XmlActionSerializer serializer = createXmlActionSerializer(username,password);
		XmlServerListenerTree listenerTree = new XmlServerListenerTree();
		CallSynchronizer callSynchronizer = new CallSynchronizer(listenerTree,serializer);
		try {
			callSynchronizer.perform(new LoginAction(new EventId(), username, password));
		} catch (IllegalActionException exception) {
			throw new LoginException(exception.getMessage());
		}
		return new XmlRemoteServerContext(callSynchronizer,listenerTree);
	}

	protected abstract XmlActionSerializer createXmlActionSerializer(String username, String password) throws RemoteException;
}
