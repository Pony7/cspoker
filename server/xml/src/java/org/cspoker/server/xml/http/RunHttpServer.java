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

import org.apache.log4j.Logger;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.server.common.game.session.PlayerKilledExcepion;
import org.cspoker.server.common.game.session.Session;
import org.cspoker.server.common.game.session.SessionManager;

public class RunHttpServer {

	static {
		Log4JPropertiesLoader
				.load("org/cspoker/server/xml/http/logging/log4j.properties");
	}
	private final static Logger logger = Logger.getLogger(RunHttpServer.class);

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		if (args.length < 1) {
			usage();
		}

		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			usage();
		}
		logger.info("Starting HTTP server at port " + port);
		(new HttpServer(port)).start();
		
		try{
		
		for(int i = 0; i < 7; i++) {
            Session session =
            	SessionManager.global_session_manager.getSession("robot-master" + i);
            
            Table table = session.getPlayerCommunication().createTable("J1 Robots " + i);
            for(int j = 1; j < 7; j++) {
                 Session botSession = SessionManager.global_session_manager.getSession("robot " + i + j);
                 botSession.getPlayerCommunication().joinTable(table.getId());
            }
            session.getPlayerCommunication().startGame();
		}
		
		} catch (IllegalActionException e) {
			e.printStackTrace();
		} catch (PlayerKilledExcepion e) {
			e.printStackTrace();
		}
		
         System.out.println("Demo started");
	}

	private static void usage() {
		logger.fatal("usage: java -jar cspoker-server.jar [portnumber]");
		System.exit(0);
	}
}
