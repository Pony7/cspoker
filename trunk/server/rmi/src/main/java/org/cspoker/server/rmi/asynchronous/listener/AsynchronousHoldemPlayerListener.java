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

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.RemoteHoldemPlayerListener;
import org.cspoker.common.api.shared.Killable;

public class AsynchronousHoldemPlayerListener extends AsynchronousListener  implements HoldemPlayerListener{

	private final RemoteHoldemPlayerListener holdemPlayerListener;
	private Executor executor;

	public AsynchronousHoldemPlayerListener(Killable connection, Executor executor, RemoteHoldemPlayerListener holdemPlayerListener) {
		super(connection, executor);
		this.holdemPlayerListener = holdemPlayerListener;
	}

	public void onNewPocketCards(final NewPocketCardsEvent newPocketCardsEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemPlayerListener.onNewPocketCards(newPocketCardsEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}


}
