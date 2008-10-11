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
package org.cspoker.client.xml.common.context;

import java.rmi.RemoteException;

import org.cspoker.common.api.account.context.RemoteAccountContext;
import org.cspoker.common.api.cashier.context.RemoteCashierContext;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.action.ActionPerformer;
import org.cspoker.common.api.shared.context.RemoteServerContext;

public class XmlRemoteServerContext implements RemoteServerContext {

	private ActionPerformer performer;
	private IDGenerator generator;

	public XmlRemoteServerContext(ActionPerformer performer, IDGenerator generator) {
		this.performer = performer;
		this.generator = generator;
	}
	
	public RemoteAccountContext getAccountContext() throws RemoteException {
		return null;
	}

	public RemoteCashierContext getCashierContext() throws RemoteException {
		return null;
	}

	public RemoteChatContext getChatContext(ChatListener chatListener)
			throws RemoteException {
		return null;
	}

	public RemoteLobbyContext getLobbyContext(LobbyListener lobbyListener)
			throws RemoteException {
		return null;
	}

	public void kill() {
		
	}

}
