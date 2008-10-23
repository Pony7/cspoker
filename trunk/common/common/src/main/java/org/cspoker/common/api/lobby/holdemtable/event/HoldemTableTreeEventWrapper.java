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

import org.cspoker.common.api.lobby.event.LobbyTreeEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListenerTree;
import org.cspoker.common.api.lobby.listener.LobbyListenerTree;

public class HoldemTableTreeEventWrapper extends LobbyTreeEvent {

	private static final long serialVersionUID = 557148706756328395L;

	private long tableID;

	private HoldemTableTreeEvent event;
	
	public HoldemTableTreeEventWrapper() {
		// no op
	}
	
	public HoldemTableTreeEventWrapper(long tableID, HoldemTableTreeEvent event){
		this.tableID = tableID;
		this.event = event;
	}
	
	@Override
	public void dispatch(LobbyListenerTree lobbyListenerTree) {
		HoldemTableListenerTree listenerTree = lobbyListenerTree.getHoldemTableListenerTree(tableID);
		event.dispatch(listenerTree);
	}
	
	@Override
	public String toString() {
		return "|Table #"+tableID+"| "+event.toString();
	}

}
