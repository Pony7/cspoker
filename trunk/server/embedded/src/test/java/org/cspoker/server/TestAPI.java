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
package org.cspoker.server;

import javax.security.auth.login.LoginException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.util.Log4JPropertiesLoader;

public class TestAPI extends TestCase {
	
	static {
		Log4JPropertiesLoader
				.load("org/cspoker/server/logging/log4j.properties");
	}
	
	private ServerContext kenzo;
	
	private static Logger logger = Logger.getLogger(TestAPI.class);

	public void testCreateTable(){
		try {
			kenzo = new ServerContextImpl("kenzo", "test");
			LobbyListener listener = new LobbyListener(){

				public void onTableCreated(TableCreatedEvent tableCreatedEvent) {
					TestAPI.logger.info(tableCreatedEvent);
				}
				public void onTableRemoved(TableRemovedEvent tableRemovedEvent) {
					TestAPI.logger.info(tableRemovedEvent);
				}
				
			};
			LobbyContext lobbyContext = kenzo.getLobbyContext(listener);
			lobbyContext.createHoldemTable("test", new TableConfiguration());
		} catch (LoginException e) {
			fail(e.getMessage());
		}
	}
	
}
