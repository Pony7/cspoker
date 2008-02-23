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
package org.cspoker.common.events.serverevents;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.player.Player;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerMessageEvent extends ServerEvent {

	private static final long serialVersionUID = -1396985826399601557L;

	private Player player;

	private String message;

	public ServerMessageEvent(Player player, String message) {
		this.player = player;
		this.message = message;
	}

	protected ServerMessageEvent() {
		// no op
	}
	
	@Override
	public String toString() {
		return player.getName() + " says: " + message;
	}

	public String getMessage() {
		return message;
	}

	public String getPlayerName() {
		return player.getName();
	}
}
