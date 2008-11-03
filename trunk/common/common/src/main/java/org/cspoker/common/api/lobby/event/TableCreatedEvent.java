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

package org.cspoker.common.api.lobby.event;

import javax.xml.bind.annotation.XmlRootElement;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.table.Table;

@XmlRootElement
@Immutable
public class TableCreatedEvent extends LobbyEvent {

	private static final long serialVersionUID = -3408596246641282753L;

	private final Table table;

	private final Player player;

	public TableCreatedEvent(Player player, Table table) {
		this.player = player;
		this.table = table;
	}

	protected TableCreatedEvent() {
		player = null;
		table = null;
	}

	@Override
	public String toString() {
		return player.getName() + " has created a new table: "
				+ table.getName() + " (#" + table.getId() + ").";
	}

	public Player getPlayer() {
		return player;
	}

	public Table getTable() {
		return table;
	}

	@Override
	public void dispatch(LobbyListener lobbyListener) {
		lobbyListener.onTableCreated(this);
	}

}
