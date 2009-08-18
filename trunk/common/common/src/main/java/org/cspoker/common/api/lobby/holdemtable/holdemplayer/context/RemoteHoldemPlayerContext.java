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

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.cspoker.common.api.shared.exception.IllegalActionException;

public interface RemoteHoldemPlayerContext extends Remote {
	
	//Actions
	
	void betOrRaise(int amount) throws RemoteException, IllegalActionException;
	
	void checkOrCall() throws RemoteException, IllegalActionException;
	
	void fold() throws RemoteException, IllegalActionException;
	
	void sitOut() throws RemoteException, IllegalActionException;
	
	/**
	 * Start the game if the table configuration is not set on 'auto-deal'.
	 * With this mechanism, players can wait for other players to join the table, before starting the game.
	 * This is also only allowed when the game not yet started. 
	 * 
	 * @throws IllegalActionException The table configuration is on 'auto-deal', there is only one player seated, or the game is already started. 
	 * 
	 */
	void startGame() throws RemoteException, IllegalActionException;

}
