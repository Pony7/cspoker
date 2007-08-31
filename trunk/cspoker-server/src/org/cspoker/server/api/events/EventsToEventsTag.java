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
import org.cspoker.server.game.events.NewCommunityCardsEvent;
import org.cspoker.server.game.events.NewDealEvent;
import org.cspoker.server.game.events.PotChangedEvent;
import org.cspoker.server.game.events.playerActionEvents.DealEvent;
import org.cspoker.server.game.events.privateEvents.NewPocketCardsEvent;
import org.cspoker.server.game.playerCommunication.GameEvents;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class EventsToEventsTag {
    
    private EventToEventTag generalEvent;
    private CardEventToEventTag cardEvent;
    private DealEventToEventTag dealEvent ;
    private PotChangedEventToEventTag potChangedEvent;
    
    public EventsToEventsTag() {
	this.generalEvent = new EventToEventTag();
	this.cardEvent = new CardEventToEventTag();
	this.dealEvent = new DealEventToEventTag();
	this.potChangedEvent = new PotChangedEventToEventTag();
    }

    public void transform(TransformerHandler response, GameEvents events) throws SAXException{
	AttributesImpl eventsAttrs = new AttributesImpl();
	eventsAttrs.addAttribute("", "lastEventNumber", "lastEventNumber", "CDATA", String.valueOf(events.getLastEventNumber()));
	response.startElement("", "events", "events", eventsAttrs);
	for(GameEvent event:events){
	    getEventToEventsTag(event).transform(response, event);
	}
	response.endElement("", "events", "events");
    }
    
    public EventToEventTag getEventToEventsTag(GameEvent event){
	if(event instanceof NewCommunityCardsEvent || event instanceof NewPocketCardsEvent)
	    return cardEvent;
	if(event instanceof NewDealEvent)
	    return dealEvent;
	if(event instanceof PotChangedEvent)
	    return potChangedEvent;
	
	return generalEvent;
    }
    
}
