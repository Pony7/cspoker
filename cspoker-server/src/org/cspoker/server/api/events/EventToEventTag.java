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
package org.cspoker.server.api.events;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.server.game.events.GameEvent;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class EventToEventTag {

    public void transform(TransformerHandler response, GameEvent event) throws SAXException{
	AttributesImpl attrs = new AttributesImpl();
	response.startElement("", "event", "event", attrs);
	
	addChildren(response, event);
	
	response.endElement("", "event", "event");
    }

    private void addMsg(TransformerHandler response, String eventMsg) throws SAXException{
	AttributesImpl attrs = new AttributesImpl();
	response.startElement("", "msg", "msg", attrs);
	response.characters(eventMsg.toCharArray(), 0, eventMsg.length());
	response.endElement("", "msg", "msg");
    }
    
    private void addChildren(TransformerHandler response, GameEvent event) throws SAXException{
	addMsg(response, event.toString());
    }
    
}
