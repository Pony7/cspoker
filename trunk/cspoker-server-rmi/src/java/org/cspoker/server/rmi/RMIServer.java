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
package org.cspoker.server.rmi;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.cspoker.common.game.RemotePlayerCommunication;
import org.cspoker.common.rmi.RemoteRMIServer;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.game.session.PlayerKilledExcepion;
import org.cspoker.server.game.session.SessionManager;

public class RMIServer implements RemoteRMIServer {

    private final XmlFileAuthenticator authenticator;
    private final int port;

    public RMIServer(XmlFileAuthenticator authenticator, int port) {
	this.authenticator = authenticator;
	this.port = port;
    }

    public RemotePlayerCommunication login(String username, String password) throws RemoteException {
	if(authenticator.hasPassword(username, password)){
	    try {
		RemotePlayerCommunication p = SessionManager.global_session_manager.getSession(username).getPlayerCommunication();
		RemotePlayerCommunication stub=(RemotePlayerCommunication)UnicastRemoteObject.exportObject(p, 0);
		return stub;
	    } catch (PlayerKilledExcepion e) {
		// bye bye bad client
		return null;
	    }
	}else{
	    throw new IllegalArgumentException("Login Failed");
	}
    }
    
    void start() throws AccessException, RemoteException {
	System.setSecurityManager(null);
	RemoteRMIServer stub=(RemoteRMIServer)UnicastRemoteObject.exportObject(this, 0);
	Registry registry= LocateRegistry.getRegistry(port);
	registry.rebind("CSPokerServer",stub);
    }

}
