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

import java.util.List;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.server.game.events.GameEvent;
import org.cspoker.server.game.events.NewCommunityCardsEvent;
import org.cspoker.server.game.events.privateEvents.NewPocketCardsEvent;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class CardEventToEventTag extends EventToEventTag{

    @Override
    protected void addChildren(TransformerHandler response, GameEvent event) throws SAXException{
	super.addChildren(response, event);

	List<Card> cards;
	boolean privateCards;

	if(event instanceof NewPocketCardsEvent){
	    NewPocketCardsEvent cardEvent = (NewPocketCardsEvent)event;
	    cards = cardEvent.getPocketCards();
	    privateCards=true;	    
	}else if(event instanceof NewCommunityCardsEvent){
	    NewCommunityCardsEvent cardEvent = (NewCommunityCardsEvent)event;
	    cards = cardEvent.getCommonCards();
	    privateCards=false;
	}else{
	    throw new IllegalStateException("Event does not contain cards");
	}
	addCards(response, cards, privateCards);

    }

    private void addCards(TransformerHandler response, List<Card> cards, boolean privateCards) throws SAXException {
	AttributesImpl attrs = new AttributesImpl();
	if(privateCards){
	    attrs.addAttribute("", "type", "type", "CDATA", "private");
	}else{
	    attrs.addAttribute("", "type", "type", "CDATA", "community");
	}
	response.startElement("", "cards", "cards", attrs);
	for(Card card:cards){
	    attrs = new AttributesImpl();
	    attrs.addAttribute("", "suit", "suit", "CDATA", card.getSuit().toString());
	    response.startElement("", "card", "card", attrs);
	    String rank = card.getRank().toString();
	    response.characters(rank.toCharArray(), 0, rank.length());
	    response.endElement("", "card", "card");
	}
	response.endElement("", "cards", "cards");
    }
}
