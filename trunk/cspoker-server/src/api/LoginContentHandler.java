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
package api;

import java.io.OutputStream;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class LoginContentHandler extends DefaultHandler {

    private LoginHandler handler;

    private boolean started = false;

    private boolean loginstarted = false;

    private boolean userstarted = false;

    private StringBuilder chars = new StringBuilder();

    private String username = "defaultuser";

    private OutputStream out;

    public LoginContentHandler(LoginHandler handler, OutputStream out) {
	this.handler = handler;
	this.out = out;
    }

    @Override
    public void startDocument() throws SAXException {
	started = true;
    }

    @Override
    public void endDocument() throws SAXException {
	started = false;
	respond();
    }

    private void respond() {
	StreamResult requestResult = new StreamResult(out);
	SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
		.newInstance();
	TransformerHandler request;

	long id;
	try {
	    id = handler.login(username);
	} catch (RuntimeException e) {
	    try {
		request = tf.newTransformerHandler();
		request.setResult(requestResult);
		request.startDocument();
		String comment = "CSPoker login response";
		request.comment(comment.toCharArray(), 0, comment.length());
		Attributes noattrs = new AttributesImpl();
		request.startElement("", "login", "login", noattrs);
		request.startElement("", "exception", "exception", noattrs);
		request.characters(e.getMessage().toCharArray(), 0, e
			.getMessage().length());
		request.endElement("", "exception", "exception");
		request.endElement("", "login", "login");
		request.endDocument();
	    } catch (SAXException f) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (TransformerConfigurationException f) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    return;
	}

	try {
	    request = tf.newTransformerHandler();
	    request.setResult(requestResult);
	    request.startDocument();
	    String comment = "CSPoker login response";
	    request.comment(comment.toCharArray(), 0, comment.length());
	    Attributes noattrs = new AttributesImpl();
	    request.startElement("", "login", "login", noattrs);
	    request.startElement("", "id", "id", noattrs);
	    request.characters(String.valueOf(id).toCharArray(), 0, String
		    .valueOf(id).length());
	    request.endElement("", "id", "id");
	    request.endElement("", "login", "login");
	    request.endDocument();
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TransformerConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	if ("login".equals(qName)) {
	    loginstarted = true;
	} else if ("username".equals(qName)) {
	    userstarted = true;
	} else {
	    throw new SAXException("Illegal syntax:" + qName);
	}
	chars.setLength(0);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	if ("login".equals(qName)) {
	    loginstarted = false;
	} else if ("username".equals(qName)) {
	    userstarted = false;
	    username = chars.toString();
	} else {
	    throw new SAXException("Illegal syntax:" + qName);
	}
	chars.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	chars.append(ch, start, length);

    }

}
