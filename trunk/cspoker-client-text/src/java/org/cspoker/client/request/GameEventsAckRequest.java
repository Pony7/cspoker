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
package org.cspoker.client.request;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.client.request.abstracts.OutputRequest;
import org.cspoker.client.request.contenthandler.EventsContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class GameEventsAckRequest extends OutputRequest{
    
    private EventsContentHandler contentHandler;

    public GameEventsAckRequest(String address) throws MalformedURLException {
	super(address);
	this.contentHandler = new EventsContentHandler();
    }

    @Override
    protected void doOutput(TransformerHandler request, String... args) throws SAXException {
	if(args.length<1)
	    throw new IllegalArgumentException("Not enough arguments.");
	
	request.startElement("", "ack", "ack", new AttributesImpl());
	String s=args[0];
	request.characters(s.toCharArray(), 0, s.length());
	request.endElement("", "ack", "ack");
    }

    @Override
    protected String getPath() {
	return "/game/events/ack/";
    }
    
    @Override
    protected String getResult() {
	List<String> events = contentHandler.getEvents();
	if(events.size()==0)
	    return "No events found."+n;
	String r="";
	for(String event:events){
	    r+=event+n;
	}
	r+="Last event number is "+contentHandler.getLastID()+n;
	return r;
    }

    
    @Override
    protected ContentHandler getContentHandler() {
	return contentHandler;
    }

}
