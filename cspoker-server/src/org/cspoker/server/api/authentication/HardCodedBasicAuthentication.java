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
package org.cspoker.server.api.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.sun.net.httpserver.BasicAuthenticator;

public class HardCodedBasicAuthentication extends BasicAuthenticator {

    private HashMap<String, String> passwords;


    public HardCodedBasicAuthentication() {
	super("cspoker");
	XMLReader xr;
	try {
	    xr = XMLReaderFactory.createXMLReader();
	} catch (SAXException e) {
	    throw new IllegalStateException("Error creating XML parser.");
	}
	DefaultHandler handler=getHandler();
	xr.setContentHandler(handler);
	xr.setErrorHandler(handler);

	InputStream is = getClass().getClassLoader().getResourceAsStream("org/cspoker/server/api/authentication/authentication.xml");

	try {
	    xr.parse(new InputSource(is));
	} catch (IOException e) {
	    throw new IllegalStateException("Error reading authentication file.");
	} catch (SAXException e) {
	    throw new IllegalStateException("Error parsing XML.");
	}
    }

    private DefaultHandler getHandler() {
	return new DefaultHandler(){

	    private StringBuilder sb=new StringBuilder();

	    @Override
	    public void startDocument() throws SAXException {
		passwords=new HashMap<String, String>();
	    }

	    @Override
	    public void characters(char[] ch, int start, int length)
	    throws SAXException {
		sb.append(ch, start, length);
	    }

	    @Override
	    public void startElement(String uri, String localName, String name,
		    Attributes attributes) throws SAXException {
		sb.setLength(0);
	    }

	    private String lastname;

	    @Override
	    public void endElement(String uri, String localName, String name)
	    throws SAXException {
		if(name.equalsIgnoreCase("name")){
		    lastname=sb.toString();
		}else if(name.equalsIgnoreCase("password")){
		    passwords.put(lastname, sb.toString());
		    System.out.println("Added credentials for "+lastname);
		}
		sb.setLength(0);
	    }

	};
    }

    @Override
    public boolean checkCredentials(String user, String pass) {
	boolean ok=false;
	if(pass.equals(passwords.get(user))){
	    System.out.println("Authentication for "+user+" succeeded.");
	    ok=true;
	}else{
	    System.out.println("Authentication for "+user+" failed.");		
	}
	return ok;
    }


}
