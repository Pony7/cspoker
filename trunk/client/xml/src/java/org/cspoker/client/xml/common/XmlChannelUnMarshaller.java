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

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.events.Event;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.exceptions.NoListenerException;
import org.cspoker.common.xml.EventAndActionJAXBContext;
import org.cspoker.common.xml.XmlEventListener;
import org.cspoker.common.xml.actions.PlayerCommunicationAction;
import org.cspoker.common.xml.events.invocation.IllegalActionEvent;
import org.cspoker.common.xml.events.invocation.SuccessfulInvocationEvent;

public class XmlChannelUnMarshaller implements XmlEventListener {

	private final static Logger logger = Logger
			.getLogger(XmlChannelUnMarshaller.class);

	private final RemoteAllEventsListener listener;

	private final Map<PlayerCommunicationAction<?>, IllegalActionEvent> illegalactionevents = new HashMap<PlayerCommunicationAction<?>, IllegalActionEvent>();

	private final Map<PlayerCommunicationAction<?>, SuccessfulInvocationEvent<?>> successfulinvocationevents = new ConcurrentHashMap<PlayerCommunicationAction<?>, SuccessfulInvocationEvent<?>>();

	public XmlChannelUnMarshaller(XmlChannel channel,
			RemoteAllEventsListener spreadingAllEventsListener) {
		listener = spreadingAllEventsListener;
		channel.registerXmlEventListener(this);
	}

	@Override
	public void collect(String xmlEvent) {
		try {
			Unmarshaller um = EventAndActionJAXBContext.context
			.createUnmarshaller();
			um.setSchema(null);
			collect((Event) um.unmarshal(new StringReader(xmlEvent.trim())));
		} catch (JAXBException e) {
			logger.fatal("Parsing failed:\n"+xmlEvent,e);
			throw new IllegalStateException("Parsing failed:\n"+xmlEvent,e);
		} 
	}

	public void collect(Event event) {
		try {
			event.dispatch(listener);
		} catch (NoListenerException e) {
			if (event instanceof SuccessfulInvocationEvent<?>) {
				SuccessfulInvocationEvent<?> se = (SuccessfulInvocationEvent<?>) event;
				if (se.getAction().getID() > 0) {
					handle(se);
				}
			} else if (event instanceof IllegalActionEvent) {
				handle((IllegalActionEvent) event);
			} else {
				logger.fatal(e);
				throw new IllegalStateException("Unknown event from server.");
			}
		} catch (RemoteException e) {
			logger.error(e);
		}
	}

	private void handle(IllegalActionEvent event) {
		illegalactionevents.put(event.getAction(), event);
		synchronized (this) {
			notifyAll();
		}
	}

	private void handle(SuccessfulInvocationEvent<?> event) {
		successfulinvocationevents.put(event.getAction(), event);
		synchronized (this) {
			notifyAll();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T waitForExecutionEnd(PlayerCommunicationAction<T> action)
			throws InterruptedException, IllegalActionException {
		synchronized (this) {
			while (!successfulinvocationevents.containsKey(action)
					&& !illegalactionevents.containsKey(action)) {
				wait();
			}
		}
		if (illegalactionevents.containsKey(action)) {
			throw illegalactionevents.remove(action).getException();
		} else if (successfulinvocationevents.containsKey(action)) {
			// Java type inference sucks. Give me Haskell!
			return (T) successfulinvocationevents.remove(action).getResult();
		} else {
			logger.fatal("Missing event.");
			throw new IllegalStateException("Missing event.");
		}
	}

}
