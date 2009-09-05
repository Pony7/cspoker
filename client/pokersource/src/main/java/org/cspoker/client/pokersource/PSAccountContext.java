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
package org.cspoker.client.pokersource;

import org.cspoker.common.api.account.context.AccountContext;
import org.cspoker.common.elements.player.PlayerId;

public class PSAccountContext implements AccountContext {

	private final PlayerId playerId;

	public PSAccountContext(PlayerId playerId) {
		this.playerId = playerId;
	}
	
	@Override
	public void setAvatar(byte[] avatar) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean hasPassword(String passwordHash) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public PlayerId getPlayerID() {
		return playerId;
	}
	
	@Override
	public byte[] getAvatar(PlayerId playerId) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void createAccount(String username, String passwordHash) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void changePassword(String passwordHash) {
		throw new UnsupportedOperationException();
	}

}
