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

import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.cspoker.common.CSPokerServer;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.external.pokersource.PokersourceConnection;
import org.cspoker.external.pokersource.commands.Login;
import org.cspoker.external.pokersource.eventlisteners.all.DefaultListener;
import org.cspoker.external.pokersource.events.AuthOk;
import org.cspoker.external.pokersource.events.Error;
import org.cspoker.external.pokersource.events.Serial;

public class PokersourceServer implements CSPokerServer {

	private final String server;

	public PokersourceServer(String server) {
		this.server = server;
	}
	
	@Override
	public ServerContext login(String username, String password) throws LoginException {
		try {
			final PokersourceConnection conn = new PokersourceConnection(server);
			class LoginResult extends DefaultListener{
				
				private boolean success = false;
				private String msg;
				private int serial;
				
				@Override
				public void onAuthOk(AuthOk authOk) {
					success = true;
				}
				
				@Override
				public void onError(Error error) {
					msg = error.getMessage();
				}
				
				@Override
				public void onSerial(Serial serial) {
					this.serial = serial.getSerial();
				}
				
				private ServerContext getResult() throws LoginException {
					if(!success) throw new LoginException(msg);
					conn.removeListener(this);
					return new PSServerContext(conn,serial);
				}
			}
			LoginResult listener = new LoginResult();
			conn.addListener(listener);
			conn.send(new Login(username, password));
			return listener.getResult();
		} catch (IOException e) {
			throw new LoginException(e.getMessage());
		}
	}
	
	

}
