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
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.common.RemoteLoginServer;
import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.common.game.session.PlayerKilledExcepion;
import org.cspoker.server.common.game.session.SessionManager;
import org.cspoker.server.common.util.threading.RequestExecutor;

public class RMIServer implements RemoteLoginServer {

	private final static Logger logger = Logger.getLogger(RMIServer.class);

	private final XmlFileAuthenticator authenticator;

	private int port;

	public RMIServer(int port){
		this(port, new XmlFileAuthenticator());
	}
	
	public RMIServer(int port, XmlFileAuthenticator authenticator) {
		this.authenticator = authenticator;
		this.port = port;
	}

	public RemotePlayerCommunication login(String username, String password)
			throws RemoteException, LoginException {
		logger.debug("Login attempt from " + username);
		if (authenticator.hasPassword(username, password)) {
			try {
				RemotePlayerCommunication p = SessionManager.global_session_manager
						.getSession(username).getPlayerCommunication();
				try {
					UnicastRemoteObject.unexportObject(p, true);
				} catch (NoSuchObjectException e) {
					// ignore
				}
				RemotePlayerCommunication stub = (RemotePlayerCommunication) UnicastRemoteObject
						.exportObject(p, 0);
				return stub;
			} catch (PlayerKilledExcepion e) {
				// bye bye bad client
				return null;
			}
		} else {
			logger.debug("Login attempt from " + username + " failed");
			throw new LoginException("Login Failed");
		}
	}

	public void start() throws AccessException, RemoteException {
		System.setSecurityManager(null);
		ExecutorService executor = RequestExecutor.getInstance();

		try {
			executor.submit(new Callable<Void>(){

				
				public Void call() throws RemoteException{
					Registry registry = LocateRegistry.createRegistry(port);
					RemoteLoginServer stub = (RemoteLoginServer) UnicastRemoteObject.exportObject(RMIServer.this, 0);
					registry.rebind("CSPokerServer", stub);
					logger.info("Started RMI server at port "+port);
					return null;
				}
				
				
				public String toString() {
					return "RMI Server startup at port "+port;
				}
				
			}).get();
		} catch (InterruptedException e) {
			logger.error(e);
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			logger.error(e);
			throw (RemoteException)(e.getCause());
		}
	}

}
