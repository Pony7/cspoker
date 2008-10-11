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

import org.cspoker.common.api.chat.event.ServerMessageEvent;
import org.cspoker.common.api.chat.event.TableMessageEvent;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.event.ServerEvent;

public class UniversalServerListener implements ServerEventListener, ChatListener, LobbyListener{

	private final ServerEventListener serverEventListener;

	public UniversalServerListener(ServerEventListener serverEventListener) {
		this.serverEventListener = serverEventListener;
	}
	
	public void onServerEvent(ServerEvent event) {
		serverEventListener.onServerEvent(event);
	}
	
	public void onServerMessage(ServerMessageEvent serverMessageEvent) {
		onServerEvent(serverMessageEvent);
	}

	public void onTableMessage(TableMessageEvent tableMessageEvent) {
		onServerEvent(tableMessageEvent);
	}

	public void onTableCreated(TableCreatedEvent tableCreatedEvent) {
		onServerEvent(tableCreatedEvent);
	}

	public void onTableRemoved(TableRemovedEvent tableRemovedEvent) {
		onServerEvent(tableRemovedEvent);
	}

}
