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

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.client.request.abstracts.OutputRequest;
import org.cspoker.client.request.contenthandler.EventsContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class GameEventsAckRequest extends OutputRequest{
    
    private EventsContentHandler contentHandler;

    public GameEventsAckRequest(String address, EventsContentHandler events) throws MalformedURLException {
	super(address);
	this.contentHandler = events;
    }

    @Override
    protected void doOutput(TransformerHandler request, String... args) throws SAXException {
	request.startElement("", "ack", "ack", new AttributesImpl());
	String s=String.valueOf(getLastId());
	request.characters(s.toCharArray(), 0, s.length());
	request.endElement("", "ack", "ack");
    }

    @Override
    protected String getPath() {
	return "/game/events/ack/";
    }
    
    @Override
    protected String getResult() {
	return "";
    }
    
    public String getLastId(){
	return contentHandler.getLastID();
    }

    
    @Override
    protected ContentHandler getContentHandler() {
	return contentHandler;
    }
    
    @Override
    public boolean requiresEventUpdate() {
        return false;
    }

}
