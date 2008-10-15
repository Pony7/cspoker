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

import org.cspoker.common.api.account.context.RemoteAccountContext;
import org.cspoker.common.api.cashier.context.RemoteCashierContext;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.context.ForwardingRemoteServerContext;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.util.lazy.IFactory1;
import org.cspoker.common.util.lazy.IFactory2;
import org.cspoker.common.util.lazy.LazyReference1;
import org.cspoker.common.util.lazy.LazyReference2;

public class ExportingServerContext extends ForwardingRemoteServerContext {

	private LazyReference1<RemoteAccountContext,RemoteException> accountContext = new LazyReference1<RemoteAccountContext,RemoteException>();
	private LazyReference1<RemoteCashierContext,RemoteException> cashierContext = new LazyReference1<RemoteCashierContext,RemoteException>();
	private LazyReference2<RemoteChatContext,RemoteException, IllegalActionException> chatContext = new LazyReference2<RemoteChatContext,RemoteException, IllegalActionException>();
	private LazyReference2<RemoteLobbyContext,RemoteException, IllegalActionException> lobbyContext = new LazyReference2<RemoteLobbyContext,RemoteException, IllegalActionException>();

	public ExportingServerContext(ServerContext serverContext) throws RemoteException {
		super(serverContext);
	}

	@Override
	public RemoteAccountContext getAccountContext() throws RemoteException {
		return accountContext.getContent(new IFactory1<RemoteAccountContext, RemoteException>(){
			public RemoteAccountContext create() throws RemoteException {
				return (RemoteAccountContext) UnicastRemoteObject.exportObject(ExportingServerContext.super.getAccountContext(), 0);
			}
		});
	}
	
	@Override
	public RemoteCashierContext getCashierContext() throws RemoteException {
		return cashierContext.getContent(new IFactory1<RemoteCashierContext, RemoteException>(){
			public RemoteCashierContext create() throws RemoteException {
				return (RemoteCashierContext) UnicastRemoteObject.exportObject(ExportingServerContext.super.getCashierContext(), 0);
			}
		});
	}
	
	@Override
	public RemoteChatContext getServerChatContext(final ChatListener chatListener)
			throws RemoteException, IllegalActionException {
		return chatContext.getContent(new IFactory2<RemoteChatContext, RemoteException, IllegalActionException>(){
			public RemoteChatContext create() throws RemoteException, IllegalActionException {
				return (RemoteChatContext) UnicastRemoteObject.exportObject(ExportingServerContext.super.getServerChatContext(chatListener), 0);
			}
		});
	}
	
	@Override
	public RemoteLobbyContext getLobbyContext(final LobbyListener lobbyListener)
			throws RemoteException, IllegalActionException {
		return lobbyContext.getContent(new IFactory2<RemoteLobbyContext, RemoteException, IllegalActionException>(){
			public RemoteLobbyContext create() throws RemoteException, IllegalActionException {
				ExportingLobbyContext remoteObject = new ExportingLobbyContext(ExportingServerContext.super.getLobbyContext(lobbyListener));
				return (LobbyContext) UnicastRemoteObject.exportObject(remoteObject, 0);
			}
		});
	}
	
	
}
