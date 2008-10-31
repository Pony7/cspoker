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
package org.cspoker.server.rmi;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.server.common.CSPokerServerImpl;

public class RunRMIServer {

	static RMIServer server;

	static {
		Log4JPropertiesLoader
				.load("org/cspoker/server/rmi/logging/log4j.properties");
	}

	private final static Logger logger = Logger.getLogger(RunRMIServer.class);

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		if (args.length != 1) {
			usage();
		}
		int port = Integer.parseInt(args[0]);
		logger.info("Starting RMI server at port " + port);
		// need to do this in two steps to prevent GC!!
		server = new RMIServer(port, new CSPokerServerImpl());
		server.start();
	}

	private static void usage() {
		logger.fatal("usage: java -jar cspoker-server-rmi.jar [portnumber]");
		System.exit(0);
	}

}
