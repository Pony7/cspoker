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

import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.lobby.listener.LobbyListenerTree;

@ThreadSafe
public class XmlLobbyListenerTree implements LobbyListenerTree {

	private final ConcurrentHashMap<Long, XmlHoldemTableListenerTree> listenerTrees = new ConcurrentHashMap<Long, XmlHoldemTableListenerTree>();
	private volatile LobbyListener lobbyListener;

	public XmlHoldemTableListenerTree getHoldemTableListenerTree(long tableId) {
		listenerTrees.putIfAbsent(tableId, new XmlHoldemTableListenerTree());
		return listenerTrees.get(tableId);
	}

	public LobbyListener getLobbyListener() {
		return lobbyListener;
	}

	public void setLobbyListener(LobbyListener lobbyListener) {
		this.lobbyListener = lobbyListener;
	}

}
