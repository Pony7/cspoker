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

import javax.xml.bind.annotation.XmlRootElement;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.event.LobbyTreeEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListenerTree;
import org.cspoker.common.api.lobby.listener.LobbyListenerTree;
import org.cspoker.common.elements.table.TableId;

@XmlRootElement
@Immutable
public class HoldemTableTreeEventWrapper extends LobbyTreeEvent {

	private static final long serialVersionUID = 557148706756328395L;

	private final TableId tableID;

	private final HoldemTableTreeEvent event;
	
	public HoldemTableTreeEventWrapper(TableId tableID, HoldemTableTreeEvent event){
		this.tableID = tableID;
		this.event = event;
	}
	
	protected HoldemTableTreeEventWrapper() {
		tableID = null;
		event = null;
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
