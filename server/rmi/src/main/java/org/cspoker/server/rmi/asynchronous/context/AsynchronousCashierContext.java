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
package org.cspoker.server.rmi.asynchronous.context;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.cspoker.common.api.cashier.context.CashierContext;
import org.cspoker.common.api.cashier.context.ForwardingCashierContext;
import org.cspoker.common.api.cashier.event.RemoteCashierListener;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.server.rmi.asynchronous.listener.AsynchronousCashierListener;

public class AsynchronousCashierContext extends ForwardingCashierContext {

	protected ConcurrentHashMap<RemoteCashierListener, AsynchronousCashierListener> wrappers = 
		new ConcurrentHashMap<RemoteCashierListener, AsynchronousCashierListener>();
	protected Executor executor;
	private Killable connection;
	
	public AsynchronousCashierContext(Killable connection, Executor executor, CashierContext cashierContext) {
		super(cashierContext);
		this.connection = connection;
		this.executor = executor;
	}
	
	@Override
	public void subscribe(RemoteCashierListener cashierListener) {
		AsynchronousCashierListener wrapper = new AsynchronousCashierListener(connection, executor, cashierListener);
		if(wrappers.putIfAbsent(cashierListener, wrapper)==null){
			super.subscribe(wrapper);
		}
		
	}
	
	@Override
	public void unSubscribe(RemoteCashierListener accountListener) {
		AsynchronousCashierListener wrapper = wrappers.remove(accountListener);
		if(wrapper!=null){
			super.unSubscribe(wrapper);
		}
	}
	
}
