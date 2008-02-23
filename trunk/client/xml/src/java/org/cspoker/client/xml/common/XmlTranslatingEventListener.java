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

import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.xml.XmlEventListener;
import org.cspoker.common.xml.handler.DelegatingToOneHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlTranslatingEventListener implements XmlEventListener {

	private final static Logger logger = Logger
	.getLogger(XmlTranslatingEventListener.class);	

	private final Set<RemoteAllEventsListener> listeners;

	public XmlTranslatingEventListener(Set<RemoteAllEventsListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public void collect(String xmlEvent) {
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(new DelegatingToOneHandler(
				new EventDelegatingHandler(listeners)));
		try {
			xr.parse(xmlEvent);
		} catch (IOException e) {
			logger.error("IOException when parsing xml request.", e);
			throw new IllegalStateException(
			"IOException when parsing xml request.");
		}
	}

}
