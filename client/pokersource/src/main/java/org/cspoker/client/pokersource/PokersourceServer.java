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
import java.util.concurrent.CountDownLatch;

import javax.security.auth.login.LoginException;

import net.sf.json.JSONException;

import org.cspoker.client.pokersource.listeners.SerialListener;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.external.pokersource.PokersourceConnection;
import org.cspoker.external.pokersource.commands.Login;
import org.cspoker.external.pokersource.eventlisteners.all.DefaultListener;
import org.cspoker.external.pokersource.events.Serial;

public class PokersourceServer implements RemoteCSPokerServer {

	private final String server;

	public PokersourceServer(String server) {
		this.server = server;
	}
	
	@Override
	public RemoteServerContext login(String username, String password) throws LoginException {
		try {
			SerialListener serial = new SerialListener();
			TransListener transListener = new TransListener();
			final PokersourceConnection conn = new PokersourceConnection(server);
			conn.addListeners(serial, transListener);
			conn.send(new Login(username, password));
			serialObtained.await();
			conn.removeListeners(serial, transListener);
			return new PSServerContext(conn, serial.getSerial());
		} catch (IOException e) {
			throw new LoginException(e.getMessage());
		} catch (JSONException e) {
			throw new LoginException(e.getMessage());
		} catch (InterruptedException e) {
			throw new LoginException(e.getMessage());
		}
	}
	
	private volatile int serial;
	private final CountDownLatch serialObtained = new CountDownLatch(1);
	
	private class TransListener extends DefaultListener{
		
		@Override
		public void onSerial(Serial serial2) {
			serial = serial2.getSerial();
			serialObtained.countDown();
		}
		
	}
	
	

}
