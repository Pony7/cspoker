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
package org.cspoker.common.api.lobby.holdemtable.holdemplayer.action;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.holdemtable.action.HoldemTableAction;
import org.cspoker.common.api.lobby.holdemtable.context.StaticHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.shared.event.EventId;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.TableId;


@Immutable
public abstract class HoldemPlayerAction<T> extends HoldemTableAction<T> {

	private static final long serialVersionUID = 4151821035256457839L;

	public HoldemPlayerAction(EventId id, TableId tableId) {
		super(id, tableId);
	}

	protected HoldemPlayerAction() {
		// no op
	}

	@Override
	public T perform(StaticHoldemTableContext holdemTableContext) throws IllegalActionException{
		return perform(holdemTableContext.getHoldemPlayerContext());
	}

	public abstract T perform(HoldemPlayerContext holdemPlayerContext) throws IllegalActionException;
}
