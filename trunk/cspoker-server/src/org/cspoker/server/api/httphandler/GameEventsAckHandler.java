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


import java.util.List;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.server.api.PlayerCommunicationFactory;
import org.cspoker.server.api.httphandler.abstracts.HttpHandlerImpl;
import org.cspoker.server.api.httphandler.abstracts.RequestStreamHandler;
import org.cspoker.server.api.httphandler.exception.HttpSaxException;
import org.cspoker.server.game.events.GameEvent;
import org.cspoker.server.game.gameControl.actions.IllegalActionException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.net.httpserver.HttpExchange;

public class GameEventsAckHandler extends RequestStreamHandler {

    @Override
    protected ContentHandler getRequestHandler(final HttpExchange http, final TransformerHandler response){
	
	return new DefaultHandler(){
	    
	    private StringBuilder sb=new StringBuilder();
	    private String ack;
	    
	    
	    @Override
	    public void startElement(String uri, String localName, String name,
	            Attributes attributes) throws SAXException {
	        sb.setLength(0);
	    }
	    
	    @Override
	    public void endElement(String uri, String localName, String name)
	            throws SAXException {
	        if(name.equalsIgnoreCase("ack"))
	            ack=sb.toString();
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
		List<GameEvent> events;
		try {
		    events=PlayerCommunicationFactory.getRegisteredPlayerCommunication(username)
		        	.getLatestGameEventsAndAck(Long.parseLong(ack));
		} catch (NumberFormatException e) {
		    throw new HttpSaxException(e, 400);
		} catch (IllegalActionException e) {
		    throw new HttpSaxException(e, 403);
		}
		response.startElement("", "events", "events", new AttributesImpl());
		for(GameEvent event:events){
		    AttributesImpl attrs = new AttributesImpl();
		    attrs.addAttribute("", "id", "id", "CDATA", "0");
		    response.startElement("", "event", "event", attrs);

		    String s=event.toString();
		    response.characters(s.toCharArray(), 0, s.length());
		    
		    response.endElement("", "event", "event");
		}
		response.endElement("", "events", "events");
	    }
	};
    }

    @Override
    protected int getDefaultStatusCode() {
	return 200;
    }

}
