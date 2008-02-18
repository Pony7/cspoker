package org.cspoker.client.xml.common;

import java.rmi.RemoteException;

import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.exceptions.IllegalActionException;

public class XmlChannelRemotePlayerCommunication implements
RemotePlayerCommunication {
	
	private final XmlChannel c;

	private XmlChannelRemotePlayerCommunication(XmlChannel c) {
		this.c = c;
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
//		TODO
	}

	public void unsubscribeAllEventsListener(
			RemoteAllEventsListener listener) throws RemoteException {
//		TODO
	}

	protected String wrap(String string) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<command>"+string+"</command>";
	}
}
