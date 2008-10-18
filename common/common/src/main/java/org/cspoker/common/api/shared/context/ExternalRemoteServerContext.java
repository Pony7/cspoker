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

import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.chat.listener.RemoteChatListener;
import org.cspoker.common.api.lobby.context.ExternalRemoteLobbyContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.lobby.listener.RemoteLobbyListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;

public interface ExternalRemoteServerContext extends RemoteServerContext{

	public RemoteChatContext getServerChatContext(RemoteChatListener chatListener) throws RemoteException, IllegalActionException;
	
	public RemoteChatContext getTableChatContext(RemoteChatListener chatListener,long tableId) throws RemoteException;
	
	public ExternalRemoteLobbyContext getLobbyContext(LobbyListener lobbyListener)
			throws RemoteException, IllegalActionException;
	
	public ExternalRemoteLobbyContext getLobbyContext(RemoteLobbyListener lobbyListener) throws RemoteException, IllegalActionException;
	
}
