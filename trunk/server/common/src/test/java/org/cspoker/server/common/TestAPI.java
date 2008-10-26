package org.cspoker.server.common;

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
				.load("org/cspoker/server/common/logging/log4j.properties");
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
