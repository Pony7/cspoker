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
package org.cspoker.server.rmi.asynchronous.context;

import java.util.concurrent.Executor;

import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.chat.listener.AsynchronousChatListener;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.listener.AsynchronousLobbyListener;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.context.ForwardingServerContext;
import org.cspoker.common.api.shared.context.ServerContext;

public class AsynchronousServerContext extends ForwardingServerContext {

	private Executor executor;

	public AsynchronousServerContext(Executor executor, ServerContext serverContext) {
		super(serverContext);
		this.executor = executor;
	}
	
	@Override
	public ChatContext getChatContext(ChatListener chatListener) {
		return super.getChatContext(new AsynchronousChatListener(executor,chatListener));
	}
	
	@Override
	public LobbyContext getLobbyContext(LobbyListener lobbyListener) {
		return super.getLobbyContext(new AsynchronousLobbyListener(executor, lobbyListener));
	}

}
