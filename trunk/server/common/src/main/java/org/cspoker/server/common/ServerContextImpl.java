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
package org.cspoker.server.common;

import javax.security.auth.login.LoginException;

import org.cspoker.common.api.account.context.AccountContext;
import org.cspoker.common.api.cashier.context.CashierContext;
import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.shared.context.ServerContext;

public class ServerContextImpl implements ServerContext {

	private final ExtendedAccountContext accountContext;
	private final CashierContext cashierContext;
	private final ChatContext chatContext;
	private final LobbyContextImpl lobbyContext;

	public ServerContextImpl(String username, String password) throws LoginException {
		this.accountContext = new AccountContextImpl(username,password);
		this.cashierContext = new CashierContextImpl(accountContext);
		this.chatContext = new ChatContextImpl(accountContext);
		this.lobbyContext = new LobbyContextImpl(accountContext);
	}

	public AccountContext getAccountContext() {
		return accountContext;
	}

	public CashierContext getCashierContext() {
		return cashierContext;
	}

	public ChatContext getChatContext() {
		return chatContext;
	}

	public LobbyContext getLobbyContext() {
		return lobbyContext;
	}

	public void die() {
		//TODO implement
	}

}
