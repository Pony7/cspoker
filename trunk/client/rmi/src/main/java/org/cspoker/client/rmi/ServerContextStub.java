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
package org.cspoker.client.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.context.ForwardingRemoteServerContext;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;

public class ServerContextStub extends ForwardingRemoteServerContext{

	public ServerContextStub(RemoteServerContext serverContext) throws RemoteException {
		super(serverContext);
	}
	
	@Override
	public RemoteChatContext getServerChatContext(ChatListener chatListener)
			throws RemoteException, IllegalActionException {
		return super.getServerChatContext((ChatListener) UnicastRemoteObject.exportObject(chatListener, 0));
	}
	
	@Override
	public RemoteChatContext getTableChatContext(ChatListener chatListener,long tableId)
			throws RemoteException, IllegalActionException {
		return super.getTableChatContext((ChatListener) UnicastRemoteObject.exportObject(chatListener, 0),tableId);
	}
	
	@Override
	public RemoteLobbyContext getLobbyContext(LobbyListener lobbyListener)
			throws RemoteException, IllegalActionException {
		return new LobbyContextStub(super.getLobbyContext((LobbyListener) UnicastRemoteObject.exportObject(lobbyListener, 0)));
	}

}
