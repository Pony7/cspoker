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
package org.cspoker.common.api.shared.context;

import java.rmi.RemoteException;

import org.cspoker.common.api.account.context.RemoteAccountContext;
import org.cspoker.common.api.cashier.context.RemoteCashierContext;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.chat.listener.RemoteChatListener;
import org.cspoker.common.api.lobby.context.ExternalRemoteLobbyContext;
import org.cspoker.common.api.lobby.context.LobbyContextConverter;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.lobby.listener.RemoteLobbyListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.TableId;

/**
 * A wrapper class that converts a ServerContextConverter in to a ExternalRemoteServerContext.
 * These 2 classes could inherit if it wasn't for a bug in Sun JDK 6 that prohibits covariant inheritance.
 * This class is a workaround for the problem.
 * 
 * @see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6294779
 * 
 * @author guy
 *
 */
public class ServerContextConverter implements ExternalRemoteServerContext {

	private ExternalServerContext serverContext;

	public ServerContextConverter(ExternalServerContext serverContext) {
		this.serverContext=serverContext;
	}
	
	public void logout() {
		serverContext.logout();
	}

	public ExternalRemoteLobbyContext getLobbyContext(
			LobbyListener lobbyListener) throws RemoteException,
			IllegalActionException {
		return new LobbyContextConverter(serverContext.getLobbyContext(lobbyListener));
	}

	public ExternalRemoteLobbyContext getLobbyContext(
			RemoteLobbyListener lobbyListener) throws RemoteException,
			IllegalActionException {
		return new LobbyContextConverter(serverContext.getLobbyContext(lobbyListener));
	}

	public RemoteChatContext getServerChatContext(
			RemoteChatListener chatListener) throws RemoteException,
			IllegalActionException {
		return serverContext.getServerChatContext(chatListener);
	}

	public RemoteChatContext getTableChatContext(
			RemoteChatListener chatListener, TableId tableId)
			throws RemoteException {
		return serverContext.getTableChatContext(chatListener, tableId);
	}

	public RemoteAccountContext getAccountContext() throws RemoteException {
		return serverContext.getAccountContext();
	}

	public RemoteCashierContext getCashierContext() throws RemoteException {
		return serverContext.getCashierContext();
	}

	public RemoteChatContext getServerChatContext(ChatListener chatListener)
			throws RemoteException, IllegalActionException {
		return serverContext.getServerChatContext(chatListener);
	}

	public RemoteChatContext getTableChatContext(ChatListener chatListener,
			TableId tableId) throws RemoteException, IllegalActionException {
		return serverContext.getServerChatContext(chatListener);
	}

}
