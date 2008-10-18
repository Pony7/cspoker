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
package org.cspoker.common.api.shared.listener;

import org.cspoker.common.api.chat.event.MessageEvent;
import org.cspoker.common.api.chat.event.ServerChatEvents;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.chat.listener.RemoteChatListener;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.lobby.listener.RemoteLobbyListener;
import org.cspoker.common.api.shared.event.ServerEvent;

public class UniversalServerListener implements ServerEventListener, ChatListener, LobbyListener, RemoteChatListener, RemoteLobbyListener{

	private final ServerEventListener serverEventListener;

	public UniversalServerListener(ServerEventListener serverEventListener) {
		this.serverEventListener = serverEventListener;
	}
	
	public void onServerEvent(ServerEvent event) {
		serverEventListener.onServerEvent(event);
	}
	
	public void onTableCreated(TableCreatedEvent tableCreatedEvent) {
		onServerEvent(tableCreatedEvent);
	}

	public void onTableRemoved(TableRemovedEvent tableRemovedEvent) {
		onServerEvent(tableRemovedEvent);
	}

	public void onMessage(MessageEvent messageEvent) {
		onServerEvent(new ServerChatEvents(messageEvent));
	}

}
