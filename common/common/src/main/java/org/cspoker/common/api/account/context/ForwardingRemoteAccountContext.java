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

import java.rmi.RemoteException;
import java.rmi.server.Unreferenced;

import org.apache.log4j.Logger;
import org.cspoker.common.api.shared.exception.IllegalActionException;


public class ForwardingRemoteAccountContext implements RemoteAccountContext, Unreferenced{


	private final static Logger logger = Logger.getLogger(ForwardingRemoteAccountContext.class);
	
	private RemoteAccountContext accountContext;

	public ForwardingRemoteAccountContext(RemoteAccountContext accountContext) {
		this.accountContext  = accountContext;
	}

	public void changePassword(String passwordHash) throws RemoteException, IllegalActionException{
		accountContext.changePassword(passwordHash);
	}
	
	public boolean hasPassword(String passwordHash) throws RemoteException, IllegalActionException {
		return accountContext.hasPassword(passwordHash);
	}

	public void createAccount(String username, String passwordHash) throws RemoteException, IllegalActionException {
		accountContext.createAccount(username, passwordHash);
	}

	public byte[] getAvatar(long playerId) throws RemoteException, IllegalActionException {
		return accountContext.getAvatar(playerId);
	}

	public void setAvatar(byte[] avatar) throws RemoteException, IllegalActionException {
		accountContext.setAvatar(avatar);
	}

	public long getPlayerID() throws RemoteException, IllegalActionException {
		return accountContext.getPlayerID();
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {
			logger.debug("Garbage collecting old context: "+this);
		} finally{
			super.finalize();
		}
	}

	public void unreferenced() {
		logger.debug("No more clients referencing: "+this);
	}
	
}
