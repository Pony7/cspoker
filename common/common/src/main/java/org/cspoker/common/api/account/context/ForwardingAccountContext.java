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

import org.cspoker.common.elements.player.PlayerId;


public class ForwardingAccountContext implements AccountContext{

	private AccountContext accountContext;

	public ForwardingAccountContext(AccountContext accountContext) {
		this.accountContext  = accountContext;
	}

	public void changePassword(String passwordHash) {
		accountContext.changePassword(passwordHash);
	}
	
	public boolean hasPassword(String passwordHash) {
		return accountContext.hasPassword(passwordHash);
	}

	public void createAccount(String username, String passwordHash) {
		accountContext.createAccount(username, passwordHash);
	}

	public byte[] getAvatar(PlayerId playerId) {
		return accountContext.getAvatar(playerId);
	}

	public void setAvatar(byte[] avatar) {
		accountContext.setAvatar(avatar);
	}

	public PlayerId getPlayerID() {
		return accountContext.getPlayerID();
	}
	
}
