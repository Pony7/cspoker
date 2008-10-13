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
import java.rmi.RemoteException;

import org.cspoker.client.xml.common.RemoteXmlServer;
import org.cspoker.client.xml.common.XmlActionSerializer;

public class RemoteHTTPServer extends RemoteXmlServer {

	public RemoteHTTPServer(String server, int port) {
		super(server,port);
	}
	
	@Override
	protected XmlActionSerializer createXmlActionSerializer(String username,
			String password) throws RemoteException {
		try {
			return new XmlHttpSerializer(new URL("http://" + server
					+ ":" + port + "/cspoker/"), username, password);
		} catch (MalformedURLException exception) {
			throw new RemoteException("Bad URL", exception);
		}
	}
	
	public String toString() {
		return "http://" + server + ":" + port;
	}

}
