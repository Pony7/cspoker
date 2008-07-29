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
package org.cspoker.client.xml.sockets;

import java.rmi.ConnectException;
import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.client.common.RemotePlayerCommunicationFactory;
import org.cspoker.client.xml.common.ChannelStateException;
import org.cspoker.client.xml.common.XmlChannelRemotePlayerCommunication;

public class RemotePlayerCommunicationFactoryForSocket implements
		RemotePlayerCommunicationFactory {

	private final static Logger logger = Logger
			.getLogger(RemotePlayerCommunicationFactoryForSocket.class);

	private final String server;
	private final int port;

	public RemotePlayerCommunicationFactoryForSocket(String server, int port) {
		this.server = server;
		this.port = port;
	}

	public XmlChannelRemotePlayerCommunication getRemotePlayerCommunication(
			String username, String password) throws ConnectException,
			LoginException {
		try {
			XmlSocketsChannel c = new XmlSocketsChannel(server, port, username,
					password);
			c.open();
			return new XmlChannelRemotePlayerCommunication(c);
		} catch (RemoteException e) {
			logger.error(e);
			throw new ConnectException("Malformed URL", e);
		} catch (ChannelStateException e) {
			logger.error(e);
			throw new IllegalStateException(e);
		}
	}

	public String toString() {
		return "socket://" + server + ":" + port;
	}

}
