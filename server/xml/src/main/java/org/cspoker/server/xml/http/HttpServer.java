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
package org.cspoker.server.xml.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.util.threading.RequestExecutor;
import org.cspoker.server.xml.http.handler.CSPokerHandler;
import org.cspoker.server.xml.http.handler.CrossDomain;

/**
 * Creates a new web server and starts it.
 */
public class HttpServer {
	private static Logger logger = Logger.getLogger(HttpServer.class);

	/**
	 * Variable holding the server object.
	 */
	private com.sun.net.httpserver.HttpServer server;

	private int port;

	public HttpServer(int port, CSPokerServer cspokerServer)
			throws RemoteException {
		this.port = port;
		try {
			server = com.sun.net.httpserver.HttpServer.create(
					new InetSocketAddress(port), 0);
		} catch (IOException e) {
			throw new RemoteException("Creating HTTP server failed", e);
		}

		loadContext(cspokerServer);
	}

	protected void loadContext(CSPokerServer cspokerServer) {
		final CSPokerHandler handler = new CSPokerHandler(cspokerServer);
		server.createContext("/cspoker/",handler);
		server.setExecutor(RequestExecutor.getInstance());
		server.createContext("/", new CrossDomain());

	}

	/**
	 * Starts this server.
	 */
	public void start() {
		server.start();
		logger.info("Started HTTP server at port " + port);
	}

}
