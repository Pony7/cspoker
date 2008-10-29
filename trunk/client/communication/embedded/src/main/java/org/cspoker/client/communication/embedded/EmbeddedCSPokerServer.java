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
package org.cspoker.client.communication.embedded;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

import org.cspoker.common.CSPokerServer;
import org.cspoker.common.api.shared.context.AsynchronousServerContext;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.server.common.CSPokerServerImpl;

public class EmbeddedCSPokerServer implements CSPokerServer {

	private final CSPokerServer server = new CSPokerServerImpl();
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public ServerContext login(String username, String password)
			throws LoginException {
		return new AsynchronousServerContext(executor, server.login(username, password));
	}

}
