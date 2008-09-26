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

import org.cspoker.common.api.lobby.DelegatingLobbyContext;
import org.cspoker.common.api.lobby.LobbyContext;
import org.cspoker.common.api.lobby.event.RemoteLobbyListener;
import org.cspoker.common.api.lobby.holdemtable.HoldemTableContext;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.server.rmi.asynchronous.listener.AsynchronousLobbyListener;

public class AsynchronousLobbyContext extends DelegatingLobbyContext {

	protected ConcurrentHashMap<RemoteLobbyListener, AsynchronousLobbyListener> wrappedListeners = new ConcurrentHashMap<RemoteLobbyListener, AsynchronousLobbyListener>();
	protected Executor executor;
	private Killable connection;
	
	protected ConcurrentHashMap<Long, HoldemTableContext> wrappedContexts = new ConcurrentHashMap<Long, HoldemTableContext>();
	
	
	public AsynchronousLobbyContext(Killable connection, Executor executor, LobbyContext lobbyContext) {
		super(lobbyContext);
		this.connection = connection;
		this.executor = executor;
	}
	
	/**
	 * Returns a wrapped HoldemTableContext that makes event listeners asynchronous.
	 * 
	 * This method MUST return the same Context each times, as they are stateful and maintain
	 * a list of event listeners!
	 * 
	 */
	@Override
	public HoldemTableContext getHoldemTableContext(long tableId) {
		HoldemTableContext context;
		if((context = wrappedContexts.get(tableId))==null){
			context = new AsynchronousHoldemTableContext(connection, executor, super.getHoldemTableContext(tableId));
			if(wrappedContexts.putIfAbsent(tableId, context)==null){
				return context;
			}else{
				return wrappedContexts.get(tableId);
			}
		}else{
			return context;
		}
	}
	
	@Override
	public void subscribe(RemoteLobbyListener lobbyListener) {
		AsynchronousLobbyListener wrapper = new AsynchronousLobbyListener(connection, executor, lobbyListener);
		if(wrappedListeners.putIfAbsent(lobbyListener, wrapper)==null){
			super.subscribe(wrapper);
		}
		
	}
	
	@Override
	public void unSubscribe(RemoteLobbyListener lobbyListener) {
		AsynchronousLobbyListener wrapper = wrappedListeners.remove(lobbyListener);
		if(wrapper!=null){
			super.unSubscribe(wrapper);
		}
	}
	
}
