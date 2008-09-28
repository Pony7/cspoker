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

import org.cspoker.common.api.lobby.context.ForwardingLobbyContext;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.util.SimpleWrapper;
import org.cspoker.server.rmi.asynchronous.listener.AsynchronousLobbyListener;

public class AsynchronousLobbyContext extends ForwardingLobbyContext {

	protected final Executor executor;
	
	protected ConcurrentHashMap<Long, SimpleWrapper<HoldemTableContext>> wrappedContexts = new ConcurrentHashMap<Long, SimpleWrapper<HoldemTableContext>>();
	
	public AsynchronousLobbyContext(Executor executor, LobbyContext lobbyContext) {
		super(lobbyContext);
		this.executor = executor;
	}
	
	@Override
	public LobbyListener wrapListener(LobbyListener listener) {
		return new AsynchronousLobbyListener(executor, listener);
	}
	
	@Override
	public HoldemTableContext getHoldemTableContext(final long tableId) {
		wrappedContexts.putIfAbsent(tableId, new SimpleWrapper<HoldemTableContext>(){

			private HoldemTableContext content = null;
			
			public synchronized HoldemTableContext getContent() {
				if(content == null){
					content = new AsynchronousHoldemTableContext(executor, lobbyContext.getHoldemTableContext(tableId));
				}
				return content;
			}
			
		});
		return wrappedContexts.get(tableId).getContent();
	}
	
}
