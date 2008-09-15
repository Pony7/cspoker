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

import org.cspoker.common.api.account.AccountContext;
import org.cspoker.common.api.shared.ServerContext;
import org.cspoker.common.api.shared.action.Action;

public abstract class AccountAction<T> extends Action<T> {

	private static final long serialVersionUID = 1672149720723666483L;

	public AccountAction(long id) {
		super(id);
	}

	protected AccountAction() {
		// no op
	}

	@Override
	public T perform(ServerContext serverContext) {
		return perform(serverContext.getAccountContext());
	}

	public abstract T perform(AccountContext accountContext);
		
}
