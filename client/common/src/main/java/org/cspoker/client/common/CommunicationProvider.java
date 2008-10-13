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
package org.cspoker.client.common;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.shared.context.RemoteServerContext;

public class CommunicationProvider implements RemoteCSPokerServer {

	public final static CommunicationProvider global_provider = new CommunicationProvider();

	private List<RemoteCSPokerServer> servers = new ArrayList<RemoteCSPokerServer>();

	public void addRemoteCSPokerServer(
			RemoteCSPokerServer server) {
		servers.add(server);
	}

	public List<RemoteCSPokerServer> getProviders() {
		return Collections.unmodifiableList(servers);
	}

	public RemoteServerContext login(String username, String password) throws LoginException, RemoteException  {

		RemoteException lastRemoteException = null;
		LoginException lastLoginException = null;
		
		for (RemoteCSPokerServer p : servers) {
			try {
				return p.login(username, password);
			} catch (RemoteException e) {
				lastRemoteException = e;
			} catch(LoginException e){
				lastLoginException = e;
			}
		}
		if (lastLoginException != null) {
			throw lastLoginException;
		}else if(lastRemoteException != null){
			throw lastRemoteException;
		}else{
			throw new IllegalStateException("No servers are registered");
		}
	}

}
