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
package org.cspoker.server.xml.common;

import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.NotThreadSafe;

import org.cspoker.common.api.lobby.context.ForwardingLobbyContext;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.context.StaticLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.listener.UniversalTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.api.shared.listener.UniversalServerListener;
import org.cspoker.common.elements.table.TableId;

@NotThreadSafe
public class XmlLobbyContext extends ForwardingLobbyContext implements StaticLobbyContext {

	private UniversalServerListener listener;
	private final ConcurrentHashMap<TableId, XmlHoldemTableContext> contexts = new ConcurrentHashMap<TableId, XmlHoldemTableContext>();
	
	public XmlLobbyContext(LobbyContext lobbyContext, UniversalServerListener listener) {
		super(lobbyContext);
		this.listener = listener;
	}

	public XmlHoldemTableContext getHoldemTableContext(TableId tableId) {
		return contexts.get(tableId);
	}

	//The context that we delegating to is responsible for synchronizing concurrent join actions.
	public void joinHoldemTable(TableId tableId) throws IllegalActionException {
		UniversalTableListener tableListener = new UniversalTableListener(listener, tableId);
		XmlHoldemTableContext newContext = new XmlHoldemTableContext(super.joinHoldemTable(tableId, tableListener),tableListener);
		contexts.put(tableId, newContext);
	}


}
