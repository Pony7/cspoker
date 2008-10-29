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

import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.util.Log4JPropertiesLoader;


public class BotRunner implements LobbyListener {

	static {
		Log4JPropertiesLoader
		.load("org/cspoker/client/bots/logging/log4j.properties");
	}

	private final static Logger logger = Logger.getLogger(BotRunner.class);

	public BotRunner(final RemoteCSPokerServer cspokerServer) {
		try {
			RemoteServerContext serverguy = cspokerServer.login("guy", "test");
			RemoteLobbyContext lobbyguy = serverguy.getLobbyContext(BotRunner.this);
			DetailedHoldemTable table = lobbyguy.createHoldemTable("BotTable", new TableConfiguration());
			CallBot guy = new CallBot(lobbyguy,"guy",table.getId(), true);

			RemoteServerContext serverkenzo = cspokerServer.login("kenzo", "test");
			RemoteLobbyContext lobbykenzo = serverkenzo.getLobbyContext(BotRunner.this);
			CallBot kenzo = new CallBot(lobbykenzo,"kenzo",table.getId(), false);
		} catch (LoginException e) {
			throw new IllegalStateException("Login Failed");
		} catch (RemoteException e) {
			logger.error(e);
			throw new IllegalStateException("Server setup failed.",e);
		} catch (IllegalActionException e) {
			logger.error(e);
			throw new IllegalStateException("Server setup failed.",e);
		}
	}

	public void onTableCreated(TableCreatedEvent tableCreatedEvent) {

	}

	public void onTableRemoved(TableRemovedEvent tableRemovedEvent) {

	}

}
