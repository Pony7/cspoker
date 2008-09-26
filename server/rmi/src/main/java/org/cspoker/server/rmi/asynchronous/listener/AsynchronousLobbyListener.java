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
package org.cspoker.server.rmi.asynchronous.listener;

import java.rmi.RemoteException;
import java.util.concurrent.Executor;

import org.cspoker.common.api.lobby.event.LobbyListener;
import org.cspoker.common.api.lobby.event.RemoteLobbyListener;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableListener;
import org.cspoker.common.api.shared.Killable;

public class AsynchronousLobbyListener extends AsynchronousListener implements LobbyListener{

	private final RemoteLobbyListener lobbyListener;
	private Executor executor;

	public AsynchronousLobbyListener(Killable connection, Executor executor, RemoteLobbyListener lobbyListener) {
		super(connection, executor);
		this.lobbyListener = lobbyListener;
	}

	public RemoteLobbyListener getLobbyListener() {
		return lobbyListener;
	}

	public HoldemTableListener getHoldemTableListener(long tableId) {
		HoldemTableListener holdemTableListener;
		try {
			holdemTableListener = lobbyListener.getHoldemTableListener(tableId);
			return new AsynchronousHoldemTableListener(connection, executor, holdemTableListener);
		} catch (RemoteException exception) {
			die();
			return null;
		}
	}

	public void onTableCreated(final TableCreatedEvent tableCreatedEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					lobbyListener.onTableCreated(tableCreatedEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onTableRemoved(final TableRemovedEvent tableRemovedEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					lobbyListener.onTableRemoved(tableRemovedEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

}
