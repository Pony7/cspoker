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
package org.cspoker.client.allcommunication;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.cspoker.client.common.CommunicationProvider;
import org.cspoker.client.rmi.RemotePlayerCommunicationFactoryForRMI;
import org.cspoker.client.xml.http.RemotePlayerCommunicationFactoryForHttp;
import org.cspoker.client.xml.sockets.RemotePlayerCommunicationFactoryForSocket;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class LoadProvidersFromXml {
	
	private static Logger logger = Logger.getLogger(LoadProvidersFromXml.class);
	private final CommunicationProvider provider;

	public LoadProvidersFromXml(CommunicationProvider provider) {
		this("org/cspoker/client/allcommunication/providers.xml", provider);
	}

	public LoadProvidersFromXml(String file, CommunicationProvider provider) {
		this.provider = provider;
		XMLReader xr;
		try {
			xr = XMLReaderFactory.createXMLReader();
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);
			throw new IllegalArgumentException("Error creating XML parser.", e);
		}
		DefaultHandler handler = getHandler();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);

		InputStream is = getClass().getClassLoader().getResourceAsStream(file);
		InputSource source = new InputSource(is);

		try {
			xr.parse(source);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new IllegalStateException(
					"Error reading authentication file: " + e.getMessage());
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);
			throw new IllegalStateException("Error parsing XML: "
					+ e.getMessage());
		}
	}

	private DefaultHandler getHandler() {
		return new DefaultHandler() {
			
			public void startElement(String uri, String localName, String name,
					Attributes attributes) throws SAXException {
				if(localName.equals("provider")){
					String address = attributes.getValue("address");
					int port = Integer.parseInt(attributes.getValue("port"));
					String type = attributes.getValue("type");
					if(type.equals("rmi")){
						provider.addRemotePlayerCommunicationProvider(new RemotePlayerCommunicationFactoryForRMI(address, port));
					}else if(type.equals("http")){
						provider.addRemotePlayerCommunicationProvider(new RemotePlayerCommunicationFactoryForHttp(address, port));
					}else if(type.equals("socket")){
						provider.addRemotePlayerCommunicationProvider(new RemotePlayerCommunicationFactoryForSocket(address, port));
					}else{
						throw new SAXException("Unknown provider type: "+type);
					}
				}
			}
		};
	}
	
}
