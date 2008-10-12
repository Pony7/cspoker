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
package org.cspoker.server.rmi.unremote.context;

import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.chat.listener.RemoteChatListener;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.listener.RemoteLobbyListener;
import org.cspoker.common.api.shared.Trigger;
import org.cspoker.common.api.shared.context.ExternalServerContext;
import org.cspoker.common.api.shared.context.ForwardingServerContext;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.server.rmi.unremote.listener.UnremoteChatListener;
import org.cspoker.server.rmi.unremote.listener.UnremoteLobbyListener;

public class UnremoteServerContext extends ForwardingServerContext implements ExternalServerContext {

	private Trigger connection;

	public UnremoteServerContext(Trigger connection, ServerContext serverContext) {
		super(serverContext);
		this.connection = connection;
	}
	
	public ChatContext getChatContext(RemoteChatListener chatListener) {
		return super.getChatContext(new UnremoteChatListener(connection,chatListener));
	}
	
	public LobbyContext getLobbyContext(RemoteLobbyListener lobbyListener) {
		return new UnremoteLobbyContext(connection, super.getLobbyContext(new UnremoteLobbyListener(connection, lobbyListener)));
	}

}
