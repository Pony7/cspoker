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
package org.cspoker.server.rmi.unremote.listener;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.RemoteHoldemPlayerListener;
import org.cspoker.common.api.shared.Trigger;
import org.cspoker.common.api.shared.listener.ForwardingListener;

public class UnremoteHoldemPlayerListener extends ForwardingListener<RemoteHoldemPlayerListener> implements HoldemPlayerListener{

	private final static Logger logger = Logger.getLogger(UnremoteHoldemPlayerListener.class);
	
	private final Trigger connection;

	public UnremoteHoldemPlayerListener(Trigger connection, RemoteHoldemPlayerListener playerListener) {
		super(playerListener);
		this.connection = connection;
	}
	
	public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {
		try {
			for (RemoteHoldemPlayerListener listener : listeners) {
				listener.onNewPocketCards(newPocketCardsEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

}
