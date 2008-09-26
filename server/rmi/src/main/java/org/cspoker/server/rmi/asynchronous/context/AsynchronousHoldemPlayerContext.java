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

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.DelegatingHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.RemoteHoldemPlayerListener;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.server.rmi.asynchronous.listener.AsynchronousHoldemPlayerListener;

public class AsynchronousHoldemPlayerContext extends DelegatingHoldemPlayerContext {

	protected ConcurrentHashMap<RemoteHoldemPlayerListener, AsynchronousHoldemPlayerListener> wrappers = 
		new ConcurrentHashMap<RemoteHoldemPlayerListener, AsynchronousHoldemPlayerListener>();
	protected Executor executor;
	private Killable connection;
	
	public AsynchronousHoldemPlayerContext(Killable connection, Executor executor, HoldemPlayerContext holdemPlayerContext) {
		super(holdemPlayerContext);
		this.connection = connection;
		this.executor = executor;
	}
	
	@Override
	public void subscribe(RemoteHoldemPlayerListener holdemPlayerListener) {
		AsynchronousHoldemPlayerListener wrapper = new AsynchronousHoldemPlayerListener(connection, executor, holdemPlayerListener);
		if(wrappers.putIfAbsent(holdemPlayerListener, wrapper)==null){
			super.subscribe(wrapper);
		}
		
	}
	
	@Override
	public void unSubscribe(RemoteHoldemPlayerListener holdemPlayerListener) {
		AsynchronousHoldemPlayerListener wrapper = wrappers.remove(holdemPlayerListener);
		if(wrapper!=null){
			super.unSubscribe(wrapper);
		}
	}
	
}
