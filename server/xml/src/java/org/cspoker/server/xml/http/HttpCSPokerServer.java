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

import org.apache.log4j.Logger;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.common.util.threading.RequestExecutor;
import org.cspoker.server.xml.http.authentication.XmlFileBasicAuthentication;
import org.cspoker.server.xml.http.handler.CSPokerHandler;
import org.cspoker.server.xml.http.handler.CrossDomain;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

/**
 * Creates a new web server and starts it.
 */
public class HttpCSPokerServer {
    private static Logger logger = Logger.getLogger(HttpCSPokerServer.class);

    private Authenticator authenticator;

    /**
     * Variable holding the server object.
     */
    private HttpServer server;

    /**
     * Creates a new server at the given port.
     * 
     * @param port
     *        The port to listen at.
     * @param authenticationFile 
     * @throws IOException 
     * @throws IOException
     */

    public HttpCSPokerServer(int port) throws IOException {
	this(port, new XmlFileAuthenticator());
    }
    public HttpCSPokerServer(int port, XmlFileAuthenticator auth) throws IOException {
	server = HttpServer.create(new InetSocketAddress(port), 0);

	authenticator = new XmlFileBasicAuthentication(auth);

	loadContext();

	HttpCSPokerServer.logger.info("Server created for port " + port + ".");
    }

    protected void loadContext(){
	HttpContext mainContext = server.createContext("/cspoker/", new CSPokerHandler());
	mainContext.setAuthenticator(authenticator);

	server.setExecutor(RequestExecutor.getInstance());
	server.createContext("/", new CrossDomain());

    }

    /**
     * Starts this server.
     * 
     * @throws IOException
     *                 There was a problem opening this server's port.
     */
    public void start() throws IOException {
	server.start();
    }

}
