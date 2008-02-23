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
package org.cspoker.client.xml.common;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.exceptions.IllegalActionException;

public class XmlChannelRemotePlayerCommunication implements
RemotePlayerCommunication {
	
	private final XmlChannel c;

	private final Set<RemoteAllEventsListener> listeners = new ConcurrentSkipListSet<RemoteAllEventsListener>(); 
	
	public XmlChannelRemotePlayerCommunication(XmlChannel c) {
		this.c = c;
		c.registerXmlEventListener(new XmlTranslatingEventListener(listeners));
	}

	public void allIn() throws IllegalActionException, RemoteException {
		c.send(wrap("<allin/>"));
	}

	public void bet(int amount) throws IllegalActionException,
	RemoteException {
		c.send(wrap("<allIn amount=\""+amount+"\">"));
	}

	public void call() throws IllegalActionException, RemoteException {
		c.send(wrap("<call/>"));
	}

	public void check() throws IllegalActionException, RemoteException {
		c.send(wrap("<check/>"));
	}

	public void createTable() throws IllegalActionException,
	RemoteException {
		c.send(wrap("<createtable/>"));
	}

	public void deal() throws IllegalActionException, RemoteException {
		c.send(wrap("<deal/>"));

	}

	public void fold() throws IllegalActionException, RemoteException {
		c.send(wrap("<fold/>"));
	}

	public void joinTable(TableId id) throws IllegalActionException,
	RemoteException {
		c.send(wrap("<jointable id=\""+id.getID()+"/>"));
	}

	public void leaveTable() throws IllegalActionException,
	RemoteException {
		c.send(wrap("<leavetable/>"));
	}

	public void raise(int amount) throws IllegalActionException,
	RemoteException {
		c.send(wrap("<raise amount=\""+amount+"/>"));
	}

	public void say(String message) throws IllegalActionException,RemoteException {
		c.send(wrap("<say>"+message+"</say>"));
	}

	public void startGame() throws IllegalActionException,
	RemoteException {
		c.send(wrap("<startgame/>"));
	}

	@Override
	public void kill() throws IllegalActionException, RemoteException {
		c.send(wrap("<kill/>"));				
	}

	public void subscribeAllEventsListener(
			RemoteAllEventsListener listener) throws RemoteException {
		listeners.add(listener);
	}

	public void unsubscribeAllEventsListener(
			RemoteAllEventsListener listener) throws RemoteException {
		listeners.remove(listener);
	}

	protected String wrap(String string) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<command>"+string+"</command>";
	}
}
