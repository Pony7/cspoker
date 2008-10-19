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
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.server.common.account.AccountContextImpl;
import org.cspoker.server.common.account.ExtendedAccountContext;
import org.cspoker.server.common.chat.ChatServer;
import org.cspoker.server.common.lobby.Lobby;

public class ServerContextImpl implements ServerContext {

	private final ExtendedAccountContext accountContext;
	private final CashierContext cashierContext;
	private ChatContext chatContext;
	private final LobbyContextImpl lobbyContext;

	public ServerContextImpl(String username, String password) throws LoginException {
		this.accountContext = new AccountContextImpl(username,password);
		this.cashierContext = new CashierContextImpl(accountContext);
		//singleton lobby is passed as an argument for flexibility.
		//TODO fix - I think we should limit the use of singletons. 
		//We should for instance be able to run 2 independent servers in the same JVM. - guy
		this.lobbyContext = new LobbyContextImpl(accountContext, Lobby.getInstance());
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
	
	public LobbyContext getLobbyContext(LobbyListener lobbyListener) {
		return lobbyContext;
	}

	public ChatContext getServerChatContext(ChatListener chatListener) {
		this.chatContext=new ChatContextImpl(accountContext,ChatServer.getInstance().getServerChatRoom(),
					chatListener);
		return getChatContext();
	}
	public ChatContext getTableChatContext(ChatListener chatListener,long tableId) {
		this.chatContext=new ChatContextImpl(accountContext,ChatServer.getInstance().getTableChatRoom(tableId),
				chatListener);
		return getChatContext();
	}

}
