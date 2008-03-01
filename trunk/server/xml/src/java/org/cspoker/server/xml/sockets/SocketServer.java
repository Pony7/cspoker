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

package org.cspoker.server.xml.sockets;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.rmi.RemoteException;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.common.util.threading.RequestExecutor;
import org.cspoker.server.xml.sockets.runnables.WaitForIO;

public class SocketServer {

	private final static Logger logger = Logger.getLogger(SocketServer.class);

	private final ServerSocketChannel server;

	private final Selector selector;

	private Executor executor;

	private final int port;

	private final XmlFileAuthenticator auth;

	public SocketServer(int port) throws RemoteException {
		this(port, new XmlFileAuthenticator());
	}
	
	public SocketServer(int port, XmlFileAuthenticator auth) throws RemoteException {
		try {
			this.auth = auth;
			this.port = port;
			// Create the server socket channel
			server = ServerSocketChannel.open();
			// nonblocking I/O
			server.configureBlocking(false);
			// host-port 8000
			server.socket().bind(new java.net.InetSocketAddress(port));
			// Create the selector
			selector = Selector.open();
			// Recording server to selector (type OP_ACCEPT)
			server.register(selector, SelectionKey.OP_ACCEPT);
		}  catch (IOException e) {
			throw new RemoteException("Creating Socket server failed",e);
		}
	}
	 
	public void start(){
		executor = RequestExecutor.getInstance();
		// Infinite server loop
		executor.execute(new WaitForIO(executor, selector, server, auth));
		logger.info("Socket server running on port " + port);
	}

}