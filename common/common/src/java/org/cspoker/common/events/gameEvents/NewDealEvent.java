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

package org.cspoker.common.events.gameEvents;

import java.util.Collections;
import java.util.List;

import org.cspoker.common.player.Player;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A class to represent new deal events.
 * 
 * @author Kenzo
 * 
 */
public class NewDealEvent extends GameEvent {

    private static final long serialVersionUID = 8048593844056212117L;

    private final List<Player> players;

    private final Player dealer;

    public NewDealEvent(List<Player> players, Player dealer) {
	this.players = Collections.unmodifiableList(players);
	this.dealer = dealer;
    }

    public Player getDealer() {
	return dealer;
    }

    @Override
    public String toString() {
	String toReturn = "A new deal with ";
	for (Player player : players) {
	    toReturn += player.getName();
	    toReturn += " (";
	    toReturn += player.getStackValue();
	    toReturn += " chips), ";
	}
	return toReturn.substring(0, toReturn.length() - 2)
	+ " as initial players of this table. " + dealer.getName()
	+ " is dealer.";
    }

    @Override
    public void toXml(ContentHandler handler) throws SAXException {
	AttributesImpl attrs = new AttributesImpl();
	attrs.addAttribute("", "type", "type", "CDATA", "newdeal");
	handler.startElement("", "event", "event", attrs);

	handler.startElement("", "players", "players", new AttributesImpl());
	for(Player player : players){
	    attrs = new AttributesImpl();
	    if(player.equals(getDealer())){
		attrs.addAttribute("", "dealer", "dealer", "CDATA", "true");
	    }
	    handler.startElement("", "player", "player", attrs);
	    String msg=String.valueOf(player.getName());
	    handler.characters(msg.toCharArray(), 0, msg.length());
	    handler.endElement("", "player", "player");
	}
	handler.endElement("", "players", "players");

	handler.endElement("", "event", "event");
    }

}
