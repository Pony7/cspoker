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

import org.cspoker.client.request.abstracts.NoOutputRequest;
import org.cspoker.client.request.contenthandler.EventsContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GameEventsRequest extends NoOutputRequest {
    
    private EventsContentHandler contentHandler;

    public GameEventsRequest(String address) throws MalformedURLException {
	super(address);

	this.contentHandler = new EventsContentHandler();
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
    protected String getPath() {
	return "/game/events/";
    }
    
    @Override
    protected ContentHandler getContentHandler() {
	return contentHandler;
    }

}
