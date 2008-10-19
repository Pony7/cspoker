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
package org.cspoker.common.api.chat.event;

import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.elements.player.Player;

@XmlRootElement
public class MessageEvent extends ChatEvent {
	
	private static final long serialVersionUID = -4744953146472211045L;
	
	private Player player;
	private String message;
	
	public MessageEvent(Player player, String message) {
		this.player = player;
		this.message = message;
	}

	protected MessageEvent() {
		// no op
	}

	public String toString() {
		return getPlayer().getName() + " says: " + getMessage();
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void dispatch(ChatListener chatListener) {
		chatListener.onMessage(this);
	}

}
