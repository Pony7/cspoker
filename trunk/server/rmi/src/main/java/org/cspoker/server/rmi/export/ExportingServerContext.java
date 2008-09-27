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
package org.cspoker.server.rmi.export;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.cspoker.common.api.account.context.AccountContext;
import org.cspoker.common.api.cashier.context.CashierContext;
import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.shared.context.ForwardingServerContext;
import org.cspoker.common.api.shared.context.ServerContext;

public class ExportingServerContext extends ForwardingServerContext {

	private AccountContext accountContext;
	private CashierContext cashierContext;
	private ChatContext chatContext;
	private LobbyContext lobbyContext;

	public ExportingServerContext(ServerContext serverContext) {
		super(serverContext);
		try {
			accountContext = (AccountContext) UnicastRemoteObject.exportObject(super.getAccountContext(), 0);
			cashierContext = (CashierContext) UnicastRemoteObject.exportObject(super.getCashierContext(), 0);
			chatContext = (ChatContext) UnicastRemoteObject.exportObject(super.getChatContext(), 0);	
			lobbyContext = (LobbyContext) UnicastRemoteObject.exportObject(new ExportingLobbyContext(this, super.getLobbyContext()), 0);
		} catch (RemoteException exception) {
			die();
		}
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
