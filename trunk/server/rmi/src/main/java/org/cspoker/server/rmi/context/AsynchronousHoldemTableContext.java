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
package org.cspoker.server.rmi.context;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.cspoker.common.api.lobby.holdemtable.DelegatingHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.event.RemoteHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.HoldemPlayerContext;
import org.cspoker.server.rmi.listener.AsynchronousHoldemTableListener;

public class AsynchronousHoldemTableContext extends DelegatingHoldemTableContext {

	protected ConcurrentHashMap<RemoteHoldemTableListener, AsynchronousHoldemTableListener> wrappers = 
		new ConcurrentHashMap<RemoteHoldemTableListener, AsynchronousHoldemTableListener>();
	protected Executor executor;
	protected AsynchronousHoldemPlayerContext holdemPlayerContext;
	private AsynchronousServerContext asynchronousServerContext;
	
	public AsynchronousHoldemTableContext(AsynchronousServerContext asynchronousServerContext, Executor executor, HoldemTableContext holdemTableContext) {
		super(holdemTableContext);
		this.asynchronousServerContext = asynchronousServerContext;
		this.holdemPlayerContext = new AsynchronousHoldemPlayerContext(asynchronousServerContext, executor,super.getHoldemPlayerContext());
		this.executor = executor;
	}
	
	@Override
	public HoldemPlayerContext getHoldemPlayerContext() {
		return holdemPlayerContext;
	}
	
	@Override
	public void subscribe(RemoteHoldemTableListener holdemTableListener) {
		AsynchronousHoldemTableListener wrapper = new AsynchronousHoldemTableListener(asynchronousServerContext, executor, holdemTableListener);
		if(wrappers.putIfAbsent(holdemTableListener, wrapper)==null){
			super.subscribe(wrapper);
		}
		
	}
	
	@Override
	public void unSubscribe(RemoteHoldemTableListener holdemTableListener) {
		AsynchronousHoldemTableListener wrapper = wrappers.remove(holdemTableListener);
		if(wrapper!=null){
			super.unSubscribe(wrapper);
		}
	}
	
}
