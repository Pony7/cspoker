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
package org.cspoker.server.http.events;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.server.game.events.Event;
import org.cspoker.server.game.events.gameEvents.NewCommunityCardsEvent;
import org.cspoker.server.game.events.gameEvents.NewDealEvent;
import org.cspoker.server.game.events.gameEvents.PotChangedEvent;
import org.cspoker.server.game.events.gameEvents.privateEvents.NewPocketCardsEvent;
import org.cspoker.server.game.playerCommunication.Events;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class EventsToEventsTag {

    private final EventToEventTag generalEvent;
    private final CardEventToEventTag cardEvent;
    private final DealEventToEventTag dealEvent ;
    private final PotChangedEventToEventTag potChangedEvent;

    public EventsToEventsTag() {
	generalEvent = new EventToEventTag();
	cardEvent = new CardEventToEventTag();
	dealEvent = new DealEventToEventTag();
	potChangedEvent = new PotChangedEventToEventTag();
    }

    public void transform(TransformerHandler response, Events events) throws SAXException{
	AttributesImpl eventsAttrs = new AttributesImpl();
	eventsAttrs.addAttribute("", "lastEventNumber", "lastEventNumber", "CDATA", String.valueOf(events.getLastEventNumber()));
	response.startElement("", "events", "events", eventsAttrs);
	for(Event event:events){
	    getEventToEventsTag(event).transform(response, event);
	}
	response.endElement("", "events", "events");
    }

    public EventToEventTag getEventToEventsTag(Event event){
	if((event instanceof NewCommunityCardsEvent) || (event instanceof NewPocketCardsEvent))
	    return cardEvent;
	if(event instanceof NewDealEvent)
	    return dealEvent;
	if(event instanceof PotChangedEvent)
	    return potChangedEvent;

	return generalEvent;
    }

}
