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
package org.cspoker.common.api.lobby.action;

import org.cspoker.common.api.lobby.LobbyContext;
import org.cspoker.common.api.lobby.event.LobbyListener;
import org.cspoker.common.api.shared.ServerContext;
import org.cspoker.common.api.shared.action.Action;
import org.cspoker.common.api.shared.event.ServerListener;

public abstract class LobbyAction<T> extends Action<T> {

	private static final long serialVersionUID = -6219914092644893577L;

	public LobbyAction(long id) {
		super(id);
	}

	protected LobbyAction() {
		// no op
	}

	@Override
	public void perform(ServerContext serverContext, ServerListener listener) {
		perform(serverContext.getLobbyContext(), listener.getLobbyListener());
	}

	public abstract void perform(LobbyContext lobbyContext,
			LobbyListener lobbyListener);
	
}
