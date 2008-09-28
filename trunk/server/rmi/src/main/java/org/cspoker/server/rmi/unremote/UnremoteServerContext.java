package org.cspoker.server.rmi.unremote;

import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.common.api.shared.context.ForwardingServerContext;
import org.cspoker.common.api.shared.context.ServerContext;

public class UnremoteServerContext extends ForwardingServerContext implements ServerContext {

	private UnremoteChatContext chatContext;
	private UnremoteLobbyContext lobbyContext;

	public UnremoteServerContext(Killable connection, ServerContext serverContext) {
		super(serverContext);
		this.chatContext = new UnremoteChatContext(connection, super.getChatContext());
		this.lobbyContext = new UnremoteLobbyContext(connection, super.getLobbyContext());
	}

	public ChatContext getChatContext() {
		return chatContext;
	}

	public LobbyContext getLobbyContext() {
		return lobbyContext;
	}

}
