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
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.cspoker.client.exceptions.JoinTableFailedException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class JoinTableHandler extends DefaultHandler {

    private URL url;
    private LoginHandler loginHandler;
    private List<String> players=new ArrayList<String>();
    
    public JoinTableHandler(String server, LoginHandler loginHandler) {
	try {
	    this.url = new URL(server+"jointable");
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	this.loginHandler = loginHandler;
    }

    public void joinTable(String tableName) throws IOException, JoinTableFailedException {
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
	    String comment = "CSPoker jointable request";
	    request.comment(comment.toCharArray(), 0, comment.length());
	    Attributes noattrs = new AttributesImpl();
	    request.startElement("", "jointable", "jointable", noattrs);
	    request.startElement("", "id", "id", noattrs);
	    request.characters(String.valueOf(loginHandler.getId()).toCharArray(), 0
		    , String.valueOf(loginHandler.getId()).length());
	    request.endElement("", "id", "id");
	    request.startElement("", "table", "table", noattrs);
	    request.characters(tableName.toCharArray(), 0
		    , tableName.length());
	    request.endElement("", "table", "table");
	    request.endElement("", "jointable", "jointable");
	    request.endDocument();
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TransformerConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	connection.getOutputStream().flush();
	
	try {
	    XMLReader xr = XMLReaderFactory.createXMLReader();
	    xr.setContentHandler(this);
	    xr.setErrorHandler(this);
	    xr.parse(new InputSource(connection.getInputStream()));
	} catch (SAXException e) {
	    if (e instanceof JoinTableFailedException) {
		throw (JoinTableFailedException) e;
	    }else {
		throw new JoinTableFailedException(e.getMessage());
	    }
	}
    }

    private StringBuilder chars = new StringBuilder();

    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	if ("jointable".equals(qName)||"players".equals(qName)
		||"player".equals(qName)||"exception".equals(qName)
		||"status".equals(qName)) {
	    // no op
	} else {
	    throw new SAXException("Illegal syntax: " + qName);
	}
	chars.setLength(0);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	if ("jointable".equals(qName)||"players".equals(qName)) {
	    // no op
	} else if ("status".equals(qName)) {
	    // no op
	} else if ("player".equals(qName)) {
	    players.add(chars.toString());
	} else if ("exception".equals(qName)) {
	    throw new JoinTableFailedException(chars.toString());
	} else {
	    throw new SAXException("Illegal syntax: " + qName);
	}
	chars.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
	chars.append(ch, start, length);
    }

    public List<String> getOtherPlayerNames() {
	return players;
    }

}
