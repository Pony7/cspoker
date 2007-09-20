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

package org.cspoker.server.sockets;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.cspoker.server.game.utilities.Configuration;
import org.cspoker.server.sockets.runnables.WaitForIO;
import org.cspoker.server.sockets.threading.LoggingThreadPool;
import org.cspoker.server.sockets.threading.SocketRunnableComparator;

public class SocketServer 
{

    public static void main(String[] args) throws NumberFormatException, IOException {

    	PropertyConfigurator.configure(Configuration.LOG4J_PROPERTIES);

	if (args.length < 1) {
	    usage();
	}

	int port=0;
	try {
	    port=Integer.parseInt(args[0]);
	} catch (NumberFormatException e) {
	    usage();
	}

	SocketServer server = new SocketServer(port);
    }

    private static void usage() {
	logger.fatal("usage: java -jar cspoker-server-sockets.jar [portnumber]");
	System.exit(0);
    }

    private static Logger logger = Logger.getLogger(SocketServer.class);

    private final ServerSocketChannel server;

    private final Selector selector;

    private final ThreadPoolExecutor executor;

    public SocketServer(int port) throws IOException {

	// Create the server socket channel
	server = ServerSocketChannel.open();
	// nonblocking I/O
	server.configureBlocking(false);
	// host-port 8000
	server.socket().bind(new java.net.InetSocketAddress(port));
	logger.info("Server running on port "+port);
	// Create the selector
	selector = Selector.open();
	// Recording server to selector (type OP_ACCEPT)
	server.register(selector,SelectionKey.OP_ACCEPT);
	
	executor = new LoggingThreadPool(
		1,
		Runtime.getRuntime().availableProcessors()+1, 
		1, TimeUnit.SECONDS,
		new PriorityBlockingQueue<Runnable>(1000, new SocketRunnableComparator()),
		"TestServer"
	);
	executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

	// Infinite server loop
	executor.submit(new WaitForIO(executor, selector, server));
	
	
    }

    

}