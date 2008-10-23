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
package org.cspoker.server.allcommunication;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.AccessException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.server.common.CSPokerServerImpl;
import org.cspoker.server.rmi.RMIServer;
import org.cspoker.server.xml.http.HttpServer;
import org.cspoker.server.xml.sockets.SocketServer;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class RunCSPoker {

	static {
		Log4JPropertiesLoader
				.load("org/cspoker/server/allcommunication/logging/log4j.properties");
	}

	private static Logger logger = Logger.getLogger(RunCSPoker.class);

	public static void main(String[] args) {
		new RunCSPoker();
	}

	/**
	 * Hack to prevent GC of Remote Object
	 */
	private static RMIServer rmiserver;
	
	private final CSPokerServer cspokerServer;

	public RunCSPoker() {
		this("org/cspoker/server/allcommunication/servers.xml");
	}

	public RunCSPoker(String file) {
		XMLReader xr;
		try {
			xr = XMLReaderFactory.createXMLReader();
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);
			throw new IllegalArgumentException("Error creating XML parser.", e);
		}
		
		cspokerServer = new CSPokerServerImpl();
		
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
				if (localName.equals("server")) {
					int port = Integer.parseInt(attributes.getValue("port"));
					String type = attributes.getValue("type");
					if (type.equals("rmi")) {
						try {
							// need to do this in two steps to prevent GC!!
							rmiserver = new RMIServer(port, cspokerServer);
							rmiserver.start();
						} catch (AccessException e) {
							logger.warn("Failed to start RMI server at port "
									+ port, e);
						} catch (RemoteException e) {
							logger.warn("Failed to start RMI server at port "
									+ port, e);
						}
					} else if (type.equals("http")) {
						try {
							(new HttpServer(port, cspokerServer)).start();
						} catch (RemoteException e) {
							logger.warn("Failed to start RMI server at port "
									+ port, e);
						}
					} else if (type.equals("socket")) {
						try {
							(new SocketServer(port, cspokerServer)).start();
						} catch (RemoteException e) {
							logger.warn("Failed to start RMI server at port "
									+ port, e);
						}
					} else {
						throw new SAXException("Unknown provider type: " + type);
					}
				}
			}
		};
	}

}
