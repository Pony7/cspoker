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
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.client.common.RemotePlayerCommunicationFactory;
import org.cspoker.common.RemotePlayerCommunication;

public class RemotePlayerCommunicationFactoryForRMI implements
		RemotePlayerCommunicationFactory {

	private final static Logger logger = Logger
			.getLogger(RemotePlayerCommunicationFactoryForRMI.class);

	private final String server;
	private final int port;

	public RemotePlayerCommunicationFactoryForRMI(String server, int port) {
		this.server = server;
		this.port = port;
	}

	public RemotePlayerCommunication getRemotePlayerCommunication(
			String username, String password) throws ConnectException,
			LoginException {
		try {
			return new RemoteLoginServerForRMI(server, port).login(username,
					password);
		} catch (AccessException e) {
			logger.error(e);
			throw new ConnectException("Connect failed", e);
		} catch (RemoteException e) {
			logger.error(e);
			throw new ConnectException("Connect failed", e);
		} catch (NotBoundException e) {
			logger.error(e);
			throw new ConnectException("Connect failed", e);
		}

	}

	public String toString() {
		return "rmi://" + server + ":" + port;
	}

}
