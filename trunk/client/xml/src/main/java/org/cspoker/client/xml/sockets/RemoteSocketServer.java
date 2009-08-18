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

import java.rmi.RemoteException;

import org.cspoker.client.xml.common.RemoteXmlServer;
import org.cspoker.client.xml.common.XmlActionSerializer;

public class RemoteSocketServer
		extends RemoteXmlServer {
	
	public RemoteSocketServer() {
		this("localhost");
	}
	
	public RemoteSocketServer(String server) {
		this(server, 8081);
	}
	
	public RemoteSocketServer(String server, int port) {
		super(server, port);
	}
	
	@Override
	public String toString() {
		return "socket://" + server + ":" + port;
	}
	
	@Override
	protected XmlActionSerializer createXmlActionSerializer(String username, String password)
			throws RemoteException {
		return new XmlSocketsChannel(server, port, username, password);
	}
	
}
