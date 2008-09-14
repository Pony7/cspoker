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

import java.io.StringWriter;
import java.rmi.RemoteException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.jaxbcontext.ActionJAXBContext;
import org.cspoker.common.xml.actions.Action;

public class XmlChannelMarshaller {

	private final static Logger logger = Logger
			.getLogger(XmlChannelMarshaller.class);

	private final XmlChannel channel;
	private final Marshaller marshaller;
	private final XmlChannelUnMarshaller unmarshaller;

	public XmlChannelMarshaller(XmlChannel channel,
			RemoteAllEventsListener listener) {
		this.channel = channel;
		try {
			marshaller = ActionJAXBContext.context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		} catch (JAXBException e) {
			logger.fatal(e);
			throw new IllegalStateException(e);
		}
		unmarshaller = new XmlChannelUnMarshaller(channel, listener);
	}

	public synchronized <T> T perform(Action<T> action)
			throws RemoteException, IllegalActionException {
		StringWriter w = new StringWriter();
		try {
			marshaller.marshal(action, w);
			channel.send(w.toString());
			return unmarshaller.waitForExecutionEnd(action);
		} catch (JAXBException e) {
			logger.fatal(e);
			throw new IllegalStateException(e);
		} catch (InterruptedException e) {
			logger.error(e);
			// Restore the interrupted status
			Thread.currentThread().interrupt();
			return null;
		} catch (ChannelStateException e) {
			logger.error(e);
			throw new RemoteException("Channel state is incorrect.", e);
		}

	}

}
