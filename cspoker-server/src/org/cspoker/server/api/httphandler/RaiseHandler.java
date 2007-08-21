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
package org.cspoker.server.api.httphandler;


import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.server.api.PlayerCommunicationFactory;
import org.cspoker.server.api.httphandler.abstracts.HttpHandlerImpl;
import org.cspoker.server.api.httphandler.abstracts.RequestStreamHandler;
import org.cspoker.server.api.httphandler.exception.HttpSaxException;
import org.cspoker.server.game.gameControl.IllegalActionException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.net.httpserver.HttpExchange;

public class RaiseHandler extends RequestStreamHandler {

    @Override
    protected ContentHandler getRequestHandler(final HttpExchange http, final TransformerHandler response){
	
	return new DefaultHandler(){
	    
	    private StringBuilder sb=new StringBuilder();
	    private String amount;
	    
	    
	    @Override
	    public void startElement(String uri, String localName, String name,
	            Attributes attributes) throws SAXException {
	        sb.setLength(0);
	    }
	    
	    @Override
	    public void endElement(String uri, String localName, String name)
	            throws SAXException {
	        if(name.equalsIgnoreCase("amount"))
	            amount=sb.toString();
		sb.setLength(0);
	    }
	    
	    @Override
	    public void characters(char[] ch, int start, int length)
	            throws SAXException {
		sb.append(ch, start, length);
	    }
	    
	    @Override
	    public void endDocument() throws SAXException {
		String username= HttpHandlerImpl.toPlayerName(http.getRequestHeaders());

		try {
		    PlayerCommunicationFactory.getRegisteredPlayerCommunication(username)
		        	.raise(Integer.parseInt(amount));
		} catch (NumberFormatException e) {
		    throw new HttpSaxException(e, 400);
		} catch (IllegalActionException e) {
		    throw new HttpSaxException(e, 403);
		}

		response.startElement("", "ok", "ok", new AttributesImpl());
		response.endElement("", "ok", "ok");
	    }
	
	};
    }

    @Override
    protected int getDefaultStatusCode() {
	return 200;
    }

}
