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

import javax.security.auth.login.LoginException;

import org.cspoker.client.common.RemotePlayerCommunicationFactory;
import org.cspoker.client.xml.common.XmlChannelRemotePlayerCommunication;
import org.cspoker.common.RemotePlayerCommunication;


public class RemotePlayerCommunicationFactoryForHttp implements RemotePlayerCommunicationFactory{

	@Override
	public RemotePlayerCommunication getRemotePlayerCommunication(
			String server, int port, String username, String password) throws ConnectException, LoginException{
		try {
			XmlHttpChannel c = new XmlHttpChannel(new URL("http://"+server+":"+port),username, password);
			c.open();
			return new XmlChannelRemotePlayerCommunication(c);
		} catch (MalformedURLException e) {
			throw new ConnectException("Malformed URL",e);
		}
	}


}
