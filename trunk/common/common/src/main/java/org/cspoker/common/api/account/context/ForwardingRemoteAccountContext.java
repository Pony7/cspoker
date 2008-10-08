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


public class ForwardingRemoteAccountContext implements RemoteAccountContext{

	private RemoteAccountContext accountContext;

	public ForwardingRemoteAccountContext(RemoteAccountContext accountContext) {
		this.accountContext  = accountContext;
	}

	public void changePassword(String passwordHash) throws RemoteException{
		accountContext.changePassword(passwordHash);
	}
	
	public boolean hasPassword(String passwordHash) throws RemoteException {
		return accountContext.hasPassword(passwordHash);
	}

	public void createAccount(String username, String passwordHash) throws RemoteException {
		accountContext.createAccount(username, passwordHash);
	}

	public byte[] getAvatar(long playerId) throws RemoteException {
		return accountContext.getAvatar(playerId);
	}

	public void setAvatar(byte[] avatar) throws RemoteException {
		accountContext.setAvatar(avatar);
	}
	
}
