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
package org.cspoker.common.api.lobby.holdemtable.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cspoker.common.api.lobby.event.LobbyTreeEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListenerTree;
import org.cspoker.common.api.lobby.listener.LobbyListenerTree;

public class HoldemTableTreeEvents extends LobbyTreeEvent {

	private static final long serialVersionUID = 557148706756328395L;

	private long tableId;

	private List<HoldemTableTreeEvent> events;
	
	public HoldemTableTreeEvents() {
		// no op
	}

	public HoldemTableTreeEvents(long tableId, List<HoldemTableTreeEvent> events){
		this.tableId = tableId;
		this.events = new ArrayList<HoldemTableTreeEvent>(events);
	}
	
	public HoldemTableTreeEvents(long tableId, HoldemTableTreeEvent event){
		this.tableId = tableId;
		this.events = new ArrayList<HoldemTableTreeEvent>(Collections.singletonList(event));
	}
	
	@Override
	public void dispatch(LobbyListenerTree lobbyListenerTree) {
		HoldemTableListenerTree listenerTree = lobbyListenerTree.getHoldemTableListenerTree(tableId);
		for(HoldemTableTreeEvent event : events){
			event.dispatch(listenerTree);
		}
	}

}
