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
package org.cspoker.server.common.chat.room;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.common.api.chat.event.MessageEvent;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.elements.player.MutablePlayer;
import org.cspoker.common.elements.player.Player;

public abstract class ChatRoom {

	public ChatRoom(){
	}
	protected final List<ChatListener> players = new CopyOnWriteArrayList<ChatListener>();
	
	public void subscribe(ChatListener listener) {
		players.add(listener);
	}

	public void unSubscribe(ChatListener listener) {
		players.remove(listener);
	}
    
	public void sendMessage(Player sender,String message){
		for(ChatListener listener: players){
			listener.onMessage(new MessageEvent(sender,message));
		}
	}

	public abstract boolean canSubscribeListener(MutablePlayer player);
	
}

