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
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;

public class ForwardingRemoteServerContext implements RemoteServerContext {

	private final RemoteServerContext serverContext;

	public ForwardingRemoteServerContext(RemoteServerContext serverContext) {
		this.serverContext = serverContext;
	}
	
	public RemoteAccountContext getAccountContext() throws RemoteException {
		return serverContext.getAccountContext();
	}

	public RemoteCashierContext getCashierContext() throws RemoteException {
		return serverContext.getCashierContext();
	}

	public RemoteChatContext getChatContext() throws RemoteException {
		return serverContext.getChatContext();
	}

	public RemoteLobbyContext getLobbyContext() throws RemoteException {
		return serverContext.getLobbyContext();
	}

	public void die() {
		serverContext.die();
	}

}
