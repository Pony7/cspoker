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
package org.cspoker.server.game.events.gameEvents.privateEvents;

import java.util.Collections;
import java.util.List;

import org.cspoker.common.game.player.Player;
import org.cspoker.server.game.elements.cards.deck.Deck.Card;
import org.cspoker.server.game.events.gameEvents.GameEvent;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class NewPocketCardsEvent extends GameEvent {

    private final Player player;

    private final List<Card> pocketCards;

    public NewPocketCardsEvent(Player player, List<Card> pocketCards) {
	this.player = player;
	this.pocketCards = Collections.unmodifiableList(pocketCards);
    }

    public List<Card> getPocketCards() {
	return pocketCards;
    }

    @Override
    public String toString() {
	String toReturn = getPlayer().getName() + " has received new pocket cards: ";
	for (Card card : getPocketCards()) {
	    toReturn += card;
	    toReturn += ", ";
	}
	return toReturn.substring(0, toReturn.length() - 2) + ".";
    }


    public Player getPlayer() {
	return player;
    }

    @Override
    public void toXml(ContentHandler handler) throws SAXException {
	AttributesImpl attrs = new AttributesImpl();
	attrs.addAttribute("", "type", "type", "CDATA", "newpocketcards");
	attrs.addAttribute("", "player", "player", "CDATA", getPlayer().getName());
	handler.startElement("", "event", "event", attrs);

	handler.startElement("", "cards", "cards", new AttributesImpl());
	for(Card card : getPocketCards()){
	    attrs = new AttributesImpl();
	    attrs.addAttribute("", "suit", "suit", "CDATA", card.getSuit().getShortDescription());
	    handler.startElement("", "card", "card", new AttributesImpl());
	    String msg=card.getRank().getShortDescription();
	    handler.characters(msg.toCharArray(), 0, msg.length());
	    handler.endElement("", "card", "card");
	}
	handler.endElement("", "cards", "cards");

	handler.endElement("", "event", "event");

    }
}
