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

import org.cspoker.common.player.Player;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A class to represent player joining games events.
 * 
 * @author Kenzo
 * 
 */
public class PlayerJoinedGameEvent extends GameEvent {

	private static final long serialVersionUID = 3276571712883586966L;

	private final Player player;

	public PlayerJoinedGameEvent(Player player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return player.getName() + " joined the table.";
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void toXml(ContentHandler handler) throws SAXException {
		AttributesImpl attrs = new AttributesImpl();
		attrs.addAttribute("", "type", "type", "CDATA", "playerjoinedgame");
		attrs.addAttribute("", "player", "player", "CDATA", getPlayer()
				.getName());
		handler.startElement("", "event", "event", attrs);

		handler.endElement("", "event", "event");
	}
}
