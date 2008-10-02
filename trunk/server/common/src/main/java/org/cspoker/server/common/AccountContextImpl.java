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
 package org.cspoker.server.common;

import javax.security.auth.login.LoginException;

import org.cspoker.server.common.authentication.XmlFileAuthenticator;

public class AccountContextImpl implements ExtendedAccountContext{

	private static XmlFileAuthenticator authenticator = new XmlFileAuthenticator();
	private final String name;

	public AccountContextImpl(String username, String password) throws LoginException {
		if(!authenticator.hasPassword(username, password)){
			throw new LoginException();
		}
		this.name = username;
	}

	public void changePassword(String passwordHash) throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}

	public void createAccount(String username, String passwordHash)throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public byte[] getAvatar(long playerId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public void setAvatar(byte[] avatar) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return name;
	}

}
