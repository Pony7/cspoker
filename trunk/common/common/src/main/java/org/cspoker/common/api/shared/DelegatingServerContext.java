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
package org.cspoker.common.api.shared;

import org.cspoker.common.api.account.AccountContext;
import org.cspoker.common.api.cashier.CashierContext;
import org.cspoker.common.api.chat.ChatContext;
import org.cspoker.common.api.lobby.LobbyContext;
import org.cspoker.common.api.shared.event.RemoteServerListener;

public class DelegatingServerContext implements ServerContext {

	private final ServerContext serverContext;

	public DelegatingServerContext(ServerContext serverContext) {
		this.serverContext = serverContext;
	}
	
	public AccountContext getAccountContext() {
		return serverContext.getAccountContext();
	}

	public CashierContext getCashierContext() {
		return serverContext.getCashierContext();
	}

	public ChatContext getChatContext() {
		return serverContext.getChatContext();
	}

	public LobbyContext getLobbyContext() {
		return serverContext.getLobbyContext();
	}

	public void subscribe(RemoteServerListener serverListener) {
		serverContext.subscribe(serverListener);
	}

	public void unSubscribe(RemoteServerListener serverListener) {
		serverContext.unSubscribe(serverListener);
	}

}
