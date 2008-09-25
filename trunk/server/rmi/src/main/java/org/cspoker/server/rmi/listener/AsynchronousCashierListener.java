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
package org.cspoker.server.rmi.listener;

import java.util.concurrent.Executor;

import org.cspoker.common.api.cashier.event.CashierListener;
import org.cspoker.common.api.cashier.event.RemoteCashierListener;
import org.cspoker.common.api.shared.ServerContext;

public class AsynchronousCashierListener extends AsynchronousListener implements CashierListener{

	private final RemoteCashierListener cashierListener;

	public AsynchronousCashierListener(ServerContext serverContext, Executor executor, RemoteCashierListener cashierListener) {
		super(serverContext, executor);
		this.cashierListener = cashierListener;
	}

	public RemoteCashierListener getCashierListener() {
		return cashierListener;
	}
	
}
