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
package org.cspoker.client.pokersource;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.cspoker.common.api.account.context.AccountContext;
import org.cspoker.common.api.cashier.context.CashierContext;
import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.external.pokersource.PokersourceConnection;
import org.cspoker.external.pokersource.commands.Logout;

public class PSServerContext implements RemoteServerContext {

	private final static Logger logger = Logger.getLogger(PSServerContext.class);
	
	private final int serial;
	private final PokersourceConnection conn;

	public PSServerContext(PokersourceConnection conn, int serial) {
		this.conn = conn;
		this.serial = serial;
	}

	@Override
	public AccountContext getAccountContext() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CashierContext getCashierContext() {
		throw new UnsupportedOperationException();
	}

	@Override
	public RemoteLobbyContext getLobbyContext(LobbyListener lobbyListener) {
		return new PSLobbyContext(conn, serial, lobbyListener);
	}

	@Override
	public void logout() {
		try {
			conn.send(new Logout());
		} catch (IOException e) {
			logger.error(e);
		}
	}

	@Override
	public ChatContext getServerChatContext(ChatListener chatListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChatContext getTableChatContext(ChatListener chatListener,
			TableId tableId) {
		throw new UnsupportedOperationException();
	}

}
