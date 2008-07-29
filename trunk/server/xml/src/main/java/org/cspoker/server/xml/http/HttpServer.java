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
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.common.util.threading.RequestExecutor;
import org.cspoker.server.xml.http.authentication.XmlFileBasicAuthentication;
import org.cspoker.server.xml.http.handler.CSPokerHandler;
import org.cspoker.server.xml.http.handler.CrossDomain;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpContext;

/**
 * Creates a new web server and starts it.
 */
public class HttpServer {
	private static Logger logger = Logger.getLogger(HttpServer.class);

	private Authenticator authenticator;

	/**
	 * Variable holding the server object.
	 */
	private com.sun.net.httpserver.HttpServer server;

	private int port;

	/**
	 * Creates a new server at the given port.
	 * 
	 * @param port
	 *            The port to listen at.
	 * @param authenticationFile
	 * @throws IOException
	 * @throws IOException
	 */

	public HttpServer(int port) throws RemoteException {
		this(port, new XmlFileAuthenticator());
	}

	public HttpServer(int port, XmlFileAuthenticator auth)
			throws RemoteException {
		this.port = port;
		try {
			server = com.sun.net.httpserver.HttpServer.create(
					new InetSocketAddress(port), 0);
		} catch (IOException e) {
			throw new RemoteException("Creating HTTP server failed", e);
		}

		authenticator = new XmlFileBasicAuthentication(auth);

		loadContext();
	}

	protected void loadContext() {
		HttpContext mainContext = server.createContext("/cspoker/",
				new CSPokerHandler());
		mainContext.setAuthenticator(authenticator);

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
