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
package org.cspoker.common.api.account.action;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.account.context.AccountContext;
import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.context.StaticServerContext;
import org.cspoker.common.api.shared.event.EventId;

@Immutable
public abstract class AccountAction<T> extends DispatchableAction<T> {

	private static final long serialVersionUID = 1672149720723666483L;

	public AccountAction(EventId id) {
		super(id);
	}

	protected AccountAction() {
		// no op
	}

	@Override
	public T perform(StaticServerContext serverContext) {
		return perform(serverContext.getAccountContext());
	}

	public abstract T perform(AccountContext accountContext);
		
}
