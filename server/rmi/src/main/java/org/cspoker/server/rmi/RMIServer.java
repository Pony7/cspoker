/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
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
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.shared.Trigger;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.server.common.util.threading.RequestExecutor;
import org.cspoker.server.rmi.asynchronous.context.AsynchronousServerContext;
import org.cspoker.server.rmi.export.ExportingServerContext;
import org.cspoker.server.rmi.unremote.context.UnremoteServerContext;

public class RMIServer
		implements RemoteCSPokerServer {
	
	private final static Logger logger = Logger.getLogger(RMIServer.class);
	
	private final int port;
	
	private final CSPokerServer cspokerServer;
	
	public RMIServer(int port, CSPokerServer cspokerServer) {
		this.port = port;
		this.cspokerServer = cspokerServer;
	}
	
	public ServerContext login(String username, String password)
			throws LoginException, RemoteException {
		ServerContext rootServer = cspokerServer.login(username, password);
		Trigger connection = new Trigger(){
			public void trigger() {
				//TODO implement
			}
		};
		RemoteServerContext context = new ExportingServerContext(
				new UnremoteServerContext(connection, new AsynchronousServerContext(new SequencePreservingExecutor(
						RequestExecutor.getInstance()), rootServer)));
		try {
			UnicastRemoteObject.unexportObject(context, true);
		} catch (NoSuchObjectException e) {
			// ignore
		}
		ServerContext stub = (ServerContext) UnicastRemoteObject.exportObject(context, 0);
		return stub;
	}
	
	public void start()
			throws AccessException, RemoteException {
		System.setSecurityManager(null);
		ExecutorService executor = RequestExecutor.getInstance();
		
		try {
			executor.submit(new Callable<Void>() {
				
				public Void call()
						throws RemoteException {
					Registry registry = LocateRegistry.createRegistry(port);
					RemoteCSPokerServer stub = (RemoteCSPokerServer) UnicastRemoteObject.exportObject(RMIServer.this, 0);
					registry.rebind("CSPokerServer", stub);
					logger.info("Started RMI server at port " + port);
					return null;
				}
				
				@Override
				public String toString() {
					return "RMI Server startup at port " + port;
				}
				
			}).get();
		} catch (InterruptedException e) {
			logger.error(e);
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			logger.error(e);
			throw new RemoteException(e.getMessage(), e);
		}
	}
	
}
