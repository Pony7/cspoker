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
package org.cspoker.client.bots;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.context.AsynchronousServerContext;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.server.common.CSPokerServerImpl;


public class BotRunner implements LobbyListener {

	static {
		Log4JPropertiesLoader
				.load("org/cspoker/client/bots/logging/log4j.properties");
	}
	
	private final static Logger logger = Logger.getLogger(BotRunner.class);
	
	public static void main(String[] args) {
		new BotRunner();
	}
	
	public BotRunner() {
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable(){
			
			public void run() {
				CSPokerServerImpl server = new CSPokerServerImpl();
				try {
					ServerContext serverguy = new AsynchronousServerContext(executor, server.login("guy", "test"));
					LobbyContext lobbyguy = serverguy.getLobbyContext(BotRunner.this);
					DetailedHoldemTable table = lobbyguy.createHoldemTable("BotTable", new TableConfiguration());
					CallBot guy = new CallBot(lobbyguy,"guy",table.getId());
					
					ServerContext serverkenzo = new AsynchronousServerContext(executor, server.login("kenzo", "test"));
					LobbyContext lobbykenzo = serverkenzo.getLobbyContext(BotRunner.this);
					CallBot kenzo = new CallBot(lobbykenzo,"kenzo",table.getId());
				} catch (LoginException e) {
					throw new IllegalStateException("Login Failed");
				}
			}
		});
	}

	public void onTableCreated(TableCreatedEvent tableCreatedEvent) {
		
	}

	public void onTableRemoved(TableRemovedEvent tableRemovedEvent) {
		
	}

}
