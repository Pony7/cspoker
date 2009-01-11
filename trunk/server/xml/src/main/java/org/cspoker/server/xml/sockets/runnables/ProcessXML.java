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
package org.cspoker.server.xml.sockets.runnables;

import java.io.StringReader;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.socket.LoginAction;
import org.cspoker.common.jaxbcontext.ActionJAXBContext;
import org.cspoker.common.jaxbcontext.AllSocketJAXBContexts;
import org.cspoker.common.util.threading.Prioritizable;
import org.cspoker.server.xml.sockets.ClientContext;
import org.cspoker.server.xml.sockets.security.PolicyFile;

public class ProcessXML implements Runnable, Prioritizable {

	private final static Logger logger = Logger.getLogger(ProcessXML.class);

	private final String xml;
	private final ClientContext context;

	public ProcessXML(String xml, ClientContext context,
			CSPokerServer cspokerServer) {
		this.xml = xml;
		this.context = context;
	}

	public void run() {
		try {
			logger.trace("recieved xml:\n" + xml);
			if (!context.isAuthenticated()) {
				// Flash client request for authorization to connect from a
				// different host
				if (xml.startsWith(PolicyFile.request)) {
					logger.info("handling flash security manager request.");
					//context.send(PolicyFile.POLICY+ DELIM);
					context.send(PolicyFile.POLICY);
					context.killAfterResponse();
				} else {
					// Check the credentials
					logger.info("Checking login.");
					Unmarshaller um = AllSocketJAXBContexts.context.createUnmarshaller();
					context.login((LoginAction) um.unmarshal(new StringReader(xml)));
				}
			} else {
				// Perform the other requests
				logger.info("Handling request from logged in.");
				Unmarshaller um = ActionJAXBContext.context.createUnmarshaller();
				DispatchableAction<?> action = (DispatchableAction<?>) um.unmarshal(new StringReader(xml));
				context.perform(action);
			}
		} catch (JAXBException exception) {
			logger.error("JAXB Exception when handling XML chunk", exception);
		}
	}

	public int getPriority() {
		return 1;
	}

	@Override
	public String toString() {
		return "ProcessXML";
	}

}
