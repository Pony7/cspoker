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
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.shared.context.ForwardingServerContext;
import org.cspoker.common.api.shared.context.ServerContext;

public class AsynchronousServerContext extends ForwardingServerContext {

	private ChatContext chatContext;
	private LobbyContext lobbyContext;
	
	public AsynchronousServerContext(Executor executor, ServerContext serverContext) {
		super(serverContext);
		chatContext = new AsynchronousChatContext(executor,super.getChatContext());
		lobbyContext = new AsynchronousLobbyContext(executor,super.getLobbyContext());
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
