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
package org.cspoker.common.api.lobby.holdemtable;

import java.rmi.RemoteException;

import org.cspoker.common.api.lobby.holdemtable.event.RemoteHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.RemoteHoldemPlayerContext;

public interface RemoteHoldemTableContext {

	//Actions
	
	void leaveTable() throws RemoteException;

	void sitIn(long seatId) throws RemoteException;

	void startGame() throws RemoteException;

	//Sub-Contexts
	
	RemoteHoldemPlayerContext getHoldemPlayerContext() throws RemoteException;
	
	//Event handlers
	
	void subscribe(RemoteHoldemTableListener holdemTableListener) throws RemoteException;
	
	void unSubscribe(RemoteHoldemTableListener holdemTableListener) throws RemoteException;
	
}
