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
package org.cspoker.common.api.lobby.holdemtable.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.cspoker.common.api.lobby.action.LobbyAction;
import org.cspoker.common.api.lobby.context.StaticLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.StaticHoldemTableContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;


@XmlAccessorType(XmlAccessType.FIELD)
public abstract class HoldemTableAction<T> extends LobbyAction<T> {

	private static final long serialVersionUID = 4151821035256457839L;

	private long tableId;

	public HoldemTableAction(long id, long tableId) {
		super(id);
		this.tableId = tableId;
	}

	protected HoldemTableAction() {
		// no op
	}

	@Override
	public T perform(StaticLobbyContext lobbyContext) throws IllegalActionException {
		return perform(lobbyContext.getHoldemTableContext(tableId));
	}

	public abstract T perform(StaticHoldemTableContext holdemTableContext) throws IllegalActionException;
	
	public long getTableId() {
		return tableId;
	}

}
