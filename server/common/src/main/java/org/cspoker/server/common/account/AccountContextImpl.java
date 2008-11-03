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
 package org.cspoker.server.common.account;

import javax.security.auth.login.LoginException;

import org.cspoker.common.elements.player.MutablePlayer;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.common.player.PlayerFactory;

public class AccountContextImpl implements ExtendedAccountContext{

	private static XmlFileAuthenticator authenticator = new XmlFileAuthenticator();
		
	private MutablePlayer player;
	

	public AccountContextImpl(String username, String password) throws LoginException {
		if(!authenticator.hasPassword(username, password)){
			throw new LoginException();
		}
		player = PlayerFactory.global_Player_Factory.createNewPlayer(username);
		
	}

	public void changePassword(String passwordHash) throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}

	public void createAccount(String username, String passwordHash)throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public byte[] getAvatar(PlayerId playerId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public void setAvatar(byte[] avatar) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public MutablePlayer getPlayer(){
		return player;
	}

	public boolean hasPassword(String passwordHash) {
		return authenticator.hasPassword(player.getName(), passwordHash);
	}
	
	public PlayerId getPlayerID() {
		return player.getId();
	}

}
