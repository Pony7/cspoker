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

import org.cspoker.common.api.lobby.event.LobbyEvent;
import org.cspoker.common.api.lobby.event.LobbyListener;

public abstract class HoldemTableEvents extends LobbyEvent {

	private static final long serialVersionUID = 557148706756328395L;

	private long tableId;

	private List<HoldemTableEvent> events;
	
	public HoldemTableEvents() {
		// no op
	}

	public HoldemTableEvents(long tableId, List<HoldemTableEvent> events){
		this.tableId = tableId;
		this.events = new ArrayList<HoldemTableEvent>(events);
	}
	
	public HoldemTableEvents(long tableId, HoldemTableEvent event){
		this.tableId = tableId;
		this.events = new ArrayList<HoldemTableEvent>(Collections.singletonList(event));
	}

	public void dispatch(LobbyListener lobbyListener){
		HoldemTableListener listener = lobbyListener.getHoldemTableListener(tableId);
		for(HoldemTableEvent event : events){
			event.dispatch(listener);
		}
	}

	public abstract void dispatch(HoldemTableListener holdemTableListener);

}
