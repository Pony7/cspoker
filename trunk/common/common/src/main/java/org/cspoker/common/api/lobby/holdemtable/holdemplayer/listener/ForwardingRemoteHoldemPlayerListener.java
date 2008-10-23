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
package org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener;

import java.rmi.RemoteException;
import java.rmi.server.Unreferenced;
import java.util.List;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.shared.listener.ForwardingListener;

public class ForwardingRemoteHoldemPlayerListener extends ForwardingListener<RemoteHoldemPlayerListener> implements RemoteHoldemPlayerListener , Unreferenced{

	private final static Logger logger = Logger.getLogger(ForwardingRemoteHoldemPlayerListener.class);
	
	public ForwardingRemoteHoldemPlayerListener() {
		super();
	}

	public ForwardingRemoteHoldemPlayerListener(List<RemoteHoldemPlayerListener> listeners) {
		super(listeners);
	}
	
	public ForwardingRemoteHoldemPlayerListener(RemoteHoldemPlayerListener listener) {
		super(listener);
	}
	
	public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) throws RemoteException {
		for(RemoteHoldemPlayerListener listener:listeners){
			listener.onNewPocketCards(newPocketCardsEvent);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {
			logger.debug("Garbage collecting old listener: "+this);
		} finally{
			super.finalize();
		}
	}

	public void unreferenced() {
		logger.debug("No more server referencing: "+this);
	}

}
