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
package org.cspoker.common.api.account.context;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;

public interface RemoteAccountContext extends Remote{

	void changePassword(String passwordHash) throws RemoteException, IllegalActionException;

	boolean hasPassword(String passwordHash) throws RemoteException, IllegalActionException;
	
	void createAccount(String username, String passwordHash) throws RemoteException, IllegalActionException;
	
	byte[] getAvatar(PlayerId playerId) throws RemoteException, IllegalActionException;
	
	void setAvatar(byte[] avatar) throws RemoteException, IllegalActionException;
	
	PlayerId getPlayerID() throws RemoteException, IllegalActionException;
	
}
