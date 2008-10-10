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
package org.cspoker.common.api.lobby.holdemtable.holdemplayer.context;

import java.rmi.RemoteException;

import org.cspoker.common.api.shared.exception.IllegalActionException;

public class ForwardingRemoteHoldemPlayerContext implements RemoteHoldemPlayerContext{

	private final RemoteHoldemPlayerContext holdemPlayerContext;

	public ForwardingRemoteHoldemPlayerContext(RemoteHoldemPlayerContext holdemPlayerContext) throws RemoteException {
		this.holdemPlayerContext  = holdemPlayerContext;
	}

	public void betOrRaise(int amount) throws RemoteException, IllegalActionException {
		holdemPlayerContext.betOrRaise(amount);
	}

	public void checkOrCall() throws RemoteException, IllegalActionException {
		holdemPlayerContext.checkOrCall();
	}

	public void fold() throws RemoteException, IllegalActionException {
		holdemPlayerContext.fold();
	}

	public void leaveGame() throws RemoteException {
		holdemPlayerContext.leaveGame();
	}
	
}
