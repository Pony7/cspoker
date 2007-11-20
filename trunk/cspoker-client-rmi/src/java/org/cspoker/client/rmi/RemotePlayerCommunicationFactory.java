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
package org.cspoker.client.rmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.cspoker.common.game.IllegalActionException;
import org.cspoker.common.game.RemotePlayerCommunication;
import org.cspoker.common.game.elements.table.TableId;
import org.cspoker.common.game.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.rmi.RemoteRMIServer;


public class RemotePlayerCommunicationFactory {

    private RemoteRMIServer server;

    public RemotePlayerCommunicationFactory(String server) throws AccessException, RemoteException, NotBoundException{
	System.setSecurityManager(null);
	Registry registry= LocateRegistry.getRegistry(server);
	this.server = (RemoteRMIServer)registry.lookup("CSPokerServer");
    }

    public RemotePlayerCommunication login(String username, String password) throws RemoteException{

	final RemotePlayerCommunication p =  server.login(username, password);

	return new RemotePlayerCommunication(){

	    public void allIn() throws IllegalActionException, RemoteException {
		p.allIn();
	    }

	    public void bet(int amount) throws IllegalActionException,
	    RemoteException {
		p.bet(amount);

	    }

	    public void call() throws IllegalActionException, RemoteException {
		p.call();
	    }

	    public void check() throws IllegalActionException, RemoteException {
		p.check();
	    }

	    public TableId createTable() throws IllegalActionException,RemoteException {
		return p.createTable();
	    }

	    public void deal() throws IllegalActionException, RemoteException {
		p.deal();

	    }

	    public void fold() throws IllegalActionException, RemoteException {
		p.fold();
	    }

	    public void joinTable(TableId id) throws IllegalActionException,
	    RemoteException {
		p.joinTable(id);
	    }

	    public void leaveTable() throws IllegalActionException,
	    RemoteException {
		p.leaveTable();
	    }

	    public void raise(int amount) throws IllegalActionException,
	    RemoteException {
		p.raise(amount);
	    }

	    public void say(String message) throws RemoteException {
		p.say(message);
	    }

	    public void startGame() throws IllegalActionException,
	    RemoteException {
		p.startGame();
	    }

	    public void subscribeAllEventsListener(
		    RemoteAllEventsListener listener) throws RemoteException {
		RemoteAllEventsListener listenerStub 
		= (RemoteAllEventsListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeAllEventsListener(listenerStub);
	    }

	    public void unsubscribeAllEventsListener(
		    RemoteAllEventsListener listener) throws RemoteException {
		p.unsubscribeAllEventsListener(listener);
	    }

	};
    }

}
