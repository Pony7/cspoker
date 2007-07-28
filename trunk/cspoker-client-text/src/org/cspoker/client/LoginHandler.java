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
package org.cspoker.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.cspoker.client.exceptions.LoginFailedException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class LoginHandler extends DefaultHandler {

    private URL url;

    private long id;

    public LoginHandler(String server) {
	try {
	    this.url = new URL(server+"login");
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void login(String username) throws IOException, LoginFailedException {

	URLConnection connection = url.openConnection();
	connection.setUseCaches(false);
	connection.setDoOutput(true);

	StreamResult requestResult = new StreamResult(connection
		.getOutputStream());
	SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
		.newInstance();
	TransformerHandler request;

	try {
	    request = tf.newTransformerHandler();
	    request.setResult(requestResult);
	    request.startDocument();
	    String comment = "CSPoker login request";
	    request.comment(comment.toCharArray(), 0, comment.length());
	    Attributes noattrs = new AttributesImpl();
	    request.startElement("", "login", "login", noattrs);
	    request.startElement("", "username", "username", noattrs);
	    request.characters(username.toCharArray(), 0, username.length());
	    request.endElement("", "username", "username");
	    request.endElement("", "login", "login");
	    request.endDocument();
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TransformerConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	try {
	    XMLReader xr = XMLReaderFactory.createXMLReader();
	    xr.setContentHandler(this);
	    xr.setErrorHandler(this);
	    xr.parse(new InputSource(connection.getInputStream()));
	} catch (SAXException e) {
	    if (e instanceof LoginFailedException) {
		throw (LoginFailedException) e;
	    }else
		throw new LoginFailedException(e.getMessage());
	}
    }

    public long getId() {
	return id;
    }

    private StringBuilder chars = new StringBuilder();

    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	if ("login".equals(qName)||"id".equals(qName)||"exception".equals(qName)) {
	    // no op
	} else {
	    throw new SAXException("Illegal syntax:" + qName);
	}
	chars.setLength(0);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	if ("login".equals(qName)) {
	    // no op
	} else if ("id".equals(qName)) {
	    id = Long.parseLong(chars.toString());
	} else if ("exception".equals(qName)) {
	    throw new LoginFailedException(chars.toString());
	} else {
	    throw new SAXException("Illegal syntax:" + qName);
	}
	chars.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
	chars.append(ch, start, length);
    }

}
