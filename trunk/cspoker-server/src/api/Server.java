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
package api;

import java.io.IOException;
import java.net.InetSocketAddress;

import api.httphandler.CreateTableHandler;
import api.httphandler.ListTablesHandler;
import api.httphandler.PingHandler;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

/**
 * Creates a new web server and starts it.
 */
public class Server {

    public static void main(String[] args) throws NumberFormatException,
	    IOException {
	
	if (args.length != 1) {
	    System.out.println("usage: java -jar cspoker-server.jar [portnumber]");
	    System.exit(0);
	}
	
	int port=0;
	try {
	    port=Integer.parseInt(args[0]);
	} catch (NumberFormatException e) {
	    System.out.println("usage: java -jar cspoker-server.jar [portnumber]");
	    System.exit(0);
	}
	
	Server server = new Server(port);
	server.start();
    }

    /**
         * Variable holding the server object.
         */
    private HttpServer server;

    /**
         * Creates a new server at the given port.
         * 
         * @param port
         *                The port to listen at.
         * @throws IOException
         */
    public Server(int port) throws IOException {
	server = HttpServer.create(new InetSocketAddress(port), 0);
	
	HttpContext pingContext = server.createContext("/ping/", new PingHandler());
	pingContext.setAuthenticator(new HardCodedBasicAuthentication());
	
	HttpContext listTableContext = server.createContext("/table/list/", new ListTablesHandler());
	listTableContext.setAuthenticator(new HardCodedBasicAuthentication());
	
	HttpContext createTableContext = server.createContext("/table/create/", new CreateTableHandler());
	createTableContext.setAuthenticator(new HardCodedBasicAuthentication());
	
	
	System.out.println("Server created for port " + port + ".");
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
