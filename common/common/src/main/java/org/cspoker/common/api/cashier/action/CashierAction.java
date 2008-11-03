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
package org.cspoker.common.api.cashier.action;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.cashier.context.CashierContext;
import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.context.StaticServerContext;
import org.cspoker.common.api.shared.event.EventId;

@Immutable
public abstract class CashierAction<T> extends DispatchableAction<T> {

	private static final long serialVersionUID = 6542312781797096164L;

	public CashierAction(EventId id) {
		super(id);
	}

	protected CashierAction() {
		// no op
	}

	@Override
	public T perform(StaticServerContext serverContext) {
		return perform(serverContext.getCashierContext());
	}

	public abstract T perform(CashierContext cashierContextr);
	
}
