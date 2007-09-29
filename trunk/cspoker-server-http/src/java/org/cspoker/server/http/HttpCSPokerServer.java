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
package org.cspoker.server.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.http.authentication.XmlFileBasicAuthentication;
import org.cspoker.server.http.httphandler.AllInHandler;
import org.cspoker.server.http.httphandler.BetHandler;
import org.cspoker.server.http.httphandler.CallHandler;
import org.cspoker.server.http.httphandler.CheckHandler;
import org.cspoker.server.http.httphandler.CreateTableHandler;
import org.cspoker.server.http.httphandler.CrossDomain;
import org.cspoker.server.http.httphandler.DealHandler;
import org.cspoker.server.http.httphandler.FoldHandler;
import org.cspoker.server.http.httphandler.GameEventsAckHandler;
import org.cspoker.server.http.httphandler.GameEventsHandler;
import org.cspoker.server.http.httphandler.JoinTableHandler;
import org.cspoker.server.http.httphandler.LeaveTableHandler;
import org.cspoker.server.http.httphandler.ListTablesHandler;
import org.cspoker.server.http.httphandler.PingHandler;
import org.cspoker.server.http.httphandler.RaiseHandler;
import org.cspoker.server.http.httphandler.StartGameHandler;

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
	HttpContext pingContext = server.createContext("/ping/", new PingHandler());
	pingContext.setAuthenticator(authenticator);

	HttpContext listTableContext = server.createContext("/table/list/", new ListTablesHandler());
	listTableContext.setAuthenticator(authenticator);

	HttpContext createTableContext = server.createContext("/table/create/", new CreateTableHandler());
	createTableContext.setAuthenticator(authenticator);

	HttpContext joinTableContext = server.createContext("/table/join/", new JoinTableHandler());
	joinTableContext.setAuthenticator(authenticator);

	HttpContext leaveTableContext = server.createContext("/table/leave/", new LeaveTableHandler());
	leaveTableContext.setAuthenticator(authenticator);

	HttpContext gameEventsContext = server.createContext("/game/events/", new GameEventsHandler());
	gameEventsContext.setAuthenticator(authenticator);

	HttpContext gameEventsAckContext = server.createContext("/game/events/ack/", new GameEventsAckHandler());
	gameEventsAckContext.setAuthenticator(authenticator);

	HttpContext startGameContext = server.createContext("/game/start/", new StartGameHandler());
	startGameContext.setAuthenticator(authenticator);

	HttpContext dealContext = server.createContext("/game/deal/", new DealHandler());
	dealContext.setAuthenticator(authenticator);

	HttpContext callContext = server.createContext("/game/call/", new CallHandler());
	callContext.setAuthenticator(authenticator);

	HttpContext betContext = server.createContext("/game/bet/", new BetHandler());
	betContext.setAuthenticator(authenticator);

	HttpContext checkContext = server.createContext("/game/check/", new CheckHandler());
	checkContext.setAuthenticator(authenticator);

	HttpContext foldContext = server.createContext("/game/fold/", new FoldHandler());
	foldContext.setAuthenticator(authenticator);

	HttpContext raiseContext = server.createContext("/game/raise/", new RaiseHandler());
	raiseContext.setAuthenticator(authenticator);

	HttpContext allInContext = server.createContext("/game/allin/", new AllInHandler());
	allInContext.setAuthenticator(authenticator);

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
