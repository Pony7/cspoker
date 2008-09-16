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

import org.cspoker.common.api.account.AccountContext;
import org.cspoker.common.api.cashier.CashierContext;
import org.cspoker.common.api.chat.ChatContext;
import org.cspoker.common.api.lobby.LobbyContext;
import org.cspoker.common.api.shared.DelegatingServerContext;
import org.cspoker.common.api.shared.ServerContext;

public class AsynchonousServerContext extends DelegatingServerContext {

	private final AccountContext accountContext;
	private final CashierContext cashierContext;
	private final ChatContext chatContext;
	private final LobbyContext lobbyContext;
	
	public AsynchonousServerContext(ServerContext serverContext) {
		super(serverContext);
		accountContext = new AsynchronousAccountContext(super.getAccountContext());
		cashierContext = new AsynchronousCashierContext(super.getCashierContext());
		chatContext = new AsynchronousChatContext(super.getChatContext());
		lobbyContext = new AsynchronousLobbyContext(super.getLobbyContext());
	}
	
	@Override
	public AccountContext getAccountContext() {
		return accountContext;
	}
	
	@Override
	public CashierContext getCashierContext() {
		return cashierContext;
	}
	
	@Override
	public ChatContext getChatContext() {
		return chatContext;
	}
	
	@Override
	public LobbyContext getLobbyContext() {
		return lobbyContext;
	}

}
