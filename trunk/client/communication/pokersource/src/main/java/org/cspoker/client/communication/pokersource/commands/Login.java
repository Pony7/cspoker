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
package org.cspoker.client.communication.pokersource.commands;

import org.cspoker.client.communication.pokersource.GeneralPacket;
import org.cspoker.client.communication.pokersource.JSONPacket;
import org.cspoker.client.communication.pokersource.eventlisteners.general.GeneralEventListener;
import org.cspoker.client.communication.pokersource.eventlisteners.poker.PokerEventListener;


public class Login extends GeneralPacket{
	
	public Login(String user, String password) {
		this.user = user;
		this.password = password;
	}
	
	private final String user;
	private final String password;
	
	@Override
	public void signal(GeneralEventListener listener) {
		throw new IllegalStateException("This is not an event");
	}
	
	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getType() {
		return "PacketLogin";
	}
	
}
