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
package org.cspoker.server.xml.sockets.security;

import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.game.player.IllegalNameException;
import org.cspoker.server.xml.sockets.ClientContext;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SocketsAuthenticator{

    private static Logger logger = Logger.getLogger(SocketsAuthenticator.class);
    
    private XmlFileAuthenticator auth;

    public final static String POSITIVE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<login/>";

    public SocketsAuthenticator(XmlFileAuthenticator auth) {
	this.auth = auth;
    }

    public boolean authenticate(ClientContext context, String xml){
	try {
	    XMLReader xr = XMLReaderFactory.createXMLReader();
	    LoginHandler handler = new LoginHandler();
	    xr.setContentHandler(handler);
	    xr.parse(new InputSource(new StringReader(xml)));
	    if(!auth.hasPassword(handler.getUsername(), handler.getPassword())){
		logger.info("login failed for "+handler.getUsername());
		context.closeConnection();
		return false;
	    }
	    context.login(handler.getUsername(), handler.getPassword(), handler.getUseragent());
	    context.send(SocketsAuthenticator.POSITIVE_RESPONSE);
	    return true;
	} catch(SAXException e){
	    logger.error("error parsing login: "+e.getMessage());
	    context.closeConnection();
	    return false;
	} catch (IOException e) {
	    logger.error("error parsing login: "+e.getMessage());
	    context.closeConnection();
	    return false;
	} catch (IllegalNameException e) {
	    logger.error("bad username: "+e.getMessage());
	    context.closeConnection();
	    return false;
	}
    }

    private class LoginHandler extends DefaultHandler{

	private String useragent="unknown";
	private String username="John Doe";
	private String password="";

	public String getUseragent() {
	    return useragent;
	}

	public String getUsername() {
	    return username;
	}

	public String getPassword() {
	    return password;
	}

	@Override
	public void startElement(String uri, String localName, String name,
		Attributes attributes) throws SAXException {
	    if (name.equals("login")) {
		if(attributes.getValue("useragent")!=null)
		    useragent = attributes.getValue("useragent");
		if(attributes.getValue("username")!=null)
		    username = attributes.getValue("username");
		if(attributes.getValue("password")!=null)
		password = attributes.getValue("password");
	    }
	}

    }

}
