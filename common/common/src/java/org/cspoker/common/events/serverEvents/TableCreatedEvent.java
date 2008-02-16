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

package org.cspoker.common.events.serverEvents;

import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.player.Player;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class TableCreatedEvent extends ServerEvent {

    private static final long serialVersionUID = -3408596246641282753L;

    private final TableId id;

    private final Player player;

    public TableCreatedEvent(Player player, TableId id) {
	this.player = player;
	this.id = id;
    }

    @Override
    public String toString() {
	return player.getName() + " has created a new table [" + id + "].";
    }
    
    public Player getPlayer(){
	return player;
    }
    
    public TableId getId() {
	return id;
    }
    
    @Override
    public void toXml(ContentHandler handler) throws SAXException {
	AttributesImpl attrs = new AttributesImpl();
	attrs.addAttribute("", "type", "type", "CDATA", "tablecreated");
	attrs.addAttribute("", "player", "player", "CDATA", getPlayer().getName());
	handler.startElement("", "event", "event", attrs);
	handler.startElement("", "id", "id", attrs);
	String msg=String.valueOf(getId().getID());
	handler.characters(msg.toCharArray(), 0, msg.length());
	handler.endElement("", "id", "id");
	handler.endElement("", "event", "event");
    }
}
