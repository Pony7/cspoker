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
package org.cspoker.client.xml.common.listener;

import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;

import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.elements.table.TableId;

@ThreadSafe
public class XmlServerListenerTree implements
		org.cspoker.common.api.shared.listener.ServerListenerTree {

	private volatile ChatListener serverChatListener;
	private final XmlLobbyListenerTree lobbyListenerTree;
	private final ConcurrentHashMap<TableId, ChatListener> tableChatListener = new ConcurrentHashMap<TableId, ChatListener>();

	public XmlServerListenerTree() {
		lobbyListenerTree = new XmlLobbyListenerTree();
	}

	public XmlLobbyListenerTree getLobbyListenerTree() {
		return lobbyListenerTree;
	}

	public ChatListener getServerChatListener() {
		return serverChatListener;
	}
	
	public void setServerChatListener(ChatListener serverChatListener) {
		this.serverChatListener = serverChatListener;
	}

	public ChatListener getTableChatListener(TableId tableID) {
		return tableChatListener.get(tableID);
	}

	public void setTableChatListener(TableId tableID, ChatListener listener) {
		tableChatListener.put(tableID, listener);
	}
	
}
