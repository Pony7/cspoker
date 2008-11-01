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
package org.cspoker.client.common;

import java.rmi.RemoteException;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.holdemtable.context.ForwardingRemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.pots.Pots;

@Immutable
public class SmartHoldemTableContext extends ForwardingRemoteHoldemTableContext {

	private final SmartHoldemTableListener smartListener;

	public SmartHoldemTableContext(RemoteHoldemTableContext holdemTableContext,
			SmartHoldemTableListener smartListener) {
		super(holdemTableContext);
		this.smartListener = smartListener;
	}

	public Pots getPots(){
		return smartListener.getPots();
	}
	
	public Set<Card> getCommunityCards(){
		return smartListener.getCommunityCards();
	}
	
	@Override
	public SmartHoldemPlayerContext sitIn(int amount,
			HoldemPlayerListener holdemPlayerListener) throws RemoteException,
			IllegalActionException {
		SmartHoldemPlayerListener smartListener = new SmartHoldemPlayerListener(holdemPlayerListener);
		return new SmartHoldemPlayerContext(super.sitIn(amount, smartListener),smartListener);
	}
	
	@Override
	public SmartHoldemPlayerContext sitIn(long seatId, int amount,
			HoldemPlayerListener holdemPlayerListener) throws RemoteException,
			IllegalActionException {
		SmartHoldemPlayerListener smartListener = new SmartHoldemPlayerListener(holdemPlayerListener);
		return new SmartHoldemPlayerContext(super.sitIn(seatId, amount, smartListener),smartListener);
	}

}
