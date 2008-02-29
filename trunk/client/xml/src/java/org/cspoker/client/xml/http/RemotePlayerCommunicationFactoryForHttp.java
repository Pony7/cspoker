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
package org.cspoker.client.xml.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.ConnectException;
import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.client.common.RemotePlayerCommunicationFactory;
import org.cspoker.client.xml.common.ChannelStateException;
import org.cspoker.client.xml.common.XmlChannelRemotePlayerCommunication;

public class RemotePlayerCommunicationFactoryForHttp implements
		RemotePlayerCommunicationFactory {

	private final static Logger logger = Logger
			.getLogger(RemotePlayerCommunicationFactoryForHttp.class);

	
	private final String server;
	private final int port;

	public RemotePlayerCommunicationFactoryForHttp(String server, int port) {
		this.server = server;
		this.port = port;
	}
	
	@Override
	public XmlChannelRemotePlayerCommunication getRemotePlayerCommunication(
			String username, String password)
			throws ConnectException, LoginException {
		try {
			XmlHttpChannel c = new XmlHttpChannel(new URL("http://" + server
					+ ":" + port + "/cspoker/"), username, password);
			c.open();
			return new XmlChannelRemotePlayerCommunication(c);
		} catch (MalformedURLException e) {
			logger.error(e);
			throw new ConnectException("Malformed URL", e);
		} catch (RemoteException e) {
			logger.error(e);
			throw new ConnectException("Malformed URL", e);
		} catch (ChannelStateException e) {
			logger.error(e);
			throw new IllegalStateException(e);
		}
	}
	

	@Override
	public String toString() {
		return "http://"+server+":"+port;
	}

}
