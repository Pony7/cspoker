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
package org.cspoker.common.api.lobby.context;

import java.util.concurrent.Executor;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.holdemtable.context.AsynchronousHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.listener.AsynchronousHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;

@Immutable
public class AsynchronousLobbyContext extends ForwardingLobbyContext {

	protected final Executor executor;
	
	public AsynchronousLobbyContext(Executor executor, LobbyContext lobbyContext) {
		super(lobbyContext);
		this.executor = executor;
	}
	
	@Override
	public HoldemTableContext joinHoldemTable(long tableId,
			HoldemTableListener holdemTableListener) throws IllegalActionException {
		return new AsynchronousHoldemTableContext(executor, 
				super.joinHoldemTable(tableId, new AsynchronousHoldemTableListener(executor, holdemTableListener)));
	}
	
}
