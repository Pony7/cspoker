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
package org.cspoker.server.rmi.unremote;

import java.rmi.RemoteException;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.ExternalHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.ForwardingHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.RemoteHoldemPlayerListener;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.common.api.shared.listener.ForwardingListener;

public class UnremoteHoldemPlayerContext extends ForwardingHoldemPlayerContext implements
ExternalHoldemPlayerContext {

	private final Killable connection;
	private UnremoteHoldemPlayerListener remoteListener;

	public UnremoteHoldemPlayerContext(Killable connection, HoldemPlayerContext chatContext) {
		super(chatContext);
		this.connection = connection;
	}

	@Override
	public HoldemPlayerListener wrapListener(HoldemPlayerListener listener) {
		remoteListener = new UnremoteHoldemPlayerListener();
		remoteListener.subscribe(listener);
		return remoteListener;
	}

	public void subscribe(RemoteHoldemPlayerListener listener) {
		remoteListener.subscribe(listener);
	}

	public void unSubscribe(RemoteHoldemPlayerListener chatListener) {
		remoteListener.unSubscribe(chatListener);
	}

	public class UnremoteHoldemPlayerListener extends ForwardingListener<RemoteHoldemPlayerListener> implements HoldemPlayerListener{

		public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {
			try {
				for (RemoteHoldemPlayerListener listener : listeners) {
					listener.onNewPocketCards(newPocketCardsEvent);
				}
			} catch (RemoteException exception) {
				connection.die();
			}
		}

	}

}
