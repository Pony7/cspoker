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

import org.cspoker.common.api.lobby.DelegatingLobbyContext;
import org.cspoker.common.api.lobby.LobbyContext;
import org.cspoker.common.api.lobby.event.RemoteLobbyListener;
import org.cspoker.common.api.lobby.holdemtable.HoldemTableContext;
import org.cspoker.server.rmi.listener.AsynchronousLobbyListener;

public class AsynchronousLobbyContext extends DelegatingLobbyContext {

	protected ConcurrentHashMap<RemoteLobbyListener, AsynchronousLobbyListener> wrappers = new ConcurrentHashMap<RemoteLobbyListener, AsynchronousLobbyListener>();
	protected Executor executor;
	private AsynchronousServerContext asynchronousServerContext;
	
	public AsynchronousLobbyContext(AsynchronousServerContext asynchronousServerContext, Executor executor, LobbyContext lobbyContext) {
		super(lobbyContext);
		this.asynchronousServerContext = asynchronousServerContext;
		this.executor = executor;
	}
	
	@Override
	public HoldemTableContext getHoldemTableContext(long tableId) {
		return new AsynchronousHoldemTableContext(asynchronousServerContext, executor, super.getHoldemTableContext(tableId));
	}
	
	@Override
	public void subscribe(RemoteLobbyListener lobbyListener) {
		AsynchronousLobbyListener wrapper = new AsynchronousLobbyListener(asynchronousServerContext, executor, lobbyListener);
		if(wrappers.putIfAbsent(lobbyListener, wrapper)==null){
			super.subscribe(wrapper);
		}
		
	}
	
	@Override
	public void unSubscribe(RemoteLobbyListener lobbyListener) {
		AsynchronousLobbyListener wrapper = wrappers.remove(lobbyListener);
		if(wrapper!=null){
			super.unSubscribe(wrapper);
		}
	}
	
}
